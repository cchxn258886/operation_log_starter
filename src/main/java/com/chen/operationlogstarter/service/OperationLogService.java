package com.chen.operationlogstarter.service;

import com.hzwotu.operationlogsdk.po.OperationLogEntity;

import java.sql.SQLException;

/**
 * @Author chenl
 * @Date 2022/9/14 3:00 下午
 * sendMessageToMQ fix operationLog to Es
 */
public interface OperationLogService {
    void sendMessageToMQ(OperationLogEntity entity) throws SQLException;
}
