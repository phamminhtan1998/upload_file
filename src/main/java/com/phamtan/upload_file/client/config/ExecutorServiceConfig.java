package com.phamtan.upload_file.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ExecutorServiceConfig {
    @Bean("custom-executor-bean")
    public ThreadPoolExecutor threadPoolExecutor(){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10,
                100,
                3, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(500)
        );
       return threadPoolExecutor;
    }
}
