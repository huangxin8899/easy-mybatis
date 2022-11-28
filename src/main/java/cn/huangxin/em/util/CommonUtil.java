package cn.huangxin.em.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 黄鑫
 * @description CommonUtil
 */
public class CommonUtil {

    /** 空字符串 */
    private static final String EMPTY = "";


    public static List<Field> getFields(Class<?> clazz, List<Field> fields) {
        if (clazz != null) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                fields.add(field);
            }
            getFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmptyStr(String str) {
        return !isEmptyStr(str);
    }

    public static boolean isEmptyStr(String str) {
        return isNull(str) || EMPTY.equals(str.trim());
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * 驼峰转下划线.
     * @param value
     * @return
     */
    public static String camelToUnderscore(String value) {
        String regex = "([A-Z])";
        Matcher matcher = Pattern.compile(regex).matcher(value);
        while (matcher.find()) {
            String target = matcher.group();
            value = value.replaceAll(target, "_"+target.toLowerCase());
        }
        return value;
    }

    // ---------------------------------------------------------------------- copy hutool

    /**
     * 集合是否为非空
     *
     * @param collection 集合
     * @return 是否为非空
     */
    public static boolean isNotEmptyCOll(Collection<?> collection) {
        return !isEmptyColl(collection);
    }

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmptyColl(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Map是否为空
     *
     * @param map 集合
     * @return 是否为空
     */
    public static boolean isEmptyMap(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    /**
     * Map是否为非空
     *
     * @param map 集合
     * @return 是否为非空
     */
    public static boolean isNotEmptyMap(Map<?, ?> map) {
        return null != map && !map.isEmpty();
    }

    /**
     * Iterable是否为空
     *
     * @param iterable Iterable对象
     * @return 是否为空
     */
    public static boolean isEmptyIte(Iterable<?> iterable) {
        return null == iterable || isEmptyIte(iterable.iterator());
    }

    /**
     * Iterator是否为空
     *
     * @param Iterator Iterator对象
     * @return 是否为空
     */
    public static boolean isEmptyIte(Iterator<?> Iterator) {
        return null == Iterator || !Iterator.hasNext();
    }

    /**
     * Iterable是否为空
     *
     * @param iterable Iterable对象
     * @return 是否为空
     */
    public static boolean isNotEmptyIte(Iterable<?> iterable) {
        return null != iterable && isNotEmptyIte(iterable.iterator());
    }

    /**
     * Iterator是否为空
     *
     * @param Iterator Iterator对象
     * @return 是否为空
     */
    public static boolean isNotEmptyIte(Iterator<?> Iterator) {
        return null != Iterator && Iterator.hasNext();
    }


    /**
     * 数组是否为空<br>
     * 此方法会匹配单一对象，如果此对象为{@code null}则返回true<br>
     * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回false<br>
     * 如果此对象为数组对象，数组长度大于0情况下返回false，否则返回true
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmptyArr(Object array) {
        if (array != null) {
            if (isArray(array)) {
                return 0 == Array.getLength(array);
            }
            return false;
        }
        return true;
    }

    /**
     * 数组是否为非空
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为非空
     */
    public static <T> boolean isNotEmptyArr(T[] array) {
        return (null != array && array.length != 0);
    }

    public static boolean isArray(Object obj) {
        return null != obj && obj.getClass().isArray();
    }

    /**
     * 判断指定对象是否为空，支持：
     *
     * <pre>
     * 1. CharSequence
     * 2. Map
     * 3. Iterable
     * 4. Iterator
     * 5. Array
     * </pre>
     *
     * @param obj 被判断的对象
     * @return 是否为空，如果类型不支持，返回false
     * @since 4.5.7
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }

        if (obj instanceof String) {
            return isEmptyStr((String) obj);
        } else if (obj instanceof Map) {
            return isEmptyMap((Map) obj);
        } else if (obj instanceof Iterable) {
            return isEmptyIte((Iterable) obj);
        } else if (obj instanceof Iterator) {
            return isEmptyIte((Iterator) obj);
        } else if (isArray(obj)) {
            return isEmptyArr(obj);
        }

        return false;
    }

}
