import { notification } from 'ant-design-vue'

const durationMap = {
  info: 4.5,
  success: 4.5,
  warning: 10,
  error: 10
}

const useNotify = (config) => {
  const { customClass, title, type = 'info', position = 'topRight', message =  '', ...otherConfig } = config

  switch (type) {
    case 'info':
      notification.open({
        duration: durationMap[type],
        ...otherConfig,
        placement: position,
        message: title,
        description: message,
        type,
        customClass: `${customClass}`,
        verticalOffset: 46,
      });
      break;
    case 'success':
      notification.success({
        duration: durationMap[type],
        ...otherConfig,
        placement: position,
        message: title,
        customClass: `${customClass}`,
        verticalOffset: 46,
      });
      break;
    case 'warning':
      notification.warning({
        duration: durationMap[type],
        ...otherConfig,
        placement: position,
        message: title,
        customClass: `${customClass}`,
        verticalOffset: 46,
      });
      break;
    case 'error':
      notification.error({
        duration: durationMap[type],
        ...otherConfig,
        placement: position,
        message: title,
        customClass: `${customClass}`,
        verticalOffset: 46,
      });
      break;
    default:
      notification.open({
        duration: durationMap[type],
        ...otherConfig,
        placement: position,
        message: title,
        customClass: `${customClass}`,
        verticalOffset: 46,
      });
  }
}

export default useNotify
