package cn.huangxin.em;

import cn.huangxin.em.util.FunctionUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * CommonBuild
 *
 * @author 黄鑫
 */
public abstract class CommonBuild<T> {

    protected <R> String getColumn(SerializableFunction<R, ?> function) {
        String tableName = FunctionUtil.getTableName(function);
        String columnName = FunctionUtil.getColumnName(function);
        return tableName + SqlConstant.DOT + columnName;
    }

    public abstract <R> T apply(boolean flag, QueryType queryType, SerializableFunction<R, ?> function, Object param);

    public abstract T apply(boolean flag, QueryType queryType, String column, Object param);

    public <R> T eq(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.EQ, function, param);
    }

    public <R> T eq(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.EQ, function, param);
    }

    public T eq(String column, Object param) {
        return this.apply(true, QueryType.EQ, column, param);
    }

    public T eq(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.EQ, column, param);
    }

    public <R> T ne(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.NE, function, param);
    }

    public <R> T ne(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.NE, function, param);
    }

    public T ne(String column, Object param) {
        return this.apply(true, QueryType.NE, column, param);
    }

    public T ne(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.NE, column, param);
    }

    public <R> T gt(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.GT, function, param);
    }

    public <R> T gt(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.GT, function, param);
    }

    public T gt(String column, Object param) {
        return this.apply(true, QueryType.GT, column, param);
    }

    public T gt(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.GT, column, param);
    }

    public <R> T ge(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.GE, function, param);
    }

    public <R> T ge(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.GE, function, param);
    }

    public T ge(String column, Object param) {
        return this.apply(true, QueryType.GE, column, param);
    }

    public T ge(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.GE, column, param);
    }

    public <R> T lt(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.LT, function, param);
    }

    public <R> T lt(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.LT, function, param);
    }

    public T lt(String column, Object param) {
        return this.apply(true, QueryType.LT, column, param);
    }

    public T lt(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.LT, column, param);
    }

    public <R> T le(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.LE, function, param);
    }

    public <R> T le(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.LE, function, param);
    }

    public T le(String column, Object param) {
        return this.apply(true, QueryType.LE, column, param);
    }

    public T le(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.LE, column, param);
    }

    public <R> T like(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.LIKE, function, param);
    }

    public <R> T like(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.LIKE, function, param);
    }

    public T like(String column, Object param) {
        return this.apply(true, QueryType.LIKE, column, param);
    }

    public T like(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.LIKE, column, param);
    }

    public <R> T notLike(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.NOT_LIKE, function, param);
    }

    public <R> T notLike(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.NOT_LIKE, function, param);
    }

    public T notLike(String column, Object param) {
        return this.apply(true, QueryType.NOT_LIKE, column, param);
    }

    public T notLike(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.NOT_LIKE, column, param);
    }

    public <R> T leftLike(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.LEFT_LIKE, function, param);
    }

    public <R> T leftLike(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.LEFT_LIKE, function, param);
    }

    public T leftLike(String column, Object param) {
        return this.apply(true, QueryType.LEFT_LIKE, column, param);
    }

    public T leftLike(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.LEFT_LIKE, column, param);
    }

    public <R> T rightLike(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.RIGHT_LIKE, function, param);
    }

    public <R> T rightLike(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.RIGHT_LIKE, function, param);
    }

    public T rightLike(String column, Object param) {
        return this.apply(true, QueryType.RIGHT_LIKE, column, param);
    }

    public T rightLike(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.RIGHT_LIKE, column, param);
    }

    public <R> T in(SerializableFunction<R, ?> function, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(true, QueryType.IN, function, list);
    }

    public <R> T in(SerializableFunction<R, ?> function, Collection<?> params) {
        return this.apply(true, QueryType.IN, function, params);
    }

    public <R> T in(boolean flag, SerializableFunction<R, ?> function, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(flag, QueryType.IN, function, list);
    }

    public <R> T in(boolean flag, SerializableFunction<R, ?> function, Collection<?> param) {
        return this.apply(flag, QueryType.IN, function, param);
    }

    public T in(String column, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(true, QueryType.IN, column, list);
    }

    public T in(String column, Collection<?> param) {
        return this.apply(true, QueryType.IN, column, param);
    }

    public T in(boolean flag, String column, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(flag, QueryType.IN, column, list);
    }


    public <R> T notIn(SerializableFunction<R, ?> function, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(true, QueryType.NOT_IN, function, list);
    }

    public <R> T notIn(SerializableFunction<R, ?> function, Collection<?> params) {
        return this.apply(true, QueryType.NOT_IN, function, params);
    }

    public <R> T notIn(boolean flag, SerializableFunction<R, ?> function, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(flag, QueryType.NOT_IN, function, list);
    }

    public <R> T notIn(boolean flag, SerializableFunction<R, ?> function, Collection<?> param) {
        return this.apply(flag, QueryType.NOT_IN, function, param);
    }

    public T notIn(String column, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(true, QueryType.NOT_IN, column, list);
    }

    public T notIn(String column, Collection<?> param) {
        return this.apply(true, QueryType.NOT_IN, column, param);
    }

    public T notIn(boolean flag, String column, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(flag, QueryType.NOT_IN, column, list);
    }

    public <R> T between(SerializableFunction<R, ?> function, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(true, QueryType.BETWEEN, function, params);
    }

    public <R> T between(boolean flag, SerializableFunction<R, ?> function, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(flag, QueryType.BETWEEN, function, params);
    }

    public T between(String column, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(true, QueryType.BETWEEN, column, params);
    }

    public T between(boolean flag, String column, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(flag, QueryType.BETWEEN, column, params);
    }

    public <R> T notBetween(SerializableFunction<R, ?> function, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(true, QueryType.NOT_BETWEEN, function, params);
    }

    public <R> T notBetween(boolean flag, SerializableFunction<R, ?> function, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(flag, QueryType.NOT_BETWEEN, function, params);
    }

    public T notBetween(String column, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(true, QueryType.NOT_BETWEEN, column, params);
    }

    public T notBetween(boolean flag, String column, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(flag, QueryType.NOT_BETWEEN, column, params);
    }

    public <R> T isNull(SerializableFunction<R, ?> function) {
        return this.apply(true, QueryType.IS_NULL, function, null);
    }

    public <R> T isNull(boolean flag, SerializableFunction<R, ?> function) {
        return this.apply(flag, QueryType.IS_NULL, function, null);
    }

    public T isNull(String column) {
        return this.apply(true, QueryType.IS_NULL, column, null);
    }

    public T isNull(boolean flag, String column) {
        return this.apply(flag, QueryType.IS_NULL, column, null);
    }

    public <R> T isNotNull(SerializableFunction<R, ?> function) {
        return this.apply(true, QueryType.IS_NOT_NULL, function, null);
    }

    public <R> T isNotNull(boolean flag, SerializableFunction<R, ?> function) {
        return this.apply(flag, QueryType.IS_NOT_NULL, function, null);
    }

    public T isNotNull(String column) {
        return this.apply(true, QueryType.IS_NOT_NULL, column, null);
    }

    public T isNotNull(boolean flag, String column) {
        return this.apply(flag, QueryType.IS_NOT_NULL, column, null);
    }

}
