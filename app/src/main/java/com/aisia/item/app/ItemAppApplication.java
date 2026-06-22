package com.aisia.item.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.aisia.item")
@MapperScan("com.aisia.item.module.mapper")
public class ItemAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemAppApplication.class, args);
    }
}
