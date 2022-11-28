package cn.huangxin.em.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderBy {

    /**
     * 表中的字段名
     */
    String fieldName() default "";

    /**
     * 排序方式：true=ASC,false=DESC
     */
    boolean sort() default true;

}
