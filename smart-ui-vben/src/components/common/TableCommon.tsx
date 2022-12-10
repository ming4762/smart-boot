import { Tag } from 'ant-design-vue'

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
      return <Tag color="#108ee9">{t('common.form.yes')}</Tag>
    }
    return <Tag color="#f50">{t('common.form.no')}</Tag>
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
        default: createSlot,
      },
    }
  }
  return {
    createColumn,
  }
}

const tableDeleteYn = (t: Function) => {
  const createSlot = ({ row }: any) => {
    const value = row.deleteYn
    if (value === false) {
      return <Tag color="#108ee9">{t('common.form.no')}</Tag>
    }
    return <Tag color="#f50">{t('common.form.yes')}</Tag>
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
        default: createSlot,
      },
    }
  }
  return {
    createColumn,
  }
}

const tableUseYn = (t: Function) => {
  const createSlot = ({ row }: any) => {
    const useYn = row.useYn
    if (useYn === true) {
      return <Tag color="#108ee9">{t('common.form.use')}</Tag>
    }
    return <Tag color="#f50">{t('common.form.noUse')}</Tag>
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
        default: createSlot,
      },
    }
  }

  return {
    createColumn,
  }
}

export { tableUseYn, tableDeleteYn }
