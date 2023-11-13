<template>
  <Modal
    v-model:open="state.open"
    dialog-class="deploy-dialog"
    :title="title"
    :append-to-body="true"
    :is-form-reset="false"
    @close="closeDialog"
  >
    <Form
      ref="form"
      show-message
      :model="formData"
      :rules="rules"
      label-width="80px"
      :label-align="true"
      label-position="left"
      validate-type="text"
    >
      <FormItem label="分类名称" prop="name">
        <Input v-model="formData.name" placeholder="请输入分类名称" @change="handleChangeName"></Input>
      </FormItem>
      <FormItem label="分类ID" prop="categoryId">
        <Input v-model="formData.categoryId" placeholder="请输入分类ID" :disabled="isEdit"></Input>
      </FormItem>
    </Form>
    <template #footer>
      <Button type="primary" @click="confirm">确定</Button>
      <Button @click="closeDialog">取消</Button>
    </template>
  </Modal>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue';
import { pinyin } from 'pinyin-pro';
import { Form, FormItem, Input, Button, Modal } from 'ant-design-vue';
import { useBlock } from '@/controller';
import { REGEXP_GROUP_NAME } from '@/common/js/verification';
import { extend } from '@opentiny/vue-renderless/common/object';
import { createOrUpdateCategory } from './js/blockSetting';

const { getGroupList } = useBlock();

const props = defineProps({
  modelValue: Boolean,
  initialValue: Object,
});

const emit = defineEmits(['update:modelValue']);

const state = reactive({
  open: false,
});

const isEdit = computed(() => Object.keys(props.initialValue).length !== 0);
const groupList = computed(getGroupList);

const form = ref(null);

const formData = reactive({ name: '', categoryId: '' });

const validateGroup = (rule, value, callback) => {
  const isRepeat = props.initialValue?.name !== value && groupList.value.some(group => group.name === value);
  if (isRepeat) {
    callback(new Error('分类名称不能重复！'));
    return;
  }
  callback();
};
const rules = reactive({
  name: [
    { required: true, message: '必填', trigger: 'blur' },
    { pattern: REGEXP_GROUP_NAME, message: '只能包含大小写字母、数字和特殊字符', trigger: 'change' },
    {
      validator: validateGroup,
      trigger: 'blur',
    },
  ],
  categoryId: [{ required: true, message: '必填', trigger: 'blur' }],
});

const handleChangeName = value => {
  if (isEdit.value) {
    // 编辑时不允许修改id
    return;
  }
  const cnReg = /[\u4e00-\u9fa5]/g;
  let id = value.replace(cnReg, match => {
    return pinyin(match, { toneType: 'none' });
  });
  // 名称不重复，id重复，后缀加上时间戳
  const isRepeat = groupList.value.some(item => item.name !== value && item.category_id === id);
  if (isRepeat) {
    id += new Date().getTime();
  }
  formData.categoryId = id;
};

const title = computed(() => `${isEdit.value ? '编辑' : '新建'}分类`);

const closeDialog = () => {
  emit('update:modelValue', false);
};

const confirm = () => {
  form.value.validate(valid => {
    if (valid) {
      const params = extend(true, {}, formData);
      if (isEdit.value) {
        params.id = props.initialValue.id;
      }
      createOrUpdateCategory(params, isEdit.value);
      closeDialog();
    }
  });
};

watch(
  () => props.modelValue,
  val => {
    state.open = val;
  },
);

watch(
  () => props.initialValue,
  val => {
    if (val) {
      formData.name = val.name ?? '';
      formData.categoryId = val.category_id ?? '';
    }
  },
);
</script>
