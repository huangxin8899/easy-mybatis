package cn.huangxin.em.factory;

import org.apache.ibatis.jdbc.SQL;

import java.util.HashMap;
import java.util.Map;

/**
 * SqlEntity
 *
 * @author 黄鑫
 */
public abstract class SqlEntity {
    protected String script;
    protected SQL sql = new SQL();
    protected Map<String, Object> paramMap = new HashMap<>();
    protected Object mateObj;

    public SQL getSql() {
        this.inject();
        return sql;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public Object getMateObj() {
        return mateObj;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    protected abstract void inject();
}
