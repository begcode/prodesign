<template>
  <tiny-form
    ref="createData"
    layout="horizontal"
    class="create-form"
    :model="state.createData"
    :rules="rules"
    validate-type="text"
    :inline-message="true"
  >
    <tiny-form-item label="变量名" prop="name">
      <tiny-input v-model="state.createData.name" placeholder="只能包含数字字母及下划线" @change="$emit('nameInput', $event)"></tiny-input>
    </tiny-form-item>
    <tiny-form-item label="初始值类型" class="var-type-item">
      <RadioGroup
        :options="VAR_TYPES"
        size="small"
        option-type="button"
        v-model:value="state.variableType"
        @change="state.variableType = $event"
      ></RadioGroup>
    </tiny-form-item>
    <tiny-form-item class="monaco-form-item init-value-item">
      <div class="create-content">
        <monaco-editor
          ref="variableEditor"
          class="create-content-editor"
          :value="editorCode"
          :showFormatBtn="true"
          :options="state.editorOptions"
          @editorDidMount="editorDidMount"
        >
          <template #toolbar>
            <div style="display: flex">
              <div>初始值</div>
              <tiny-popover
                title="数据写法和JS写法一致"
                placement="bottomLeft"
                effect="dark"
                trigger="hover"
                popper-class="state-data-example-tips"
              >
                <div class="create-content-description">查看示例</div>
                <template #content>
                  <div class="tips-content">
                    <div class="create-content-demo">
                      <ul>
                        <li>字符串: "string"</li>
                        <li>数字: 123</li>
                        <li>布尔值: true/false</li>
                        <li>对象: {"name": "xxx"}</li>
                        <li>数组: ["1", "2"]</li>
                        <li>空值: null</li>
                        <li>JS表达式: (需要先选择JS表达式类型)</li>
                        <li class="ml20">示例1： t('i18nkey1')</li>
                        <li class="ml20">示例2： function fnName() {}</li>
                        <li class="ml20">示例3： { getValue: () => {} }</li>
                      </ul>
                    </div>
                  </div>
                </template>
              </tiny-popover>
            </div>
          </template>
          <template #buttons>
            <editor-i18n-tool ref="i18nToolRef" @confirm="insertContent"></editor-i18n-tool>
          </template>
        </monaco-editor>
      </div>
    </tiny-form-item>
    <tiny-form-item v-if="state.hasAccessor" class="store-form-item monaco-form-item">
      <monaco-editor ref="getterEditor" class="variable-editor" :options="options" :value="state.getterEditorValue">
        <template #toolbar>
          <div style="display: flex">
            <div>getter</div>
            <tiny-popover placement="bottomLeft" trigger="hover" popper-class="state-data-example-tips">
              <div class="create-content-description">查看示例</div>
              <template #content>
                <div class="tips-content">
                  <div class="create-content-demo">
                    <pre><code>{{ getterExample }}</code></pre>
                  </div>
                </div>
              </template>
            </tiny-popover>
          </div>
        </template>
      </monaco-editor>
    </tiny-form-item>
    <tiny-form-item v-if="state.hasAccessor" class="store-form-item monaco-form-item">
      <monaco-editor ref="setterEditor" class="variable-editor" :options="options" :value="state.setterEditorValue">
        <template #toolbar>
          <div style="display: flex">
            <div>setter</div>
            <tiny-popover trigger="hover" popper-class="state-data-example-tips" placement="bottomLeft">
              <div class="create-content-description">查看示例</div>
              <template #content>
                <div class="tips-content">
                  <div class="create-content-demo">
                    <pre><code>{{ setterExample }}</code></pre>
                  </div>
                </div>
              </template>
            </tiny-popover>
          </div>
        </template>
      </monaco-editor>
    </tiny-form-item>
    <div class="show-advanced" @click="state.hasAccessor = !state.hasAccessor">
      {{ (state.hasAccessor ? '移除' : '添加') + '高级属性' }}
    </div>
  </tiny-form>
</template>

<script>
import { reactive, ref, computed, watch } from 'vue';
import { Popover, Form, FormItem, Input, RadioGroup } from 'ant-design-vue';
import { MonacoEditor } from '@/common';
import { verifyJsVarName } from '@/common/js/verification';
import * as Monaco from 'monaco-editor';
import { validateMonacoEditorData } from './js/common';
import EditorI18nTool from './EditorI18nTool.vue';

