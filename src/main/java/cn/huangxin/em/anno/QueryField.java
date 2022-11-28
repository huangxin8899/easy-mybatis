package cn.huangxin.em.anno;

import cn.huangxin.em.QueryType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 黄鑫
 * @description QueryField
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryField {

    /**
     * 表中的字段名
     */
    String fieldName() default "";

    QueryType type() default QueryType.EQ;

}
