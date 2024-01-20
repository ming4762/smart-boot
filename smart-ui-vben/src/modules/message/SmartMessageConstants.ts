export const getMessageTypeEnum = (t: Function) => {
  return [
    {
      label: t('smart.message.systemMessage.form.messageType.announcement'),
      value: 'ANNOUNCEMENT',
    },
    {
      label: t('smart.message.systemMessage.form.messageType.systemMessage'),
      value: 'SYSTEM_MESSAGE',
    },
  ]
}

export const getMessagePriorityEnum = (t: Function) => {
  return [
    {
      label: t('smart.message.systemMessage.form.messagePriority.LOW'),
      value: 'LOW',
      color: 'green',
    },
    {
      label: t('smart.message.systemMessage.form.messagePriority.MIDDLE'),
      value: 'MIDDLE',
      color: 'orange',
    },
    {
      label: t('smart.message.systemMessage.form.messagePriority.HIGH'),
      value: 'HIGH',
      color: 'pink',
    },
  ]
}