export default {
  components: {
    MonacoEditor,
    TinyForm: Form,
    TinyFormItem: FormItem,
    TinyInput: Input,
    TinyPopover: Popover,
    RadioGroup,
    EditorI18nTool,
  },
  props: {
    createData: {
      type: Object,
    },
    dataSource: {
      type: Object,
    },
    flag: {
      type: String,
    },
    updateKey: {
      type: String,
    },
  },
  emits: ['nameInput'],
  setup(props) {
    const variableEditor = ref(null);
    const getterEditor = ref(null);
    const setterEditor = ref(null);

    const i18nToolRef = ref(null);

    const getEditor = () => variableEditor.value;

    const isAccessorData = data => [data?.accessor?.getter?.type, data?.accessor?.setter?.type].includes('JSFunction');
    const getPropsCreateData = () => ({
      name: '',
      ...props.createData,
      variable: isAccessorData(props.createData.variable) ? props.createData.variable.defaultValue : props.createData.variable,
    });

    const DEFAULT_GETTER = 'function getter() {}';
    const DEFAULT_SETTER = 'function setter() {}';

    const LANG_TYPES = {
      JSON: 'json',
      JS: 'javascript',
    };
    const VAR_TYPES = [
      { label: 'JSON类型', value: LANG_TYPES.JSON },
      { label: 'JS表达式类型', value: LANG_TYPES.JS },
    ];
    const getVarType = () => (props.createData.variable?.type === 'JSExpression' ? LANG_TYPES.JS : LANG_TYPES.JSON);

    const state = reactive({
      errorMessage: '',
      createData: getPropsCreateData(),
      hasAccessor: isAccessorData(props.createData?.variable),
      variableType: getVarType(),
      getterEditorValue: props.createData.variable?.accessor?.getter?.value || DEFAULT_GETTER,
      setterEditorValue: props.createData.variable?.accessor?.setter?.value || DEFAULT_SETTER,
      editorOptions: {
        language: getVarType(),
        lineNumbers: true,
        // 禁用滚动条边边一直显示的边框
        overviewRulerBorder: false,
        renderLineHighlightOnlyWhenFocus: true,
      },
    });

    const changeLanguage = language => {
      state.editorOptions.language = language;
      Monaco.editor.setModelLanguage(variableEditor.value.getEditor().getModel(), language);
    };

    watch(
      () => props.createData.variable,
      () => {
        state.errorMessage = '';
        state.createData = getPropsCreateData();
        state.hasAccessor = isAccessorData(props.createData?.variable);
        state.getterEditorValue = props.createData.variable?.accessor?.getter?.value || DEFAULT_GETTER;
        state.setterEditorValue = props.createData.variable?.accessor?.setter?.value || DEFAULT_SETTER;
        state.variableType = getVarType();
        if (state.editorOptions.language !== state.variableType) {
          changeLanguage(state.variableType);
        }
      },
    );

    watch(
      () => state.variableType,
      () => {
        changeLanguage(state.variableType);
      },
    );

    const validate = () => {
      if (state.errorMessage) {
        return { success: false, message: state.errorMessage };
      }
      // JS表达式不进行校验
      if (state.variableType === LANG_TYPES.JS) {
        return { success: true };
      }
      return validateMonacoEditorData(variableEditor.value, '初始数据');
    };

    const getDefaultValue = () => {
      // JS表达式使用字符串值，不进行解析
      if (state.variableType === LANG_TYPES.JS) {
        return { type: 'JSExpression', value: getEditor().getEditor().getValue() };
      }
      return getEditor().getValue();
    };

    const getFormData = () => {
      const defaultValue = getDefaultValue();
      if (!state.hasAccessor) return defaultValue;

      const getter = getterEditor.value.getEditor().getValue();
      const setter = setterEditor.value.getEditor().getValue();
      if (!getter && !setter) return defaultValue;

      const result = { defaultValue };
      if (getter && getter !== DEFAULT_GETTER) {
        result.accessor = { ...result.accessor, getter: { type: 'JSFunction', value: getter } };
      }
      if (setter && setter !== DEFAULT_SETTER) {
        result.accessor = { ...result.accessor, setter: { type: 'JSFunction', value: setter } };
      }

      return result;
    };

    const validateName = (rule, name, callback) => {
      state.errorMessage = '';

      if (!name) {
        state.errorMessage = 'state 属性名称未定义';
      } else if (!verifyJsVarName(name)) {
        state.errorMessage = ' state 属性名称只能以字母或下划线开头且仅包含数字字母及下划线';
      } else if (Object.keys(props.dataSource).includes(name) && (props.flag !== 'update' || name !== props.updateKey)) {
        state.errorMessage = '已存在同名 state 属性';
      }

      state.errorMessage ? callback(new Error(state.errorMessage)) : callback();
    };

    const rules = {
      name: { validator: validateName, required: true },
    };

    const editorCode = computed(() => {
      const { type, value } = state.createData.variable || {};

      if (type === 'JSExpression') {
        return value;
      }

      return JSON.stringify(state.createData.variable, null, 2);
    });

    const addContextMenu = (editorInstance, id, label, handler) => {
      editorInstance.addAction({
        id,
        label,
        precondition: null,
        keybindingContext: null,
        contextMenuGroupId: 'navigation',
        contextMenuOrder: 1,
        run: handler,
      });
    };

    const editorDidMount = editorInstance => {
      addContextMenu(editorInstance, 'addI18n', '插入词条', () => {
        i18nToolRef.value.state.showPopover = true;
      });

      // 支持对象类型数据或表达式，不显示语法校验报错
      const diagnosticsOptions = variableEditor.value.editor.getMonaco().languages?.typescript?.javascriptDefaults.getDiagnosticsOptions();
      variableEditor.value.editor.getMonaco().languages?.typescript?.javascriptDefaults.setDiagnosticsOptions({
        ...diagnosticsOptions,
        noSyntaxValidation: true,
        noSemanticValidation: true,
      });
    };

    const insertContent = (insertText = '') => {
      const monacoEditor = getEditor().editor.getEditor();
      const selection = monacoEditor.getSelection();
      const range = new Monaco.Range(
        selection?.startLineNumber || 1,
        selection?.startColumn || 1,
        selection?.endLineNumber || 1,
        selection?.endColumn || 1,
      );

      monacoEditor.executeEdits('', [{ range, text: insertText }]);
      getEditor().formatCode();
      monacoEditor.focus();
    };

    const options = {
      lineNumbers: true,
      language: 'javascript',
      // 禁用滚动条边边一直显示的边框
      overviewRulerBorder: false,
      renderLineHighlightOnlyWhenFocus: true,
    };
    const getPopupContainer = trigger => {
      return trigger.parentElement;
    };
    const getterExample = 'function getter() {\r\n  // this.state.name = `${this.props.firstName} ${this.props.lastName}`\r\n}'; // eslint-disable-line
    const setterExample =
      "function setter() {\r\n  // const [firstName, lastName] = this.state.name.split(' ')\r\n  // this.emit('update:firstName', firstName)\r\n  // this.emit('update:lastName', lastName)\r\n}"; // eslint-disable-line

    return {
      state,
      VAR_TYPES,
      variableEditor,
      getterEditor,
      setterEditor,
      i18nToolRef,
      editorCode,
      rules,
      options,
      getterExample,
      setterExample,
      getEditor,
      validateName,
      editorDidMount,
      validate,
      getFormData,
      insertContent,
      getPopupContainer,
    };
  },
};
</script>

