/**
 * 系统异常信息 国际化信息
 */
export default {
  trans: true,
  key: 'system.views.exception',
  data: {
    title: {
      exceptionMessage: '异常信息',
      requestIp: '请求IP',
      serverIp: '服务器IP',
      requestPath: '请求路径',
      operateUserId: '操作人员ID',
      userFeedback: '用户是否反馈',
      feedbackMessage: '用户反馈信息',
      feedbackTime: '反馈时间',
      resolved: '是否已处理',
      resolvedMessage: '处理信息',
      resolvedUserId: '处理人员ID',
      resolvedTime: '处理时间',
      showStackTrace: '查看堆栈信息',
    },
    validate: {},
    rules: {},
    search: {
      exceptionMessage: '请输入异常信息',
      requestIp: '请输入请求IP',
      serverIp: '请输入服务器IP',
    },
  },
}
