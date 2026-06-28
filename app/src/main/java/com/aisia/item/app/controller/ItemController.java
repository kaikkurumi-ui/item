package com.aisia.item.app.controller;

import com.aisia.item.app.domain.ItemDetailInfoVo;
import com.aisia.item.app.domain.ItemInfoVo;
import com.aisia.item.app.domain.ItemListVo;
import com.aisia.item.module.entity.Item;
import com.aisia.item.module.mapper.ItemMapper;
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

@RestController
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/list")
    public ItemListVo list(@RequestParam("page") Integer page,
                           @RequestParam(value = "keyword",required = false) String keyword){
        log.info("查询商品列表,第{}页",page);
        Integer pageSize = 5;
        List<Item> items = itemService.getByPage(page,pageSize,keyword);
        // 通过判断查询的商品集合大小，和每页大小做对比
        Boolean isEnd = items.size() < pageSize;
        List<ItemInfoVo> list = new ArrayList<>(items.size());
        for (Item item : items) {
            ItemInfoVo itemInfoVo = new ItemInfoVo();
            itemInfoVo.setItemImage(item.getItemImages().split("\\$")[0])
                    .setPrice(item.getPrice())
                    .setTitle(item.getTitle());
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
