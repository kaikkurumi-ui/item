package com.aisia.item.module.mapper;

import com.aisia.item.module.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ItemMapper {

    @Select("SELECT * FROM item WHERE item.item.is_deleted = 0")
    List<Item> getAll();

    @Select("SELECT * FROM item WHERE id = #{itemId} AND item.item.is_deleted = 0")
    Item getById(@Param("itemId") Long itemId);

    int insert(Item item);

    int update(Item item);

    @Update("UPDATE item SET is_deleted = 1 WHERE id = #{itemId} AND is_deleted = 0")
    int delete(@Param("itemId") Long itemId);

    List<Item> getItemListByPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("keyword") String keyword);

    Long getTotal(@Param("keyword") String keyword);

    @Select("SELECT * FROM item WHERE id = #{itemId}")
    Item extractById(Long itemId);

    List<Item> consoleGetItemListByPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("keyword") String keyword);

    List<Item> appGetByPage(@Param("offset") Integer offset, @Param("strIds") String strIds, @Param("keyword") String keyword, @Param("pageSize") Integer pageSize);

    @Update("UPDATE item SET is_deleted = 1 WHERE category_id = #{categoryId} AND is_deleted = 0")
    Integer deleteByCategoryId(Long categoryId);

    void saveBatch(@Param("cachedDataList") List<Item> cachedDataList);
}
