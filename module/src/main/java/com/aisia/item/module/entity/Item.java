package com.aisia.item.module.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.Instant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品信息表
 * </p>
 *
 * @author kaikai
 * @since 2026-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("item")
public class Item {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品图片，用$符拼接
     */
    @TableField("item_images")
    private String itemImages;

    /**
     * 商品标题
     */
    @TableField("title")
    private String title;

    /**
     * 商品价格
     */
    @TableField("price")
    private Float price;

    /**
     * 商品详情介绍
     */
    @TableField("description")
    private String description;

    @TableField("create_time")
    private Long createTime;

    @TableField("update_time")
    private Long updateTime = Instant.now().getEpochSecond();

    @TableField("is_deleted")
    private Integer isDeleted;


}
