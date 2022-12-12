package cn.huangxin.em.factory;

import cn.huangxin.em.QueryType;
import cn.huangxin.em.anno.PrimaryKey;
import cn.huangxin.em.util.CommonUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

import static cn.huangxin.em.util.CommonUtil.camelToUnderscore;

/**
 * UpdateEntity
 *
 * @author 黄鑫
 */
public class UpdateEntity extends ConditionEntity<UpdateEntity> {

    private String update;
    private final List<String> setList = new ArrayList<>();

    public UpdateEntity(Object mateObj) {
        this.mateObj = mateObj;
    }

    public UpdateEntity(String script, Object mateObj) {
        this.script = script;
        this.mateObj = mateObj;
    }

    String getUpdate() {
        return update;
    }

    void setUpdate(String update) {
        this.update = update;
    }

    List<String> getSetList() {
        return setList;
    }


    @Override
    protected void inject() {
        this.sql = new SQL();
        if (CommonUtil.isNotEmptyStr(this.update)) {
            this.sql.UPDATE(this.update);
        }
        if (this.setList.size() > 0) {
            this.sql.SET(this.setList.toArray(new String[this.setList.size()]));
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
