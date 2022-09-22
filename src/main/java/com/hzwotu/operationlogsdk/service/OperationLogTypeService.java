package com.hzwotu.operationlogsdk.service;

import com.hzwotu.operationlogsdk.po.OperationLogTypeEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author chenl
 * @Date 2022/9/15 5:59 下午
 */
public interface OperationLogTypeService {
    List<OperationLogTypeEntity> selectAll() throws SQLException;
}
