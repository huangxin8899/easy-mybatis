package cn.huangxin.em.factory;

import cn.huangxin.em.SqlBuild;
import cn.huangxin.em.QueryType;
import cn.huangxin.em.SerializableFunction;
import cn.huangxin.em.util.CommonUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author 黄鑫
 * @description SqlEntity
 */
public class SelectEntity<T> extends SqlEntity implements SqlBuild<SelectEntity<T>> {

    private Class<T> type;

    private final List<String> selectList = new ArrayList<>();
    private final List<String> fromList = new ArrayList<>();
    private final List<String> whereList = new ArrayList<>();
    private final List<String> joinList = new ArrayList<>();
    private final List<String> innerJoinList = new ArrayList<>();
    private final List<String> outerJoinList = new ArrayList<>();
    private final List<String> leftOuterJoinList = new ArrayList<>();
    private final List<String> rightOuterJoinList = new ArrayList<>();
    private final List<String> orderByList = new ArrayList<>();
    private final List<String> groupByList = new ArrayList<>();
    private final List<String> havingList = new ArrayList<>();
    private final Map<Integer, List<String>> orMap = new HashMap<>();
    private Boolean isOr = Boolean.FALSE;
    private Integer orNum = 0;

    List<String> getSelectList() {
        return selectList;
    }

    List<String> getFromList() {
        return fromList;
    }

    List<String> getWhereList() {
        return whereList;
    }

    List<String> getJoinList() {
        return joinList;
    }

    List<String> getInnerJoinList() {
        return innerJoinList;
    }

    List<String> getOuterJoinList() {
        return outerJoinList;
    }

    List<String> getLeftOuterJoinList() {
        return leftOuterJoinList;
    }

    List<String> getRightOuterJoinList() {
        return rightOuterJoinList;
    }

    List<String> getOrderByList() {
        return orderByList;
    }

    List<String> getGroupByList() {
        return groupByList;
    }

    List<String> getHavingList() {
        return havingList;
    }

    @Override
    public <R> SelectEntity<T> apply(boolean flag, QueryType queryType, SerializableFunction<R, ?> function, Object param) {
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
        return this;
    }

    @Override
    public SelectEntity<T> apply(boolean flag, QueryType queryType, String column, Object param) {
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
        return this;
    }

    @Override
    protected void inject() {
        this.sql = new SQL();
        if (this.selectList.size() > 0) {
            this.sql.SELECT(this.selectList.toArray(new String[this.selectList.size()]));
        }
        if (this.fromList.size() > 0) {
            this.sql.FROM(this.fromList.toArray(new String[this.fromList.size()]));
        }
        if (this.whereList.size() > 0) {
            this.sql.WHERE(this.whereList.toArray(new String[this.whereList.size()]));
        }
        if (this.joinList.size() > 0) {
            this.sql.JOIN(this.joinList.toArray(new String[this.joinList.size()]));
        }
        if (this.innerJoinList.size() > 0) {
            this.sql.INNER_JOIN(this.innerJoinList.toArray(new String[this.innerJoinList.size()]));
        }
        if (this.outerJoinList.size() > 0) {
            this.sql.OUTER_JOIN(this.outerJoinList.toArray(new String[this.outerJoinList.size()]));
        }
        if (this.leftOuterJoinList.size() > 0) {
            this.sql.LEFT_OUTER_JOIN(this.leftOuterJoinList.toArray(new String[this.leftOuterJoinList.size()]));
        }
        if (this.rightOuterJoinList.size() > 0) {
            this.sql.RIGHT_OUTER_JOIN(this.rightOuterJoinList.toArray(new String[this.rightOuterJoinList.size()]));
        }
        if (this.orderByList.size() > 0) {
            this.sql.ORDER_BY(this.orderByList.toArray(new String[this.orderByList.size()]));
        }
        if (this.groupByList.size() > 0) {
            this.sql.GROUP_BY(this.groupByList.toArray(new String[this.groupByList.size()]));
        }
        if (this.havingList.size() > 0) {
            this.sql.HAVING(this.havingList.toArray(new String[this.havingList.size()]));
        }
        for (List<String> list : this.orMap.values()) {
            this.sql.OR().WHERE(list.toArray(new String[list.size()]));
        }
    }

    public SelectEntity(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }


    public SelectEntity<T> or(Consumer<SelectEntity<T>> consumer) {
        return or(true, consumer);
    }

    public SelectEntity<T> or(boolean flag, Consumer<SelectEntity<T>> consumer) {
        if (flag) {
            try {
                this.isOr = Boolean.TRUE;
                this.orNum++;
                this.orMap.put(this.orNum, new ArrayList<>());
                consumer.accept(this);
            } finally {
                this.isOr = Boolean.FALSE;
            }
        }
        return this;
    }

}
