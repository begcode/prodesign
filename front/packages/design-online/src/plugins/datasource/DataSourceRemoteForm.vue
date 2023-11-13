<template>
  <Form
    ref="serviceFormRef"
    class="create-form"
    label-position="left"
    label-width="15%"
    :model="state.serviceForm"
    :rules="rules"
    validate-type="text"
  >
    <!-- <tiny-form-item label="名称" prop="name">
      <tiny-input v-model="state.serviceForm.name" placeholder="只能包含数字字母及下划线"></tiny-input>
    </tiny-form-item> -->

    <FormItem label="描述" name="description">
      <Input v-model:value="state.serviceForm.description" placeholder="请输入"></Input>
    </FormItem>

    <FormItem label="地址" name="uri">
      <div class="textarea-warp">
        <Input class="border-input" v-model:value="state.serviceForm.uri" resize="none" placeholder="请输入" compact>
          <template #addonBefore>
            <Select class="selectResType" v-model:value="state.serviceForm.method" placeholder="请选择">
              <SelectOption v-for="item in state.requestData" :key="item.value" :label="item.value" :value="item.value" />
            </Select>
          </template>
          <template #addonAfter><Icon icon="ant-design:send-outlined" style="cursor: pointer" @click="sendRequest">请求</Icon></template>
        </Input>
      </div>
    </FormItem>
  </Form>
</template>

<script>
import { reactive, watchEffect, ref } from 'vue';
import { Form, FormItem, Input, Select, SelectOption, Button } from 'ant-design-vue';
import { Icon } from '@begcode/components';

export default {
  components: {
    Form,
    FormItem,
    Input,
    Select,
    SelectOption,
    Button,
    Icon,
  },
  props: {
    modelValue: {
      type: Object,
      default: () => ({}),
    },
  },
  emits: ['sendRequest'],
  expose: ['getServiceForm'],
  setup(props, { emit }) {
    const state = reactive({
      serviceForm: {},
      requestData: [
        { text: 'JSONP', value: 'JSONP' },
        { text: 'GET', value: 'GET' },
        { text: 'POST', value: 'POST' },
        { text: 'PUT', value: 'PUT' },
        { text: 'DELETE', value: 'DELETE' },
      ],
    });

    watchEffect(() => {
      state.serviceForm = props.modelValue;
    });

    const rules = {
      uri: [{ required: true, message: '必填', trigger: 'change' }],
      method: { required: true, message: '必选', trigger: 'change' },
    };

    const sendRequest = () => {
      emit('sendRequest');
    };
    const serviceFormRef = ref(null);

    const getServiceForm = () => {
      return serviceFormRef.value;
    };

    return {
      sendRequest,
      state,
      rules,
      serviceFormRef,
      getServiceForm,
    };
  },
};
</script>

<style lang="less" scoped>
.create-form {
  padding: 12px 0;

  .error-tip {
    color: var(--ti-lowcode-datasource-error-tip-color);
    margin-top: 4px;
    font-size: 12px;
  }
  :deep(.tiny-form-item__label) {
    color: var(--ti-lowcode-datasource-label-color);
  }
  .textarea-warp {
    display: flex;
    justify-content: flex-start;
    align-items: center;

    .selectResType {
      width: 100px;
      border: none;
    }
    :deep(.tiny-input-group__prepend) {
      background: var(--ti-lowcode-datasource-respones-select-color-bg);
      border-color: var(--ti-lowcode-datasource-select-border-color);
      .tiny-input-suffix {
        .tiny-input-display-only {
          .tiny-input__inner {
            border-color: var(--ti-lowcode-datasource-select-border-right-color-bg);
            border-left: none;
          }
        }
      }
    }
    :deep(.tiny-input-display-only) {
      .tiny-input__inner {
        border-left: none;
      }
    }
    :deep(.tiny-input-group__append) {
      border: none;
      background: var(--ti-lowcode-datasource-respones-color-bg);
    }
  }
  .tiny-button-group {
    width: 100%;
  }
  :deep(.tiny-group-item) {
    display: flex;
    width: 100%;
    button {
      position: relative;
      min-width: inherit;
      padding: 0 4px;
      margin: 0;
      width: 100%;
    }
    li {
      flex: 1 1 0;
      &:not(:last-child) {
        button:before {
          content: '';
          display: inline-block;
          width: 1px;
          height: 100%;
          position: absolute;
          top: 0;
          right: 0;
          z-index: 99;
        }
      }
    }
  }

  :deep(.tiny-form-item__label) {
    height: 30px;
    line-height: 30px;
  }
}
</style>
