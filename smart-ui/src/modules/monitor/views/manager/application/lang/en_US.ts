/**
 * 应用管理表 国际化信息
 */
export default {
  trans: true,
  key: 'monitor.views.application',
  data: {
    title: {
			id: 'id',
			name: 'Application name',
			applicationCode: 'Application code',
			statusInterval: 'Status interval',
			offlineInterval: 'offline interval',
			token: 'Token',
			serializeEventCode: 'Serialize event code'
    },
		validate: {
			name: 'Please enter application name',
			applicationCode: 'Please enter application code',
			statusInterval: 'Please enter status interval',
			offlineInterval: 'Please enter offline interval',
			token: 'Please enter token',
			serializeEventCode: 'Please enter serialize event code'
    },
		rules: {
			'name_NOT_EMPTY': 'Application name cannot be empty',
			'applicationCode_NOT_EMPTY': 'Application code cannot be empty'
		}
  }
}
