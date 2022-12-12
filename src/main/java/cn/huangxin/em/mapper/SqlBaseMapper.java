package cn.huangxin.em.mapper;

import cn.huangxin.em.factory.InsertEntity;
import cn.huangxin.em.factory.SelectEntity;
import cn.huangxin.em.factory.UpdateEntity;

import java.util.List;

/**
 * @author 黄鑫
 * @description SqlBaseMapper
 */
public interface SqlBaseMapper {

    /**
     * 查询单条数据返回实体类型
     *
     * @param selectEntity 自定义sql实体类
     * @param <T>          具体类型
     * @return 定义的实体类型
     */
    <T> T selectOne(SelectEntity<T> selectEntity);

    /**
     * 查询数据返回
     *
     * @param selectEntity 自定义sql实体类
     * @param <T>          具体类型
     * @return 返回参数
     */
    <T> List<T> selectList(SelectEntity<T> selectEntity);

    int insert(InsertEntity insertEntity);

    int update(UpdateEntity updateEntity);

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
