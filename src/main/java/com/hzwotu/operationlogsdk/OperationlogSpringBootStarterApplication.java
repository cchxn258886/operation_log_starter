package com.hzwotu.operationlogsdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class OperationlogSpringBootStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(OperationlogSpringBootStarterApplication.class, args);
    }

}
