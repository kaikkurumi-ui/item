<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">
    <#assign primaryKeyField = "id">
    <#assign primaryKeyType = "Object">

    <#list table.fields as field>
        <#if field.keyFlag>
            <#assign primaryKeyField = field.columnName>
            <#assign primaryKeyType = field.propertyType>
            <#break>
        </#if>
    </#list>

    <insert id="insert" parameterType="${package.Entity}.${entity}" useGeneratedKeys="true" keyProperty="${primaryKeyField}">
        insert into ${table.name} (
        <trim suffixOverrides=",">
            <#list table.fields as field>
            <#-- 1. 排除主键 -->
                <#if !field.keyFlag>
                <#-- 2. is_deleted 字段直接输出，不加任何判断 -->
                    <#if field.columnName == 'is_deleted'>
                        ${field.columnName},
                    <#else>
                    <#-- 3. 其他字段进行非空判断（字符串额外判断 != ''） -->
                        <if test="${field.propertyName} != null<#if field.propertyType == "String"> and ${field.propertyName} != ''</#if>">
                            ${field.columnName},
                        </if>
                    </#if>
                </#if>
            </#list>
        </trim>
        ) VALUES (
        <trim suffixOverrides=",">
            <#list table.fields as field>
                <#if !field.keyFlag>
                    <#if field.columnName == 'is_deleted'>
                        #${r'{'}${field.propertyName}${r'}'},
                    <#else>
                        <if test="${field.propertyName} != null<#if field.propertyType == "String"> and ${field.propertyName} != ''</#if>">#${r'{'}${field.propertyName}${r'}'},</if>
                    </#if>
                </#if>
            </#list>
        </trim>
        )
    </insert>

    <update id="update" parameterType="${package.Entity}.${entity}">
        update ${table.name}
        <set>
            <#list table.fields as field>
            <#-- 1. 排除主键，且排除 create_time -->
                <#if !field.keyFlag && field.columnName != 'create_time'>
                <#-- 2. 字符串类型额外判断 != ''，使用安全取值 -->
                    <if test="${field.propertyName} != null<#if (field.propertyType!"") == "String"> and ${field.propertyName} != ''</#if>">
                        ${field.columnName} = #${r'{'}${field.propertyName}${r'}'},
                    </if>
                </#if>
            </#list>
        </set>
        <#-- 3. 使用动态主键变量，防止硬编码报错 -->
        where ${primaryKeyField} = #${r'{'}${primaryKeyField}${r'}'}
    </update>
<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="${cacheClassName}"/>

</#if>
<#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
<#list table.fields as field>
<#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.commonFields as field><#--生成公共字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#list>
<#list table.fields as field>
<#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
    </resultMap>

</#if>
<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
<#list table.commonFields as field>
        ${field.columnName},
</#list>
        ${table.fieldNames}
    </sql>

</#if>
</mapper>
