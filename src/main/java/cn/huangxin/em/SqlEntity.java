package cn.huangxin.em;

import cn.huangxin.em.util.CommonUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 黄鑫
 * @description SqlEntity
 */
public class SqlEntity<T> extends CommonBuild<SqlEntity<T>> {

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

    @Override
    public <R> SqlEntity<T> apply(boolean flag, QueryType queryType, SerializableFunction<R, ?> function, Object param) {
        if (flag) {
            String resolve = QueryType.resolve(queryType, this.getColumn(function), param, this.paramMap);
            if (CommonUtil.isNotEmptyStr(resolve)) {
                this.sql.WHERE(resolve);
            }
        }
        return this;
    }

    @Override
    public SqlEntity<T> apply(boolean flag, QueryType queryType, String column, Object param) {
        if (flag) {
            String resolve = QueryType.resolve(queryType, column, param, this.paramMap);
            if (CommonUtil.isNotEmptyStr(resolve)) {
                this.sql.WHERE(resolve);
            }
        }
        return this;
    }
}
