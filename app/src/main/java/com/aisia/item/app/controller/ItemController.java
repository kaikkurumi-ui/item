package com.aisia.item.app.controller;

import com.aisia.item.app.domain.ImageVo;
import com.aisia.item.app.domain.ItemDetailInfoVo;
import com.aisia.item.app.domain.ItemInfoVo;
import com.aisia.item.app.domain.ItemListVo;
import com.aisia.item.module.entity.CategoryEntity;
import com.aisia.item.module.entity.Item;
import com.aisia.item.module.mapper.ItemMapper;
import com.aisia.item.module.service.CategoryService;
import com.aisia.item.module.service.ItemService;
import com.aisia.item.module.utils.ImageUtil;
import com.aliyuncs.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        List<Item> items = itemService.appGetByPage(page,pageSize,keyword);
        // 通过判断查询的商品集合大小，和每页大小做对比
        Boolean isEnd = items.size() < pageSize;
        List<ItemInfoVo> list = new ArrayList<>(items.size());
        List<CategoryEntity> categories = categoryService.list();
        Map<Long, CategoryEntity> categoryMap = categories
                .stream()
                .collect(Collectors.toMap(CategoryEntity::getId, Function.identity()));
        for (Item item : items) {
            CategoryEntity category = categoryMap.get(item.getCategoryId());
            //如果分类不存在则不返回该商品数据
            if(category == null){
                continue;
            }
            ItemInfoVo itemInfoVo = new ItemInfoVo();
            String imageUrl = item.getItemImages().split("\\$")[0];
            Float ar = 0F;
            try {
                ar = ImageUtil.calculateAr(imageUrl);
            } catch (URISyntaxException | IOException | ClientException e) {
                log.error("error:{}", e.getMessage());
                continue;
            }
            ImageVo imageVo = ImageVo.builder()
                    .url(imageUrl)
                    .ar(ar).build();
            itemInfoVo.setImageVo(imageVo)
                    .setPrice(item.getPrice())
                    .setTitle(item.getTitle())
                    .setCategoryName(category.getName());
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
        Item item = itemService.getById(itemId);
        ItemDetailInfoVo itemDetailInfoVo = new ItemDetailInfoVo();

        String[] imagesArr = item.getItemImages().split("\\$");
        CategoryEntity category = categoryService.getById(item.getCategoryId());
        //如果商品类目不存在则不返回数据
        if (Objects.isNull(category)){
            log.error("category not exist");
            return null;
        }
        itemDetailInfoVo.setItemImages(Arrays.asList(imagesArr))
                .setPrice(item.getPrice())
                .setTitle(item.getTitle())
                .setDescription(item.getDescription())
                .setCategoryName(category.getName())
                .setCategoryImage(category.getCategoryImage());
        return itemDetailInfoVo;
    }
}
