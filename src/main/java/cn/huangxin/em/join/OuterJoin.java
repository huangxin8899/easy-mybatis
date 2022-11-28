package cn.huangxin.em.join;

import cn.huangxin.em.SerializableFunction;

/**
 * @author 黄鑫
 * @description LeftJoin
 */
public class OuterJoin extends AbstractJoin<OuterJoin> {

    public <E, T> OuterJoin(SerializableFunction<E, ?> left, SerializableFunction<T, ?> right) {
        init(left, right);
    }


    public OuterJoin(String left, String right) {
        init(left, right);
    }


}
