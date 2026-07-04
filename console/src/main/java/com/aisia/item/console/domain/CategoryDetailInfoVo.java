package com.aisia.item.console.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDetailInfoVo {
    private String categoryName;
    private String categoryImage;
    private String categoryDescription;
}
