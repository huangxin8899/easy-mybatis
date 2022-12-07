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
public interface SqlBuild<T> {

    default  <R> String getColumn(SerializableFunction<R, ?> function) {
        String tableName = FunctionUtil.getTableName(function);
        String columnName = FunctionUtil.getColumnName(function);
        return tableName + SqlConstant.DOT + columnName;
    }

    <R> T apply(boolean flag, QueryType queryType, SerializableFunction<R, ?> function, Object param);

    T apply(boolean flag, QueryType queryType, String column, Object param);

    default <R> T eq(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.EQ, function, param);
    }

    default <R> T eq(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.EQ, function, param);
    }

    default T eq(String column, Object param) {
        return this.apply(true, QueryType.EQ, column, param);
    }

    default T eq(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.EQ, column, param);
    }

    default <R> T ne(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.NE, function, param);
    }

    default <R> T ne(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.NE, function, param);
    }

    default T ne(String column, Object param) {
        return this.apply(true, QueryType.NE, column, param);
    }

    default T ne(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.NE, column, param);
    }

    default <R> T gt(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.GT, function, param);
    }

    default <R> T gt(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.GT, function, param);
    }

    default T gt(String column, Object param) {
        return this.apply(true, QueryType.GT, column, param);
    }

    default T gt(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.GT, column, param);
    }

    default <R> T ge(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.GE, function, param);
    }

    default <R> T ge(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.GE, function, param);
    }

    default T ge(String column, Object param) {
        return this.apply(true, QueryType.GE, column, param);
    }

    default T ge(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.GE, column, param);
    }

    default <R> T lt(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.LT, function, param);
    }

    default <R> T lt(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.LT, function, param);
    }

    default T lt(String column, Object param) {
        return this.apply(true, QueryType.LT, column, param);
    }

    default T lt(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.LT, column, param);
    }

    default <R> T le(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.LE, function, param);
    }

    default <R> T le(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.LE, function, param);
    }

    default T le(String column, Object param) {
        return this.apply(true, QueryType.LE, column, param);
    }

    default T le(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.LE, column, param);
    }

    default <R> T like(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.LIKE, function, param);
    }

    default <R> T like(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.LIKE, function, param);
    }

    default T like(String column, Object param) {
        return this.apply(true, QueryType.LIKE, column, param);
    }

    default T like(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.LIKE, column, param);
    }

    default <R> T notLike(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.NOT_LIKE, function, param);
    }

    default <R> T notLike(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.NOT_LIKE, function, param);
    }

    default T notLike(String column, Object param) {
        return this.apply(true, QueryType.NOT_LIKE, column, param);
    }

    default T notLike(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.NOT_LIKE, column, param);
    }

    default <R> T leftLike(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.LEFT_LIKE, function, param);
    }

    default <R> T leftLike(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.LEFT_LIKE, function, param);
    }

    default T leftLike(String column, Object param) {
        return this.apply(true, QueryType.LEFT_LIKE, column, param);
    }

    default T leftLike(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.LEFT_LIKE, column, param);
    }

    default <R> T rightLike(SerializableFunction<R, ?> function, Object param) {
        return this.apply(true, QueryType.RIGHT_LIKE, function, param);
    }

    default <R> T rightLike(boolean flag, SerializableFunction<R, ?> function, Object param) {
        return this.apply(flag, QueryType.RIGHT_LIKE, function, param);
    }

    default T rightLike(String column, Object param) {
        return this.apply(true, QueryType.RIGHT_LIKE, column, param);
    }

    default T rightLike(boolean flag, String column, Object param) {
        return this.apply(flag, QueryType.RIGHT_LIKE, column, param);
    }

    default <R> T in(SerializableFunction<R, ?> function, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(true, QueryType.IN, function, list);
    }

    default <R> T in(SerializableFunction<R, ?> function, Collection<?> params) {
        return this.apply(true, QueryType.IN, function, params);
    }

    default <R> T in(boolean flag, SerializableFunction<R, ?> function, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(flag, QueryType.IN, function, list);
    }

    default <R> T in(boolean flag, SerializableFunction<R, ?> function, Collection<?> param) {
        return this.apply(flag, QueryType.IN, function, param);
    }

    default T in(String column, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(true, QueryType.IN, column, list);
    }

    default T in(String column, Collection<?> param) {
        return this.apply(true, QueryType.IN, column, param);
    }

    default T in(boolean flag, String column, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(flag, QueryType.IN, column, list);
    }


    default <R> T notIn(SerializableFunction<R, ?> function, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(true, QueryType.NOT_IN, function, list);
    }

    default <R> T notIn(SerializableFunction<R, ?> function, Collection<?> params) {
        return this.apply(true, QueryType.NOT_IN, function, params);
    }

    default <R> T notIn(boolean flag, SerializableFunction<R, ?> function, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(flag, QueryType.NOT_IN, function, list);
    }

    default <R> T notIn(boolean flag, SerializableFunction<R, ?> function, Collection<?> param) {
        return this.apply(flag, QueryType.NOT_IN, function, param);
    }

    default T notIn(String column, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(true, QueryType.NOT_IN, column, list);
    }

    default T notIn(String column, Collection<?> param) {
        return this.apply(true, QueryType.NOT_IN, column, param);
    }

    default T notIn(boolean flag, String column, Object... params) {
        List<Object> list = Arrays.asList(params);
        return this.apply(flag, QueryType.NOT_IN, column, list);
    }

    default <R> T between(SerializableFunction<R, ?> function, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(true, QueryType.BETWEEN, function, params);
    }

    default <R> T between(boolean flag, SerializableFunction<R, ?> function, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(flag, QueryType.BETWEEN, function, params);
    }

    default T between(String column, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(true, QueryType.BETWEEN, column, params);
    }

    default T between(boolean flag, String column, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(flag, QueryType.BETWEEN, column, params);
    }

    default <R> T notBetween(SerializableFunction<R, ?> function, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(true, QueryType.NOT_BETWEEN, function, params);
    }

    default <R> T notBetween(boolean flag, SerializableFunction<R, ?> function, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(flag, QueryType.NOT_BETWEEN, function, params);
    }

    default T notBetween(String column, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(true, QueryType.NOT_BETWEEN, column, params);
    }

    default T notBetween(boolean flag, String column, Object param1, Object param2) {
        List<Object> params = Arrays.asList(param1, param2);
        return this.apply(flag, QueryType.NOT_BETWEEN, column, params);
    }

    default <R> T isNull(SerializableFunction<R, ?> function) {
        return this.apply(true, QueryType.IS_NULL, function, null);
    }

    default <R> T isNull(boolean flag, SerializableFunction<R, ?> function) {
        return this.apply(flag, QueryType.IS_NULL, function, null);
    }

    default T isNull(String column) {
        return this.apply(true, QueryType.IS_NULL, column, null);
    }

    default T isNull(boolean flag, String column) {
        return this.apply(flag, QueryType.IS_NULL, column, null);
    }

    default <R> T isNotNull(SerializableFunction<R, ?> function) {
        return this.apply(true, QueryType.IS_NOT_NULL, function, null);
    }

    default <R> T isNotNull(boolean flag, SerializableFunction<R, ?> function) {
        return this.apply(flag, QueryType.IS_NOT_NULL, function, null);
    }

    default T isNotNull(String column) {
        return this.apply(true, QueryType.IS_NOT_NULL, column, null);
    }

    default T isNotNull(boolean flag, String column) {
        return this.apply(flag, QueryType.IS_NOT_NULL, column, null);
    }
}
