
export default {
  generator: {
    views: {
      template: {
        label: {
          templateType: {
            templateCode: 'Code template',
            templateDbDict: 'DB dict template'
          }
        },
        title: {
          userGroup: 'User group'
        },
        table: {
          name: 'Name',
          templateType: 'Template Type',
          language: 'Language',
          remark: 'Remark',
          filenameSuffix: 'Filename Suffix'
        },
        notice: {
          onlyDeleteMy: 'Only self created templates can be deleted'
        },
        validate: {
          templateType: 'Please enter template type',
          name: 'Please enter name',
          remark: 'Please enter remark'
        }
      }
    }
  }
}
