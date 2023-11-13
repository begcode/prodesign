<template>
  <Popover trigger="hover" :open-delay="1000" popper-class="toolbar-right-popover" append-to-body :content="isLoading ? '保存中' : '保存'">
    <span :class="[isLoading ? 'saving' : 'icon']" @click="openApi">
      <span v-show="!isSaved()" class="dots"></span>
      <Icon :icon="icon"></Icon>
    </span>
  </Popover>
  <Modal
    class="dialog-box"
    :modal="false"
    :fullscreen="true"
    :append-to-body="true"
    :open="state.visible"
    title="Schema 本地与线上差异"
    @update:open="state.visible = $event"
  >
    <vue-monaco
      v-if="state.visible"
      ref="editor"
      class="monaco-editor"
      :diffEditor="true"
      :options="editorOptions"
      :value="state.code"
      :original="state.originalCode"
    ></vue-monaco>
    <template #footer>
      <Button @click="close">取 消</Button>
      <Button type="primary" @click="saveApi">保 存</Button>
    </template>
  </Modal>
</template>

<script>
import { reactive, ref, onBeforeMount } from 'vue';
import { VueMonaco } from '@/common';
import { Button, Popover, Modal } from 'ant-design-vue';
import { useCanvas } from '@/controller';
import { theme } from '@/controller/adapter';
import { openCommon, saveCommon } from './js/index';
import { isLoading } from './js/index';
import { Icon } from '@begcode/components';
export const api = {
  saveCommon,
  openCommon,
};
export default {
  components: {
    Icon,
    VueMonaco,
    Button,
    Popover,
    Modal,
  },
  props: {
    icon: {
      type: String,
      default: 'ant-design:save-outlined',
    },
  },
  setup() {
    // 获取当前页面的全量信息
    const { isSaved } = useCanvas();

    const state = reactive({
      visible: false,
      code: '',
      originalCode: '',
      disabled: false,
      timer: null,
    });

    const editor = ref(null);

    const close = () => {
      state.visible = false;
      state.originalCode = '';
    };
    const openApi = () => {
      if (!isLoading.value) {
        openCommon();
      }
    };
    const saveApi = () => {
      saveCommon();
    };
    // 保存或新建区块
    const editorOptions = {
      theme: theme(),
      tabSize: 2,
      language: 'json',
      autoIndent: true,
      lineNumbers: true,
      formatOnPaste: true,
      automaticLayout: true,
      roundedSelection: true,
      minimap: {
        enabled: false,
      },
    };

    onBeforeMount(() => {
      clearTimeout(state.timer);
    });

    return {
      state,
      editor,
      editorOptions,
      isLoading,
      close,
      isSaved,
      openApi,
      saveApi,
    };
  },
};
</script>

<style lang="less" scoped>
.dots {
  width: 6px;
  height: 6px;
  background: var(--ti-lowcode-toolbar-dot-color);
  border-radius: 50%;
  display: inline-block;
  position: absolute;
  top: 4px;
  right: 3px;
  z-index: 100;
}
.saving {
  width: 32px;
  height: 32px;
  display: inline-block;
  cursor: not-allowed;
  color: var(--ti-lowcode-disabled-color);
  font-size: 20px;
  margin-bottom: -2px;
}

.dialog-box {
  :deep(.tiny-dialog-box) {
    display: flex;
    flex-direction: column;

    .tiny-dialog-box__body {
      flex: 1;
    }
  }

  .monaco-editor {
    width: 100%;
    height: 100%;
  }
}
</style>

<style>
.changeRole a {
  color: var(--ti-lowcode-canvas-handle-hover-bg);
  padding: 0 5px;
}
</style>
