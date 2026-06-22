package com.aisia.item.module.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
public class Item {

    /**
     * 商品ID
     */
    private Long id;

    /**
     * 商品图片，用$符拼接
     */
    private String itemImages;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品价格
     */
    private Float price;

    /**
     * 商品详情介绍
     */
    private String description;

    /**
     * 创建时间（Unix时间戳，秒）
     */
    private Long createTime;

    /**
     * 更新时间（Unix时间戳，秒）
     */
    private Long updateTime;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    private Integer isDeleted;

}
