<template>
  <div class="h-full min-editor">
    <CodeMirror :value="getValue" @change="handleValueChange" :lang="lang" :readonly="readonly" :bordered="bordered" />
  </div>
</template>
<script lang="ts" setup>
import { computed } from 'vue';
import CodeMirror from 'vue-codemirror6';
import { json } from '@codemirror/lang-json';
import { javascript } from '@codemirror/lang-javascript';
import { html } from '@codemirror/lang-html';
import { isString } from 'lodash-es';
import { MODE } from './typing';

const props = defineProps({
  value: { type: [Object, String] as PropType<Record<string, any> | string> },
  mode: {
    type: String as PropType<MODE>,
    default: MODE.JSON,
    validator(value: any) {
      // 这个值必须匹配下列字符串中的一个
      return Object.values(MODE).includes(value);
    },
  },
  readonly: { type: Boolean },
  autoFormat: { type: Boolean, default: true },
  bordered: { type: Boolean, default: false },
});

const emit = defineEmits(['change', 'update:value', 'format-error']);

const lang = computed(() => {
  const { mode } = props;
  if (mode === MODE.JSON) {
    return json();
  } else if (mode === MODE.JS) {
    return javascript();
  } else if (mode === MODE.HTML) {
    return html();
  }
  return null;
});

const getValue = computed(() => {
  const { value, mode, autoFormat } = props;
  if (!autoFormat || mode !== MODE.JSON) {
    return value as string;
  }
  let result = value;
  if (isString(value)) {
    try {
      result = JSON.parse(value);
    } catch (e) {
      emit('format-error', value);
      return value as string;
    }
  }
  return JSON.stringify(result, null, 2);
});

function handleValueChange(v) {
  emit('update:value', v);
  emit('change', v);
}
</script>
<style scoped>
.min-editor {
  min-height: 200px;
  min-width: 300px;
  border: #d4d9e1 1px solid;
}
</style>
