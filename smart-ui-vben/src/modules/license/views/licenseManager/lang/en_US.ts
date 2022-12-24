/**
 * 许可证管理 国际化信息
 */
export default {
  trans: true,
  key: 'smart.license',
  data: {
    title: {
      licenseCode: 'License code',
      projectId: 'Project',
      version: 'License version',
      macAddress: 'Mac address',
      ipAddress: 'IP address',
      cpuSerial: 'CPU serial',
      mainBoardSerial: 'Main board serial',
      effectiveTime: 'Effective',
      expirationTime: 'Expiration',
      status: 'status',
      times: 'Validity',
    },
    validate: {
      licenseCode: 'Please enter license code',
      version: 'Please enter license version',
      macAddress: 'Please enter mac address',
      ipAddress: 'Please enter IP address',
      cpuSerial: 'Please enter CPU serial',
      mainBoardSerial: 'Please enter main board serial',
      effectiveTime: 'Please enter effective',
      expirationTime: 'Please enter expiration',
      status: 'Please enter status',
      times: 'Please enter validity',
    },
    rules: {},
    search: {
      licenseCode: 'Please enter license code',
      version: 'Please enter license version',
    },
    button: {
      generator: 'Generator',
    },
    message: {
      generatorConfirm: 'Are you sure you want to generate a license?',
      generatorSuccess: 'Generator success',
      reGeneratorConfirm: 'The license has been generated. Are you sure you want to regenerate it?',
    },
  },
}
