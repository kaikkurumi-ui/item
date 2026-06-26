package com.aisia.item.console.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static String formatTime(Long timestamp) {
        // 将时间戳转换为Instant,数据库存储是时间戳单位是秒,使用ofEpochSecond转换
        Instant instant = Instant.ofEpochSecond(timestamp);

        // 使用DateTimeFormatter定义你想要的日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化Instant到具体的日期时间字符串
        String formattedDateTime = instant.atZone(ZoneId.systemDefault()).format(formatter);

        return formattedDateTime.format(formattedDateTime);
    }
}
