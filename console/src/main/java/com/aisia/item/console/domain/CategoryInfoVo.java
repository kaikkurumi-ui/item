package com.aisia.item.console.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryInfoVo {
    private String categoryName;
    private String categoryImage;
}
