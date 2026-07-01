package com.aisia.item.module.mapper;

import com.aisia.item.module.entity.Item;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 商品信息表 Mapper 接口
 * </p>
 *
 * @author kaikai
 * @since 2026-06-29
 */
public interface ItemMapper extends BaseMapper<Item> {

    @Select("select * from item where item.item.id = #{itemId} and is_deleted = 0")
    Item getById(@Param("itemId") Long itemId);

    @Select("select * from item where item.item.id = #{itemId}")
    Item extractById(@Param("itemId") Long itemId);

    @Update("update item set is_deleted = 1 where id = #{itemId} and is_deleted = 0")
    int delete(@Param("itemId") Long itemId);
}
