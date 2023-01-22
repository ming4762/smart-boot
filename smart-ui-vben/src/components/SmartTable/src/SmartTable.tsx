import type { SmartTableProps, TableActionType } from './types/SmartTableType'
import type { VxeGridInstance } from 'vxe-table'

import { computed, defineComponent, Ref, ref, unref } from 'vue'

import { TableSearchLayout } from '/@/components/Layout'
import { BasicForm, useForm } from '/@/components/Form'
import { useModal } from '/@/components/Modal'

import { smartTableProps } from './props'
import { useTableSearchForm } from './hooks/useTableSearchForm'
import { omit } from 'lodash-es'
import { useLoading } from './hooks/useLoading'
import { usePagination } from './hooks/usePagination'
import { useTableAjax } from './hooks/useTableAjax'
import { useTableToolbarConfig } from './hooks/useTableToolbarConfig'
import { useTableModalAddEditConfig } from './hooks/useTableModalAddEdit'
import { createTableContext } from './hooks/userSmartTableContext'
import SmartTableAddEditModal from './components/SmartTableAddEditModal'
import './renderer/VxeTableButtonRenderer'
import { error } from '/@/utils/log'

export default defineComponent({
  name: 'SmartTable',
  components: {
    BasicForm,
  },
  props: smartTableProps,
  emits: ['register'],
  setup(props, { emit, slots, attrs }) {
    const tableElRef = ref<VxeGridInstance>() as Ref<VxeGridInstance>
    const wrapRef = ref(null)

    const innerPropsRef = ref<Partial<SmartTableProps>>()
    /**
     * 表格计算属性
     */
    const getTableProps = computed(() => {
      return { ...props, ...unref(innerPropsRef) } as SmartTableProps
    })

    const setProps = (props: Partial<SmartTableProps>) => {
      innerPropsRef.value = { ...unref(innerPropsRef), ...props }
    }

    /**
     * 加载状态
     */
    const { getLoading, setLoading } = useLoading(getTableProps)

    // -------------- 分页 ---------------------------
    const {
      getPaginationInfo,
      getPagination,
      setPagination,
      getShowPagination,
      setShowPagination,
    } = usePagination(getTableProps)

    const [registerSearchForm, searchFormAction] = useForm()
    /**
     * vxe-table函数
     */
    // @ts-ignore
    const commitVxeProxy = (code, ...args) => unref(tableElRef)?.commitProxy(code, args)
    const getCheckboxRecords = (isFull: boolean) =>
      unref(tableElRef)?.getCheckboxRecords(isFull) || []
    const getRadioRecord = (isFull: boolean) => unref(tableElRef)?.getRadioRecord(isFull)
    const setRadioRow = (row: any) => unref(tableElRef)!.setRadioRow(row)
    const setCheckboxRow = (rows: any | any[], checked: boolean) =>
      unref(tableElRef)!.setCheckboxRow(rows, checked)

    // -------------- 加载函数 ------------------------
    searchFormAction.getFieldsValue
    const { reload, getProxyConfigRef, deleteByRow, deleteByCheckbox } = useTableAjax(
      getTableProps,
      tableElRef,
      {
        commitVxeProxy,
        getSearchFormModel: searchFormAction.getFieldsValue,
        getCheckboxRecords,
        setLoading,
      },
    )

    // -------------- 搜索表单 ------------------------
    const {
      getSearchFormProps,
      handleSearchInfoChange,
      getSearchFormSlot,
      getSearchFormColumnSlot,
    } = useTableSearchForm(getTableProps, slots, reload)

    // -------------- 添加修改操作 ---------------------
    const [registerAddEditModal, { openModal: openAddEditModal }] = useModal()
    const addEditModalRef = ref()
    const getAddEditFieldsValue = () => unref(addEditModalRef).getFieldsValue()
    const resetAddEditFields = () => unref(addEditModalRef).resetFields()
    const setAddEditFieldsValue = (data: Recordable) => unref(addEditModalRef).setFieldsValue(data)
    const validateAddEdit = () => unref(addEditModalRef).validate()
    const validateAddEditFields = () => unref(addEditModalRef).validateFields()
    const {
      getHasAddEdit,
      showAddModal,
      editByCheckbox,
      editByRow,
      getAddEditFormProps,
      getAddEditModalProps,
      getAddEditFormSlots,
    } = useTableModalAddEditConfig(getTableProps, slots, {
      getCheckboxRecords,
      openAddEditModal,
      reload,
    })

    // ------------- toolbar配置 ----------------------
    const { getToolbarConfigInfo } = useTableToolbarConfig(getTableProps, {
      deleteByCheckbox,
      showAddModal,
      editByCheckbox,
    })
    /**
     * 获取table v-bing
     */
    const getTableBindValues = computed<SmartTableProps>(() => {
      let propsData: SmartTableProps = {
        ...attrs,
        ...unref(getTableProps),
        loading: unref(getLoading),
        // data: dataSource,
        pagerConfig: unref(getPaginationInfo),
        toolbarConfig: unref(getToolbarConfigInfo),
        proxyConfig: unref(getProxyConfigRef),
      }

      propsData = omit(propsData, [])
      return propsData
    })

    /**
     *
     */
    const tableAction: TableActionType = {
      reload,
      setProps,
      setLoading,
      // 分页
      getPagination,
      setPagination,
      commitVxeProxy,
      getShowPagination,
      setShowPagination,
      deleteByCheckbox,
      getCheckboxRecords,
      getRadioRecord,
      // openAddEditModal,
      showAddModal,
      editByCheckbox,
      editByRow,
      getAddEditFieldsValue,
      resetAddEditFields,
      setAddEditFieldsValue,
      deleteByRow,
      setRadioRow,
      setCheckboxRow,
      validateAddEdit,
      validateAddEditFields,
    }

    createTableContext({ ...tableAction, wrapRef, getBindValues: getTableBindValues })

    emit('register', tableAction, searchFormAction)

    return {
      registerSearchForm,
      getSearchFormProps,
      getTableProps,
      tableAction,
      getTableBindValues,
      getPaginationInfo,
      tableElRef,
      handleSearchInfoChange,
      getSearchFormSlot,
      getSearchFormColumnSlot,
      getHasAddEdit,
      registerAddEditModal,
      getAddEditFormProps,
      getAddEditModalProps,
      addEditModalRef,
      wrapRef,
      getAddEditFormSlots,
    }
  },
  render() {
    const {
      getTableProps: { useSearchForm },
    } = this

    // const $this = this

    const slots: any = {
      default: renderTable(this),
    }
    /**
     * 渲染搜索表格
     */
    if (useSearchForm) {
      slots.search = renderSearchForm(this)
    }
    // @ts-ignore
    return <TableSearchLayout ref="wrapRef">{slots}</TableSearchLayout>
  },
})

