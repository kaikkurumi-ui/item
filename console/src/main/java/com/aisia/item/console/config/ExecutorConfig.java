package com.aisia.item.console.config;

import com.alibaba.excel.annotation.ExcelProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ExecutorConfig {
    @Bean("ExcelExecutor")
    public ThreadPoolTaskExecutor commonExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数：常驻线程数
        executor.setCorePoolSize(10);
        // 最大线程数：极限情况下最多创建的线程数
        executor.setMaxPoolSize(10);
        // 队列容量：当核心线程满时，任务排队的队列大小
        executor.setQueueCapacity(200);
        // 线程名前缀
        executor.setThreadNamePrefix("common-pool-");
        // 空闲线程存活时间（秒）
        executor.setKeepAliveSeconds(60);

        // 拒绝策略：当线程池和队列都满了时的处理方式
        // CallerRunsPolicy 表示让调用者线程（如主线程）去执行，防止任务丢失
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 初始化
        executor.initialize();
        return executor;
    }
}
