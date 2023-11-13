<template>
  <Modal :open="$attrs.open" title="发布区块" width="550px" append-to-body draggable @update:open="setOpen">
    <Form ref="deployBlockRef" label-position="left" label-width="100px" label-align :model="formState" :rules="formRules">
      <FormItem label="版本号" prop="version">
        <Input v-model="formState.version" placeholder="请输入X.Y.Z格式版本号，如1.0.0"></Input>
      </FormItem>
      <FormItem label="版本描述" prop="deployInfo">
        <Input v-model="formState.deployInfo" type="textarea" placeholder="请输入此次发布的修改点"></Input>
      </FormItem>
      <FormItem label="保存设置" prop="needToSave" class="form-item-save">
        <Checkbox v-model="formState.needToSave">发布成功后保存最新数据</Checkbox>
      </FormItem>
      <FormItem label="schema比对">
        <Button class="compare-button" type="text" @click="changeCompare"> 查看本次发布的改动点 </Button>
        <Popover placement="top" trigger="hover" append-to-body :width="162" content="区块本地schema和线上新版本schema进行比对">
          <icon-help-circle></icon-help-circle>
        </Popover>
      </FormItem>
    </Form>
    <template #footer>
      <Button type="primary" @click="deployBlock"> 确定 </Button>
      <Button @click="setVisible(false)">取消</Button>
    </template>
    <Modal
      v-model:open="state.compareOpen"
      class="dialog-box"
      :modal="false"
      :fullscreen="true"
      :append-to-body="true"
      title="Schema 本地与线上差异"
    >
      <vue-monaco
        v-if="state.compareOpen"
        ref="editor"
        class="monaco-editor"
        :diffEditor="true"
        :options="editorOptions"
        :value="state.code"
        :original="state.originalCode"
        @change="changeCode"
      ></vue-monaco>
      <template #footer>
        <Button type="primary" @click="save">保存</Button>
        <Button @click="close">取消</Button>
      </template>
    </Modal>
  </Modal>
</template>

<script>
import { reactive, ref, watch } from 'vue';
import { iconHelpCircle } from '@opentiny/vue-icon';
import { Checkbox, Input, Button, Modal, Form, Popover, FormItem } from 'ant-design-vue';
import { theme } from '@/controller/adapter';
import { useLayout } from '@/controller';
import { getSchema, setSchema } from '@/canvas';
import VueMonaco from './VueMonaco.vue';

export default {
  components: {
    Checkbox,
    IconHelpCircle: iconHelpCircle(),
    Button,
    Modal,
    Form,
    Input,
    FormItem,
    Popover,
    VueMonaco,
  },
  props: {
    nextVersion: {
      type: String,
      default: '',
    },
  },
  emits: ['update:open'],
  setup(props, { emit, attrs }) {
    const formState = reactive({
      deployInfo: '',
      version: '',
      needToSave: true,
    });

    const state = reactive({
      compareOpen: false,
      schemaCompare: false,
      code: '',
      originalCode: '',
      newCode: '',
    });

    const deployBlockRef = ref(null);

    const formRules = {
      deployInfo: [{ required: true, message: '必填', trigger: 'blur' }],
      version: [
        { required: true, message: '必填', trigger: 'blur' },
        { type: 'version', message: '请输入正确的X.Y.Z版本格式', trigger: 'blur' },
      ],
    };

    const editor = ref(null);
    const editorOptions = {
      theme: theme(),
      tabSize: 2,
      language: 'javascript',
      autoIndent: true,
      lineNumbers: true,
      formatOnPaste: true,
      automaticLayout: true,
      roundedSelection: true,
      minimap: {
        enabled: false,
      },
    };

    watch(
      () => attrs.open,
      open => {
        if (open && !formState.version) {
          formState.version = props.nextVersion;
        }
      },
    );

    const setOpen = open => emit('update:open', open);

    const deployBlock = async () => {
      deployBlockRef.value.validate(valid => {
        const { PLUGIN_NAME, getPluginApi } = useLayout();
        const { getEditBlock, publishBlock } = getPluginApi(PLUGIN_NAME.BlockManage);
        if (valid) {
          const params = {
            block: getEditBlock(),
            is_compile: true,
            deploy_info: formState.deployInfo,
            version: formState.version,
            needToSave: formState.needToSave,
          };
          publishBlock(params);
          setOpen(false);
          formState.deployInfo = '';
          formState.version = '';
          formState.needToSave = true;
        }
      });
    };

    const changeCompare = async value => {
      const { PLUGIN_NAME, getPluginApi } = useLayout();
      const { getEditBlock } = getPluginApi(PLUGIN_NAME.BlockManage);
      if (value) {
        const api = getPluginApi(PLUGIN_NAME.BlockManage);
        const block = getEditBlock();
        const remote = await api.getBlockById(block?.id);
        const originalObj = remote?.content || {};
        state.originalCode = JSON.stringify(originalObj, null, 2);

        // 转为普通对象，和线上代码顺序保持一致
        const pageSchema = getSchema() || {};
        if (pageSchema.componentName === 'Block') {
          state.code = JSON.stringify(pageSchema, null, 2);
        } else {
          // 非区块编辑
          state.code = state.originalCode;
        }
        state.compareOpen = true;
      }
    };

    const changeCode = code => {
      if (typeof code !== 'string') {
        return;
      }
      state.newCode = code;
    };

    const close = () => {
      state.schemaCompare = false;
      state.compareOpen = false;
    };

    const save = () => {
      const pageSchema = JSON.parse(state.newCode);
      setSchema(pageSchema);

      close();
    };

    return {
      formState,
      state,
      deployBlockRef,
      formRules,
      editor,
      editorOptions,
      setOpen,
      changeCompare,
      changeCode,
      close,
      save,
      deployBlock,
    };
  },
};
</script>

<style lang="less" scoped>
.dialog-box {
  :deep(.tiny-dialog-box__body) {
    height: 100%;
  }
}
.monaco-editor {
  width: 100%;
  height: 100%;
}
.form-item-save {
  :deep(.tiny-form-item__content) {
    line-height: 0;
  }
}
.compare-button {
  padding-left: 0;
  padding-right: 8px;
  line-height: 28px;
  vertical-align: middle;
}
</style>
