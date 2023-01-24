<template>
  <BasicModal
    @register="registerModal"
    width="600px"
    style="top: 15px"
    :title="$t('generator.views.code.button.createCode')">
    <BasicForm @register="registerForm">
      <template #form-templateIdList="{ model }">
        <SmartTableSelect
          v-model:value="model.templateIdList"
          :table-props="{}"
          title="选择模板"
          defaultFullscreen
          multiple
          label-field="name"
          value-field="templateId">
          <template #table="{ setSelectData }">
            <TemplateSelectTable :set-select-data="setSelectData" />
          </template>
        </SmartTableSelect>
      </template>
    </BasicForm>
  </BasicModal>
</template>

<script lang="ts" setup>
import { useI18n } from '/@/hooks/web/useI18n'

import { BasicModal, useModalInner } from '/@/components/Modal'
import { BasicForm, useForm } from '/@/components/Form'
import { SmartTableSelect } from '/@/components/Form'
import TemplateSelectTable from './TemplateSelectTable.vue'

const { t } = useI18n()

const [registerModal] = useModalInner((codeConfigData: Recordable) => {
  const { remarks, tableName, className } = codeConfigData
  setFieldsValue({
    description: remarks,
    tableName,
    className,
  })
})

const [registerForm, { setFieldsValue }] = useForm({
  labelCol: {
    span: 6,
  },
  wrapperCol: {
    span: 17,
  },
  baseColProps: {
    span: 24,
  },
  showActionButtonGroup: false,
  schemas: [
    {
      label: t('generator.views.codeCreateForm.title.description'),
      field: 'description',
      component: 'Input',
    },
    {
      label: t('generator.views.codeCreateForm.title.tableName'),
      field: 'tableName',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      label: t('generator.views.codeCreateForm.title.className'),
      field: 'className',
      component: 'Input',
      required: true,
    },
    {
      label: t('generator.views.codeCreateForm.title.packages'),
      field: 'packages',
      component: 'Input',
      required: true,
    },
    {
      label: t('generator.views.codeCreateForm.title.controllerBasePath'),
      field: 'controllerBasePath',
      component: 'Input',
      required: true,
    },
    {
      label: t('generator.views.codeCreateForm.title.customConfig'),
      field: 'customConfig',
      component: 'InputTextArea',
      componentProps: {
        placeholder: t('generator.views.codeCreateForm.message.customConfig'),
      },
    },
    {
      label: t('generator.views.codeCreateForm.title.templateList'),
      field: 'templateIdList',
      component: 'Input',
      slot: 'form-templateIdList',
    },
  ],
})
</script>

<style scoped></style>
