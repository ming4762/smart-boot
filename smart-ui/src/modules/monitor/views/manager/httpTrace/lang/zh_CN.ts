/**
 * 客户端HttpTrace 国际化信息
 */
export default {
  trans: true,
  key: 'monitor.views.client.httpTrace',
  data: {
		title: {
			applicationCode: '客户端编码',
			clientId: '客户端ID',
			httpMethod: '请求方式',
			timeTaken: '用时(毫秒)',
			url: '请求地址',
			responseStatus: '状态',
			timestamp: '时间戳'
    },
		validate: {
		},
		rules: {
		},
		search: {
			applicationCode: '请输入客户端编码',
			clientId: '请输入客户端ID',
			httpMethod: '请输入请求方式',
			responseStatus: '请输入状态',
			timestamp: '请输入时间戳'
		}
  }
}
