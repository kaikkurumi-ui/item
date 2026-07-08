package com.aisia.item.module.mapper;

import com.aisia.item.module.entity.FileEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 文件存储表 Mapper 接口
 * </p>
 *
 * @author kaikai
 * @since 2026-07-07
 */
@Mapper
public interface FileMapper {

    int insert(FileEntity fileEntity);

    int update(FileEntity fileEntity);

    @Select("SELECT * FROM file WHERE id = #{id} AND is_deleted = 0")
    FileEntity getById(@Param("id") Long id);

    @Select("SELECT * FROM file WHERE id = #{id}")
    FileEntity extractById(@Param("id") Long id);

    @Update("UPDATE file SET is_deleted = 1 WHERE id = #{id} AND is_deleted = 0")
    int delete(@Param("id") Long id);
}