<style lang="less" scoped>
.create-form {
  padding: 12px;
  height: calc(100% - 50px);
  overflow-y: auto;

  .error-tip {
    color: var(--ti-lowcode-error-tip-color);
    margin-top: 4px;
    font-size: 12px;
  }

  .tiny-form-item:not(:last-child) {
    margin-bottom: 12px;
  }

  .init-value-item {
    margin-top: -22px;
  }

  .tiny-form-item {
    &.var-type-item {
      :deep(.tiny-button-group ul.tiny-group-item li.active button:not(.disabled)) {
        background: var(--ti-lowcode-data-radio-group-active-bg);
      }
      :deep(.tiny-button-group ul.tiny-group-item li button:not(.disabled)) {
        background: var(--ti-lowcode-data-radio-group-bg);
        border: 0px;
        border-radius: 0px;
      }
    }
    &.monaco-form-item {
      margin-bottom: 24px;
      :deep(.tiny-form-item__label) {
        position: relative;
        z-index: 1;
        margin-bottom: -58px;
      }
    }
  }

  :deep(.tiny-form-item__label) {
    color: var(--ti-lowcode-toolbar-icon-color);
    .label-left-wrap {
      display: flex;
    }
  }
}

.create-content {
  color: var(--ti-lowcode-create-color);
}

.tips-content {
  padding: 12px;
  .create-content-head {
    display: flex;
    flex: 1;
    font-size: 12px;

    .icon-info-circle {
      font-size: 14px;
      margin-right: 4px;
      color: var(--ti-lowcode-toolbar-icon-color);
    }
  }
  .create-content-tip {
    font-size: 14px;
  }

  .create-content-demo {
    font-size: 14px;
    .ml20 {
      margin-left: 20px;
    }
    li {
      margin-top: 8px;
    }
  }
}

.create-content-description {
  color: var(--ti-lowcode-description-color);
  margin-left: 8px;
  cursor: pointer;
}
.create-content-editor {
  height: 230px;
  .monaco-editor {
    height: 200px !important;
  }
}

.variable-editor {
  height: 230px;
  :deep(.toolbar) {
    margin-bottom: 8px;
  }
  .monaco-editor {
    height: 200px !important;
  }
}
:deep(.tiny-form-item__label) {
  height: unset;
}
.show-advanced {
  font-size: 12px;
  margin-top: 40px;
  color: var(--ti-lowcode-data-advanced-text-color);
  &:hover {
    color: var(--ti-lowcode-data-advanced-text-hover-color);
    cursor: pointer;
  }
}
</style>

<style lang="less">
.tiny-popover.tiny-popper.state-data-example-tips {
  background-color: var(--ti-lowcode-data-example-bg-color);
  color: var(--ti-lowcode-data-example-color);
  .popper__arrow {
    &,
    &::after {
      border-bottom-color: var(--ti-lowcode-data-example-bg-color);
    }
  }
}
</style>
