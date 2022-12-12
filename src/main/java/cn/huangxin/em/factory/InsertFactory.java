package cn.huangxin.em.factory;

import cn.huangxin.em.SqlConstant;
import cn.huangxin.em.util.AnnoUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

import static cn.huangxin.em.util.CommonUtil.getFields;

/**
 * InsertFactory
 *
 * @author 黄鑫
 */
public class InsertFactory {

    public static <Insert> InsertEntity getSql(Insert insertObj) {
        String sql = createInsertInto(insertObj) +
                createColumns(insertObj) +
                createValues(insertObj);
        return new InsertEntity(sql, insertObj);
    }

    public static <Insert> InsertEntity getBatchSql(Collection<Insert> insertList) {
        Object val = insertList.toArray()[0];
        String sql = SqlConstant.PRE_SCRIPT + createInsertInto(val) + createBatchColumns(val) + createBatchValues(val) + SqlConstant.POST_SCRIPT;
        return new InsertEntity(sql, insertList);
    }


    private static <Insert> String createInsertInto(Insert insertObj) {
        return SqlConstant.INSERT_INTO_ + AnnoUtil.getTableName(insertObj.getClass()) + SqlConstant.SPACE;
    }

    private static <Insert> String createColumns(Insert insertObj) {
        StringJoiner joiner = new StringJoiner(SqlConstant.COMMA, SqlConstant.PRE_IN, SqlConstant.POST_IN);
        List<Field> fields = getFields(insertObj.getClass(), new ArrayList<>());
        try {
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                Object val = field.get(insertObj);
                if (val == null) {
                    continue;
                }
                joiner.add(AnnoUtil.getFieldName(field));
                field.setAccessible(accessible);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return joiner + SqlConstant.SPACE;
    }

    private static <Insert> String createValues(Insert insertObj) {
        StringBuilder segment = new StringBuilder();
        List<Field> fields = getFields(insertObj.getClass(), new ArrayList<>());
        StringJoiner joiner = new StringJoiner(SqlConstant.COMMA, SqlConstant.PRE_IN, SqlConstant.POST_IN);
        try {
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                Object val = field.get(insertObj);
                if (val == null) {
                    continue;
                }
                joiner.add(SqlConstant.wrapParam(field.getName()));
                field.setAccessible(accessible);
            }
            segment.append(SqlConstant.VALUES_).append(joiner);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return segment.toString();
    }

    private static <Insert> String createBatchColumns(Insert insertObj) {
        StringJoiner joiner = new StringJoiner(SqlConstant.COMMA, SqlConstant.PRE_IN, SqlConstant.POST_IN);
        List<Field> fields = getFields(insertObj.getClass(), new ArrayList<>());
        for (Field field : fields) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            joiner.add(AnnoUtil.getFieldName(field));
            field.setAccessible(accessible);
        }
        return joiner + SqlConstant.SPACE;
    }

    private static <Insert> String createBatchValues(Insert insertObj) {
        StringBuilder segment = new StringBuilder();
        List<Field> fields = getFields(insertObj.getClass(), new ArrayList<>());
        StringJoiner joiner = new StringJoiner(SqlConstant.COMMA, SqlConstant.PRE_FOREACH_SCRIPT + SqlConstant.PRE_IN, SqlConstant.POST_IN + SqlConstant.POST_FOREACH_SCRIPT);
        for (Field field : fields) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            joiner.add(SqlConstant.wrapParam(SqlConstant.ITEM + SqlConstant.DOT + field.getName()));
            field.setAccessible(accessible);
        }
        segment.append(SqlConstant.VALUES_).append(joiner);
        return segment.toString();
    }

}
