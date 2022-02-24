/**
 * 客户端慢SQL 国际化信息
 */
export default {
	trans: true,
	key: 'monitor.views.client.slowSql',
	data: {
		title: {
			applicationCode: 'Application code',
			clientId: 'Client ID',
			datasourceName: 'Datasource',
			sqlText: 'SQL',
			parameter: 'Parameter',
			useMillis: 'Use millis(ms)',
			timestamp: 'Timestamp'
		},
		validate: {
			applicationCode: 'Please enter application code',
			clientId: 'Please enter client id',
			datasourceName: 'Please enter datasource'
		},
		rules: {
		}
	}
}
