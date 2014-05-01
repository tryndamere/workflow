package com.rocky.workflow.impl.db;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rocky.workflow.util.CollectionUtils;

/**
 * 基础的dao类，此处只是做流程配置参数查询
 * Created by rocky on 14-4-9.
 */
public class DbUtilsDao<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbUtilsDao.class);

    private QueryRunner queryRunner;

    private DataSource dataSource;
//为了兼容spring2.5 删除掉了
//    private Class<T> entityClass ;
//
//    public DbUtilsDao(){
//        entityClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0] ;
//    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public  List<T> select(String sql, Class<T> entityClass, Object ...params) throws SQLException {
        LOGGER.debug("select sql : {}" , sql);
        queryRunner = new QueryRunner(dataSource);
        ResultSetHandler<List<T>> resultSetHandler =  new BeanListHandler<T>(entityClass);
        return queryRunner.query(sql, resultSetHandler, params);
    }

    public T selectOne(String sql, Class<T> entityClass, Object ...params) throws SQLException {
        List<T> results = select(sql, entityClass, params);
        if (CollectionUtils.isNotEmpty(results) && results.size() == 1){
            return results.get(0);
        }
        return null;
    }
}
