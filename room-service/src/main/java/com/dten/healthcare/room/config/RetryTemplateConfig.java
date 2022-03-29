package com.dten.healthcare.room.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.net.SocketTimeoutException;
import java.util.Collections;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/8/27
 * @Description:
 */
@Configuration
public class RetryTemplateConfig {
    private static final Logger logger = LoggerFactory.getLogger(RetryTemplateConfig.class);
    private static final int attempts = 9;
    private static final long interval = 6000L;
    private static final long timeOut = 6000L;
    private static final double multiplier = 2;

    @Bean(name = "retryTemplate")
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // 超时重试策略
//        TimeoutRetryPolicy retryPolicy = new TimeoutRetryPolicy();
//        retryPolicy.setTimeout(timeOut);
//        retryTemplate.setRetryPolicy(retryPolicy);
//        retryTemplate.setBackOffPolicy(new NoBackOffPolicy());

        // 简单重试策略
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(attempts, Collections.singletonMap(SocketTimeoutException.class, true));
        retryTemplate.setRetryPolicy(retryPolicy);

        /// 固定回退时间(秒)
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(interval);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        // 指数回退(秒)，1s,2s,4s...
//        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
//        exponentialBackOffPolicy.setInitialInterval(interval);
//        exponentialBackOffPolicy.setMultiplier(multiplier);
//        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

        RetryListener[] listeners = {
                new RetryListener() {
                    @Override
                    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                        logger.info("RetryTemplateConfig: open");
                        return true;
                    }

                    @Override
                    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
                                                               Throwable throwable) {
                        logger.info("RetryTemplateConfig: close");
                    }

                    @Override
                    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
                                                                 Throwable throwable) {
                        logger.info("RetryTemplateConfig: onError");
                    }
                }
        };
        retryTemplate.setListeners(listeners);

        return retryTemplate;
    }
}
