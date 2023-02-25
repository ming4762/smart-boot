import { defHttp } from '/@/utils/http/axios'
import TreeUtils from '/@/utils/TreeUtils'
import { merge } from 'lodash-es'

enum Api {
  list = 'sys/function/list',
  getById = 'sys/function/getById',
  save = 'sys/function/saveUpdate',
  delete = 'sys/function/batchDeleteById',
}

export const listTree = async (params) => {
  const parameter = {
    sortName: 'seq',
    ...merge(params, {
      parameter: {
        QUERY_CREATE_UPDATE_USER: true,
      },
    }),
  }
  const result = await defHttp.post({
    url: Api.list,
    data: parameter,
  })
  return (
    TreeUtils.convertList2Tree(
      result,
      (item) => item.functionId,
      (item) => item.parentId,
      0,
    ) || []
  )
}

export const listApi = (params) => {
  const parameter = {
    sortName: 'seq',
    ...params,
  }
  return defHttp.post({
    url: Api.list,
    data: parameter,
  })
}

export const getByIdApi = async (data) => {
  const {
    function: functionData,
    createUser,
    parent,
    updateUser,
  } = await defHttp.post({ url: Api.getById, data: data.functionId })
  return {
    ...functionData,
    createUser: createUser && createUser.fullName,
    updateUser: updateUser && updateUser.fullName,
    parentName: (parent && parent.functionName) || '根目录',
  }
}

export const saveApi = ({ body: { insertRecords, updateRecords } }) => {
  return defHttp.post({ url: Api.save, data: [...insertRecords, ...updateRecords][0] })
}

export const deleteApi = ({ body: { removeRecords } }) => {
  return defHttp.post({ url: Api.delete, data: removeRecords.map((item) => item.functionId) })
}
