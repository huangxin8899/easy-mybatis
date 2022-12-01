package cn.huangxin.em.factory;

import cn.huangxin.em.QueryType;
import cn.huangxin.em.SqlConstant;
import cn.huangxin.em.SqlEntity;
import cn.huangxin.em.anno.*;
import cn.huangxin.em.join.*;
import cn.huangxin.em.util.AnnoUtil;
import cn.huangxin.em.util.CommonUtil;
import org.apache.ibatis.jdbc.SQL;

import java.beans.Introspector;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static cn.huangxin.em.util.CommonUtil.*;

/**
 * @author 黄鑫
 * @description SqlFactory
 */
public class SelectFactory {


    public static <Q, T> SqlEntity<T> getSqlById(Serializable id, Class<T> voClass) {
        SqlEntity<T> sqlEntity = new SqlEntity<>(voClass);
        // select部分生成
        createSelectSegment(voClass, sqlEntity.getSql());

        // FROM部分
        createFromSegment(voClass, sqlEntity.getSql());

        // 条件部分生成
        createWhereSegment(id, voClass, sqlEntity);

        return sqlEntity;
    }

    private static <T> SQL createWhereSegment(Serializable id, Class<T> voClass, SqlEntity<T> sqlEntity) {
        SQL sql = sqlEntity.getSql();
        Map<String, Object> paramMap = sqlEntity.getParamMap();
        String segment = new String();
        List<Field> fields = getFields(voClass, new ArrayList<>());
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                segment = QueryType.resolve(QueryType.EQ, camelToUnderscore(field.getName()), id, paramMap);
                break;
            }
        }
        if (isEmptyStr(segment)) {
            throw new NullPointerException("SqlFactory#getSqlById()方法，id为null");
        }
        return sql.WHERE(segment);
    }

    public static <Q> SqlEntity<Q> getSql(Q queryObj) {
        return (SqlEntity<Q>) getSql(queryObj, queryObj.getClass());
    }

    public static <Q, T> SqlEntity<T> getSql(Q queryObj, Class<T> voClass, AbstractJoin<?>... joins) {


        SqlEntity<T> sqlEntity = new SqlEntity<>(voClass);

        // select部分生成
        createSelectSegment(voClass, sqlEntity.getSql());

        // FROM部分
        createFromSegment(queryObj, sqlEntity.getSql());

        // 联表部分生成
        createJoinSegment(sqlEntity, joins);

        // 条件部分生成
        createConditionSegment(queryObj, sqlEntity);

        // 分组部分生成
        createGroupBy(queryObj, sqlEntity, joins);

        // 排序部分生成
        createOrderBy(queryObj, sqlEntity, joins);

        System.out.println(sqlEntity.getSql().toString());

        return sqlEntity;
    }

    /**
     * 返回部分sql生成
     */
    private static SQL createSelectSegment(Class<?> clazz, SQL sql) {
        List<Field> fields = getFields(clazz, new ArrayList<>());
        for (Field field : fields) {
            StringBuilder segment = new StringBuilder();
            if (field.isAnnotationPresent(ResultIgnore.class)) {
                continue;
            }
            if (field.isAnnotationPresent(ResultField.class)) {
                ResultField resultField = field.getAnnotation(ResultField.class);
                // 拼接表名
                if (isNotEmptyStr(resultField.tableName())) {
                    segment.append(resultField.tableName())
                            .append(SqlConstant.DOT);
                }
                // 拼接字段名
                if (isNotEmptyStr(resultField.fieldName())) {
                    segment.append(resultField.fieldName());
                } else {
                    segment.append(camelToUnderscore(field.getName()));
                }
            } else {
                segment.append(camelToUnderscore(field.getName()));
            }
            segment.append(SqlConstant._AS_)
                    .append(field.getName());
            sql.SELECT(segment.toString());
        }
        return sql;
    }

    private static <Q> SQL createFromSegment(Q queryObj, SQL sql) {
        return createFromSegment(queryObj.getClass(), sql);
    }

    private static <T> SQL createFromSegment(Class<T> aClass, SQL sql) {
        return sql.FROM(AnnoUtil.getTableName(aClass));
    }



    private static <T> SQL createJoinSegment(SqlEntity<T> sqlEntity, AbstractJoin<?>[] joins) {
        SQL sql = sqlEntity.getSql();
        for (AbstractJoin<?> join : joins) {
            if (join instanceof LeftJoin) {
                sql.LEFT_OUTER_JOIN(join.getJoinSegment(sqlEntity.getParamMap()));
            } else if (join instanceof RightJoin) {
                sql.RIGHT_OUTER_JOIN(join.getJoinSegment(sqlEntity.getParamMap()));
            } else if (join instanceof InnerJoin) {
                sql.INNER_JOIN(join.getJoinSegment(sqlEntity.getParamMap()));
            } else if (join instanceof Join) {
                sql.JOIN(join.getJoinSegment(sqlEntity.getParamMap()));
            } else if (join instanceof OuterJoin) {
                sql.OUTER_JOIN(join.getJoinSegment(sqlEntity.getParamMap()));
            }
        }
        return sql;
    }

    private static <Q, T> SQL createConditionSegment(Q queryObj, SqlEntity<T> sqlEntity) {
        SQL sql = sqlEntity.getSql();
        Map<String, Object> paramMap = sqlEntity.getParamMap();
        try {
            List<Field> fields = getFields(queryObj.getClass(), new ArrayList<>());
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                Query query = field.getAnnotation(Query.class);
                if (query != null) {
                    Object val = field.get(queryObj);
                    if (CommonUtil.isEmpty(val)) {
                        continue;
                    }
                    String fieldName = AnnoUtil.getFieldName(field);
                    String resolve = QueryType.resolve(query.value(), fieldName, val, paramMap);
                    if (isNotEmptyStr((resolve))) {
                        sql.WHERE(resolve);
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return sql;
    }

    private static <Q, T> SQL createGroupBy(Q queryObj, SqlEntity<T> sqlEntity, AbstractJoin<?>[] joins) {
        SQL sql = sqlEntity.getSql();
        Map<String, Object> paramMap = sqlEntity.getParamMap();
        try {
            List<Field> fields = getAllFields(queryObj, joins);
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                GroupBy groupBy = field.getAnnotation(GroupBy.class);
                if (groupBy != null) {
                    sql.GROUP_BY(AnnoUtil.getFieldName(field));
                }
                Having having = field.getAnnotation(Having.class);
                if (having != null) {
                    Object val = field.get(queryObj);
                    if (CommonUtil.isEmpty(val)) {
                        continue;
                    }
                    String fieldName = AnnoUtil.getFieldName(field);
                    String resolve = QueryType.resolve(having.value(), fieldName, val, paramMap);
                    if (isNotEmptyStr(resolve)) {
                        sql.HAVING(resolve);
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return sql;
    }

    private static <Q, T> SQL createOrderBy(Q queryObj, SqlEntity<T> sqlEntity, AbstractJoin<?>[] joins) {
        SQL sql = sqlEntity.getSql();
        List<Field> fields = getAllFields(queryObj, joins);
        for (Field field : fields) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            OrderBy orderBy = field.getAnnotation(OrderBy.class);
            if (orderBy != null) {
                String fieldName = AnnoUtil.getFieldName(field);
                String sort = orderBy.value() ? SqlConstant._ASC_ : SqlConstant._DESC_;
                String segment = fieldName + sort;
                sql.ORDER_BY(segment);
            }
            field.setAccessible(accessible);
        }
        return sql;
    }

    private static <Q, T> List<Field> getAllFields(Q queryObj, AbstractJoin<?>[] joins) {
        List<Field> fields = new ArrayList<>();
        Set<Class<?>> set = new HashSet<>();
        set.add(queryObj.getClass());
        for (AbstractJoin<?> join : joins) {
            Class<?> leftClass = join.getLeftClass();
            Class<?> rightClass = join.getRightClass();
            if (leftClass != null) {
                set.add(leftClass);
            }
            if (rightClass != null) {
                set.add(rightClass);
            }
        }
        for (Class<?> clazz : set) {
            getFields(clazz, fields);
        }
        return fields;
    }

    private static <T> T createInstance(Class<T> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        // 优先使用无参构造
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return (T) constructor.newInstance();
            }
        }
        // 没有无参构造，使用有参构造，随机选一个构造器
        Constructor<?> constructor = constructors[0];
        Object[] args = new Object[constructor.getParameterCount()];
        return (T) constructor.newInstance(args);
    }

}
