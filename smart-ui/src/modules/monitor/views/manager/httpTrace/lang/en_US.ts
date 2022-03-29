/**
 * 客户端HttpTrace 国际化信息
 */
export default {
  trans: true,
  key: 'monitor.views.client.httpTrace',
  data: {
		title: {
			applicationCode: 'Application code',
			clientId: 'Client ID',
			httpMethod: 'Http method',
			timeTaken: 'Time taken(ms)',
			url: 'URL',
			responseStatus: 'status',
			timestamp: 'timestamp'
    },
		validate: {
		},
		rules: {
		},
		search: {
			applicationCode: 'Please enter application code',
			clientId: 'Please enter client id',
			httpMethod: 'Please select http method',
			responseStatus: 'Please select status',
			timestamp: 'Please enter'
		}
  }
}
