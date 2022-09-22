package com.hzwotu.operationlogsdk.service.impl;

import com.hzwotu.operationlogsdk.aspect.config.OperationLogConfigProperties;
import com.hzwotu.operationlogsdk.po.OperationLogEntity;
import com.hzwotu.operationlogsdk.service.OperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @Author chenl
 * @Date 2022/9/14 3:01 下午
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {
    private final Logger logger = LoggerFactory.getLogger(OperationLogServiceImpl.class);

    private static final String JDBC_TYPE_NAME = "com.mysql.cj.jdbc.Driver";
    @Autowired
    OperationLogConfigProperties operationLogConfigProperties;

    @Override
    public void insert(OperationLogEntity entity) throws SQLException {
        if (Objects.isNull(entity)) {
            return;
        }
        logger.debug("preparedStatementEntity:"+entity);
        try {
            Class.forName(JDBC_TYPE_NAME);
        } catch (Exception e) {
            throw new RuntimeException("没有mysql-jdbc类");
        }


        Connection connection = null;
        try {
            connection = DriverManager.getConnection(operationLogConfigProperties.getJdbcUrl(), operationLogConfigProperties.getUserName(), operationLogConfigProperties.getPassWord());
        } catch (SQLException e) {
            throw new RuntimeException("jdbcurl配置存在问题");
        }

        PreparedStatement preparedStatement = null;
        try {
            String sqlString = "insert into operation_log(code,module ,action,key_code,param ,admin_code,ip,created_at) values (?,?,?,?,?,?,?,?);";
            preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, entity.getCode());
            preparedStatement.setString(2, entity.getModule());
            preparedStatement.setObject(3, entity.getAction());
            preparedStatement.setString(4, entity.getKeyCode());
            preparedStatement.setString(5, entity.getParam());
            preparedStatement.setString(6, entity.getAdminCode());
            preparedStatement.setString(7, entity.getIp());
            preparedStatement.setObject(8, entity.getCreatedAt());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("插入数据库失败:", ex);
        } finally {
            preparedStatement.close();
            connection.close();
        }

    }
}

