package cn.huangxin.demo.domain;

import cn.huangxin.em.QueryType;
import cn.huangxin.em.anno.PrimaryKey;
import cn.huangxin.em.anno.Query;

/**
 * Way
 *
 * @author 黄鑫
 */
public class Way {

    @PrimaryKey
    @Query
    private Integer id;
    @Query(QueryType.LIKE)
    private String way;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }
}
