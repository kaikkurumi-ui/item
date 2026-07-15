package com.aisia.item.console.handle;

import cn.hutool.jwt.JWT;
import com.aisia.item.module.utils.JsonSerializableUtil;
import com.aisia.item.module.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.file.PathMatcher;

@Component
public class RequestHandler implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //查看Cookie中的信息是否过期
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if("user_sign".equals(cookie.getName())){
                String sign = cookie.getValue();
                JWT jwt = JwtUtil.parseAndVerifySign(sign);
                // jwt过期则返回null
                if(jwt == null){
                    return false;
                }
            }
        }
         return true;
        // 如果采用session方案，需要查看session
//        HttpSession session = request.getSession(false);
//        if(session == null){
//            return false;
//        }
//        String sign = session.getAttribute("user_sign").toString();
//        JWT jwt = JwtUtil.parseAndVerifySign(sign);
//        return jwt != null;
    }
}
