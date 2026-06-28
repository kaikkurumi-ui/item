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
            //.setUpdateTime(Instant.now().getEpochSecond())
            .setIsDeleted(0);
        int incrementId = itemMapper.createItem(item);
        Long id = item.getId();
        return incrementId > 0 ? "自增id:" + id: "失败";
    }

    public String update(Long itemId, String itemImages, String title, Float price, String description) {
        Item item = new Item();
        item.setId(itemId).
                setItemImages(itemImages)
                .setTitle(title)
                .setPrice(price)
                .setDescription(description)
                .setCreateTime(System.currentTimeMillis() / 1000)
                //.setUpdateTime(Instant.now().getEpochSecond())
                .setIsDeleted(0);
        return itemMapper.updateItem(item) > 0 ? "成功" : "失败";
    }

    public Long edit(Long itemId, String itemImages, String title, Float price, String description,Integer isDeleted) {
        // 如果修改商品图片，商品图片数量至少2个
        if(StringUtils.isNotBlank(itemImages)){
            if(itemImages.split("\\$").length < 2){
                throw new RuntimeException("at least 2 images required when images provided");
            }
        }
        // 校验价格
        if(price != null && price < 0){
            throw new IllegalArgumentException("price must be positive");
        }
        Item item = new Item();
        item.setItemImages(itemImages)
                .setTitle(title)
                .setPrice(price)
                .setDescription(description)
                .setCreateTime(System.currentTimeMillis() / 1000)
                .setIsDeleted(isDeleted);
        if(itemId == null){
            // 新增
            int affRows = itemMapper.createItem(item);
            if (affRows > 0) {
                return item.getId();
            }else {
                throw new RuntimeException("insert item fail");
            }
        }else {
            // 更新
            // 判断该商品是否在数据库中
            Item item2 = itemMapper.getItemById(itemId);
            if(item2 == null){
                throw new RuntimeException("item no exist");
            }
            item.setId(itemId);
            int affRows = itemMapper.updateItem(item);
            if(affRows > 0){
                return itemId;
            }else {
                throw new RuntimeException("update item fail");
            }
        }
    }

    public String delete(Long itemId) {
        return itemMapper.deleteItem(itemId) > 0 ? "成功" : "失败";
    }

    public List<Item> getByPage(Integer page,Integer pageSize,String keyword) {
        Integer offset = (page -1) * pageSize;
        return itemMapper.getItemListByPage(offset,pageSize,keyword);
    }

    public Long getTotal() {
        return itemMapper.getTotal();
    }
}
