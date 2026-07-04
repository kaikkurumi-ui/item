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
 * 分类信息表
 * </p>
 *
 * @author kaikai
 * @since 2026-07-03
 */
@Getter
@Setter
@ToString
@TableName("category")
public class CategoryEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 分类图片
     */
    @TableField("category_image")
    private String categoryImage;

    /**
     * 分类详情描述
     */
    @TableField("`description`")
    private String description;

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
     * 是否删除,逻辑删除,1是删除
     */
    @TableField("is_deleted")
    private Integer isDeleted;
}
