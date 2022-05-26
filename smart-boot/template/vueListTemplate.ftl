<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="gridRef"
      v-bind="tableProps"
      :size="tableSizeConfig"
      border
      :column-config="columnConfig"
      :toolbar-config="toolbarConfig"
      :columns="columns"
      height="auto"
      stripe
      highlight-hover-row>
<#if mainTable.page>
      <template #pager>
        <vxe-pager
          v-bind="pageProps"
          :page-sizes="[500, 1000, 2000, 5000]"
          :layouts="['Sizes', 'PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'FullJump', 'Total']" />
      </template>
</#if>
      <#if (mainTable.rightButtonList?size>0)>
      <template #toolbar_tools>
        <a-form layout="inline">
          <a-form-item>
            <#list mainTable.rightButtonList as item>
            <a-button
              :size="buttonSizeConfig"
              type="primary"
              <#if item="ADD">
              @click="() => handleAddEdit(true, null)"
              <#elseif item="EDIT">
              @click="handleEditByCheckbox"
              <#elseif item="DELETE">
              danger
              @click="handleDeleteByCheckbox"
              </#if>
              style="margin-left: 5px">
              <#if item="ADD">
              {{ $t('common.button.add') }}
              <#elseif item="EDIT">
              {{ $t('common.button.edit') }}
              <#elseif item="DELETE">
              {{ $t('common.button.delete') }}
              </#if>
            </a-button>
            </#list>
          </a-form-item>
        </a-form>
      </template>
      </#if>
      <#if (mainTable.leftButtonList?size>0)>
      <template #toolbar_buttons>
        <a-form layout="inline" style="margin-left: 10px">
          <#if (mainTable.searchColNum = 1)>
          <#list mainTable.codeSearchConfigList as item>
          <#if item.javaProperty='useYn'>
          <a-form-item :label="$t('common.table.useYn')">
          <#elseif item.javaProperty='createTime'>
          <a-form-item :label="$t('common.table.createTime')">
          <#elseif item.javaProperty='updateTime'>
          <a-form-item :label="$t('common.table.updateTime')">
          <#elseif item.javaProperty='createUserId'>
          <a-form-item :label="$t('common.table.createUser')">
          <#elseif item.javaProperty='remark'>
          <a-form-item :label="$t('common.table.remark')">
          <#else >
          <a-form-item :label="$t('${mainTable.i18nPrefix}.title.${item.javaProperty}')">
          </#if>
            <#if (item.controlType='INPUT')>
            <a-input v-model:value="searchModel.${item.javaProperty}" :size="formSizeConfig" :placeholder="$t('${mainTable.i18nPrefix}.search.${item.javaProperty}')" />
            <#elseif (item.controlType='NUMBER')>
            <a-input-number v-model:value="searchModel.${item.javaProperty}" style="width: 100%" :size="formSizeConfig" :placeholder="$t('${mainTable.i18nPrefix}.search.${item.javaProperty}')" />
            <#elseif (item.controlType='TEXTAREA')>
            <a-textarea v-model:value="searchModel.${item.javaProperty}" :size="formSizeConfig" :placeholder="$t('${mainTable.i18nPrefix}.search.${item.javaProperty}')" />
            <#elseif (item.controlType='PASSWORD')>
            <a-input-password v-model:value="searchModel.${item.javaProperty}" :size="formSizeConfig" :placeholder="$t('${mainTable.i18nPrefix}.search.${item.javaProperty}')" />
            <#elseif (item.controlType='RADIO')>
            <a-radio-group v-model:value="searchModel.${item.javaProperty}" name="radioGroup-${item.javaProperty}">
              // TODO: 待开发
            </a-radio-group>
            <#elseif (item.controlType='SWITCH_TYPE')>
            <a-switch v-model:checked="searchModel.${item.javaProperty}" />
            </#if>
          </a-form-item>
          </#list>
          </#if>
          <a-form-item>
            <#list mainTable.leftButtonList as item>
            <a-button
              :size="buttonSizeConfig"
              style="margin-left: 5px"
              <#if item="SEARCH">
              type="primary"
              @click="loadData"
              <#elseif item="RESET">
              @click="handleReset"
              </#if>>
              <#if item="SEARCH">
              {{ $t('common.button.search') }}
              <#elseif item="RESET">
              {{ $t('common.button.reset') }}
              </#if>
            </a-button>
            </#list>
          </a-form-item>
        </a-form>
      </template>
      </#if>
      <#if (mainTable.rowButtonType!='NONE')>
      <template #table-operation="{ row }">
        <#if (mainTable.rowButtonType="MORE")>
        <a-dropdown>
          <a-button :size="tableButtonSizeConfig" type="primary">
            Actions
            <DownOutlined />
          </a-button>
          <template #overlay>
            <a-menu @click="({ key }) => handleActions(row, key)">
              <#list mainTable.rowButtonList as item>
              <a-menu-item key="${item}">
                <#if (item = "EDIT")>
                {{ $t('common.button.edit') }}
                </#if>
                <#if (item = "DELETE")>
                {{ $t('common.button.delete') }}
                </#if>
                <#if (item = "ADD")>
                {{ $t('common.button.add') }}
                </#if>
              </a-menu-item>
              </#list>
            </a-menu>
          </template>
        </a-dropdown>
        </#if>
      </template>
      </#if>
    </vxe-grid>
    <#if (mainTable.codeFormConfigList?size>0)>
    <a-modal
      v-bind="modalProps">
      <a-spin :spinning="spinning">
        <a-form
          ref="formRef"
          :rules="rules"
          :label-col="{span: 6}"
          :wrapper-col="{span: 17}"
          v-bind="formProps">
          <a-row>
            <#list mainTable.codeFormConfigList as item>
            <#if item.hidden>
            <a-input v-model:value="formProps.model.${item.javaProperty}" style="display: none" />
             <#else>
            <a-col :span="${24/mainTable.formColNum}">
              <a-form-item
                <#if item.javaProperty="seq">
                :label="$t('common.table.seq')"
                <#elseif item.javaProperty="useYn">
                :label="$t('common.table.useYn')"
                <#elseif item.javaProperty="remark">
                :label="$t('common.table.remark')"
                <#else >
                :label="$t('${mainTable.i18nPrefix}.title.${item.javaProperty}')"
                </#if>
                name="${item.javaProperty}">
                <#if (item.controlType='INPUT')>
                <a-input
                  v-model:value="formProps.model.${item.javaProperty}"
                  <#if item.javaProperty="remark">
                  :placeholder="$t('common.formValidate.remark')" />
                  <#elseif item.javaProperty="seq">
                  :placeholder="$t('common.formValidate.seq')" />
                  <#else >
                  :placeholder="$t('${mainTable.i18nPrefix}.validate.${item.javaProperty}')" />
                  </#if>
                <#elseif (item.controlType='NUMBER')>
                <a-input-number
                  v-model:value="formProps.model.${item.javaProperty}"
                  style="width: 100%"
                  <#if item.javaProperty="seq">
                  :placeholder="$t('common.formValidate.seq')" />
                  <#else >
                  :placeholder="$t('${mainTable.i18nPrefix}.validate.${item.javaProperty}')" />
                  </#if>
                <#elseif (item.controlType='TEXTAREA')>
                <a-textarea v-model:value="formProps.model.${item.javaProperty}" :placeholder="$t('${mainTable.i18nPrefix}.validate.${item.javaProperty}')" />
                <#elseif (item.controlType='PASSWORD')>
                <a-input-password v-model:value="formProps.model.${item.javaProperty}" :placeholder="$t('${mainTable.i18nPrefix}.validate.${item.javaProperty}')" />
                <#elseif (item.controlType='RADIO')>
                <a-radio-group v-model:value="formProps.model.${item.javaProperty}" name="radioGroup-${item.javaProperty}"></a-radio-group>
                <#elseif (item.controlType='SWITCH_TYPE')>
                <a-switch v-model:checked="formProps.model.${item.javaProperty}" />
                </#if>
              </a-form-item>
            </a-col>
            </#if>
            </#list>
          </a-row>
        </a-form>
      </a-spin>
    </a-modal>
    </#if>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'

