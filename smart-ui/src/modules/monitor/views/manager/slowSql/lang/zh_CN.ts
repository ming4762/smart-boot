/**
 * 客户端慢SQL 国际化信息
 */
export default {
	trans: true,
	key: 'monitor.views.client.slowSql',
	data: {
		title: {
			applicationCode: '应用编码',
			clientId: '客户端ID',
			datasourceName: '数据源',
			sqlId: 'SQL ID',
			sqlText: 'SQL',
			parameter: '参数',
			useMillis: '执行用时(ms)',
			timestamp: '时间戳'
		},
		validate: {
			applicationCode: '请输入客户端编码',
			clientId: '请输入客户端ID',
			datasourceName: '请输入数据源'
		},
		rules: {
		}
	}
}
