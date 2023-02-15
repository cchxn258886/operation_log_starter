package com.chen.operationlogstarter.aspect.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @Author chenl
 * @Date 2022/9/23 9:46 上午
 */
@Component
public class ThreadPoolConfigOperationLog {
    private final Logger logger = LoggerFactory.getLogger(ThreadPoolConfigOperationLog.class);

    /**
     * 单例直接当BEAN丢给Spring就好了
     *
     * */
    @Bean(destroyMethod = "shutdown")
    public ExecutorService threadPool(){
        logger.info("logMsgAnnotation 线程池启动成功");
        return new ThreadPoolExecutor(1,1,1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
    }
}