import { <#if (mainTable.rowButtonType="MORE")>DownOutlined</#if> } from '@ant-design/icons-vue'

import { useVxeTable<#if (mainTable.codeFormConfigList?size>0)>, useAddEdit</#if>, useVxeDelete } from '@/components/hooks'
import SizeConfigHooks from '@/components/config/SizeConfigHooks'

import { handleLoadData<#if (mainTable.codeFormConfigList?size>0)>, handleGetById, handleSaveUpdate</#if>, handleDelete } from './${className}Hook'

export default defineComponent({
  name: '${className}ListView',
  components: {
    <#if (mainTable.rowButtonType="MORE")>DownOutlined</#if>
  },
  setup () {
    const { t } = useI18n()
    const gridRef = ref()

    /**
     * 查询数据hook
     */
    const { tableProps, handleReset<#if mainTable.page>, pageProps</#if><#if (mainTable.codeFormConfigList?size>0)>, searchModel</#if>, loadData } = useVxeTable(handleLoadData, {
      paging: ${mainTable.page?string("true", "false")}
    })
    <#if (mainTable.codeFormConfigList?size>0)>

    /**
     * 添加保存hook
     */
    const addEditHook = useAddEdit(gridRef, handleGetById, loadData, handleSaveUpdate, t, {
      idField: '${mainTable.idField.javaProperty!}'
    })
    </#if>

    /**
     * 删除hook
     */
    const deleteHook = useVxeDelete(gridRef, t, handleDelete, { idField: '${mainTable.idField.javaProperty!}', listHandler: loadData })

    <#if (mainTable.rowButtonType="MORE")>
    /**
     * 按钮操作
     * @param row 行数据
     * @param action 操作
     */
    const handleActions = (row: any, action: string) => {
      switch (action) {
        <#list mainTable.rowButtonList as item>
        case '${item}': {
          <#if (item = "EDIT")>
          addEditHook.handleAddEdit(false, row.${mainTable.idField.javaProperty!})
          <#elseif (item = "DELETE")>
          deleteHook.handleDeleteByRow(row)
          </#if>
          break
        }
        </#list>
      }
    }
    </#if>
    onMounted(loadData)
    return {
      gridRef,
      ...SizeConfigHooks(),
      <#if (mainTable.codeFormConfigList?size>0)>
      ...addEditHook,
      </#if>
      <#if (mainTable.codeFormConfigList?size>0)>
      searchModel,
      </#if>
      ...deleteHook,
      tableProps,
      <#if mainTable.page>
      pageProps,
      </#if>
      handleReset,
      <#if (mainTable.rowButtonType="MORE")>
      handleActions,
      </#if>
      loadData
    }
  },
  data () {
    return {
      columnConfig: {
        <#if mainTable.columnSort>
        resizable: true
        </#if>
      },
      toolbarConfig: {
        slots: {
          <#if (mainTable.rightButtonList?size>0)>
          tools: 'toolbar_tools'<#if (mainTable.leftButtonList?size>0)>,</#if>
          </#if>
          <#if (mainTable.leftButtonList?size>0)>
          buttons: 'toolbar_buttons'
          </#if>
        }
      },
      <#if (mainTable.codeFormConfigList?size>0)>
      rules: {
        <#list mainTable.codeFormConfigList as item>
        <#if (item.ruleList?size>0) >
        ${item.javaProperty}: [
          <#list item.ruleList as rule>
          {
            required: true,
            trigger: [
              <#list rule.ruleTrigger as trigger>
              '${trigger?lower_case}'<#sep>,
              </#list>

            ],
            <#if rule.ruleType='NUMBER'>
            type: 'number',
            </#if>
            <#if item.javaProperty="seq">
            message: this.$t('common.formValidate.seq')
            <#elseif item.javaProperty="remark">
            message: this.$t('common.formValidate.remark')
            <#else >
            message: this.$t('${mainTable.i18nPrefix}.rules.${item.javaProperty}_${rule.ruleType}')
            </#if>
          }<#sep>,
          </#list>

        ]<#sep>,
        </#if>
        </#list>
      },
      </#if>
      columns: [
       <#if mainTable.showCheckbox>
        {
          type: 'checkbox',
          width: 60,
          align: 'center',
          fixed: 'left'
        },
       </#if>
       <#list mainTable.codePageConfigList as item>
        {
          field: '${item.javaProperty}',
          <#if item.fixed??>
          fixed: '${item.fixed}',
          </#if>
          <#if item.hidden>
          hidden: true,
          </#if>
          <#if item.sortable>
          sortable: true,
          </#if>
          <#if item.javaProperty='createTime'>
          title: '{common.table.createTime}',
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          <#elseif item.javaProperty='updateTime'>
          title: '{common.table.updateTime}',
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          <#elseif item.javaProperty='createUserId'>
          title: '{common.table.createUser}',
          formatter: ({ row }: any) => {
            if (row.createUser) {
              return row.createUser.fullName
            }
            return ''
          },
          <#elseif item.javaProperty='remark'>
          title: '{common.table.remark}',
          <#elseif item.javaProperty='useYn'>
          title: '{common.table.useYn}',
          <#elseif item.javaProperty='deleteYn'>
          title: '{common.table.deleteYn}',
          <#elseif item.javaProperty='updateUserId'>
          title: '{common.table.updateUser}',
          formatter: ({ row }: any) => {
            if (row.updateUser) {
              return row.updateUser.fullName
            }
            return ''
          },
          <#elseif item.javaProperty='seq'>
          title: '{common.table.seq}',
          <#elseif item.javaProperty='useYn'>
          title: '{common.table.useYn}',
          <#else >
          title: '{${mainTable.i18nPrefix}.title.${item.javaProperty}}',
          </#if>
          width: ${item.width}
        },
       </#list>
        <#if (mainTable.rowButtonType!='NONE')>
        {
          title: '{common.table.operation}',
          field: 'operation',
          width: 120,
          fixed: 'right',
          slots: {
            default: 'table-operation'
          }
        }
        </#if>
      ]
    }
  }
})
</script>
