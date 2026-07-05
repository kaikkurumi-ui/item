package com.aisia.item.app.controller;

import com.aisia.item.app.domain.ItemDetailInfoVo;
import com.aisia.item.app.domain.ItemInfoVo;
import com.aisia.item.app.domain.ItemListVo;
import com.aisia.item.module.entity.CategoryEntity;
import com.aisia.item.module.entity.Item;
import com.aisia.item.module.entity.ItemAndCategory;
import com.aisia.item.module.mapper.ItemMapper;
import com.aisia.item.module.service.CategoryService;
import com.aisia.item.module.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ItemListVo list(@RequestParam("page") Integer page,
                           @RequestParam(value = "keyword",required = false) String keyword){
        log.info("查询商品列表,第{}页",page);
        Integer pageSize = 5;
        List<ItemAndCategory> items = itemService.getItemAndCateByPage(page,pageSize,keyword);
        // 通过判断查询的商品集合大小，和每页大小做对比
        Boolean isEnd = items.size() < pageSize;
        List<ItemInfoVo> list = new ArrayList<>(items.size());
        for (ItemAndCategory i : items) {
            ItemInfoVo itemInfoVo = new ItemInfoVo();
            itemInfoVo.setItemImage(i.getItemImages().split("\\$")[0])
                    .setPrice(i.getPrice())
                    .setTitle(i.getTitle())
                    .setCategoryName(i.getCategoryName());
            list.add(itemInfoVo);
        }
        ItemListVo itemListVo = new ItemListVo();
        itemListVo.setList(list);
        itemListVo.setIsEnd(isEnd);
        return itemListVo;
    }

    @GetMapping("/info")
    public ItemDetailInfoVo info(@RequestParam("itemId") Long itemId){
        log.info("查询商品id详情:{}",itemId);
        ItemAndCategory i = itemService.getItemAndCateById(itemId);
        ItemDetailInfoVo itemDetailInfoVo = new ItemDetailInfoVo();

        String[] imagesArr = i.getItemImages().split("\\$");
        itemDetailInfoVo.setItemImages(Arrays.asList(imagesArr))
                .setPrice(i.getPrice())
                .setTitle(i.getTitle())
                .setDescription(i.getDescription())
                .setCategoryName(i.getCategoryName())
                .setCategoryImage(i.getCategoryImage());
        return itemDetailInfoVo;
    }
}
