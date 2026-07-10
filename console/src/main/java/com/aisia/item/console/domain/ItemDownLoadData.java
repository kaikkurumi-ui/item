package com.aisia.item.console.domain;

import com.aisia.item.console.converter.CustomDateConverter;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ContentRowHeight(30)
@HeadRowHeight(25)
@ColumnWidth(35)
public class ItemDownLoadData {

    @ExcelIgnore
    private Long id;

    @ExcelProperty("商品图片地址")
    @ColumnWidth(50)
    private String itemImages;

    @ExcelProperty("商品标题")
    private String title;

    @ExcelProperty("商品描述")
    private String description;

    @ExcelProperty("分类id")
    private Long categoryId;

    @ExcelProperty("商品价格")
    private Float price;

    @ExcelProperty(value = "商品创建日期",converter = CustomDateConverter.class)
    private Date createTime; //使用自定义转换器
}
