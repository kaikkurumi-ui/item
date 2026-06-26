package com.aisia.item.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ItemListVo {
    List<ItemInfoVo> list;
    Boolean isEnd;
}
