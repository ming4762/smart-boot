
export default {
  generator: {
    views: {
      code: {
        table: {
          connectionName: '连接名称',
          configName: '配置名称',
          tableName: '表名',
          type: '类型',
          remarks: '表备注'
        },
        title: {
          dbMessage: '数据库信息',
          tableSetting: '表格配置',
          formSetting: '表单配置',
          searchSetting: '查询配置',
          design: '设计',
          showCheckBox: '显示复选框',
          isPage: '是否分页',
          invented: '虚拟滚动',
          columnSort: '列顺序可调',
          leftButton: '左侧按钮',
          rightButton: '右侧按钮',
          rowButtonType: {
            title: '行按钮类型',
            none: '无',
            single: '统一',
            more: '多个',
            text: '文本'
          },
          rowButtonList: '行操作按钮',
          formColNum: '表单列数',
          searchColNum: '搜索列数',
          relateTable: '关联附表',
          tableType: {
            single: '单表',
            main: '主表',
            addendum: '附表'
          },
          colNum: {
            one: '一列',
            two: '两列',
            three: '三列',
            four: '四列'
          },
          controlList: {
            input: '文本框',
            textarea: '文本域',
            number: '数字',
            password: '密码',
            select: '下拉框',
            transfer: '穿梭框',
            selectTable: '下拉表格',
            radio: '单选框',
            checkbox: '多选框',
            switch_type: '开关',
            date: '日期',
            time: '时间',
            datetime: '日期时间',
            file: '文件'
          },
          ruleList: {
            notEmpty: '非空',
            PHONE: '手机号码',
            EMAIL: '邮箱',
            NUMBER: '数字',
            REGEXP: '正则'
          }
        },
        button: {
          createCode: '生成代码',
          syncTableData: '同步表信息'
        },
        validate: {
          connectionName: '请选择数据库连接',
          tableName: '请输入表名',
          configName: '请输入配置名',
          syncTable: '请先同步表信息',
          tableSetting: '请进行表格配置',
          formSetting: '请进行表单配置',
          searchSetting: '请进行搜索配置'
        },
        message: {
          saveConfirmContent: '非空字段：【{0}】未设置form'
        }
      },
      codeCreateForm: {
        title: {
          description: '功能描述',
          tableName: '表名',
          className: '实体类名',
          packages: '包名',
          extPackages: 'ext包名',
          controllerBasePath: 'controller路径'
        },
        message: {
          choseTemplate: '请选择模板'
        },
        validate: {
          packages: '请输入包名',
          className: '请输入实体类名',
          controllerBasePath: '路径不能为空'
        }
      },
      tableField: {
        title: {
          columnName: '字段名称',
          typeName: '字段类型',
          columnSize: '字段长度',
          decimalDigits: '小数位数',
          columnDef: '默认值',
          nullable: '允许空值',
          remarks: '表备注',
          primaryKey: '主键',
          indexed: '索引'
        }
      },
      tableSetting: {
        title: {
          title: '标题',
          sortable: '是否排序',
          fixed: '列冻结',
          width: '宽度',
          align: '对齐方式',
          resizable: '支持拖动',
          visible: '是否渲染',
          hidden: '是否隐藏',
          editable: '是否可编辑',
          format: '格式化'
        }
      },
      formSetting: {
        title: {
          controlType: '控件类型',
          readonly: '是否只读',
          used: '是否传送后台',
          useTableSearch: '查询数据库',
          keyColumnName: 'key字段',
          valueColumnName: 'value字段',
          tableWhere: '查询条件',
          rules: '验证规则'
        }
      },
      searchSetting: {
        title: {
          searchSymbol: '搜索标识'
        }
      },
      addendumTable: {
        title: {
          relatedColumn: '关联字段'
        },
        validate: {
          relatedColumn: '请选择关联字段',
          relatedColumnWithConfig: '请设置关联字段，配置：{0}'
        }
      }
    }
  }
}
