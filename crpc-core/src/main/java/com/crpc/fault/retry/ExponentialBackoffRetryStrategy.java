package com.crpc.fault.retry;

import com.github.rholder.retry.*;
import com.crpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 递增间隔重试
 */
@Slf4j
public class ExponentialBackoffRetryStrategy implements RetryStrategy {

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws ExecutionException, RetryException {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class) // 定义失败的条件
                .withWaitStrategy(WaitStrategies.incrementingWait(0L  , TimeUnit.SECONDS , 3L , TimeUnit.SECONDS)) // 使用自定义的等待策略
                .withStopStrategy(StopStrategies.stopAfterAttempt(3)) // 最多重试三次
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("重试次数 {} 当前重试时长 {}", attempt.getAttemptNumber() , attempt.getAttemptNumber());
                    }
                })
                .build();
        return retryer.call(callable);
    }

}
