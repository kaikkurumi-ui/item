package ${package.Mapper};

<#list importMapperFrameworkPackages as pkg>
import ${pkg};
</#list>
<#if importMapperJavaPackages?size !=0>

  <#list importMapperJavaPackages as pkg>
import ${pkg};
   </#list>
</#if>

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if mapperAnnotationClass??>
@${mapperAnnotationClass.simpleName}
</#if>
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}> {
<#else>
@Mapper
public interface ${table.mapperName} {
</#if>

    int insert(${entity} ${table.entityPath});

    int update(${entity} ${table.entityPath});

<#list table.fields as field>
    <#if field.keyFlag>
        <#macro param name><#noparse>#{</#noparse>${name}}</#macro>
        @Select("SELECT * FROM ${table.name} WHERE ${field.name} = <@param name=field.propertyName/> AND is_deleted = 0")
        ${entity} getById(@Param("${field.propertyName}") ${field.propertyType} ${field.propertyName});

        @Select("SELECT * FROM ${table.name} WHERE ${field.name} = <@param name=field.propertyName/>")
        ${entity} extractById(@Param("${field.propertyName}") ${field.propertyType} ${field.propertyName});

        @Update("UPDATE ${table.name} SET is_deleted = 1 WHERE ${field.name} = <@param name=field.propertyName/> AND is_deleted = 0")
        int delete(@Param("${field.propertyName}") ${field.propertyType} ${field.propertyName});
    </#if>
</#list>
<#list mapperMethodList as m>
    /**
     * generate by ${m.indexName}
     *
    <#list m.tableFieldList as f>
     * @param ${f.propertyName} ${f.comment}
    </#list>
     */
    ${m.method}
</#list>
}
