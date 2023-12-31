<template>
  <slot>
    <div class="data-source-wrap">
      <Button class="data-source-button" :reset-time="0" @click="click">{{ title }}</Button>
    </div>
  </slot>
  <Modal append-to-body title="编写代码" :open="showDialog" width="40%" @update:open="showDialog = $event" @opened="open">
    <Collapse v-if="example" v-model="activeNames">
      <CollapsePanel title="查看示例" name="1">
        <pre>{{ example }}</pre>
      </CollapsePanel>
    </Collapse>
    <monaco-editor
      v-if="showEditor"
      ref="editor"
      class="data-source-monaco"
      :value="JSON.stringify(editorValue, null, 2)"
      :options="options"
    />
    <template #footer>
      <div class="bind-dialog-footer">
        <Button @click="cancel">取 消</Button>
        <Button type="info" @click="confirm">确 定</Button>
      </div>
    </template>
  </Modal>
</template>

<script>
import { reactive, getCurrentInstance, h, ref, computed } from 'vue';
import { VueMonaco } from '@/common';
import { Button, Collapse, CollapsePanel, Modal } from 'ant-design-vue';
import { getExample, useCanvas, useProperties } from '@/controller';
import { theme } from '@/controller/adapter';

export default {
  components: {
    Button,
    MonacoEditor: VueMonaco,
    Modal,
    Collapse,
    CollapsePanel,
  },
  inheritAttrs: false,
  props: {
    name: {
      type: String,
      default: '',
    },
    modelValue: {
      type: [String, Array],
      default: '',
    },
  },
  setup(props, { emit }) {
    const instance = getCurrentInstance();
    const example = getExample(props.name);
    const { pageState } = useCanvas();
    const { setProp } = useProperties();

    const options = reactive({
      roundedSelection: true,
      automaticLayout: true,
      autoIndent: true,
      theme: theme(),
      language: 'json',
      formatOnPaste: true,
      minimap: { enabled: false },
    });

    const pageSchema = reactive(pageState.pageSchema);
    const editorValue = computed(() => props.modelValue);
    const showEditor = ref(false);
    const showDialog = ref(false);
    const title = ref(`编辑${props.name}`);

    // 获取初始值
    if (props.name === 'imports') {
      editorValue.value = pageSchema.bridge?.imports;
    } else if (props.name === 'inputs' || props.name === 'outputs') {
      editorValue.value = pageSchema[props.name];
    }

    const click = () => {
      showDialog.value = true;
    };

    const cancel = () => {
      showDialog.value = false;
    };

    const confirm = () => {
      const name = props.name;
      const editorValue = instance.refs.editor.getEditor().getValue();
      const value = editorValue ? JSON.parse(editorValue) : editorValue;

      if (name === 'imports') {
        pageSchema.bridge = {};
        pageSchema.bridge.imports = value;
      } else if (name === 'inputs' || name === 'outputs') {
        pageSchema[name] = value;
      } else {
        setProp(props.name, value);
        emit('codeEditConfirm', { name: props.name, value: value });
      }

      showDialog.value = false;
    };

    const open = () => {
      showDialog.value = true;
      showEditor.value = true;
    };

    return {
      example,
      options,
      showEditor,
      showDialog,
      title,
      editorValue,
      open,
      click,
      cancel,
      confirm,
    };
  },
};
</script>

<style lang="less" scoped>
.data-source-wrap {
  display: flex;
  align-items: center;

  .data-source-button {
    width: 100%;
    max-width: inherit;
    display: inline-block;
    padding: 0 8px;
  }

  .bind-dialog-footer {
    display: flex;
    justify-content: center;
    align-items: center;

    :deep(.tiny-button--default) {
      background: var(--ti-lowcode-toolbar-bg);
    }
  }
}

.data-source-monaco {
  width: 100%;
  height: 320px;
}
</style>
