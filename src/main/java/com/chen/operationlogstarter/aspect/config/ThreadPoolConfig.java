package com.chen.operationlogstarter.aspect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @Author chenl
 * @Date 2022/9/23 9:46 上午
 */
@Component
public class ThreadPoolConfig {

    @Bean
    public ExecutorService threadPool(){
        return new ThreadPoolExecutor(1,1,1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
    }
}
