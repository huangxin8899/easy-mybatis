package cn.huangxin.em.factory;

import cn.huangxin.em.QueryType;
import cn.huangxin.em.anno.PrimaryKey;
import cn.huangxin.em.util.CommonUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

import static cn.huangxin.em.util.CommonUtil.camelToUnderscore;

/**
 * DeleteEntity
 *
 * @author 黄鑫
 */
public class DeleteEntity extends ConditionEntity<DeleteEntity> {

    private String delete;

    public DeleteEntity(Object mateObj) {
        this.mateObj = mateObj;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    @Override
    protected void inject() {
        this.sql = new SQL();
        if (CommonUtil.isNotEmptyStr(this.delete)) {
            this.sql.DELETE_FROM(this.delete);
        }
        if (this.whereList.size() > 0) {
            this.sql.WHERE(this.whereList.toArray(new String[this.whereList.size()]));
        } else {
            createWhereId();
            this.sql.WHERE(this.whereList.toArray(new String[this.whereList.size()]));
        }
    }

    private void createWhereId() {
        StringBuilder segment = new StringBuilder();
        CommonUtil.fieldPredicate(this.mateObj.getClass(), field -> {
            try {
                boolean flag = field.isAnnotationPresent(PrimaryKey.class);
                if (flag) {
                    Object id = field.get(this.mateObj);
                    if (id == null) {
                        throw new NullPointerException();
                    }
                    segment.append(QueryType.resolve(QueryType.EQ, camelToUnderscore(field.getName()), id, paramMap));
                    this.whereList.add(segment.toString());
                }
                return flag;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
