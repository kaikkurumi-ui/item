package com.aisia.item.module.mapper;

import com.aisia.item.module.entity.Item;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ItemMapper {

    @Select("SELECT * FROM item WHERE item.item.is_deleted = 0")
    List<Item> getAll();

    @Select("SELECT * FROM item WHERE id = #{itemId} AND item.item.is_deleted = 0")
    Item getItemInfo(@Param("itemId") Long itemId);

    int createItem(Item item);

    int updateItem(Item item);

    @Update("UPDATE item SET is_deleted = 1 WHERE id = #{itemId} AND is_deleted = 0")
    int deleteItem(@Param("itemId") Long itemId);
}
