package com.chen.operationlogstarter.mq;

import com.hzwotu.operationlogsdk.aspect.config.OperationLogRocketmqProperties;
import com.wotu.sdk.rocketmqhttp.RocketmqMessage;
import com.wotu.sdk.rocketmqhttp.producer.AbstractHttpRocketmqProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author chenl
 * @Date 2023/2/1 7:28 下午
 */
@Component
public class MqProducer extends AbstractHttpRocketmqProducer {
    private static final Logger log = LoggerFactory.getLogger(MqProducer.class);



    public MqProducer(OperationLogRocketmqProperties operationLogRocketmqProperties) {
        super(operationLogRocketmqProperties.getInstanceId(), operationLogRocketmqProperties.getTopicName());
    }


    /**
     * 同步发送消息
     *
     * @param message 消息
     * @return 消息id
     */
    public String sendSync(RocketmqMessage message) {
        return super.sendSync(message);
    }
}
