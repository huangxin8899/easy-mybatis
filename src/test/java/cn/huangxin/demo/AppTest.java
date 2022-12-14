package cn.huangxin.demo;

import cn.huangxin.demo.domain.Hero;
import cn.huangxin.demo.domain.Way;
import cn.huangxin.demo.domain.vo.HeroVo;
import cn.huangxin.em.factory.InsertEntity;
import cn.huangxin.em.factory.InsertFactory;
import cn.huangxin.em.factory.SelectEntity;
import cn.huangxin.em.factory.SelectFactory;
import cn.huangxin.em.join.LeftJoin;
import cn.huangxin.em.mapper.SqlMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

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
        hero.setName("菲欧娜");
        // 等价于,SELECT id AS id, way_id AS wayId, name AS name FROM hero WHERE (name LIKE CONCAT('%',?,'%'))
        SelectEntity<Hero> selectEntity = SelectFactory.getSql(hero);
        System.out.println(selectEntity.getSql().toString());
//        Hero selectOne = sqlMapper.selectOne(sqlEntity);
//        System.out.println(selectOne);
    }

    @Test
    void testById() {
        // 等价于，SELECT id AS id, way_id AS wayId, name AS name FROM hero WHERE (id = ?)
        SelectEntity<Hero> selectEntity = SelectFactory.getSqlById(1, Hero.class);
        Hero hero = sqlMapper.selectOne(selectEntity);
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
        SelectEntity<HeroVo> selectEntity = SelectFactory.getSql(hero, HeroVo.class, leftJoin);
        HeroVo heroVo = sqlMapper.selectOne(selectEntity);
        System.out.println(heroVo);
    }

    @Test
    void testUpdate() {
        Hero hero = new Hero();
        hero.setId(10);
        hero.setWayId(5);
        sqlMapper.sqlUpdate("UPDATE `em_demo`.`hero` SET `way_id` = #{wayId} WHERE `id` = #{id}", hero);
        System.out.println(hero);
    }

    @Test
    void testInsert() {
        Hero hero = new Hero();
        hero.setWayId(5);
        hero.setName("布隆");
        Hero hero1 = new Hero();
        hero1.setWayId(5);
        hero1.setName("布隆");
        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero);
        heroes.add(hero1);
        InsertEntity insert = InsertFactory.getBatchSql(heroes);
        sqlMapper.insert(insert);
        System.out.println();
    }



    @Test
    void testHero() {

    }


}
