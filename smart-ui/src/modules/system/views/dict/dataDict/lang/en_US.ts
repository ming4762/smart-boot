export const dictGroupI18n = {
  trans: true,
  key: 'system.views.dictGroup',
  data: {
    title: {
      dictCode: 'Dict code',
      dictName: 'Dict name'
    },
    validate: {
      dictCode: 'Please enter dict code',
      dictName: 'Please enter dict name'
    }
  }
}

/**
 * 字典序表 国际化信息
 */
export const dictItemI18n = {
  trans: true,
  key: 'system.views.dictItem',
  data: {
    title: {
      dictCode: 'Dict code',
      dictItemCode: 'Dict item code',
      dictItemName: 'Dict item name'
    },
    validate: {
      dictCode: 'Please enter dict code',
      dictItemCode: 'Please enter dict item code',
      dictItemName: 'Please enter dict item name'
    },
    rules: {
      'dictItemCode_NOT_EMPTY': 'Dict item code cannot be empty',
      'dictItemName_NOT_EMPTY': 'Dict item name cannot be empty'
    },
    message: {
      dictCodeNull: 'Please select a dict first'
    }
  }
}

