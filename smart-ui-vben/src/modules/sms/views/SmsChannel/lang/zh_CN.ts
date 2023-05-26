/**
 * 短信通道表 国际化信息
 */
export default {
  trans: true,
  key: 'smart.sms.channel',
  data: {
    title: {
      channelCode: '通道编号',
      channelName: '通道名称',
      channelType: '通道类型',
      isDefault: '是否是默认的',
      channelProperties: '通道参数',
    },
    validate: {
      channelCode: '请输入通道编号',
      channelName: '请输入通道名称',
      channelType: '请输入通道类型',
    },
    rules: {},
    search: {
      channelCode: '请输入通道编号',
      channelName: '请输入通道名称',
      channelType: '请输入通道类型',
    },
    button: {
      setDefault: '设为默认',
    },
    message: {
      setDefault: '只能有一个默认的通道，确定要设为默认吗？',
      setDefaultSuccess: '设置默认成功',
    },
  },
}
