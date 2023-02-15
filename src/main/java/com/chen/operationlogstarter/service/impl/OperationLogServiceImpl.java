package com.chen.operationlogstarter.service.impl;

import com.alibaba.fastjson2.JSON;
import com.hzwotu.operationlogsdk.mq.MqProducer;
import com.hzwotu.operationlogsdk.po.OperationLogEntity;
import com.hzwotu.operationlogsdk.service.OperationLogService;
import com.wotu.sdk.rocketmqhttp.RocketmqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;


/**
 * @Author chenl
 * @Date 2023/1/10 11:27 上午
 * 发送MQ
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {
    private static final String OPERATION_LOG_SAVE = "TAG_OPERATION_LOG_SAVE";
    private static final Logger log = LoggerFactory.getLogger(OperationLogServiceImpl.class);

    @Autowired
    MqProducer mqProducer;

    @Override
    public void sendMessageToMQ(OperationLogEntity entity) {
        try {
            String mqJson = JSON.toJSONString(entity);
            RocketmqMessage rocketmqMessage = new RocketmqMessage();
            if (Objects.isNull(mqJson)) {
                mqJson = "";
            }
            rocketmqMessage.setContent(mqJson.getBytes(StandardCharsets.UTF_8));
            rocketmqMessage.setTag(OPERATION_LOG_SAVE);
            String messageId = mqProducer.sendSync(rocketmqMessage);
            log.debug("===> messageID:{}", messageId);
        } catch (Exception e) {
            log.error("====> OperationLogServiceESImpl#sendMQ:", e);
            throw new RuntimeException("发送消息失败");
        }
    }
}
