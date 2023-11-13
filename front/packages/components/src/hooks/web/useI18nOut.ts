import { getCurrentInstance } from 'vue';

export function useI18n() {
  const internalInstance = getCurrentInstance();
  return { t: internalInstance.appContext.config.globalProperties.$t };
}
