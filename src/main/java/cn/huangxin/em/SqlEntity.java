package cn.huangxin.em;

import org.apache.ibatis.jdbc.SQL;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 黄鑫
 * @description SqlEntity
 */
public class SqlEntity<T> {

    private SQL sql = new SQL();

    private Map<String, Object> paramMap = new HashMap<>();

    private Class<T> type;

    public SqlEntity(Class<T> type) {
        this.type = type;
    }

    public SQL getSql() {
        return sql;
    }

    public void setSql(SQL sql) {
        this.sql = sql;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Class<T> getType() {
        return type;
    }

}
