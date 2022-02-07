
export default {
  generator: {
    views: {
      code: {
        table: {
          connectionName: 'Connection name',
          configName: 'Config name',
          tableName: 'Table name',
          type: 'Type',
          remarks: 'Table remark'
        },
        title: {
          dbMessage: 'DB information',
          tableSetting: 'Table setting',
          formSetting: 'Form setting',
          searchSetting: 'Search setting',
          design: 'Design',
          showCheckBox: 'Show check box',
          isPage: 'Pagination',
          invented: 'Virtual scrolling',
          columnSort: 'Order adjustable',
          leftButton: 'Left button',
          rightButton: 'Right button',
          rowButtonType: {
            title: 'Row button type',
            none: 'none',
            single: 'unified',
            more: 'more',
            text: 'text'
          },
          rowButtonList: 'Row button',
          formColNum: 'Form col num',
          searchColNum: 'Search col num',
          relateTable: 'Relate addendum',
          tableType: {
            single: 'single',
            main: 'main',
            addendum: 'addendum'
          },
          colNum: {
            one: 'One column',
            two: 'Two column',
            three: 'Three column',
            four: 'Four column'
          },
          controlList: {
            input: 'INPUT',
            textarea: 'TEXTAREA',
            number: 'NUMBER',
            password: 'PASSWORD',
            select: 'SELECT',
            transfer: 'TRANSFER',
            selectTable: 'SELECT_TABLE',
            radio: 'RADIO',
            checkbox: 'CHECKBOX',
            switch_type: 'SWITCH',
            date: 'DATE',
            time: 'TIME',
            datetime: 'DATETIME',
            file: 'FILE'
          },
          ruleList: {
            notEmpty: 'NOT_EMPTY',
            PHONE: 'PHONE',
            EMAIL: 'EMAIL',
            NUMBER: 'NUMBER',
            REGEXP: 'REGEXP'
          },
          i18nPrefix: 'I18n prefix'
        },
        button: {
          createCode: 'Generate code',
          syncTableData: 'Sync table information'
        },
        validate: {
          connectionName: 'Please select a database connection',
          tableName: 'Please enter a table name',
          configName: 'Please enter the configuration name',
          syncTable: 'Please synchronize table information first',
          tableSetting: 'Please configure the table',
          formSetting: 'Please configure the form',
          searchSetting: 'Please configure the search',
          i18nPrefix: 'Please enter the i18n prefix'
        },
        message: {
          saveConfirmContent: 'Non empty field: [{0}] has no form set'
        }
      },
      codeCreateForm: {
        title: {
          description: 'Function description',
          tableName: 'Table name',
          className: 'Model class name',
          packages: 'packages',
          extPackages: 'ext packages',
          controllerBasePath: 'controller path',
          choseAddendum: 'Select addendum'
        },
        message: {
          choseTemplate: 'Please select a template'
        },
        validate: {
          packages: 'Please enter package',
          className: 'Please enter model class name',
          controllerBasePath: 'Path cannot be empty'
        }
      },
      tableField: {
        title: {
          columnName: 'Column name',
          typeName: 'Column type',
          columnSize: 'Column size',
          decimalDigits: 'Decimal digits',
          columnDef: 'Default value',
          nullable: 'Nullable',
          remarks: 'Table remark',
          primaryKey: 'Primary key',
          indexed: 'Indexed'
        }
      },
      tableSetting: {
        title: {
          title: 'Title',
          sortable: 'Sortable',
          fixed: 'Fixed',
          width: 'Width',
          align: 'Align',
          resizable: 'Resizable',
          visible: 'Render',
          hidden: 'Hidden',
          editable: 'Editable',
          format: 'Format'
        }
      },
      formSetting: {
        title: {
          controlType: 'Control type',
          readonly: 'Readonly',
          used: 'Transfer background',
          useTableSearch: 'Query DB',
          keyColumnName: 'Key column',
          valueColumnName: 'Value column',
          tableWhere: 'Query criteria',
          rules: 'Rules'
        }
      },
      searchSetting: {
        title: {
          searchSymbol: 'Search identity'
        }
      },
      addendumTable: {
        title: {
          relatedColumn: 'Associated field'
        },
        validate: {
          relatedColumn: 'Please select an associated field',
          relatedColumnWithConfig: 'Please set the associated field, configuration: {0}'
        }
      }
    }
  }
}
