package com.baidu.item.console.controller;

import com.baidu.item.module.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/create")
    public String create(@RequestParam("itemImages") String itemImages,
                         @RequestParam("title") String title,
                         @RequestParam("price") Float price,
                         @RequestParam("description") String description){
        log.info("创建商品,itemImages:{},title:{},price:{},description:{}", itemImages, title, price, description);
        return itemService.create(itemImages, title, price, description);
    }

    @RequestMapping("/update")
    public String update(@RequestParam("itemId") Long itemId,
                          @RequestParam("itemImages") String itemImages,
                          @RequestParam("title") String title,
                          @RequestParam("price") Float price,
                          @RequestParam("description") String description){
        log.info("更新商品信息,itemId:{},itemImages:{},title:{},price:{},description:{}", itemId, itemImages, title, price, description);
        return itemService.update(itemId, itemImages, title, price, description);
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("itemId") Long itemId){
        log.info("删除商品,itemId:{}", itemId);
        return itemService.delete(itemId);
    }
}
