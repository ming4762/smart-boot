<template>
  <div class="full-height page-container">
    <LayoutSeparate :show-line="false" first-size="280px" class="full-height">
      <template #first>
        <div class="full-height dept-container">
          <SysDeptTree async show-search @select="handleDeptSelected" />
        </div>
      </template>
      <template #second>
        <SmartTable @register="registerTable" :size="getTableSize">
          <template #table-operation="{ row }">
            <SmartVxeTableAction :actions="getTableActions(row)" />
          </template>
          <template #table-userType="{ row }">
            <span>
              {{ getUserTypeMap[row.userType] }}
            </span>
          </template>
          <template #search-userType="{ model }">
            <a-select style="width: 100px" v-model:value="model.userType" allowClear>
              <a-select-option
                v-for="item in userTypeListRef"
                :key="'userType_' + item.dictItemCode"
                :value="item.dictItemCode">
                {{ item.dictItemName }}
              </a-select-option>
            </a-select>
          </template>
        </SmartTable>
      </template>
    </LayoutSeparate>
    <UserAccountUpdateModal @register="registerAccountModal" />
  </div>
</template>

<script lang="ts" setup>
import { computed, createVNode, ref, unref } from 'vue'
import { useI18n } from '/@/hooks/web/useI18n'

import { message, Modal } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

import { useLoadDictItem } from '/@/modules/system/hooks/dict/SysDictHooks'
import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'
import { hasPermission } from '/@/common/auth/AuthUtils'
import { useModal } from '/@/components/Modal'

import { LayoutSeparate } from '/@/components/LayoutSeparate'
import SysDeptTree from '/@/modules/system/views/dept/components/SysDeptTree.vue'

import {
  SmartTable,
  useSmartTable,
  SmartVxeTableAction,
  ActionItem,
} from '/@/components/SmartTable'
import UserAccountUpdateModal from './account/UserAccountUpdateModal.vue'

import { getAddEditFormSchemas, getSearchSchemas, getTableColumns } from './UserListView.config'
import {
  listApi,
  deleteApi,
  saveUpdateWithDataScopeApi,
  getByIdWithDataScopeApi,
  setUseYnApi,
  createAccountApi,
} from './UserListView.api'
import { SYS_USER_TYPE, SystemPermissions } from '/@/modules/system/constants/SystemConstants'

const { t } = useI18n()
const { getTableSize } = useSizeSetting()

const { dictData: userTypeListRef } = useLoadDictItem(ref('SYSTEM_USER_TYPE'))
const getUserTypeMap = computed(() => {
  const result: { [index: string]: string } = {}
  result[SYS_USER_TYPE] = '系统用户'
  for (let userType of unref(userTypeListRef)) {
    result[userType.dictItemCode] = userType.dictItemName
  }
  return result
})

/**
 * 权限处理
 */
const permissions = SystemPermissions.user
const hasPermissionUpdateSystemUser = hasPermission('sys:systemUser:update')
const hasSystemUserUpdate = (type: string) => {
  return hasPermissionUpdateSystemUser || type !== SYS_USER_TYPE
}

/**
 * 选中组织架构操作
 * @param selectedKeys
 */
const currentDeptId = ref<number | null>(null)
const handleDeptSelected = (selectedKeys: Array<number>) => {
  if (selectedKeys.length > 0) {
    currentDeptId.value = selectedKeys[0]
  } else {
    currentDeptId.value = null
  }
  // 重新加载数据
  reload()
}

/**
 * 账户弹窗
 */
const [registerAccountModal, { openModal }] = useModal()

/**
 * table行按钮
 */
const getTableActions = (row): ActionItem[] => {
  return [
    {
      label: t('common.button.edit'),
      onClick: () => editByRowModal(row),
      disabled: !hasPermission(permissions.update) || !hasSystemUserUpdate(row.userType),
    },
    {
      label: t('system.views.user.button.showAccount'),
      disabled: !hasPermission('sys:account:query') || !hasSystemUserUpdate(row.userType),
      onClick: () => openModal(true, row),
    },
  ]
}
/**
 * 用户操作验证
 * @param userList
 */
const validateOperateUser = (userList: Array<any>) => {
  if (userList.length === 0) {
    message.warn(t('system.views.user.validate.selectUser'))
    return false
  }
  if (!hasPermissionUpdateSystemUser) {
    // 如果没有修改系统用户的权限，判断用户中是否有系统用户
    const hasSysUser = userList.some(({ userType }: any) => userType === SYS_USER_TYPE)
    if (hasSysUser) {
      message.error(t('system.views.user.validate.noSysUserUpdatePermission'))
      return false
    }
  }
  return true
}

/**
 * 启停用户
 * @param useYn
 */
