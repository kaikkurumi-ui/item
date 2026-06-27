package com.aisia.item.app.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ItemDetailInfoVo {
    private List<String> itemImages;
    private String title;
    private Float price;
    private String description;
}
