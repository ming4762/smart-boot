import { defHttp } from '/@/utils/http/axios'

enum Api {
  list = 'smart/file/list',
  getById = 'smart/file/getById',
  delete = 'smart/file/batchDeleteFile',
  uploadFile = 'smart/file/upload',
  download = '/public/file/download/',
}

export const listApi = (params) => {
  return defHttp.post({
    url: Api.list,
    data: {
      ...params,
    },
  })
}

export const uploadFileApi = (data, file: File) => {
  return defHttp.uploadFile(
    {
      url: Api.uploadFile,
    },
    {
      data: data,
      file: {
        file: file,
      },
    },
  )
}

export const deleteApi = (removeRecords: Recordable[]) => {
  return defHttp.post({
    url: Api.delete,
    data: removeRecords.map((item) => item.fileId),
  })
}

export const getByIdApi = (model: Recordable) => {
  return defHttp.post({
    url: Api.getById,
    data: model.fileId,
  })
}

export const downloadApi = async (id) => {
  let url = `${defHttp.getApiUrl()}${Api.download}${id}`
  // 申请临时token
  const tempToken = await defHttp.applyTempToken('smart:file:download')
  url = url + '?access-token=' + tempToken
  window.open(url)
}
