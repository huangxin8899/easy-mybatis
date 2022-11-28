package cn.huangxin.em.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultField {

    /**
     * 表名
     */
    String tableName() default "";

    /**
     * 表中的字段名
     */
    String fieldName() default "";
}
