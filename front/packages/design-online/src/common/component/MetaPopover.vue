<template>
  <tiny-popover width="267" trigger="click" placement="bottom-end" :visible-arrow="true" popperClass="option-popper">
    <template #content>
      <slot name="body">
        <meta-child-item :meta="meta" @update:modelValue="onValueChange"></meta-child-item>
      </slot>
    </template>
    <slot name="reference">
      <tiny-button>{{ title }}</tiny-button>
    </slot>
  </tiny-popover>
</template>

<script>
import { computed } from 'vue';
import { Popover, Button } from 'ant-design-vue';
import MetaChildItem from './MetaChildItem.vue';

export default {
  name: 'MetaPopover',
  components: {
    MetaChildItem,
    TinyPopover: Popover,
    TinyButton: Button,
  },
  inheritAttrs: false,
  props: {
    meta: {
      type: Object,
      default: {},
    },
  },
  emits: ['update:modelValue'],
  setup(props, { attrs, emit }) {
    const title = computed(() => `编辑${props.meta.label?.text?.zh_CN || props.meta.property || '属性'}`);
    const onValueChange = ({ propertyKey, propertyValue }) => {
      const newPropertyValue = { ...props.meta.widget?.props?.modelValue, [propertyKey]: propertyValue };
      emit('update:modelValue', newPropertyValue);
    };

    return { title, onValueChange };
  },
};
</script>
