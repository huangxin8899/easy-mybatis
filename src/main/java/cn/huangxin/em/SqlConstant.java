package cn.huangxin.em;

/**
 * @author 黄鑫
 * @description SqlConstant
 */
public interface SqlConstant {
    String SELECT = "SELECT";
    String SELECT_ = "SELECT ";

    String INSERT_INTO = "INSERT INTO";
    String INSERT_INTO_ = "INSERT INTO ";

    String VALUES_ = "VALUE ";

    String ITEM = "item";

    String PRE_SCRIPT = "<script>\n";
    String POST_SCRIPT = "\n</script>";

    String PRE_BATCH_SCRIPT = "<foreach collection=\"list\" separator=\",\" item=\"" + ITEM + "\">\n";
    String POST_BATCH_SCRIPT = "\n</foreach>";


    String FROM = "FROM";
    String FROM_ = "FROM ";

    String JOIN = "JOIN";
    String JOIN_ = "JOIN ";

    String LEFT_JOIN = "LEFT JOIN";
    String LEFT_JOIN_ = "LEFT JOIN ";


    String ON = "ON";
    String ON_ = "ON ";
    String _ON_ = " ON ";


    String AND = "AND";
    String AND_ = "AND ";
    String _AND_ = " AND ";

    String AS = "AS";
    String _AS = " AS";
    String AS_ = "AS ";
    String _AS_ = " AS ";

    String BETWEEN = "BETWEEN";
    String BETWEEN_ = "BETWEEN ";
    String _BETWEEN_ = " BETWEEN ";

    String DOT = ".";

    String SPACE = " ";

    String COMMA = ",";
    String COMMA_ = ", ";

    String EQUAL = "=";
    String EQUAL_ = "= ";
    String _EQUAL_ = " = ";

    String NOT_EQUAL = "<>";
    String NOT_EQUAL_ = "<> ";
    String _NOT_EQUAL_ = " <> ";

    String GT = ">";
    String GT_ = "> ";
    String _GT_ = " > ";

    String GE = ">=";
    String GE_ = ">= ";
    String _GE_ = " >= ";

    String LT = "<";
    String LT_ = "< ";
    String _LT_ = " < ";

    String LE = "<=";
    String LE_ = "<= ";
    String _LE_ = " <= ";

    String _LIKE_ = " LIKE ";
    String LIKE_ = "LIKE ";

    String ARG = "arg";

    String PRE_PARAM = "#{";
    String POST_PARAM = "}";

    String PRE_IN = "(";
    String POST_IN = ")";

    String PRE_CONCAT = "CONCAT('%',";
    String POST_CONCAT = ",'%')";

    String NOT = "NOT";
    String NOT_ = "NOT ";
    String _NOT_ = " NOT ";

    String IN = "IN";
    String IN_ = "IN ";
    String _IN_ = " IN ";

    String IS = "IS";
    String IS_ = "IS ";
    String _IS_ = " IS ";

    String NULL = "NULL";
    String NULL_ = " NULL ";
    String _NULL_ = " NULL ";

    String ASC = "ASC";
    String ASC_ = "ASC ";
    String _ASC_ = " ASC ";

    String DESC = "DESC";
    String DESC_ = "DESC ";
    String _DESC_ = " DESC ";

}
