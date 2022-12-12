package cn.huangxin.em.factory;

import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 黄鑫
 * @description SqlEntity
 */
public class SelectEntity<T> extends ConditionEntity<SelectEntity<T>> {

    private Class<T> type;

    private final List<String> selectList = new ArrayList<>();
    private final List<String> fromList = new ArrayList<>();
    private final List<String> joinList = new ArrayList<>();
    private final List<String> innerJoinList = new ArrayList<>();
    private final List<String> outerJoinList = new ArrayList<>();
    private final List<String> leftOuterJoinList = new ArrayList<>();
    private final List<String> rightOuterJoinList = new ArrayList<>();
    private final List<String> orderByList = new ArrayList<>();
    private final List<String> groupByList = new ArrayList<>();
    private final List<String> havingList = new ArrayList<>();

    List<String> getSelectList() {
        return selectList;
    }

    List<String> getFromList() {
        return fromList;
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

    public SelectEntity(Object mateObj, Class<T> type) {
        this.mateObj = mateObj;
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }


}
