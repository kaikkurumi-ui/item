package com.aisia.item.console.controller;

import com.aisia.item.module.entity.UserEntity;
import com.aisia.item.module.service.UserService;
import com.aisia.item.module.utils.JsonSerializableUtil;
import com.aisia.item.module.utils.JwtUtil;
import com.aisia.item.module.utils.PasswordUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Duration;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(@RequestParam("name") String name,
                        @RequestParam("password") String password,
                        HttpServletResponse response,
                        HttpServletRequest request) throws IOException {
        UserEntity user = userService.queryByUsername(name);
        if (user == null) {
            return "用户不存在";
        }
        // 校验密码
        boolean verified = PasswordUtil.verifyPassword(password, user.getPassword(), user.getSalt());
        if (!verified) {
            return "密码错误";
        }

        // Cookie方案
        String jsonStr = JsonSerializableUtil.getJsonSerial(user.getId().toString());
        String sign = JwtUtil.generateSign(jsonStr);
        Cookie cookie = new Cookie("user_sign", sign);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) Duration.ofDays(7).toSeconds());
        response.addCookie(cookie);

        //Session方案
//        String sign = JwtUtil.generateSign(user.getId().toString());
//        HttpSession session = request.getSession();
//        String sessionId = session.getId();
//
//        session.setAttribute("user_sign",sign);

        return "登录成功";
    }
}
