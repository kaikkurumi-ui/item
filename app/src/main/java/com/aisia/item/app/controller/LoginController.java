package com.aisia.item.app.controller;

import com.aisia.item.module.entity.UserEntity;
import com.aisia.item.module.service.UserService;
import com.aisia.item.module.utils.PasswordUtil;
import com.aisia.item.module.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(@RequestParam("name") String name,
                        @RequestParam("password") String password) {
        UserEntity user = userService.queryByUsername(name);
        if (user == null) {
            return "用户不存在";
        }
        // 校验密码
        boolean verified = PasswordUtil.verifyPassword(password, user.getPassword(), user.getSalt());
        if (!verified) {
            return "密码错误";
        }
        String sign = JwtUtil.generateSign(user.getId().toString());
        return sign;
    }
}
