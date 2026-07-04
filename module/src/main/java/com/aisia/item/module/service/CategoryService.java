package com.aisia.item.module.service;

import com.aisia.item.module.entity.CategoryEntity;
import com.aisia.item.module.mapper.CategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 分类信息表 服务实现类
 * </p>
 *
 * @author kaikai
 * @since 2026-07-03
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public int insert(CategoryEntity categoryEntity){
        return categoryMapper.insert(categoryEntity);
    }

    public int update(CategoryEntity categoryEntity){
        return categoryMapper.update(categoryEntity);
    }

    public CategoryEntity getById(Long id){
        return categoryMapper.getById(id);
    }

    public CategoryEntity extractById(Long id){
        return categoryMapper.extractById(id);
    }

    public int delete(Long id){
        return categoryMapper.delete(id);
    }

    public List<CategoryEntity> list() {
        return categoryMapper.getAll();
    }

    public List<Long> getCategoryIds(String keyword) {
        return categoryMapper.getCategoryIds(keyword);
    }
}
