package com.baidu.item.console;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.baidu.item")
@MapperScan("com.baidu.item.module.mapper")
public class ItemConsoleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemConsoleApplication.class, args);
    }
}
