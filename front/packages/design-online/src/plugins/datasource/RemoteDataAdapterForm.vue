<template>
  <div v-if="name" class="data-handle">
    <div>{{ name }}<slot name="title"></slot></div>
  </div>
  <monaco-editor ref="editor" :value="modelValue" class="monaco-editor" :options="options" @change="change" />
</template>

<script>
import { ref } from 'vue';
import { VueMonaco } from '@/common';
import { theme } from '@/controller/adapter';

export default {
  components: {
    MonacoEditor: VueMonaco,
  },
  props: {
    name: {
      type: String,
      default: '',
    },
    modelValue: {
      type: Object,
      default: () => ({}),
    },
  },
  emits: ['update:modelValue'],
  setup(props, { emit }) {
    const change = value => {
      if (typeof value !== 'string') {
        return;
      }

      emit('update:modelValue', value);
    };
    const editor = ref(null);
    const getEditorValue = () => editor.value?.getEditor()?.getValue();

    return {
      editor,
      options: {
        theme: theme(),
        roundedSelection: true,
        automaticLayout: true,
        autoIndent: true,
        formatOnPaste: true,
        language: 'javascript',
        mouseStyle: 'default',
        minimap: { enabled: false },
      },
      getEditorValue,
      change,
    };
  },
};
</script>

<style lang="less" scoped>
.data-handle {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background: var(--ti-lowcode-datasource-toolbar-bg);
  margin-top: 12px;
  font-size: 14px;

  div {
    color: var(--ti-lowcode-datasource-toolbar-breadcrumb-color);
  }

  .icon-del {
    cursor: pointer;
    color: var(--ti-lowcode-datasource-toolbar-icon-color);
  }
}

.monaco-editor {
  min-height: 120px;
  height: 100%;
  margin-top: 8px;
}
</style>
