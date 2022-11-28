package cn.huangxin.em.join;

import cn.huangxin.em.SerializableFunction;

/**
 * @author 黄鑫
 * @description LeftJoin
 */
public class InnerJoin extends AbstractJoin<InnerJoin> {

    public <E, T> InnerJoin(SerializableFunction<E, ?> left, SerializableFunction<T, ?> right) {
        init(left, right);
    }


    public InnerJoin(String left, String right) {
        init(left, right);
    }


}
