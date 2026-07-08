package com.aisia.item.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ItemInfoVo {
    private ImageVo imageVo;
    private String title;
    private Float price;
    private String categoryName;
}
