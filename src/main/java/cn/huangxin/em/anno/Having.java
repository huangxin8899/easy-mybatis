package cn.huangxin.em.anno;

import cn.huangxin.em.QueryType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Having {

    QueryType value() default QueryType.EQ;
}
