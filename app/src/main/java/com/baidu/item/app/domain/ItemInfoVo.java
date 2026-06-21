package com.baidu.item.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ItemInfoVo {
    private String ItemImage;
    private String title;
    private Float price;
}
