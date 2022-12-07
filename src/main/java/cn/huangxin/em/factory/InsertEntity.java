package cn.huangxin.em.factory;

/**
 * InsertEntity
 *
 * @author 黄鑫
 */
public class InsertEntity {

    private String script;
    private Object mateObj;

    public InsertEntity() {
    }

    public InsertEntity(String script, Object mateObj) {
        this.script = script;
        this.mateObj = mateObj;
    }

    public String getScript() {
        return script;
    }

    public Object getMateObj() {
        return mateObj;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public void setMateObj(Object mateObj) {
        this.mateObj = mateObj;
    }
}
