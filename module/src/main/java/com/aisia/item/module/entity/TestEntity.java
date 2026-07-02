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
 *
 * </p>
 *
 * @author kaikai
 * @since 2026-07-02
 */
@Getter
@Setter
@ToString
@TableName("test")
public class TestEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    @TableField("`name`")
    private String name;

    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;

    /**
     * 班级
     */
    @TableField("clazz")
    private String clazz;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Long updateTime = System.currentTimeMillis();

    /**
     * 是否删除,逻辑删除,1是删除
     */
    @TableField("is_deleted")
    private Integer isDeleted;
}
