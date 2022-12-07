package cn.huangxin.em.util;

import cn.huangxin.em.anno.From;
import cn.huangxin.em.anno.PrimaryKey;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static cn.huangxin.em.util.CommonUtil.camelToUnderscore;

/**
 * AnnoUtil
 *
 * @author 黄鑫
 */
public class AnnoUtil {

    public static String getFieldName(Field field) {
        if (field.isAnnotationPresent(cn.huangxin.em.anno.Field.class)) {
            return field.getAnnotation(cn.huangxin.em.anno.Field.class).value();
        }
        return camelToUnderscore(field.getName());
    }

    public static String getTableName(Class<?> aClass) {
        if (aClass.isAnnotationPresent(From.class)) {
            return aClass.getAnnotation(From.class).value();
        }
        return camelToUnderscore(Introspector.decapitalize(aClass.getSimpleName()));
    }

    public static String getPrimaryName(Class<?> aClass) {
        List<Field> fields = CommonUtil.getFields(aClass, new ArrayList<>());
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                return field.getName();
            }
        }
        return "id";
    }

    public static String getPrimaryColumn(Class<?> aClass) {
        List<Field> fields = CommonUtil.getFields(aClass, new ArrayList<>());
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                return getFieldName(field);
            }
        }
        return "id";
    }
}
