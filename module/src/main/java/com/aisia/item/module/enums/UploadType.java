package com.aisia.item.module.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
public enum UploadType {
    IMAGE(1,"/image"),
    VIDEO(2,"/video"),
    FILE(3,"/file")
    ;

    private final Integer type;
    private final String name;

    UploadType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * 根据状态码获取枚举实例
     *
     * @param type 类型
     * @return 对应的枚举实例，如果未找到则返回 null
     */
    public static UploadType getByType(Integer type){
        for (UploadType uploadType : UploadType.values()) {
            if(Objects.equals(uploadType.getType(), type)){
                return uploadType;
            }
        }
        return null;
    }
}
