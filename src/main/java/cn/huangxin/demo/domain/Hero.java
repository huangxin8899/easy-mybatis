package cn.huangxin.demo.domain;

import cn.huangxin.em.QueryType;
import cn.huangxin.em.anno.From;
import cn.huangxin.em.anno.PrimaryKey;
import cn.huangxin.em.anno.QueryField;

/**
 * Hero
 * 此处@From注解声明表名
 * 若无注解，则默认表名为类名的驼峰转下划线
 * 例：HeroTb（类名） -> hero_tb（表名）
 * @author 黄鑫
 */
@From("hero")
public class Hero {

    /**
     * 此处@PrimaryKey注解声明主键
     * 此处@QueryField注解声明查询条件
     * fieldName属性为表中的字段名，默认为变量名的驼峰转下划线
     * type属性为查询条件的类型，默认为相等(QueryType.EQ)
     * 解析格式为：{fieldName} = #{arg}
     * 下方注解等价于，id = #{id}
     */
    @PrimaryKey
    @QueryField(fieldName = "id", type = QueryType.EQ)
    private Integer id;
    /**
     * 等价于，way_id = #{wayId}
     */
    @QueryField
    private Integer wayId;
    /**
     * 等价于，name like CONCAT('%',#{name},'%')
     */
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
