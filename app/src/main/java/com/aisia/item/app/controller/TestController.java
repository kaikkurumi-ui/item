package com.aisia.item.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    //请求总数
    public static int clientTotal = 5000;

    //线程数
    public static int threadTotal = 200;

    @RequestMapping("/string-builder")
    public Integer testStringBuilder() throws InterruptedException {
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 1000; i++) {
            list.add("1");
        }
        StringBuilder sb = new StringBuilder();
        List<Thread> threads = new ArrayList<>();
        for (String s : list) {
            Thread t = new Thread(() -> {
                sb.append(s);
            });
            t.start();  // 启动线程
            threads.add(t);
        }
        // 等待所有线程完成
        for (Thread t : threads) {
            t.join();
        }
        System.out.println("StringBuilder长度: " + sb.length());
        return sb.length();
    }

    @RequestMapping("/string-buffer")
    public Integer testStringBuffer() throws InterruptedException {
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 1000; i++) {
            list.add("1");
        }
        StringBuffer sb = new StringBuffer();
        List<Thread> threads = new ArrayList<>();
        for (String s : list) {
            Thread t = new Thread(() -> {
                sb.append(s);
            });
            t.start();  // 启动线程
            threads.add(t);
        }
        // 等待所有线程完成
        for (Thread t : threads) {
            t.join();
        }
        System.out.println("StringBuffer长度: " + sb.length());
        return sb.length();
    }

    @RequestMapping("/string-example1")
    public Integer stringExample1() {
        StringBuilder stringBuilder = new StringBuilder();
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(200); //同一时刻最多有200线程在执行
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal); //5000次基数后await才放行
        for (int i = 1; i <= clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire(); //获取访问权限
                    stringBuilder.append("1");
                    semaphore.release(); //释放
                } catch (InterruptedException e) {
                    log.error("error:{}",e.getMessage());
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
            log.info("size:{}", stringBuilder.length());
            executorService.shutdown();
        } catch (InterruptedException e) {
            log.error("error:{}",e.getMessage());
        }
        return stringBuilder.length();
    }


    @RequestMapping("/string-example2")
    public Integer stringExample2() {
        StringBuffer stringBuffer = new StringBuffer();
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(200); //同一时刻最多有200线程在执行
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal); //5000次基数后await才放行
        for (int i = 1; i <= clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire(); //获取访问权限
                    stringBuffer.append("1");
                    semaphore.release(); //释放
                } catch (InterruptedException e) {
                    log.error("error:{}",e.getMessage());
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
            log.info("size:{}", stringBuffer.length());
            executorService.shutdown();
        } catch (InterruptedException e) {
            log.error("error:{}",e.getMessage());
        }
        return stringBuffer.length();
    }
}
