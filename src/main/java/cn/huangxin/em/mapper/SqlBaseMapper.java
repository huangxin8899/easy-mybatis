package cn.huangxin.em.mapper;

import cn.huangxin.em.factory.InsertEntity;
import cn.huangxin.em.factory.SelectEntity;

import java.util.List;
import java.util.Map;

/**
 * @author 黄鑫
 * @description SqlBaseMapper
 */
public interface SqlBaseMapper {

    /**
     * 查询单条数据返回实体类型
     * @param selectEntity     自定义sql实体类
     * @return              定义的实体类型
     * @param <T>           具体类型
     */
    <T> T selectOne(SelectEntity<T> selectEntity);

    /**
     *查询数据返回
     *
     * @param selectEntity 自定义sql实体类
     * @return          返回参数
     * @param <T>       具体类型
     */
    <T> List<T> selectList(SelectEntity<T> selectEntity);

    int insert(InsertEntity insertEntity);

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
