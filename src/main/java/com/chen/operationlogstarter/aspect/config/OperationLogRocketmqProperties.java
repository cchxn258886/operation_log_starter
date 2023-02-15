package com.chen.operationlogstarter.aspect.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author chenl
 * @Date 2023/2/1 5:44 下午
 */
@Configuration
@ConfigurationProperties(prefix = "wotu.rocket.operation.log")
public class OperationLogRocketmqProperties {
    /**
     * 若实例有命名空间，则实例ID必须传入；若实例无命名空间，则实例ID传入null空值或字符串空值
     */
    private String instanceId;
    /**
     * 主题名称
     */
    private String topicName;
    /**
     * 消费者组id
     */
    private String groupId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
