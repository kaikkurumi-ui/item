package com.aisia.item.app.controller;

import com.aisia.item.module.entity.UserEntity;
import com.aisia.item.module.service.UserService;
import com.aisia.item.module.utils.PasswordUtil;
import com.aisia.item.module.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@Slf4j
public class RegisterController {

    @Autowired
    private UserService userService;

    @RequestMapping("/register")
    public String register(@RequestParam("name") String name,
                           @RequestParam("password") String password,
                           @RequestParam("phone") String phone,
                           @RequestParam("avatar") String avatar) {
        UserEntity user = new UserEntity();
        user.setPhone(phone);
        user.setAvatar(avatar);
        user.setName(name);
        // 随机加盐处理
        PasswordUtil.PasswordInfo passwordInfo = PasswordUtil.encryptPassword(password);
        user.setPassword(passwordInfo.hashedPassword);
        user.setSalt(passwordInfo.salt);
        user.setCreateTime(Instant.now().getEpochSecond());
        user.setIsDeleted(0);
        int result = userService.insert(user);
        String sign = "";
        if (result > 0) {
            sign = JwtUtil.generateSign(user.getId().toString());
            return sign;
        }
        return "注册失败";
    }
}

