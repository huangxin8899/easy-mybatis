package cn.huangxin.demo.domain;

import cn.huangxin.em.QueryType;
import cn.huangxin.em.anno.From;
import cn.huangxin.em.anno.PrimaryKey;
import cn.huangxin.em.anno.QueryField;

/**
 * Hero
 *
 * @author 黄鑫
 */
@From("hero")
public class Hero {

    @PrimaryKey
    @QueryField
    private Integer id;
    @QueryField
    private Integer wayId;
    @QueryField(type = QueryType.LIKE)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWayId() {
        return wayId;
    }

    public void setWayId(Integer wayId) {
        this.wayId = wayId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
