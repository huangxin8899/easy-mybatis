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
 * 提示：SqlMapper,SqlEntity,LeftJoin,SelectFactory均为easy-mybatis内置类
 * SqlMapper提供执行操作
 * SqlEntity为sql执行对象
 * LeftJoin封装联表操作
 * SelectFactory提供SqlEntity的构造
 *
 * @author 黄鑫
 */
@SpringBootTest
public class AppTest {

    @Resource
    private SqlMapper sqlMapper;

    @Test
    void testOne() {
        Hero hero = new Hero();
        hero.setName("李青");
         // 等价于,SELECT id AS id, way_id AS wayId, name AS name FROM hero WHERE (name LIKE CONCAT('%',?,'%'))
        SqlEntity<Hero> sqlEntity = SelectFactory.getSql(hero);
        Hero selectOne = sqlMapper.selectOne(sqlEntity);
        System.out.println(selectOne);
    }

    @Test
    void testById() {
        // 等价于，SELECT id AS id, way_id AS wayId, name AS name FROM hero WHERE (id = ?)
        SqlEntity<Hero> sqlEntity = SelectFactory.getSqlById(1, Hero.class);
        Hero hero = sqlMapper.selectOne(sqlEntity);
        System.out.println(hero);
    }

    @Test
    void testJoin() {
        Hero hero = new Hero();
        hero.setName("李青");
        // 等价于：LEFT OUTER JOIN way ON hero.way_id = way.id AND way.way = ?
        LeftJoin leftJoin = new LeftJoin(Hero::getWayId, Way::getId);
        leftJoin.eq(Way::getWay, "打野");
        /*
        等价于：
        SELECT hero.id AS heroId, name AS name, way AS way
        FROM hero
        LEFT OUTER JOIN way ON hero.way_id = way.id AND way.way = ?
        WHERE (name LIKE CONCAT('%',?,'%'))
         */
        SqlEntity<HeroVo> sqlEntity = SelectFactory.getSql(hero, HeroVo.class, leftJoin);
        HeroVo heroVo = sqlMapper.selectOne(sqlEntity);
        System.out.println(heroVo);
    }
}
