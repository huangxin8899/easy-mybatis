package cn.huangxin.em.util;

import cn.huangxin.em.SerializableFunction;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 黄鑫
 * @description LambdaUtil
 */
public class FunctionUtil {

    private static final Map<SerializableFunction<?, ?>, String> COLUMN_NAME_MAP = new ConcurrentHashMap<>();
    private static final Map<SerializableFunction<?, ?>, String> TABLE_NAME_MAP = new ConcurrentHashMap<>();
    private static final Map<SerializableFunction<?, ?>, Class<?>> CLASS_MAP = new ConcurrentHashMap<>();

    public static <T, R> String getColumnName(SerializableFunction<T, R> function) {
        String fieldName = COLUMN_NAME_MAP.get(function);
        if (fieldName == null) {
            resolve(function);
            fieldName = COLUMN_NAME_MAP.get(function);
        }
        return fieldName;
    }

    public static <T, R> String getTableName(SerializableFunction<T, R> function) {
        String className = TABLE_NAME_MAP.get(function);
        if (className == null) {
            resolve(function);
            className = TABLE_NAME_MAP.get(function);
        }
        return className;
    }

    public static <T, R> Class<?> getTableClass(SerializableFunction<T, R> function) {
        Class<?> clazz = CLASS_MAP.get(function);
        if (clazz == null) {
            resolve(function);
            CLASS_MAP.get(function);
        }
        return clazz;
    }

    private static <T, R> void resolve(SerializableFunction<T, R> function) {
        String fieldName = null;
        try {
            // 获取SerializedLambda
            Method method = function.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(function);
            // implMethodName 即为Field对应的Getter方法名
            String implMethodName = serializedLambda.getImplMethodName();
            if (implMethodName.startsWith("get") && implMethodName.length() > 3) {
                fieldName = Introspector.decapitalize(implMethodName.substring(3));

            } else if (implMethodName.startsWith("is") && implMethodName.length() > 2) {
                fieldName = Introspector.decapitalize(implMethodName.substring(2));
            } else if (implMethodName.startsWith("lambda$")) {
                throw new IllegalArgumentException("SerializableFunction不能传递lambda表达式,只能使用方法引用");

            } else {
                throw new IllegalArgumentException(implMethodName + "不是Getter方法引用");
            }
            // 获取的Class是字符串，并且包名是“/”分割，需要替换成“.”，才能获取到对应的Class对象
            int begin = serializedLambda.getInstantiatedMethodType().indexOf("L") + 1;
            int end = serializedLambda.getInstantiatedMethodType().indexOf(";");
            String className = serializedLambda.getInstantiatedMethodType().substring(begin, end).replace("/", ".");
            Class<?> clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
            Field field = clazz.getDeclaredField(fieldName);

            CLASS_MAP.put(function, clazz);
            COLUMN_NAME_MAP.put(function, AnnoUtil.getFieldName(field));
            TABLE_NAME_MAP.put(function, AnnoUtil.getTableName(clazz));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException |
                 NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

}
