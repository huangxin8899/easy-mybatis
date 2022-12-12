package cn.huangxin.em.join;

import cn.huangxin.em.QueryType;
import cn.huangxin.em.SerializableFunction;
import cn.huangxin.em.SqlBuild;
import cn.huangxin.em.SqlConstant;
import cn.huangxin.em.util.CommonUtil;
import cn.huangxin.em.util.FunctionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 黄鑫
 * @description Join
 */
public abstract class AbstractJoin<T> implements SqlBuild<T> {

    protected Class<?> leftClass;
    protected String leftTableName;
    protected String leftColumnName;
    protected Class<?> rightClass;
    protected String rightTableName;
    protected String rightColumnName;
    protected StringBuilder joinSegment = new StringBuilder();
    protected List<JoinCondition> conditions = new ArrayList<>();
    protected final T typeThis = (T) this;

    public Class<?> getLeftClass() {
        return leftClass;
    }

    public Class<?> getRightClass() {
        return rightClass;
    }

    protected void initSegment() {
        this.joinSegment.append(this.rightTableName)
                .append(SqlConstant._ON_)
                .append(this.leftTableName)
                .append(SqlConstant.DOT)
                .append(this.leftColumnName)
                .append(SqlConstant._EQUAL_)
                .append(this.rightTableName)
                .append(SqlConstant.DOT)
                .append(this.rightColumnName)
                .append(SqlConstant.SPACE);
    }

    protected <L, R> void init(SerializableFunction<L, ?> left, SerializableFunction<R, ?> right) {
        this.leftClass = FunctionUtil.getTableClass(left);
        this.leftTableName = FunctionUtil.getTableName(left);
        this.leftColumnName = FunctionUtil.getColumnName(left);
        this.rightClass = FunctionUtil.getTableClass(right);
        this.rightTableName = FunctionUtil.getTableName(right);
        this.rightColumnName = FunctionUtil.getColumnName(right);
        this.initSegment();
    }

    protected void init(String left, String right) {
        if (CommonUtil.isEmptyStr(left) || CommonUtil.isEmptyStr(right)) {
            throw new NullPointerException();
        }
        String[] lefts = left.split("\\.", 2);
        String[] rights = right.split("\\.", 2);
        if (lefts.length < 2 || rights.length < 2) {
            throw new IllegalArgumentException();
        }
        this.leftTableName = lefts[0];
        this.leftColumnName = lefts[1];
        this.rightTableName = rights[0];
        this.rightColumnName = rights[1];
        this.initSegment();
    }

    public String getJoinSegment(Map<String, Object> paramMap) {
        for (JoinCondition condition : this.conditions) {
            if (CommonUtil.isEmpty(condition.param)) {
                continue;
            }
            String resolve = QueryType.resolve(condition.queryType, condition.column, condition.param, paramMap);
            if (CommonUtil.isNotEmptyStr(resolve)) {
                this.joinSegment
                        .append(SqlConstant.AND_)
                        .append(resolve)
                        .append(SqlConstant.SPACE);
            }
        }
        return this.joinSegment.toString();
    }

    class JoinCondition {
        private QueryType queryType;
        private String column;
        private Object param;

        public JoinCondition(QueryType queryType, String column, Object param) {
            this.queryType = queryType;
            this.column = column;
            this.param = param;
        }
    }

    @Override
    public <R> T apply(boolean flag, QueryType queryType, SerializableFunction<R, ?> function, Object param) {
        if (flag) {
            this.conditions.add(new JoinCondition(queryType, this.getColumn(function), param));
        }
        return typeThis;
    }

    @Override
    public T apply(boolean flag, QueryType queryType, String column, Object param) {
        if (flag) {
            this.conditions.add(new JoinCondition(queryType, column, param));
        }
        return typeThis;
    }
}
