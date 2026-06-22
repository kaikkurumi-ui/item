package com.aisia.item.console;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.aisia.item")
@MapperScan("com.aisia.item.module.mapper")
public class ItemConsoleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemConsoleApplication.class, args);
    }
}
