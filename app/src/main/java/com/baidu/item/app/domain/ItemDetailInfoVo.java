package com.baidu.item.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ItemDetailInfoVo {
    private List<String> ItemImages;
    private String title;
    private Float price;
    private String description;
}
