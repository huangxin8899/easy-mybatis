package cn.huangxin.em.factory;

import cn.huangxin.em.QueryType;
import cn.huangxin.em.SqlConstant;
import cn.huangxin.em.anno.AllowNull;
import cn.huangxin.em.anno.PrimaryKey;
import cn.huangxin.em.util.AnnoUtil;
import cn.huangxin.em.util.CommonUtil;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import static cn.huangxin.em.util.CommonUtil.camelToUnderscore;

/**
 * UpdateFactory
 *
 * @author 黄鑫
 */
public class UpdateFactory {

    public static <U> UpdateEntity getSqlById(U updateObj) {
        UpdateEntity updateEntity = new UpdateEntity(updateObj);

        createUpdateSegment(updateObj, updateEntity);

        createSetSegment(updateObj, updateEntity);

        createWhereByIdSegment(updateObj, updateEntity);
        return updateEntity;
    }

    public static <U> UpdateEntity getSqlByIdBatch(Collection<U> updateObjs) {
        Object val = updateObjs.toArray()[0];
        String updateScript = createUpdateScript(val);
        return new UpdateEntity(updateScript, updateObjs);
    }

    public static <U> UpdateEntity getSql(U updateObj) {
        UpdateEntity updateEntity = new UpdateEntity(updateObj);

        createUpdateSegment(updateObj, updateEntity);

        createSetSegment(updateObj, updateEntity);

        return updateEntity;
    }

    private static <U> void createUpdateSegment(U updateObj, UpdateEntity updateEntity) {
        updateEntity.setUpdate(AnnoUtil.getPrimaryColumn(updateObj.getClass()));
    }

    private static <U> void createSetSegment(U updateObj, UpdateEntity updateEntity) {
        Map<String, Object> paramMap = updateEntity.getParamMap();
        CommonUtil.fieldConsumer(updateObj.getClass(), field -> {
            try {
                Object val = field.get(updateObj);
                if (val == null) {
                    return;
                }
                StringBuilder segment = new StringBuilder();
                String paramName = SqlConstant.ARG + paramMap.size();
                segment.append(AnnoUtil.getFieldName(field))
                        .append(SqlConstant._EQUAL_)
                        .append(SqlConstant.wrapParam(paramName));
                paramMap.put(paramName, val);
                updateEntity.getSetList().add(segment.toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static <U> void createWhereByIdSegment(U updateObj, UpdateEntity updateEntity) {
        Map<String, Object> paramMap = updateEntity.getParamMap();
        StringBuilder segment = new StringBuilder();
        CommonUtil.fieldPredicate(updateObj.getClass(), field -> {
            try {
                boolean flag = field.isAnnotationPresent(PrimaryKey.class);
                if (flag) {
                    Object id = field.get(updateObj);
                    if (id == null) {
                        throw new NullPointerException();
                    }
                    segment.append(QueryType.resolve(QueryType.EQ, camelToUnderscore(field.getName()), id, paramMap));
                    updateEntity.getWhereList().add(segment.toString());
                }
                return flag;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static String createUpdateScript(Object updateObj) {
        Class<?> objClass = updateObj.getClass();
        StringBuilder segment = new StringBuilder();
        segment.append(SqlConstant.UPDATE_)
                .append(AnnoUtil.getTableName(objClass))
                .append(SqlConstant.SPACE)
                .append(SqlConstant.SET_SCRIPT);
        CommonUtil.fieldConsumer(objClass, field -> {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                return;
            }
            segment.append(createCaseByIdScript(objClass, field));
        });
        segment.append(SqlConstant.POST_TRIM);
        segment.append(createForeachByIdScript(objClass));
        return SqlConstant.wrapScript(segment.toString());
    }

    private static String createCaseByIdScript(Class<?> cls, Field field) {
        StringBuilder segment = new StringBuilder();
        String primaryColumn = AnnoUtil.getPrimaryColumn(cls);
        String primaryName = SqlConstant.ITEM + SqlConstant.DOT + AnnoUtil.getPrimaryName(cls);
        String fieldName = AnnoUtil.getFieldName(field);
        String paramName = SqlConstant.ITEM + SqlConstant.DOT + field.getName();
        segment.append(SqlConstant.PRE_CASE_SCRIPT)
                .append(fieldName)
                .append(SqlConstant.POST_CASE_SCRIPT)
                .append("<foreach collection=\"list\" item=\"item\">");
        boolean allow = !field.isAnnotationPresent(AllowNull.class);
        if (allow) {
            if (CommonUtil.isNumeric(field.getType())) {
                segment.append("<if test=\"")
                        .append(paramName)
                        .append(" != null\">\n");
            } else {
                segment.append("<if test=\"")
                        .append(paramName)
                        .append(" != null and ")
                        .append(paramName)
                        .append(" != '' \">\n");
            }
        }
        segment.append(SqlConstant.WHEN_)
                .append(primaryColumn)
                .append(SqlConstant._EQUAL_)
                .append(SqlConstant.wrapParam(primaryName))
                .append(SqlConstant._THEN_)
                .append(SqlConstant.wrapParam(paramName));
        if (allow) {
            segment.append("\n</if>\n");
        }
        segment.append(SqlConstant.POST_FOREACH_SCRIPT)
                .append(SqlConstant.POST_TRIM);
        return segment.toString();
    }

    private static String createForeachByIdScript(Class<?> cls) {
        String primaryColumn = AnnoUtil.getPrimaryColumn(cls);
        String primaryName = SqlConstant.ITEM + SqlConstant.DOT + AnnoUtil.getPrimaryName(cls);
        StringBuilder segment = new StringBuilder();
        segment.append(SqlConstant.WHERE_)
                .append(primaryColumn)
                .append(SqlConstant._IN_)
                .append("\n<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\" open=\"(\" close=\")\">\n")
                .append(SqlConstant.wrapParam(primaryName))
                .append(SqlConstant.POST_FOREACH_SCRIPT);
        return segment.toString();
    }
}
