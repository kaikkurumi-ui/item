package com.aisia.item.module.mapper;

import com.aisia.item.module.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author kaikai
 * @since 2026-07-14
 */
@Mapper
public interface UserMapper {

    int insert(UserEntity userEntity);

    int update(UserEntity userEntity);

    @Select("SELECT * FROM user WHERE id = #{id} AND is_deleted = 0")
    UserEntity getById(@Param("id") Long id);

    @Select("SELECT * FROM user WHERE id = #{id}")
    UserEntity extractById(@Param("id") Long id);

    @Update("UPDATE user SET is_deleted = 1 WHERE id = #{id} AND is_deleted = 0")
    int delete(@Param("id") Long id);

    @Select("SELECT * FROM user WHERE name = #{username} AND is_deleted = 0")
    UserEntity queryByUsername(@Param("username") String username);
}