const handleSetUseYn = (useYn: boolean) => {
  const userList = getCheckboxRecords(false)
  // 验证用户
  const result = validateOperateUser(userList)
  if (!result) {
    return false
  }
  Modal.confirm({
    title: t('system.views.user.validate.setUserUseYn', {
      msg: useYn ? t('common.message.use') : t('common.message.noUse'),
    }),
    icon: createVNode(ExclamationCircleOutlined),
    onOk: async () => {
      await setUseYnApi(userList, useYn)
      // 重新加载数据
      reload()
    },
  })
}

/**
 * 创建账户
 */
const handleCreateAccount = () => {
  const userList = getCheckboxRecords(false)
  if (userList.length === 0) {
    message.warn(t('system.views.user.validate.selectUser'))
    return false
  }
  if (!hasPermissionUpdateSystemUser) {
    // 如果没有修改系统用户的权限，判断用户中是否有系统用户
    const hasSysUser = userList.some(({ userType }: any) => userType === SYS_USER_TYPE)
    if (hasSysUser) {
      message.error(t('system.views.user.validate.noSysUserUpdatePermission'))
      return false
    }
  }
  // 判断是否有停用用户
  const hasNoUse = userList.some((item) => item.useYn === false)
  if (hasNoUse) {
    message.warn(t('system.views.user.message.noUseUserNotCreateAccount'))
    return false
  }
  Modal.confirm({
    title: t('system.views.user.validate.createAccountConfirm'),
    icon: createVNode(ExclamationCircleOutlined),
    onOk: () => createAccountApi(userList),
  })
}

const [
  registerTable,
  { editByRowModal, getCheckboxRecords, reload, deleteByCheckbox, showAddModal },
] = useSmartTable({
  columns: getTableColumns(t),
  stripe: true,
  highlightHoverRow: true,
  height: 'auto',
  border: true,
  align: 'left',
  pagerConfig: true,
  useSearchForm: true,
  sortConfig: {
    remote: true,
    defaultSort: {
      field: 'seq',
      order: 'asc',
    },
  },
  searchFormConfig: {
    layout: 'inline',
    schemas: getSearchSchemas(t),
    colon: true,
    // size: 'small',
    searchWithSymbol: true,
    actionColOptions: {
      span: undefined,
    },
  },
  addEditConfig: {
    modalConfig: {
      width: '700px',
    },
    formConfig: {
      colon: true,
      schemas: getAddEditFormSchemas(t, userTypeListRef),
      labelCol: {
        span: 4,
      },
      wrapperCol: {
        span: 19,
      },
    },
  },
  proxyConfig: {
    ajax: {
      query: ({ ajaxParameter }) => {
        const parameter = {
          ...ajaxParameter,
        }
        const deptId = unref(currentDeptId)
        if (deptId) {
          parameter.deptIdList = [deptId]
        }
        return listApi(parameter)
      },
      delete: deleteApi,
      save: saveUpdateWithDataScopeApi,
      getById: getByIdWithDataScopeApi,
    },
  },
  toolbarConfig: {
    refresh: true,
    resizable: true,
    buttons: [
      {
        code: 'ModalAdd',
        auth: permissions.add,
        props: {
          onClick: () => {
            showAddModal({ deptId: unref(currentDeptId) })
          },
        },
      },
      {
        name: t('common.button.use'),
        isAnt: true,
        auth: permissions.useYn,
        props: {
          onClick: () => handleSetUseYn(true),
          type: 'primary',
        },
      },
      {
        name: t('common.button.noUse'),
        isAnt: true,
        auth: permissions.useYn,
        props: {
          onClick: () => handleSetUseYn(false),
          type: 'primary',
        },
      },
      {
        name: t('system.views.user.button.createAccount'),
        isAnt: true,
        auth: permissions.createAccount,
        props: {
          onClick: () => handleCreateAccount(),
          type: 'primary',
        },
      },
      {
        code: 'delete',
        props: {
          onClick: () => {
            const userList = getCheckboxRecords(false)
            // 验证用户
            const result = validateOperateUser(userList)
            if (!result) {
              return false
            }
            // 验证是否包含系统用户
            const sysUserValidate = userList.some((item: any) => item.userType === SYS_USER_TYPE)
            if (sysUserValidate) {
              message.error(t('system.views.user.validate.sysUserNoDelete'))
              return false
            }
            // 执行删除操作
            deleteByCheckbox()
          },
        },
      },
    ],
  },
})
</script>

<style scoped lang="less">
.page-container {
  :deep(.smart-search-container) {
    .ant-col {
      //padding: 0 5px;
    }
  }
}
.dept-container {
  background: white;
  margin-right: 5px;
  padding: 10px;
}
</style>
