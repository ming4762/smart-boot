/**
 * 应用管理表 国际化信息
 */
export default {
  trans: true,
  key: 'monitor.views.application',
  data: {
    title: {
			id: 'id',
			name: '应用名称',
			applicationCode: '应用编码',
			statusInterval: '状态检测间隔时间',
			offlineInterval: '离线检测事件间隔',
			token: 'Token',
			serializeEventCode: '序列化的事件编码列表'
    },
		validate: {
			name: '请输入应用名称',
			applicationCode: '请输入应用编码',
			statusInterval: '请输入状态检测间隔时间',
			offlineInterval: '请输入离线检测事件间隔',
			token: '请输入Token',
			serializeEventCode: '请输入序列化的事件编码列表'
    },
		rules: {
			'name_NOT_EMPTY': '应用名称不能为空',
			'applicationCode_NOT_EMPTY': '应用编码不能为空'
		}
  }
}
