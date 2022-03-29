package com.dten.healthcare.room.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/8/27
 * @Description:
 */
@Configuration
@EnableAsync
public class AsyncTaskConfig implements AsyncConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTaskConfig.class);
    private static final int coreSize = 4;
    private static final int maxSize = 8;
    private static final int queueSize = 20;
    private static final int awaitTerminationSeconds = 60;
    private static final int keepAliveSeconds = 60;
    private static final String threadPrefix = "room-async-";

    @Bean(name = "getAsyncExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(coreSize);
        taskExecutor.setMaxPoolSize(maxSize);
        taskExecutor.setQueueCapacity(queueSize);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        taskExecutor.setAllowCoreThreadTimeOut(false);
        taskExecutor.setThreadNamePrefix(threadPrefix);

        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        logger.info("AsyncTaskConfig: getAsyncUncaughtExceptionHandler()");
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
