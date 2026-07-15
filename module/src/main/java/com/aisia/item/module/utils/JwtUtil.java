package com.aisia.item.module.utils;

import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.AsymmetricJWTSigner;
import cn.hutool.jwt.signers.HMacJWTSigner;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * JWT 工具类（使用 HS512 对称加密）
 */
public class JwtUtil {

    private static final String SECRET_KEY = "YWlzaWExMzE0ZmVubWFvYmFvYmVpa2VrZWFpYQ==";
    private static final byte[] KEY_BYTES = SECRET_KEY.getBytes(StandardCharsets.UTF_8);

    // Sign 过期时间（毫秒），这里设置为 7 天
    private static final long EXPIRE_TIME = TimeUnit.DAYS.toMillis(7);

    /**
     * 生成 JWT Sign
     *
     * @param userId 用户ID
     * @return JWT 字符串
     */
    public static String generateSign(String userId) {
        // 1. 准备载荷（Payload）数据
        Map<String, Object> payload = new HashMap<>();

        // 存放自定义用户信息
        payload.put("user_id", userId);

        // 设置标准字段：签发时间、过期时间
        long now = System.currentTimeMillis();
        payload.put(JWTPayload.ISSUED_AT, new Date(now));
        payload.put(JWTPayload.EXPIRES_AT, new Date(now + EXPIRE_TIME));

        // 2. 使用 JWTUtil 创建 Sign
        // 参数1：载荷 Map；参数2：密钥的字节数组
        return JWTUtil.createToken(payload, JWTSignerUtil.hs512(KEY_BYTES));
    }

    /**
     * 解析并验证 JWT Sign
     *
     * @param sign JWT 字符串
     * @return 解析后的 JWT 对象，如果验证失败则返回 null
     */
    public static JWT parseAndVerifySign(String sign) {
        try {
            // 1. 解析 Sign
            JWT jwt = JWTUtil.parseToken(sign);

            // 2. 设置密钥并验证签名和有效期
            // 验证失败会抛出异常：ExpiredJwtException（过期）或 SignatureVerificationException（签名错误）
            boolean isValid = jwt
                    .setSigner(JWTSignerUtil.hs512(KEY_BYTES))
                    .validate(0);

            if (isValid) {
                return jwt; // 验证通过，返回 JWT 对象以便获取载荷
            }
        } catch (Exception e) {
            // 记录日志，便于排查
            System.err.println("Sign 验证失败: " + e.getMessage());
        }
        return null;
    }

    /**
     * 从 JWT 中获取用户 ID
     *
     * @param Sign JWT 字符串
     * @return 用户 ID，如果获取失败返回 null
     */
    public static String getUserIdFromSign(String Sign) {
        JWT jwt = parseAndVerifySign(Sign);
        if (jwt != null) {
            // getPayload("user_id") 返回的是 Object，需要根据实际类型转换
            Object userIdObj = jwt.getPayload("user_id");
            return userIdObj != null ? userIdObj.toString() : null;
        }
        return null;
    }
}