/**
 * 渲染搜索表单
 * @param smartTableInstance
 */
const renderSearchForm = (smartTableInstance) => {
  return () => {
    const {
      registerSearchForm,
      getSearchFormProps,
      handleSearchInfoChange,
      getSearchFormSlot,
      getSearchFormColumnSlot,
    } = smartTableInstance
    const formAttrs = {
      ...getSearchFormProps,
      onSubmit: handleSearchInfoChange,
      submitOnReset: true,
      onRegister: registerSearchForm,
    }
    // 获取插槽
    const searchFormSlots = {
      ...getSearchFormSlot,
      ...getSearchFormColumnSlot,
    }
    if (
      Object.keys(getSearchFormSlot).length + Object.keys(getSearchFormColumnSlot).length >
      Object.keys(searchFormSlots).length
    ) {
      error('搜索表单插槽命名重复')
    }
    return <BasicForm {...formAttrs}>{searchFormSlots}</BasicForm>
  }
}

/**
 * 渲染表格
 */
const renderTable = (instance) => {
  const slots: any = {}
  return () => {
    const {
      getTableBindValues,
      $slots,
      getHasAddEdit,
      registerAddEditModal,
      getAddEditFormProps,
      getAddEditModalProps,
      getAddEditFormSlots,
    } = instance
    const result = [
      <vxe-grid ref="tableElRef" {...getTableBindValues}>
        {{ ...slots, ...$slots }}
      </vxe-grid>,
    ]
    if (getHasAddEdit) {
      result.push(
        <SmartTableAddEditModal
          ref="addEditModalRef"
          {...getAddEditModalProps}
          onRegister={registerAddEditModal}
          formConfig={getAddEditFormProps}>
          {getAddEditFormSlots}
        </SmartTableAddEditModal>,
      )
    }
    return result
  }
}
