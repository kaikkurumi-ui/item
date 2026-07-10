package com.aisia.item.console.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@Data
@EqualsAndHashCode
public class ItemUploadData {

    @ExcelProperty("商品图片地址")
    private String itemImages;
    @ExcelProperty("商品标题")
    private String title;
    @ExcelProperty("商品描述")
    private String description;

    @ExcelProperty("分类id")
    private Long categoryId;

    @ExcelProperty("商品价格")
    private Float price;

    @ExcelProperty(value = "商品创建日期")
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private Date createTime;
}
