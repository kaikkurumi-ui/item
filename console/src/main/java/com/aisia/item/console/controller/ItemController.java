package com.aisia.item.console.controller;

import com.aisia.item.console.domain.ItemDetailInfoVo;
import com.aisia.item.console.domain.ItemInfoVo;
import com.aisia.item.console.domain.ItemListVo;
import com.aisia.item.console.utils.DateTimeUtil;
import com.aisia.item.module.entity.Item;
import com.aisia.item.module.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/create")
    public String create(@RequestParam("itemImages") String itemImages,
                         @RequestParam("title") String title,
                         @RequestParam("price") Float price,
                         @RequestParam("description") String description) {
        log.info("创建商品,itemImages:{},title:{},price:{},description:{}", itemImages, title, price, description);
        return itemService.create(itemImages, title, price, description);
    }

    @RequestMapping("/update")
    public String update(@RequestParam("itemId") Long itemId,
                         @RequestParam("itemImages") String itemImages,
                         @RequestParam("title") String title,
                         @RequestParam("price") Float price,
                         @RequestParam("description") String description) {
        log.info("更新商品信息,itemId:{},itemImages:{},title:{},price:{},description:{}", itemId, itemImages, title, price, description);
        return itemService.update(itemId, itemImages, title, price, description);
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("itemId") Long itemId) {
        log.info("删除商品,itemId:{}", itemId);
        return itemService.delete(itemId);
    }

    @RequestMapping("/list")
    public ItemListVo list(@RequestParam("page") Integer page){
        log.info("console端获取商品列表,页码:{}",page);
        // 指定分页大小
        Integer pageSize = 5;
        List<Item> items = itemService.getByPage(page,pageSize); //查询数据
        Long total = itemService.getTotal(); //总页数
        List<ItemInfoVo> infoVoList = new ArrayList<>(items.size());
        for (Item item : items) {
            ItemInfoVo itemInfoVo = ItemInfoVo.builder()
                    .itemImage(item.getItemImages().split("\\$")[0])
                    .price(item.getPrice())
                    .title(item.getTitle())
                    .build();
            infoVoList.add(itemInfoVo);
        }
        ItemListVo itemListVo = new ItemListVo();
        itemListVo.setList(infoVoList);
        itemListVo.setPageSize(pageSize);
        itemListVo.setTotal(total);
        return itemListVo;
    }

    @RequestMapping("/info")
    public ItemDetailInfoVo info(@RequestParam("itemId") Long itemId) {
        log.info("console端根据商品id获取商品详情:{}", itemId);
        Item item = itemService.getInfo(itemId);
        ItemDetailInfoVo itemDetailInfoVo = new ItemDetailInfoVo();
        itemDetailInfoVo.setItemImages(Arrays.asList(item.getItemImages().split("\\$")));
        itemDetailInfoVo.setDescription(item.getDescription());
        itemDetailInfoVo.setTitle(item.getTitle());
        itemDetailInfoVo.setPrice(item.getPrice());
        // 将时间戳转换为指定日期格式
        itemDetailInfoVo.setCreateTime(DateTimeUtil.formatTime(item.getCreateTime()));
        itemDetailInfoVo.setUpdateTime(DateTimeUtil.formatTime(item.getUpdateTime()));
        return itemDetailInfoVo;
    }
}
