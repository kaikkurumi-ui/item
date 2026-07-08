package com.aisia.item.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 文件存储表
 * </p>
 *
 * @author kaikai
 * @since 2026-07-07
 */
@Getter
@Setter
@ToString
@TableName("file")
public class FileEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id ;

    /**
     * 1-图⽚，2-视频，3-⽂件
     */
    @TableField("`type`")
    private Integer type ;

    /**
     * 资源存放地点
     */
    @TableField("resource_uri")
    private String resourceUri ;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime ;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Long updateTime = System.currentTimeMillis() / 1000;

    /**
     * 逻辑删除，默认值0
     */
    @TableField("is_deleted")
    private Integer isDeleted ;
}
