package com.chen.operationlogstarter.service;

import com.chen.operationlogstarter.po.OperationLogEntity;

import java.sql.SQLException;

/**
 * @Author chenl
 * @Date 2022/9/14 3:00 下午
 */
public interface OperationLogService {
    void insert(OperationLogEntity entity) throws SQLException;
}
