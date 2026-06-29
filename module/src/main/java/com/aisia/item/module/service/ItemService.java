package com.aisia.item.module.service;

import com.aisia.item.module.entity.Item;
import com.aisia.item.module.mapper.ItemMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.ResourceTransactionManager;

import java.time.Instant;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ResourceTransactionManager resourceTransactionManager;

    public List<Item> getAll() {
        return itemMapper.getAll();
    }

    public Item getById(Long itemId) {
        return itemMapper.getById(itemId);
    }

    public Long insert(String itemImages, String title, Float price, String description) {
        Item item = new Item();
        item.setItemImages(itemImages)
                .setTitle(title)
                .setPrice(price)
                .setDescription(description)
                .setCreateTime(System.currentTimeMillis() / 1000)
                //.setUpdateTime(Instant.now().getEpochSecond())
                .setIsDeleted(0);
        int incrementId = itemMapper.insert(item);
        Long id = item.getId();
        return incrementId > 0 ? id : -1;
    }

    public Boolean update(Long itemId, String itemImages, String title, Float price, String description,Integer isDeleted) {
        Item item = new Item();
        item.setId(itemId).
                setItemImages(itemImages)
                .setTitle(title)
                .setPrice(price)
                .setDescription(description)
                .setCreateTime(System.currentTimeMillis() / 1000)
                //.setUpdateTime(Instant.now().getEpochSecond())
                .setIsDeleted(0);
        return itemMapper.update(item) > 0;
    }

    public Long edit(Long itemId, String itemImages, String title, Float price, String description, Integer isDeleted) {
        // 如果修改商品图片，商品图片数量至少2个
        if (StringUtils.isNotBlank(itemImages)) {
            if (itemImages.split("\\$").length < 2) {
                throw new RuntimeException("at least 2 images required when images provided");
            }
        }
        // 校验价格
        if (price != null && price < 0) {
            throw new IllegalArgumentException("price must be positive");
        }
        if (itemId == null) {
            // 新增
            Long id = insert(itemImages, title, price, description);
            if (id > 0) {
                return id;
            } else {
                throw new RuntimeException("insert item fail");
            }
        } else {
            // 更新
            // 判断该商品是否在数据库中
            Item item2 = itemMapper.extractById(itemId);
            if (item2 == null) {
                throw new RuntimeException("item no exist");
            }
            Boolean success = update(itemId, itemImages, title, price, description, isDeleted);
            if (success) {
                return itemId;
            } else {
                throw new RuntimeException("update item fail");
            }
        }
    }

    public String delete(Long itemId) {
        return itemMapper.delete(itemId) > 0 ? "成功" : "失败";
    }

    public List<Item> getByPage(Integer page, Integer pageSize, String keyword) {
        Integer offset = (page - 1) * pageSize;
        return itemMapper.getItemListByPage(offset, pageSize, keyword);
    }

    public Long getTotal(String keyword) {
        return itemMapper.getTotal(keyword);
    }

    public Item extractById(Long itemId) {
        return itemMapper.extractById(itemId);
    }
}
