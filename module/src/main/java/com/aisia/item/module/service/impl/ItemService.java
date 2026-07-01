package com.aisia.item.module.service.impl;

import com.aisia.item.module.entity.Item;
import com.aisia.item.module.mapper.ItemMapper;
import com.aisia.item.module.service.IItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService extends ServiceImpl<ItemMapper, Item> implements IItemService {

    public List<Item> getAll() {
        return baseMapper.selectList(Wrappers.query(Item.class).eq("is_deleted", 0));
    }

    public Item getById(Long itemId) {
        return baseMapper.getById(itemId);
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
        int incrementId = baseMapper.insert(item);
        Long id = item.getId();
        return incrementId > 0 ? id : -1;
    }

    public Boolean update(Long id, String itemImages, String title, Float price, String description,Integer isDeleted) {
        UpdateWrapper<Item> wrapper = new UpdateWrapper<>();
        wrapper.set("item_images",itemImages);
        wrapper.set(StringUtils.isNotEmpty(title),"title",title);
        wrapper.set("price",price);
        wrapper.set(StringUtils.isNotEmpty(description),"description",description);
        wrapper.set(isDeleted != null,"is_deleted",isDeleted);
        wrapper.eq("id",id);
        return baseMapper.update(wrapper) > 0;
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
            Item item2 = baseMapper.extractById(itemId);
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
        return baseMapper.delete(itemId) > 0 ? "成功" : "失败";
    }

    public Page<Item> getByPage(Integer pageNum, Integer pageSize, String keyword) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted",0);
        wrapper.like(StringUtils.isNotEmpty(keyword),"title",keyword);
        Page<Item> page = new Page<>(pageNum,pageSize);
        return baseMapper.selectPage(page, wrapper);
    }

    public Long getTotal(String keyword) {
        return this.lambdaQuery()
                .eq(Item::getIsDeleted, 0)
                .like(StringUtils.isNotEmpty(keyword), Item::getTitle, keyword)
                .count();
    }

    public Item extractById(Long itemId) {
        return baseMapper.extractById(itemId);
    }
}
