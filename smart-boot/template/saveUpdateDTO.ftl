package ${packages}.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
* ${mainTable.tableName} - ${(description)!''}
* @author GCCodeGenerator
* ${.now}
*/
@Ge
@Getter
@Setter
@ToString
public class ${className}SaveUpdateDTO implements Serializable {


    <#list mainTable.codeFormConfigList as item>
    /**
    * ${item.title}
    */
    <#list item.ruleList as rule>
    <#if rule.ruleType="NOT_EMPTY">
    @NotNull(message = "${rule.message}")
    <#elseif rule.ruleType="EMAIL">
    @Email(message = "${rule.message}")
    <#elseif rule.ruleType="PHONE">
    @Mobile(message = "${rule.message}")
    </#if>
    </#list>
    private ${item.simpleJavaType} ${item.javaProperty};
    </#list>

}
