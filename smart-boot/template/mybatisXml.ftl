<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packages}.mapper.${className}Mapper">
    <sql id="columnList">
        <#list mainTable.codePageConfigList as item>
        <!--   ${(item.title)!''} <#if item.primaryKey>主键</#if>  <#if item.indexed>索引</#if>   -->
        ${item.columnName},
        </#list>
    </sql>

    <resultMap id="commonResultMap" type="${packages}.model.${className}PO">
        <#list mainTable.codePageConfigList as item>
        <#if item.primaryKey>
        <!--   ${(item.title)!''} <#if item.primaryKey>主键</#if>  <#if item.indexed>索引</#if>   -->
        <id column="${item.columnName}" property="${item.javaProperty}" javaType="${item.javaType}" jdbcType="<#if item.typeName="INT">INTEGER<#elseif item.typeName="DATETIME">DATE<#else >${item.typeName}</#if>"/>
        <#else >
        <!--   ${(item.title)!''} <#if item.primaryKey>主键</#if>  <#if item.indexed>索引</#if>   -->
        <result column="${item.columnName}" property="${item.javaProperty}" javaType="${item.javaType}" jdbcType="<#if item.typeName="INT">INTEGER<#elseif item.typeName="DATETIME">DATE<#else >${item.typeName}</#if>"/>
        </#if>
        </#list>
    </resultMap>
</mapper>
