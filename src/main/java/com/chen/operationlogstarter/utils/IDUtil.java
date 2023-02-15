package com.chen.operationlogstarter.utils;

import java.util.UUID;

/**
 * @Author chenl
 * @Date 2023/2/1 5:37 下午
 */
public class IDUtil {
    public static String getIdGen() {
        return UUID.randomUUID().toString().replace("-", "").substring(0,24);
    }
}
