package cn.huangxin.em.mapper;

import cn.huangxin.em.SqlEntity;

import java.util.List;
import java.util.Map;

/**
 * @author 黄鑫
 * @description SqlBaseMapper
 */
public interface SqlBaseMapper {

    /**
     * 查询单条数据返回Map<String, Object>
     *
     * @param sql sql语句
     * @return Map<String, Object>
     */
    Map<String, Object> selectOne(String sql);

    /**
     * 查询单条数据返回Map<String, Object>
     *
     * @param sql   sql语句
     * @param value 参数
     * @return Map<String, Object>
     */
    Map<String, Object> selectOne(String sql, Object value);

    /**
     * 查询单条数据返回实体类型
     *
     * @param sql        sql语句
     * @param resultType 具体类型
     * @return 定义的实体类型
     */
    <T> T selectOne(String sql, Class<T> resultType);

    /**
     * 查询单条数据返回实体类型
     *
     * @param sql        sql语句
     * @param value      参数
     * @param resultType 具体类型
     * @return 定义的实体类型
     */
    <T> T selectOne(String sql, Object value, Class<T> resultType);

    /**
     * 查询单条数据返回实体类型
     * @param sqlEntity     自定义sql实体类
     * @return              定义的实体类型
     * @param <T>           具体类型
     */
    <T> T selectOne(SqlEntity<T> sqlEntity);

    /**
     * 查询数据返回
     *
     * @param sql sql语句
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> selectList(String sql);

    /**
     * 查询数据返回
     *
     * @param sql   sql语句
     * @param value 参数
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> selectList(String sql, Object value);

    /**
     * 查询数据返回
     *
     * @param sql        sql语句
     * @param resultType 具体类型
     * @return List<T>
     */
    <T> List<T> selectList(String sql, Class<T> resultType);

    /**
     * 查询数据返回
     *
     * @param sql        sql语句
     * @param value      参数
     * @param resultType 具体类型
     * @return List<T>
     */
    <T> List<T> selectList(String sql, Object value, Class<T> resultType);

    /**
     *查询数据返回
     *
     * @param sqlEntity 自定义sql实体类
     * @return          返回参数
     * @param <T>       具体类型
     */
    <T> List<T> selectList(SqlEntity<T> sqlEntity);

    /**
     * 插入数据
     *
     * @param sql sql语句
     * @return int
     */
    int sqlInsert(String sql);

    /**
     * 插入数据
     *
     * @param sql   sql语句
     * @param value 参数
     * @return int
     */
    int sqlInsert(String sql, Object value);

    /**
     * 更新数据
     *
     * @param sql sql语句
     * @return int
     */
    int sqlUpdate(String sql);

    /**
     * 更新数据
     *
     * @param sql   sql语句
     * @param value 参数
     * @return int
     */
    int sqlUpdate(String sql, Object value);

    /**
     * 删除数据
     *
     * @param sql sql语句
     * @return int
     */
    int sqlDelete(String sql);

    /**
     * 查询数据返回List<T>
     *
     * @param sql   sql语句
     * @param value 参数
     * @return int
     */
    int sqlDelete(String sql, Object value);


}
