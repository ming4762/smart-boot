import type { VxeGridPropTypes } from 'vxe-table'

import defHttp, { ApiServiceEnum } from '@/config/axios'
import TreeUtils from '@/utils/TreeUtils'

enum Api {
  list = 'sys/user/list',
  delete = 'sys/user/batchDeleteById',
  saveUpdateWithDataScope = 'sys/user/saveUpdateWithDataScope',
  getByIdWithDataScope = 'sys/user/getByIdWithDataScope',
  getById = 'sys/user/getById',
  setUseYn = 'sys/user/setUseYn',
  createAccount = 'sys/user/createAccount',
  saveAccountSetting = 'sys/user/saveAccountSetting',
  deptTreeList = 'sys/dept/list',
}

export const listApi = (ajaxParameter) => {
  return defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.list,
    data: ajaxParameter,
  })
}

export const deleteApi = (params: VxeGridPropTypes.ProxyAjaxDeleteParams) => {
  return defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.delete,
    data: params.body.removeRecords.map((item) => item.userId),
  })
}

export const saveUpdateWithDataScopeApi = async ({
  body,
}: VxeGridPropTypes.ProxyAjaxSaveParams) => {
  const saveList = [...body.insertRecords, ...body.updateRecords]
  if (saveList.length === 0) {
    return false
  }
  const model = saveList[0]
  if (model.userType === 'SYSTEM_USER') {
    model.deptId = null
  }
  return await defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.saveUpdateWithDataScope,
    data: saveList[0],
  })
}

export const getByIdWithDataScopeApi = async (params) => {
  const result = await defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.getByIdWithDataScope,
    data: params.userId,
  })
  return {
    ...result,
    dataScopeList: result.dataScopeList || [],
  }
}

export const setUseYnApi = (userList: any[], useYn: boolean) => {
  return defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.setUseYn,
    data: {
      idList: userList.map((item) => item.userId),
      useYn,
    },
  })
}

export const createAccountApi = (userList: any[]) => {
  return defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.createAccount,
    data: userList.map((item) => item.userId),
  })
}

export const saveAccountSettingApi = (data) => {
  return defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.saveAccountSetting,
    data: data,
  })
}

export const getByIdApi = (id: string | null) => {
  return defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.getById,
    data: id,
  })
}

export const getDeptTreeListApi = async () => {
  const data = await defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.deptTreeList,
    data: {
      sortName: 'seq',
      propertyList: ['deptId', 'deptName', 'parentId'],
    },
  })
  return (
    TreeUtils.convertList2Tree(
      data,
      (item) => item.deptId,
      (item) => item.parentId,
      0,
    ) || []
  )
}
