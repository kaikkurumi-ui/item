package com.aisia.item.module.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.core.util.RandomUtil;

public class PasswordUtil {

    /**
     * 加密密码（注册时调用）
     * @param plainPassword 用户输入的原始密码
     * @return 返回包含密文密码和盐值的对象
     */
    public static PasswordInfo encryptPassword(String plainPassword) {
        // 1. 生成16位随机盐值（包含大小写字母和数字）
        String salt = RandomUtil.randomString(16);

        // 2. 密码 + 盐值 拼接后MD5加密
        String hashedPassword = SecureUtil.md5(plainPassword + salt);

        // 3. 返回结果
        return new PasswordInfo(hashedPassword, salt);
    }

    /**
     * 验证密码（登录时调用）
     * @param plainPassword 用户输入的原始密码
     * @param storedHash 数据库中存储的密文密码
     * @param storedSalt 数据库中存储的盐值
     * @return true=密码正确，false=密码错误
     */
    public static boolean verifyPassword(String plainPassword, String storedHash, String storedSalt) {
        // 用同样的方式重新加密
        String hashedPassword = SecureUtil.md5(plainPassword + storedSalt);
        // 比较是否一致
        return hashedPassword.equals(storedHash);
    }

    /**
     * 密码信息封装类（用于返回结果）
     */
    public static class PasswordInfo {
        public String hashedPassword; // 密文密码
        public String salt;           // 盐值

        public PasswordInfo(String hashedPassword, String salt) {
            this.hashedPassword = hashedPassword;
            this.salt = salt;
        }
    }
}