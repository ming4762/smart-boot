/**
 * ${description} 国际化信息
 */
export default {
  trans: true,
  key: '${mainTable.i18nPrefix}',
  data: {
		title: {
	  <#list mainTable.codePageConfigList as item>
	  <#if item.javaProperty!='seq' & item.javaProperty!='remark' & item.javaProperty!='useYn' & item.javaProperty!='deleteYn' & item.javaProperty!='createUserId' & item.javaProperty!='createTime' & item.javaProperty!='updateUserId' & item.javaProperty!='updateTime'>
			${item.javaProperty}: '${item.title}',
	  </#if>
	  </#list>
    },
		validate: {
	  <#list mainTable.codeFormConfigList as item>
	  <#if item.javaProperty!='seq' & item.javaProperty!='remark' & item.javaProperty!='useYn' & item.controlType!='radio' & item.controlType!='checkbox' & item.controlType!='switch_type'>
			${item.javaProperty}: '<#if item.controlType="select" || item.controlType="date" || item.controlType="time" || item.controlType="datetime">请选择<#else>请输入</#if>${item.title}',
	  </#if>
	  </#list>
		},
		rules: {
	  <#list mainTable.codeFormConfigList as item>
	  <#list item.ruleList as rule>
	  <#if item.javaProperty!='seq' & item.javaProperty!='remark' & item.javaProperty!='useYn'>
			'${item.javaProperty}_${rule.ruleType}': '${rule.message}',
	  </#if>
	  </#list>
	  </#list>
		},
		search: {
		<#list mainTable.codeSearchConfigList as item>
		<#if item.javaProperty!='seq' & item.javaProperty!='remark' & item.javaProperty!='useYn' & item.controlType!='radio' & item.controlType!='checkbox' & item.controlType!='switch_type'>
			${item.javaProperty}: '<#if item.controlType="select" || item.controlType="date" || item.controlType="time" || item.controlType="datetime">请选择<#else>请输入</#if>${item.title}',
		</#if>
		</#list>
		}
  }
}
