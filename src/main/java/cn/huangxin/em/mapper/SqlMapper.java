package cn.huangxin.em.mapper;

import cn.huangxin.em.factory.InsertEntity;
import cn.huangxin.em.factory.SelectEntity;
import cn.huangxin.em.factory.UpdateEntity;
import cn.huangxin.em.util.AnnoUtil;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.exceptions.TooManyResultsException;
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
import java.util.Collection;
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

    @Override
    public <T> T selectOne(SelectEntity<T> selectEntity) {
        List<T> list = this.selectList(selectEntity.getSql().toString(), selectEntity.getParamMap(), selectEntity.getType());
        return this.getOne(list);
    }

    @Override
    public <T> List<T> selectList(SelectEntity<T> selectEntity) {
        return this.selectList(selectEntity.getSql().toString(), selectEntity.getParamMap(), selectEntity.getType());
    }

    @Override
    public int insert(InsertEntity insertEntity) {
        return this.sqlInsert(insertEntity.getScript(), insertEntity.getMateObj());
    }

    @Override
    public int update(UpdateEntity updateEntity) {
        return this.sqlUpdate(updateEntity.getSql().toString(), updateEntity.getParamMap());
    }

    public int updateBatchById(UpdateEntity updateEntity) {
        return this.sqlUpdate(updateEntity.getScript(), updateEntity.getMateObj());
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
     * 查询数据返回
     *
     * @param sql        sql语句
     * @param value      参数
     * @param resultType 具体类型
     * @return List<T>
     */
    private <T> List<T> selectList(String sql, Object value, Class<T> resultType) {
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

    /**
     * 插入数据
     *
     * @param sql   sql语句
     * @param value 参数
     * @return int
     */
    private int sqlInsert(String sql, Object value) {
        if (value == null) {
            return 0;
        }
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            MSUtils msUtils = new MSUtils(sqlSession.getConfiguration());
            Class<?> parameterType = value.getClass();
            if (Collection.class.isAssignableFrom(parameterType)) {
                parameterType = ((Collection<?>) value).toArray()[0].getClass();
            }
            String msId = msUtils.insertDynamic(sql, parameterType);
            return sqlSession.insert(msId, value);
        }
    }

    /**
     * 更新数据
     *
     * @param sql   sql语句
     * @param value 参数
     * @return int
     */
    private int sqlUpdate(String sql, Object value) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            MSUtils msUtils = new MSUtils(sqlSession.getConfiguration());
            Class<?> parameterType = value != null ? value.getClass() : null;
            String msId = msUtils.updateDynamic(sql, parameterType);
            return sqlSession.update(msId, value);
        }
    }

    private int sqlUpdateBatch(List<UpdateEntity> updateEntityList) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            MSUtils msUtils = new MSUtils(sqlSession.getConfiguration());
            int i = 0;
            for (UpdateEntity updateEntity : updateEntityList) {
                Object value = updateEntity.getParamMap();
                Class<?> parameterType = value != null ? value.getClass() : null;
                String msId = msUtils.updateDynamic(updateEntity.getSql().toString(), parameterType);
                i += sqlSession.update(msId, value);
            }
            return i;
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

        private void newInsertMappedStatement(String msId, SqlSource sqlSource, Class<?> parameterType) {
            this.configuration.setUseGeneratedKeys(true);
            MappedStatement ms = (new MappedStatement.Builder(this.configuration, msId, sqlSource, SqlCommandType.INSERT)).resultMaps(new ArrayList<ResultMap>() {
                {
                    this.add((new ResultMap.Builder(MSUtils.this.configuration, "defaultResultMap", Integer.TYPE, new ArrayList(0))).build());
                }
            }).keyProperty(AnnoUtil.getPrimaryName(parameterType)).keyColumn(AnnoUtil.getPrimaryColumn(parameterType)).build();
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

        private String insertDynamic(String sql, Class<?> parameterType) {
            String msId = this.newMsId(sql + parameterType, SqlCommandType.INSERT);
            if (!this.hasMappedStatement(msId)) {
                SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, parameterType);
                this.newInsertMappedStatement(msId, sqlSource, parameterType);
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
