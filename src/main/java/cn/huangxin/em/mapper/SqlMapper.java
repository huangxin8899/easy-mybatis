package cn.huangxin.em.mapper;

import cn.huangxin.em.SqlEntity;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 黄鑫
 * @description SqlMapper
 */
@Component
public class SqlMapper implements SqlBaseMapper {

    @Resource
    SqlSessionFactory sqlSessionFactory;

    public SqlMapper() {
    }

    private <T> T getOne(List<T> list) {
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    /**
     * 查询单条数据返回Map<String, Object>
     *
     * @param sql sql语句
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> selectOne(String sql) {
        List<Map<String, Object>> list = this.selectList(sql);
        return (Map) this.getOne(list);
    }

    /**
     * 查询单条数据返回Map<String, Object>
     *
     * @param sql   sql语句
     * @param value 参数
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> selectOne(String sql, Object value) {
        List<Map<String, Object>> list = this.selectList(sql, value);
        return (Map) this.getOne(list);
    }

    /**
     * 查询单条数据返回实体类型
     *
     * @param sql        sql语句
     * @param resultType 具体类型
     * @return 定义的实体类型
     */
    @Override
    public <T> T selectOne(String sql, Class<T> resultType) {
        List<T> list = this.selectList(sql, resultType);
        return this.getOne(list);
    }

    /**
     * 查询单条数据返回实体类型
     *
     * @param sql        sql语句
     * @param value      参数
     * @param resultType 具体类型
     * @return 定义的实体类型
     */
    @Override
    public <T> T selectOne(String sql, Object value, Class<T> resultType) {
        List<T> list = this.selectList(sql, value, resultType);
        return this.getOne(list);
    }

    @Override
    public <T> T selectOne(SqlEntity<T> sqlEntity) {
        List<T> list = this.selectList(sqlEntity.getSql().toString(), sqlEntity.getParamMap(), sqlEntity.getType());
        return this.getOne(list);
    }

    /**
     * 查询数据返回
     *
     * @param sql sql语句
     * @return List<Map < String, Object>>
     */
    @Override
    public List<Map<String, Object>> selectList(String sql) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            MSUtils msUtils = new MSUtils(sqlSession.getConfiguration());
            String msId = msUtils.select(sql);
            return sqlSession.selectList(msId);
        }
    }

    /**
     * 查询数据返回
     *
     * @param sql   sql语句
     * @param value 参数
     * @return List<Map < String, Object>>
     */
    @Override
    public List<Map<String, Object>> selectList(String sql, Object value) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            MSUtils msUtils = new MSUtils(sqlSession.getConfiguration());
            Class<?> parameterType = value != null ? value.getClass() : null;
            String msId = msUtils.selectDynamic(sql, parameterType);
            return sqlSession.selectList(msId, value);
        }
    }

    /**
     * 查询数据返回
     *
     * @param sql        sql语句
     * @param resultType 具体类型
     * @return List<T>
     */
    @Override
    public <T> List<T> selectList(String sql, Class<T> resultType) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            MSUtils msUtils = new MSUtils(sqlSession.getConfiguration());
            String msId;
            if (resultType == null) {
                msId = msUtils.select(sql);
            } else {
                msId = msUtils.select(sql, resultType);
            }

            return sqlSession.selectList(msId);
        }
    }

    /**
     * 查询数据返回
     *
     * @param sql        sql语句
     * @param value      参数
     * @param resultType 具体类型
     * @return List<T>
     */
    @Override
    public <T> List<T> selectList(String sql, Object value, Class<T> resultType) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            MSUtils msUtils = new MSUtils(sqlSession.getConfiguration());
            Class<?> parameterType = value != null ? value.getClass() : null;
            String msId;
            if (resultType == null) {
                msId = msUtils.selectDynamic(sql, parameterType);
            } else {
                msId = msUtils.selectDynamic(sql, parameterType, resultType);
            }

            return sqlSession.selectList(msId, value);
        }
    }

    @Override
    public <T> List<T> selectList(SqlEntity<T> sqlEntity) {
        return this.selectList(sqlEntity.getSql().toString(), sqlEntity.getParamMap(), sqlEntity.getType());
    }

    /**
     * 插入数据
     *
     * @param sql sql语句
     * @return int
     */
    @Override
    public int sqlInsert(String sql) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            MSUtils msUtils = new MSUtils(sqlSession.getConfiguration());
            String msId = msUtils.insert(sql);
            return sqlSession.insert(msId);
        }
    }

    /**
     * 插入数据
     *
     * @param sql   sql语句
     * @param value 参数
     * @return int
     */
    @Override
    public int sqlInsert(String sql, Object value) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            MSUtils msUtils = new MSUtils(sqlSession.getConfiguration());
            Class<?> parameterType = value != null ? value.getClass() : null;
            String msId = msUtils.insertDynamic(sql, parameterType);
            return sqlSession.insert(msId, value);
        }
    }

    /**
     * 更新数据
     *
     * @param sql sql语句
     * @return int
     */
    @Override
    public int sqlUpdate(String sql) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            MSUtils msUtils = new MSUtils(sqlSession.getConfiguration());
            String msId = msUtils.update(sql);
            return sqlSession.update(msId);
        }
    }

    /**
     * 更新数据
     *
     * @param sql   sql语句
     * @param value 参数
     * @return int
     */
    @Override
    public int sqlUpdate(String sql, Object value) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            MSUtils msUtils = new MSUtils(sqlSession.getConfiguration());
            Class<?> parameterType = value != null ? value.getClass() : null;
            String msId = msUtils.updateDynamic(sql, parameterType);
            return sqlSession.update(msId, value);
        }
    }

    /**
     * 删除数据
     *
     * @param sql sql语句
     * @return int
     */
    @Override
    public int sqlDelete(String sql) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            MSUtils msUtils = new MSUtils(sqlSession.getConfiguration());
            String msId = msUtils.delete(sql);
            return sqlSession.delete(msId);
        }
    }

    /**
     * 查询数据返回List<T>
     *
     * @param sql   sql语句
     * @param value 参数
     * @return int
     */
    @Override
    public int sqlDelete(String sql, Object value) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            MSUtils msUtils = new MSUtils(sqlSession.getConfiguration());
            Class<?> parameterType = value != null ? value.getClass() : null;
            String msId = msUtils.deleteDynamic(sql, parameterType);
            return sqlSession.delete(msId, value);
        }
    }

    /**
     * 进行预编译
     */
    private class MSUtils {
        private Configuration configuration;
        private LanguageDriver languageDriver;

        private MSUtils(Configuration configuration) {
            this.configuration = configuration;
            this.languageDriver = configuration.getDefaultScriptingLanguageInstance();
        }

        private String newMsId(String sql, SqlCommandType sqlCommandType) {
            StringBuilder msIdBuilder = new StringBuilder(sqlCommandType.toString());
            msIdBuilder.append(".").append(sql.hashCode());
            return msIdBuilder.toString();
        }

        private boolean hasMappedStatement(String msId) {
            return this.configuration.hasStatement(msId, false);
        }

        private void newSelectMappedStatement(String msId, SqlSource sqlSource, final Class<?> resultType) {
            MappedStatement ms = (new MappedStatement.Builder(this.configuration, msId, sqlSource, SqlCommandType.SELECT)).resultMaps(new ArrayList<ResultMap>() {
                {
                    this.add((new ResultMap.Builder(MSUtils.this.configuration, "defaultResultMap", resultType, new ArrayList(0))).build());
                }
            }).build();
            this.configuration.addMappedStatement(ms);
        }

        private void newInsertMappedStatement(String msId, SqlSource sqlSource) {
            this.configuration.setUseGeneratedKeys(true);
            MappedStatement ms = (new MappedStatement.Builder(this.configuration, msId, sqlSource, SqlCommandType.INSERT)).resultMaps(new ArrayList<ResultMap>() {
                {
                    this.add((new ResultMap.Builder(MSUtils.this.configuration, "defaultResultMap", Integer.TYPE, new ArrayList(0))).build());
                }
            }).keyProperty("id").build();
            this.configuration.addMappedStatement(ms);
        }

        private void newUpdateMappedStatement(String msId, SqlSource sqlSource, SqlCommandType sqlCommandType) {
            MappedStatement ms = (new MappedStatement.Builder(this.configuration, msId, sqlSource, sqlCommandType)).resultMaps(new ArrayList<ResultMap>() {
                {
                    this.add((new ResultMap.Builder(MSUtils.this.configuration, "defaultResultMap", Integer.TYPE, new ArrayList(0))).build());
                }
            }).build();
            this.configuration.addMappedStatement(ms);
        }

        private String select(String sql) {
            String msId = this.newMsId(sql, SqlCommandType.SELECT);
            if (!this.hasMappedStatement(msId)) {
                StaticSqlSource sqlSource = new StaticSqlSource(this.configuration, sql);
                this.newSelectMappedStatement(msId, sqlSource, Map.class);
            }
            return msId;
        }

        private String selectDynamic(String sql, Class<?> parameterType) {
            String msId = this.newMsId(sql + parameterType, SqlCommandType.SELECT);
            if (!this.hasMappedStatement(msId)) {
                SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, parameterType);
                this.newSelectMappedStatement(msId, sqlSource, Map.class);
            }
            return msId;
        }

        private String select(String sql, Class<?> resultType) {
            String msId = this.newMsId(resultType + sql, SqlCommandType.SELECT);
            if (!this.hasMappedStatement(msId)) {
                StaticSqlSource sqlSource = new StaticSqlSource(this.configuration, sql);
                this.newSelectMappedStatement(msId, sqlSource, resultType);
            }
            return msId;
        }

        private String selectDynamic(String sql, Class<?> parameterType, Class<?> resultType) {
            String msId = this.newMsId(resultType + sql + parameterType, SqlCommandType.SELECT);
            if (!this.hasMappedStatement(msId)) {
                SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, parameterType);
                this.newSelectMappedStatement(msId, sqlSource, resultType);
            }
            return msId;
        }

        private String insert(String sql) {
            String msId = this.newMsId(sql, SqlCommandType.INSERT);
            if (!this.hasMappedStatement(msId)) {
                StaticSqlSource sqlSource = new StaticSqlSource(this.configuration, sql);
                this.newInsertMappedStatement(msId, sqlSource);
            }
            return msId;
        }

        private String insertDynamic(String sql, Class<?> parameterType) {
            String msId = this.newMsId(sql + parameterType, SqlCommandType.INSERT);
            if (!this.hasMappedStatement(msId)) {
                SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, parameterType);
                this.newInsertMappedStatement(msId, sqlSource);
            }
            return msId;
        }

        private String update(String sql) {
            String msId = this.newMsId(sql, SqlCommandType.UPDATE);
            if (!this.hasMappedStatement(msId)) {
                StaticSqlSource sqlSource = new StaticSqlSource(this.configuration, sql);
                this.newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);
            }
            return msId;
        }

        private String updateDynamic(String sql, Class<?> parameterType) {
            String msId = this.newMsId(sql + parameterType, SqlCommandType.UPDATE);
            if (!this.hasMappedStatement(msId)) {
                SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, parameterType);
                this.newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);
            }
            return msId;
        }

        private String delete(String sql) {
            String msId = this.newMsId(sql, SqlCommandType.DELETE);
            if (!this.hasMappedStatement(msId)) {
                StaticSqlSource sqlSource = new StaticSqlSource(this.configuration, sql);
                this.newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);
            }
            return msId;
        }

        private String deleteDynamic(String sql, Class<?> parameterType) {
            String msId = this.newMsId(sql + parameterType, SqlCommandType.DELETE);
            if (!this.hasMappedStatement(msId)) {
                SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, parameterType);
                this.newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);
            }
            return msId;
        }
    }
}
