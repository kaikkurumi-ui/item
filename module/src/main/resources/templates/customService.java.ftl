package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
<#if generateService>
import ${package.Service}.${table.serviceImplName};
</#if>
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>()<#if generateService>, ${table.serviceName}</#if> {

}
<#else>
public class ${table.serviceImplName} {

    <#assign primaryKeyField = "">
    <#assign primaryKeyType = "">
    <#list table.fields as field>
        <#if field.keyFlag>
            <#assign primaryKeyField = field.name>
            <#assign primaryKeyType = field.propertyType>
            <#break>
        </#if>
    </#list>
    <#assign baseEntityTypeName = table.entityName?remove_ending("Entity")?cap_first>
    <#assign baseEntityFieldName = table.entityName?remove_ending("Entity")>
    @Autowired
    private ${baseEntityTypeName}Mapper ${baseEntityFieldName}Mapper;

    public int insert(${entity} ${table.entityPath}){
        return ${baseEntityFieldName}Mapper.insert(${table.entityPath});
    }

    public int update(${entity} ${table.entityPath}){
        return ${baseEntityFieldName}Mapper.update(${table.entityPath});
    }

    public ${entity} getById(${primaryKeyType} ${primaryKeyField}){
        return ${baseEntityFieldName}Mapper.getById(${primaryKeyField});
    }

    public ${entity} extractById(${primaryKeyType} ${primaryKeyField}){
        return ${baseEntityFieldName}Mapper.extractById(${primaryKeyField});
    }

    public int delete(${primaryKeyType} ${primaryKeyField}){
        return ${baseEntityFieldName}Mapper.delete(${primaryKeyField});
    }
}
</#if>
