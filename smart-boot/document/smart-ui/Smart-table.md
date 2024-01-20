# SMART-TABLE组件使用说明

对vxe-table组件进行封装增强

## 一、使用示例

## 二、 使用说明

### 1、useSmartTable

使用该模式下如何使用响应性

## 三、Props

> 支持vxe-table原有功能

序号 | 属性 | 类型 | 默认值 | 可选值 | 说明
-- | -- | -- | -- | -- | --
1|useSearchForm|boolean|false|true/false|是否使用搜索表单
2|searchFormConfig|SmartSearchFormProps|||参考下方搜索表单配置
3|checkboxConfig|SmartCheckboxConfig|||参考下方复选框配置
|||||
|||||
|||||
|||||
|||||
|||||

### 1、搜索表单配置

| 序号 | 属性 | 类型 | 默认值 | 可选值 | 说明 |
| ---- | ---- | ---- | ------ | ------ | ---- |
|1 | schemas|Array<SmartSearchFormSchema>|||参考下方搜索表单列配置|
| | searchWithSymbol |boolean|||搜索是否带有符号，参考下面搜索符号说明|
| | |||||

### 2、搜索表单列配置
| 序号 | 属性 | 类型 | 默认值 | 可选值 | 说明 |
| ---- | ---- | ---- | ------ | ------ | ---- |
|1 | searchSymbol |String|||参考下面搜索符号说明|
|2 | customSymbol |(schema, value, model) => Recordable|||自定义搜索符号，函数返回|

### 3、搜索符号说明

### 4、复选框配置

> 支持vxe-table原有复选框配置选项
>
> rowTrigger: single 与 vxe-table原有属性 trigger: row冲突

| 序号 | 属性 | 类型 | 默认值 | 可选值 | 说明 |
| ---- | ---- | ---- | ------ | ------ | ---- |
|1 | rowTrigger |String|single|single、multiple|single：点击行单行选中  multiple：点击行多选|
|2 | rowCtrl |boolean|true||是否支持ctrl选中，rowTrigger必须为single|
|3 | rowShift |boolean|true||是否支持shift多选，rowTrigger必须为single|