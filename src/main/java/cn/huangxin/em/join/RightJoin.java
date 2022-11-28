package cn.huangxin.em.join;

import cn.huangxin.em.SerializableFunction;

/**
 * @author 黄鑫
 * @description LeftJoin
 */
public class RightJoin extends AbstractJoin<RightJoin> {

    public <E, T> RightJoin(SerializableFunction<E, ?> left, SerializableFunction<T, ?> right) {
        init(left, right);
    }


    public RightJoin(String left, String right) {
        init(left, right);
    }


}
