# easy-mybatis
一个在mybatis基础上封装的工具

# 用法

## 准备工作

现有一张`way`表和一张`hero`表，如下：
way表：

| **id** | **way** |
| ------ | ------- |
| 1      | 上路    |
| 2      | 打野    |
| 3      | 中路    |
| 4      | 下路    |
| 5      | 辅助    |

hero表：

| **id** | **way_id** | **name** |
| ------ | ---------- | -------- |
| 1      | 1          | 菲欧娜   |
| 2      | 1          | 盖伦     |
| 3      | 2          | 李青     |
| 4      | 2          | 格雷福斯 |
| 5      | 3          | 瑞兹     |
| 6      | 3          | 卡特琳娜 |
| 7      | 4          | 艾希     |
| 8      | 4          | 凯特琳   |
| 9      | 5          | 索拉卡   |
| 10     | 5          | 娑娜     |

对应sql文件如下：
[em_demo.sql](https://www.yuque.com/attachments/yuque/0/2022/sql/12543633/1669689634848-106d2a88-c8c9-4748-b43d-e32a0f0b4fcb.sql?_lake_card=%7B%22src%22%3A%22https%3A%2F%2Fwww.yuque.com%2Fattachments%2Fyuque%2F0%2F2022%2Fsql%2F12543633%2F1669689634848-106d2a88-c8c9-4748-b43d-e32a0f0b4fcb.sql%22%2C%22name%22%3A%22em_demo.sql%22%2C%22size%22%3A2119%2C%22type%22%3A%22%22%2C%22ext%22%3A%22sql%22%2C%22source%22%3A%22%22%2C%22status%22%3A%22done%22%2C%22download%22%3Atrue%2C%22taskId%22%3A%22u08311f49-e354-409b-b9bc-a556130e0e8%22%2C%22taskType%22%3A%22upload%22%2C%22__spacing%22%3A%22both%22%2C%22id%22%3A%22u0aa5f421%22%2C%22margin%22%3A%7B%22top%22%3Atrue%2C%22bottom%22%3Atrue%7D%2C%22card%22%3A%22file%22%7D)

编写实体类：

Hero实体类

```java
/**
 * 此处@From注解声明表名
 * 若无注解，则默认表名为类名的驼峰转下划线
 * 例：HeroTb（类名） -> hero_tb（表名）
 */
@From("hero")
public class Hero {

    /**
     * 此处@PrimaryKey注解声明主键
     * 此处@Query注解声明查询条件的类型，默认为相等(QueryType.EQ)
     * 此处@Field注解声明表中的字段名，若无此注解，默认为变量名的驼峰转下划线
     * 解析格式为：{Field} = #{arg}
     * 下方注解等价于，id = #{id}
     */
    @PrimaryKey
    @Query(QueryType.EQ)
    @Field("id")
    private Integer id;
    /**
     * 等价于，way_id = #{wayId}
     */
    @Query
    private Integer wayId;
    /**
     * 等价于，name like CONCAT('%',#{name},'%')
     */
    @Query(QueryType.LIKE)
    private String name;

    // getter and setter...
}
```

way实体类

```java
public class Way {

    @PrimaryKey
    @Query
    private Integer id;
    @Query(QueryType.LIKE)
    private String way;
}
```

hero查询返回实体类

```java
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
}
```

## 开始使用

```java
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
        SqlEntity<Hero> selectEntity = SelectFactory.getSql(hero);
        Hero selectOne = sqlMapper.selectOne(selectEntity);
        System.out.println(selectOne);
    }

    @Test
    void testById() {
        // 等价于，SELECT id AS id, way_id AS wayId, name AS name FROM hero WHERE (id = ?)
        SqlEntity<Hero> selectEntity = SelectFactory.getSqlById(1, Hero.class);
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
        SqlEntity<HeroVo> selectEntity = SelectFactory.getSql(hero, HeroVo.class, leftJoin);
        HeroVo heroVo = sqlMapper.selectOne(selectEntity);
        System.out.println(heroVo);
    }
}

```