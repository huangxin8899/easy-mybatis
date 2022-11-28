package cn.huangxin.em.join;

import cn.huangxin.em.SerializableFunction;

/**
 * @author 黄鑫
 * @description LeftJoin
 */
public class LeftJoin extends AbstractJoin<LeftJoin> {

    public <L, R> LeftJoin(SerializableFunction<L, ?> left, SerializableFunction<R, ?> right) {
        init(left, right);
    }


    public LeftJoin(String left, String right) {
        init(left, right);
    }


}
