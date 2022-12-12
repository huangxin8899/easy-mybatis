package cn.huangxin.em.factory;

import cn.huangxin.em.QueryType;
import cn.huangxin.em.SerializableFunction;
import cn.huangxin.em.SqlBuild;
import cn.huangxin.em.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * ConditionEntity
 *
 * @author 黄鑫
 */
public abstract class ConditionEntity<T extends ConditionEntity<T>> extends SqlEntity implements SqlBuild<T> {

    protected final List<String> whereList = new ArrayList<>();
    protected final Map<Integer, List<String>> orMap = new HashMap<>();
    protected Boolean isOr = Boolean.FALSE;
    protected Integer orNum = 0;

    List<String> getWhereList() {
        return whereList;
    }

    @Override
    public <R> T apply(boolean flag, QueryType queryType, SerializableFunction<R, ?> function, Object param) {
        if (flag) {
            String resolve = QueryType.resolve(queryType, this.getColumn(function), param, this.paramMap);
            if (CommonUtil.isNotEmptyStr(resolve)) {
                if (this.isOr) {
                    this.orMap.get(this.orNum).add(resolve);
                } else {
                    this.whereList.add(resolve);
                }
            }
        }
        return (T) this;
    }

    @Override
    public T apply(boolean flag, QueryType queryType, String column, Object param) {
        if (flag) {
            String resolve = QueryType.resolve(queryType, column, param, this.paramMap);
            if (CommonUtil.isNotEmptyStr(resolve)) {
                if (this.isOr) {
                    this.orMap.get(this.orNum).add(resolve);
                } else {
                    this.whereList.add(resolve);
                }
            }
        }
        return (T) this;
    }

    public T or(Consumer<T> consumer) {
        return or(true, consumer);
    }

    public T or(boolean flag, Consumer<T> consumer) {
        if (flag) {
            try {
                this.isOr = Boolean.TRUE;
                this.orNum++;
                this.orMap.put(this.orNum, new ArrayList<>());
                consumer.accept((T) this);
            } finally {
                this.isOr = Boolean.FALSE;
            }
        }
        return (T) this;
    }
}
