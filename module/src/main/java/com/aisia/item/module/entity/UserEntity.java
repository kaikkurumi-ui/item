package com.aisia.item.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author kaikai
 * @since 2026-07-14
 */
@Getter
@Setter
@ToString
@TableName("user")
public class UserEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名字
     */
    @TableField("`name`")
    private String name;

    /**
     * 密码
     */
    @TableField("`password`")
    private String password;

    /**
     * 盐
     */
    @TableField("salt")
    private String salt;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Long updateTime = System.currentTimeMillis() / 1000;

    /**
     * 逻辑删除，默认值0
     */
    @TableField("is_deleted")
    private Integer isDeleted;
}
