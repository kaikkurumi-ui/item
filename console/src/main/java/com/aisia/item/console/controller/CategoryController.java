package com.aisia.item.console.controller;

import com.aisia.item.console.domain.CategoryDetailInfoVo;
import com.aisia.item.console.domain.CategoryInfoVo;
import com.aisia.item.console.domain.CategoryListVo;
import com.aisia.item.module.entity.CategoryEntity;
import com.aisia.item.module.service.CategoryService;
import com.aisia.item.module.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemService itemService;

    @RequestMapping("/list")
    public CategoryListVo list() {
        log.info("分类列表查询");
        List<CategoryEntity> categoryList = categoryService.list();
        CategoryListVo categoryListVo = new CategoryListVo();
        List<CategoryInfoVo> categoryInfoVoList = new ArrayList<>();
        for (CategoryEntity categoryEntity : categoryList) {
            CategoryInfoVo vo = new CategoryInfoVo();
            vo.setCategoryImage(categoryEntity.getCategoryImage());
            vo.setCategoryName(categoryEntity.getName());
            categoryInfoVoList.add(vo);
        }
        categoryListVo.setList(categoryInfoVoList);
        return categoryListVo;
    }


    @RequestMapping("/info")
    public CategoryDetailInfoVo info(@RequestParam("categoryId") Long categoryId) {
        CategoryEntity categoryEntity = categoryService.extractById(categoryId);
        return CategoryDetailInfoVo.builder()
                .categoryName(categoryEntity.getName())
                .categoryImage(categoryEntity.getCategoryImage())
                .categoryDescription(categoryEntity.getDescription())
                .build();
    }

    @RequestMapping("/insert")
    public String insert(@RequestParam("categoryName") String categoryName,
                         @RequestParam("categoryImage") String categoryImage,
                         @RequestParam("categoryDescription") String categoryDescription) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryImage(categoryImage);
        categoryEntity.setDescription(categoryDescription);
        categoryEntity.setName(categoryName);
        categoryEntity.setIsDeleted(0);
        categoryEntity.setCreateTime(Instant.now().getEpochSecond());
        int result = categoryService.insert(categoryEntity);
        return result > 0 ? "成功" : "失败";
    }

    @RequestMapping("/update")
    public String update(@RequestParam("categoryId") Long categoryId,
                         @RequestParam("categoryName") String categoryName,
                         @RequestParam("categoryImage") String categoryImage,
                         @RequestParam("categoryDescription") String categoryDescription){
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);
        categoryEntity.setCategoryImage(categoryImage);
        categoryEntity.setDescription(categoryDescription);
        categoryEntity.setName(categoryName);
        categoryEntity.setIsDeleted(0);
        int result = categoryService.update(categoryEntity);
        return result > 0 ? "成功" : "失败";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("categoryId") Long categoryId){
        // 删除改类目下的所有商品
        Integer itemNums = itemService.deleteByCategoryId(categoryId);

        int result = categoryService.delete(categoryId);
        return result > 0 ? "成功" : "失败";
    }
}
