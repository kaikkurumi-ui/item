package com.aisia.item.console.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListVo {
    List<CategoryInfoVo> list;
}
