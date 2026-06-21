package com.baidu.item.app.controller;

import com.baidu.item.app.domain.ItemDetailInfoVo;
import com.baidu.item.app.domain.ItemInfoVo;
import com.baidu.item.app.domain.ItemListVo;
import com.baidu.item.module.entity.Item;
import com.baidu.item.module.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/list")
    public ItemListVo list(){
        log.info("查询商品列表");
        List<Item> items = itemService.getAll();
        List<ItemInfoVo> list = new ArrayList<>(items.size());
        for (Item item : items) {
            ItemInfoVo itemInfoVo = new ItemInfoVo();
            itemInfoVo.setItemImage(item.getItemImages())
                    .setPrice(item.getPrice())
                    .setTitle(item.getTitle());
            list.add(itemInfoVo);
        }
        ItemListVo itemListVo = new ItemListVo();
        itemListVo.setList(list);
        return itemListVo;
    }

    @GetMapping("/info")
    public ItemDetailInfoVo info(@RequestParam("itemId") Long itemId){
        log.info("查询商品详情:{}",itemId);
        Item item = itemService.getInfo(itemId);
        ItemDetailInfoVo itemDetailInfoVo = new ItemDetailInfoVo();

        String[] imagesArr = item.getItemImages().split("\\$");

        itemDetailInfoVo.setItemImages(Arrays.asList(imagesArr))
                .setPrice(item.getPrice())
                .setTitle(item.getTitle())
                .setDescription(item.getDescription());
        return itemDetailInfoVo;
    }
}
