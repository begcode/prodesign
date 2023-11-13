<template>
  <div v-if="isOpen">
    <plugin-setting title="全局设置" @cancel="close" @save="saveGlobalDataHandle">
      <template #content>
        <Collapse v-model:activeKey="activeNames" :bordered="false">
          <CollapsePanel header="请求参数处理函数（willFetch）" key="willFetch">
            <data-handler-editor v-model="state.willFetchValue"></data-handler-editor>
          </CollapsePanel>
          <CollapsePanel header="请求完成回调函数（dataHandler）" key="dataHandler">
            <data-handler-editor v-model="state.dataHandlerValue"></data-handler-editor>
          </CollapsePanel>
          <CollapsePanel header="请求失败后的回调函数（errorHandler）" key="errorHandler">
            <data-handler-editor v-model="state.errorHandlerValue"></data-handler-editor>
          </CollapsePanel>
        </Collapse>
      </template>
    </plugin-setting>
  </div>
</template>

<script>
import DataHandlerEditor from './RemoteDataAdapterForm.vue';
import { watch, ref, nextTick, reactive } from 'vue';
import { requestGlobalDataHandler } from './js/http';
import { useApp, useModal, useResource } from '@/controller';
import { PluginSetting } from '@/common';
import { Collapse, CollapsePanel } from 'ant-design-vue';
import { constants } from '@/utils';

const { DEFAULT_INTERCEPTOR } = constants;
const isOpen = ref(false);

export const open = () => {
  isOpen.value = true;
};

export const close = () => {
  isOpen.value = false;
};

export default {
  components: {
    DataHandlerEditor,
    PluginSetting,
    Collapse,
    CollapsePanel,
  },
  setup() {
    const { confirm } = useModal();

    const state = reactive({
      dataHandlerValue: useResource().resState?.dataHandler?.value,
      willFetchValue: useResource().resState.willFetch?.value,
      errorHandlerValue: useResource().resState?.errorHandler?.value,
    });

    const saveGlobalDataHandle = () => {
      const id = useApp().appInfoState.selectedId;

      const handler = {
        dataHandler: { type: 'JSFunction', value: state.dataHandlerValue || DEFAULT_INTERCEPTOR.dataHandler.value },
        willFetch: { type: 'JSFunction', value: state.willFetchValue || DEFAULT_INTERCEPTOR.willFetch.value },
        errorHandler: { type: 'JSFunction', value: state.errorHandlerValue || DEFAULT_INTERCEPTOR.errorHandler.value },
      };

      requestGlobalDataHandler(id, { data_source_global: handler }).then(data => {
        if (data) {
          useResource().resState.dataHandler = { type: 'JSFunction', value: state.dataHandlerValue };
          useResource().resState.willFetch = { type: 'JSFunction', value: state.willFetchValue };
          useResource().resState.errorHandler = { type: 'JSFunction', value: state.errorHandlerValue };
          confirm({
            title: '提示',
            message: '全局请求处理函数设置成功',
          });
        }
      });
    };

    watch(
      () => isOpen.value,
      value => {
        nextTick(() => {
          value && window.dispatchEvent(new Event('resize'));
        });
      },
    );

    const activeNames = ref('willFetch');

    return {
      activeNames,
      isOpen,
      close,
      saveGlobalDataHandle,
      state,
    };
  },
};
</script>
<style lang="less" scoped>
.plugin-setting :deep(.monaco-editor) {
  height: calc(100% - 54px);
}
.tiny-collapse {
  height: 100%;
  display: flex;
  flex-direction: column;
}
.is-active {
  flex-grow: 2;
}
.tiny-collapse-item {
  margin-bottom: 3px;
  :deep(.tiny-collapse-item__wrap) {
    height: 100%;
    .tiny-collapse-item__content {
      height: 100%;
    }
  }
}
</style>
