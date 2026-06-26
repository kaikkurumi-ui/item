package com.aisia.item.console.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemListVo {
    private List<ItemInfoVo> list;
    private Long total;
    private Integer pageSize;
}