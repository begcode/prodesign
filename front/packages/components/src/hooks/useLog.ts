import { getCurrentInstance } from 'vue';

export function useLog() {
  const internalInstance = getCurrentInstance();
  return internalInstance.appContext.config.globalProperties.$log;
}
