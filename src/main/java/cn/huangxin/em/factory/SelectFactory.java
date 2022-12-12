package cn.huangxin.em.factory;

import cn.huangxin.em.QueryType;
import cn.huangxin.em.SqlConstant;
import cn.huangxin.em.anno.*;
import cn.huangxin.em.join.*;
import cn.huangxin.em.util.AnnoUtil;
import cn.huangxin.em.util.CommonUtil;

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


    public static <T> SelectEntity<T> getSqlById(Serializable id, Class<T> resultClass) {
        SelectEntity<T> selectEntity = new SelectEntity<>(id, resultClass);
        // select部分生成
        createSelectSegment(resultClass, selectEntity);

        // FROM部分
        createFromSegment(resultClass, selectEntity);

        // 条件部分生成
        createWhereSegment(id, resultClass, selectEntity);

        return selectEntity;
    }

    public static <Q> SelectEntity<Q> getSql(Q queryObj) {
        return (SelectEntity<Q>) getSql(Boolean.TRUE, queryObj, queryObj.getClass());
    }

    public static <Q> SelectEntity<Q> getSql(boolean enableAnno, Q queryObj) {
        return (SelectEntity<Q>) getSql(enableAnno, queryObj, queryObj.getClass());
    }

    public static <Q, T> SelectEntity<T> getSql(Q queryObj, Class<T> resultClass, AbstractJoin<?>... joins) {
        return getSql(Boolean.TRUE, queryObj, resultClass, joins);
    }

    public static <Q, T> SelectEntity<T> getSql(boolean enableAnno, Q queryObj, Class<T> resultClass, AbstractJoin<?>... joins) {


        SelectEntity<T> selectEntity = new SelectEntity<>(queryObj, resultClass);

        // select部分生成
        createSelectSegment(resultClass, selectEntity);

        // FROM部分
        createFromSegment(queryObj, selectEntity);

        // 联表部分生成
        createJoinSegment(selectEntity, joins);

        if (enableAnno) {

            // 条件部分生成
            createConditionSegment(queryObj, selectEntity);

            // 分组部分生成
            createGroupBy(queryObj, selectEntity, joins);

            // 排序部分生成
            createOrderBy(queryObj, selectEntity, joins);
        }


        return selectEntity;
    }

    /**
     * 返回部分sql生成
     */
    private static <T> void createSelectSegment(Class<?> clazz, SelectEntity<T> selectEntity) {
        if (CommonUtil.isBaseClass(clazz)) {
            throw new RuntimeException("暂不支持基本类型及其包装类");
        }
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
            selectEntity.getSelectList().add(segment.toString());
        }
    }

    private static <Q, T> void createFromSegment(Q queryObj, SelectEntity<T> selectEntity) {
        selectEntity.getFromList().add(AnnoUtil.getTableName(queryObj.getClass()));
    }

    private static <T> void createFromSegment(Class<T> aClass, SelectEntity<T> selectEntity) {
        selectEntity.getFromList().add(AnnoUtil.getTableName(aClass));
    }

    private static <T> void createJoinSegment(SelectEntity<T> selectEntity, AbstractJoin<?>[] joins) {
        for (AbstractJoin<?> join : joins) {
            if (join instanceof LeftJoin) {
                selectEntity.getLeftOuterJoinList().add(join.getJoinSegment(selectEntity.getParamMap()));
            } else if (join instanceof RightJoin) {
                selectEntity.getRightOuterJoinList().add(join.getJoinSegment(selectEntity.getParamMap()));
            } else if (join instanceof InnerJoin) {
                selectEntity.getInnerJoinList().add(join.getJoinSegment(selectEntity.getParamMap()));
            } else if (join instanceof Join) {
                selectEntity.getJoinList().add(join.getJoinSegment(selectEntity.getParamMap()));
            } else if (join instanceof OuterJoin) {
                selectEntity.getOuterJoinList().add(join.getJoinSegment(selectEntity.getParamMap()));
            }
        }
    }

    private static <T> void createWhereSegment(Serializable id, Class<T> resultClass, SelectEntity<T> selectEntity) {
        if (CommonUtil.isBaseClass(resultClass)) {
            throw new RuntimeException("暂不支持基本类型及其包装类");
        }
        Map<String, Object> paramMap = selectEntity.getParamMap();
        String segment = new String();
        List<Field> fields = getFields(resultClass, new ArrayList<>());
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                segment = QueryType.resolve(QueryType.EQ, camelToUnderscore(field.getName()), id, paramMap);
                break;
            }
        }
        if (isEmptyStr(segment)) {
            throw new NullPointerException();
        }
        selectEntity.getWhereList().add(segment);
    }

    private static <Q, T> void createConditionSegment(Q queryObj, SelectEntity<T> selectEntity) {
        Map<String, Object> paramMap = selectEntity.getParamMap();
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
                    String column = AnnoUtil.getTableName(queryObj.getClass()) + SqlConstant.DOT + AnnoUtil.getFieldName(field);
                    String resolve = QueryType.resolve(query.value(), column, val, paramMap);
                    if (isNotEmptyStr((resolve))) {
                        selectEntity.getWhereList().add(resolve);
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static <Q, T> void createGroupBy(Q queryObj, SelectEntity<T> selectEntity, AbstractJoin<?>[] joins) {
        Map<String, Object> paramMap = selectEntity.getParamMap();
        try {
            List<Field> fields = getAllFields(queryObj, joins);
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                GroupBy groupBy = field.getAnnotation(GroupBy.class);
                if (groupBy != null) {
                    selectEntity.getGroupByList().add(AnnoUtil.getFieldName(field));
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
                        selectEntity.getHavingList().add(resolve);
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static <Q, T> void createOrderBy(Q queryObj, SelectEntity<T> selectEntity, AbstractJoin<?>[] joins) {
        List<Field> fields = getAllFields(queryObj, joins);
        for (Field field : fields) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            OrderBy orderBy = field.getAnnotation(OrderBy.class);
            if (orderBy != null) {
                String fieldName = AnnoUtil.getFieldName(field);
                String sort = orderBy.value() ? SqlConstant._ASC_ : SqlConstant._DESC_;
                String segment = fieldName + sort;
                selectEntity.getOrderByList().add(segment);
            }
            field.setAccessible(accessible);
        }
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
