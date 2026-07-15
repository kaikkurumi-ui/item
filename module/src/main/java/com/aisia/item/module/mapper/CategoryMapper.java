package com.aisia.item.module.mapper;

import com.aisia.item.module.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 分类信息表 Mapper 接口
 * </p>
 *
 * @author kaikai
 * @since 2026-07-03
 */
@Mapper
public interface CategoryMapper {

    int insert(CategoryEntity categoryEntity);

    int update(CategoryEntity categoryEntity);

    @Select("SELECT * FROM category WHERE id = #{id} AND is_deleted = 0")
    CategoryEntity getById(@Param("id") Long id);

    @Select("SELECT * FROM category WHERE id = #{id}")
    CategoryEntity extractById(@Param("id") Long id);

    @Update("UPDATE category SET is_deleted = 1 WHERE id = #{id} AND is_deleted = 0")
    int delete(@Param("id") Long id);

    @Select("SELECT * FROM category WHERE is_deleted = 0")
    List<CategoryEntity> getAll();

    List<Long> getCategoryIds(String keyword);
}
