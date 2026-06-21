package com.baidu.item.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.baidu.item")
@MapperScan("com.baidu.item.module.mapper")
public class ItemAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemAppApplication.class, args);
    }
}
