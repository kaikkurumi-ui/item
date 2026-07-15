package com.aisia.item.module.utils;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class JsonSerializableUtil {

    public static String getJsonSerial(String userId){
        LoginInfo loginInfo = new LoginInfo(userId);
        String jsonString = JSON.toJSONString(loginInfo);
        return jsonString;
    }

    public static LoginInfo parseJson(String text){
        LoginInfo loginInfo = JSON.parseObject(text, LoginInfo.class);
        return loginInfo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginInfo {
        private String userId;
    }
}
