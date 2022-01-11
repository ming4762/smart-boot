
const tableDeleteYn = (t: Function) => {
  const createSlot = ({ row }: any) => {
    const deleteYn = row.deleteYn
    if (deleteYn === false) {
      return <a-tag color="#108ee9">{t('common.form.noDelete')}</a-tag>
    }
    return <a-tag color="#f50">{t('common.form.delete')}</a-tag>
  }
  /**
   * 创建列信息
   */
  const createColumn = () => {
    return {
      title: t('common.table.deleteYn'),
      field: 'deleteYn',
      width: 100,
      slots: {
        default: createSlot
      }
    }
  }

  return {
    createColumn
  }
}

const tableUseYn = (t: Function) => {
  const createSlot = ({ row }: any) => {
    const useYn = row.useYn
    if (useYn === true) {
      return <a-tag color="#108ee9">{t('common.form.use')}</a-tag>
    }
    return <a-tag color="#f50">{t('common.form.noUse')}</a-tag>
  }
  /**
   * 创建列信息
   */
  const createColumn = () => {
    return {
      title: t('common.table.useYn'),
      field: 'useYn',
      width: 100,
      slots: {
        default: createSlot
      }
    }
  }

  return {
    createColumn
  }
}

export  {
  tableUseYn,
  tableDeleteYn
}
