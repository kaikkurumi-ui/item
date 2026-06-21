package com.baidu.item.module.service;

import com.baidu.item.module.entity.Item;
import com.baidu.item.module.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemMapper itemMapper;

    public List<Item> getAll(){
        return itemMapper.getAll();
    }

    public Item getInfo(Long itemId) {
        return itemMapper.getItemInfo(itemId);
    }

    public String create(String itemImages, String title, Float price, String description) {
        Item item = new Item();
        item.setItemImages(itemImages)
            .setTitle(title)
            .setPrice(price)
            .setDescription(description)
            .setCreateTime(System.currentTimeMillis() / 1000)
            .setUpdateTime(Instant.now().getEpochSecond())
            .setIsDeleted(0);
            return itemMapper.createItem(item) > 0 ? "成功" : "失败";
    }

    public String update(Long itemId, String itemImages, String title, Float price, String description) {
        Item item = new Item();
        item.setId(itemId)
            .setItemImages(itemImages)
            .setTitle(title)
            .setPrice(price)
            .setDescription(description)
            .setUpdateTime(Instant.now().getEpochSecond());
            return itemMapper.updateItem(item) > 0 ? "成功" : "失败";
    }

    public String delete(Long itemId) {
        return itemMapper.deleteItem(itemId) > 0 ? "成功" : "失败";
    }
}
