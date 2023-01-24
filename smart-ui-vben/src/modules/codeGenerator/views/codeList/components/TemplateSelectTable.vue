<template>
  <LayoutSeparate first-size="200px" class="full-height">
    <template #first></template>
    <template #second>
      <SmartTable @register="registerTable" />
    </template>
  </LayoutSeparate>
</template>

<script lang="ts" setup>
import { propTypes } from '/@/utils/propTypes'
import { useI18n } from '/@/hooks/web/useI18n'

import { SmartTable, useSmartTable } from '/@/components/SmartTable'
import { LayoutSeparate } from '/@/components/LayoutSeparate'
import { TemplateType as templateTypeConstants } from '/@/modules/codeGenerator/constants/DatabaseConstants'

const props = defineProps({
  setSelectData: propTypes.func.isRequired,
})
const { t } = useI18n()

const [registerTable] = useSmartTable({
  useSearchForm: true,
  proxyConfig: {

  },
  searchFormConfig: {
    colon: true,
    layout: 'inline',
    baseColProps: {
      span: 12,
    },
    actionColOptions: {
      span: 12,
    },
    schemas: [
      {
        label: t('generator.views.template.table.name'),
        field: 'name',
        component: 'Input',
      },
    ],
  },
  columns: [
    {
      type: 'checkbox',
      width: 60,
      fixed: 'left',
    },
    {
      field: 'name',
      title: '{generator.views.template.table.name}',
      width: 200,
      fixed: 'left',
      align: 'left',
      headerAlign: 'center',
    },
    {
      field: 'templateType',
      title: '{generator.views.template.table.templateType}',
      width: 140,
      formatter: ({ row }: any) => {
        const templateType = templateTypeConstants[row.templateType]
        if (templateType) {
          return t(templateType.label)
        }
        return ''
      },
    },
    {
      field: 'language',
      title: '{generator.views.template.table.language}',
      width: 200,
    },
    {
      field: 'remark',
      title: '{generator.views.template.table.remark}',
      minWidth: 200,
      align: 'left',
      headerAlign: 'center',
    },
  ],
})
</script>

<style scoped></style>
