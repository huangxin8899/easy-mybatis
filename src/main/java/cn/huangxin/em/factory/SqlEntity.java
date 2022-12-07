package cn.huangxin.em.factory;

import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SqlEntity
 *
 * @author 黄鑫
 */
public abstract class SqlEntity {
    protected SQL sql = new SQL();
    protected Map<String, Object> paramMap = new HashMap<>();

    public SQL getSql() {
        this.inject();
        return sql;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    protected abstract void inject();
}
