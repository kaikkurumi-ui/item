package com.aisia.item.app.controller;

import com.aisia.item.app.domain.CategoryInfoVo;
import com.aisia.item.app.domain.CategoryListVo;
import com.aisia.item.module.entity.CategoryEntity;
import com.aisia.item.module.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/list")
    public CategoryListVo list() {
        List<CategoryEntity> categoryEntities = categoryService.list();
        CategoryListVo vo = new CategoryListVo();
        List<CategoryInfoVo> list = new ArrayList<>();
        for (CategoryEntity entity : categoryEntities) {
            CategoryInfoVo categoryInfoVo = new CategoryInfoVo();
            categoryInfoVo.setCategoryName(entity.getName());
            categoryInfoVo.setCategoryImage(entity.getCategoryImage());
            list.add(categoryInfoVo);
        }
        vo.setList(list);
        return vo;
    }
}
