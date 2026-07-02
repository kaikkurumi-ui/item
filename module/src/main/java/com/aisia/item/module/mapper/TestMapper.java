package com.aisia.item.module.mapper;

import com.aisia.item.module.entity.TestEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author kaikai
 * @since 2026-07-02
 */
@Mapper
public interface TestMapper {

    int insert(TestEntity testEntity);

    int update(TestEntity testEntity);

    @Select("SELECT * FROM test WHERE id = #{id} AND is_deleted = 0")
    TestEntity getById(@Param("id") Long id);

    @Select("SELECT * FROM test WHERE id = #{id}")
    TestEntity extractById(@Param("id") Long id);

    @Update("UPDATE test SET is_deleted = 1 WHERE id = #{id} AND is_deleted = 0")
    int delete(@Param("id") Long id);
}
