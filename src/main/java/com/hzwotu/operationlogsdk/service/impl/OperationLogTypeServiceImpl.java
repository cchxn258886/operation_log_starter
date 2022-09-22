package com.hzwotu.operationlogsdk.service.impl;

import com.hzwotu.operationlogsdk.aspect.config.OperationLogConfigProperties;
import com.hzwotu.operationlogsdk.po.OperationLogTypeEntity;
import com.hzwotu.operationlogsdk.service.OperationLogTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author chenl
 * @Date 2022/9/15 6:00 下午
 */
@Service
public class OperationLogTypeServiceImpl implements OperationLogTypeService {
    private final Logger logger = LoggerFactory.getLogger(OperationLogServiceImpl.class);

    private static final String JDBC_TYPE_NAME = "com.mysql.cj.jdbc.Driver";
    @Autowired
    OperationLogConfigProperties operationLogConfigProperties;


    @Override
    public List<OperationLogTypeEntity> selectAll() throws SQLException {
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

        String sql = "select * from operation_module;";
        ArrayList<OperationLogTypeEntity> result = new ArrayList<>();
        Statement statement = null;
        try {
             statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Integer code = resultSet.getInt("id");
                String urlName = resultSet.getString("en_name");
                String resultName = resultSet.getString("zh_name");
                OperationLogTypeEntity operationLogTypeEntity = new OperationLogTypeEntity(code, urlName, resultName);
                result.add(operationLogTypeEntity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            statement.close();
            connection.close();
        }
        return result;
    }
}
