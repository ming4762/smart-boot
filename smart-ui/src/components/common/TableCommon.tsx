/**
 * Boolean列配置
 * @param t
 * @param title
 * @param field
 */
export const tableBooleanColumn = (t: Function, title: string, field: string) => {
  const createSlot = ({ row }: any) => {
    const value = row[field]
    if (value === true) {
      return <a-tag color="#108ee9">{t('common.form.yes')}</a-tag>
    }
    return <a-tag color="#f50">{t('common.form.no')}</a-tag>
  }
  /**
   * 创建列信息
   */
  const createColumn = () => {
    return {
      title: title,
      field: field,
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

const tableDeleteYn = (t: Function) => {
  const createSlot = ({ row }: any) => {
    const value = row.deleteYn
    if (value === false) {
      return <a-tag color="#108ee9">{t('common.form.no')}</a-tag>
    }
    return <a-tag color="#f50">{t('common.form.yes')}</a-tag>
  }
  /**
   * 创建列信息
   */
  const createColumn = () => {
    return {
      title: '{common.table.deleteYn}',
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
      title: '{common.table.useYn}',
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
