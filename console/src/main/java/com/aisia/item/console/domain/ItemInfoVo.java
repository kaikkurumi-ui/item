package com.aisia.item.console.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class ItemInfoVo {
    private String itemImage;
    private String title;
    private Float price;
}
