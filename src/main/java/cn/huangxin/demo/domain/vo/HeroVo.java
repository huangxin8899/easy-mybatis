package cn.huangxin.demo.domain.vo;

import cn.huangxin.em.anno.ResultField;

/**
 * HeroVo
 *
 * @author 黄鑫
 */
public class HeroVo {

    /**
     * 此处@ResultField注解声明返回字段，默认为变量名的驼峰映射下划线
     * tableName属性为表名
     * fieldName属性为表中的字段名
     * 解析格式为：{tableName}.{fieldName} AS 变量名
     * 下方注解等价于，hero.id AS heroId
     */
    @ResultField(tableName = "hero", fieldName = "id")
    private Integer heroId;
    /**
     * 等价于，name AS name
     */
    private String name;
    /**
     * 等价于，way AS way
     */
    private String way;

    public Integer getHeroId() {
        return heroId;
    }

    public void setHeroId(Integer heroId) {
        this.heroId = heroId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    @Override
    public String toString() {
        return "HeroVo{" +
                "heroId=" + heroId +
                ", name='" + name + '\'' +
                ", way='" + way + '\'' +
                '}';
    }
}
