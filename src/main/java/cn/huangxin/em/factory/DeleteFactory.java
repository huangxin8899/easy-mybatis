package cn.huangxin.em.factory;

import cn.huangxin.em.QueryType;
import cn.huangxin.em.anno.PrimaryKey;
import cn.huangxin.em.util.AnnoUtil;
import cn.huangxin.em.util.CommonUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * DeleteFactory
 *
 * @author 黄鑫
 */
public class DeleteFactory {


    public static DeleteEntity getSqlById(Serializable id, Class<?> cls) {
        DeleteEntity deleteEntity = new DeleteEntity(id);

        createDeleteFrom(cls, deleteEntity);

        createWhereSegment(id, cls, deleteEntity);

        return deleteEntity;
    }

    private static void createDeleteFrom(Class<?> cls, DeleteEntity deleteEntity) {
        deleteEntity.setDelete(AnnoUtil.getTableName(cls));
    }

    private static void createWhereSegment(Serializable id, Class<?> cls, DeleteEntity deleteEntity) {
        Map<String, Object> paramMap = deleteEntity.getParamMap();
        StringBuilder segment = new StringBuilder();
        CommonUtil.fieldPredicate(cls, field -> {
            try {
                boolean flag = field.isAnnotationPresent(PrimaryKey.class);
                if (flag) {
                    segment.append(QueryType.resolve(QueryType.EQ, CommonUtil.camelToUnderscore(field.getName()), id, paramMap));
                    deleteEntity.getWhereList().add(segment.toString());
                }
                return flag;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
