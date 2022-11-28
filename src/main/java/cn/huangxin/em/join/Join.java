package cn.huangxin.em.join;

import cn.huangxin.em.SerializableFunction;

/**
 * @author 黄鑫
 * @description LeftJoin
 */
public class Join extends AbstractJoin<Join> {

    public <E, T> Join(SerializableFunction<E, ?> left, SerializableFunction<T, ?> right) {
        init(left, right);
    }


    public Join(String left, String right) {
        init(left, right);
    }


}
