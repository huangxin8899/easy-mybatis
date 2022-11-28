package cn.huangxin.demo.domain.vo;

import cn.huangxin.em.anno.ResultField;

/**
 * HeroVo
 *
 * @author 黄鑫
 */
public class HeroVo {

    @ResultField(tableName = "hero", fieldName = "id")
    private Integer heroId;
    private String name;
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
