package cn.huangxin.demo;

import cn.huangxin.demo.domain.Hero;
import cn.huangxin.demo.domain.Way;
import cn.huangxin.demo.domain.vo.HeroVo;
import cn.huangxin.em.SqlEntity;
import cn.huangxin.em.factory.SelectFactory;
import cn.huangxin.em.join.LeftJoin;
import cn.huangxin.em.mapper.SqlMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * AppTest
 *
 * @author 黄鑫
 */
@SpringBootTest
public class AppTest {

    @Resource
    private SqlMapper sqlMapper;

    @Test
    void testDemo() {
        Hero hero = new Hero();
        hero.setName("李青");
        SqlEntity<HeroVo> sqlEntity = SelectFactory.getSql(hero, HeroVo.class, new LeftJoin(Hero::getWayId, Way::getId));
        HeroVo heroVo = sqlMapper.selectOne(sqlEntity);
        System.out.println(heroVo);
    }
}
