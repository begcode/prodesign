<template>
  <tiny-select v-model="value" placeholder="请选择" no-data-text="none" @change="change">
    <tiny-option v-for="(item, index) in data" :key="index" :label="item.text" :value="item.value"> </tiny-option>
  </tiny-select>
</template>

<script>
import { Select, SelectOption } from 'ant-design-vue';
import { useProperties } from '@/controller';
import { computed } from 'vue';

export default {
  components: {
    TinySelect: Select,
    TinyOption: SelectOption,
  },
  inheritAttrs: false,
  props: {
    name: {
      type: String,
      default: '',
    },
    modelValue: {
      type: String,
      default: '',
    },
    data: {
      type: Object,
      default: () => [],
    },
    changeEvt: {
      type: Boolean,
      default: true,
    },
  },

  setup(props, { emit }) {
    const { setProp } = useProperties();
    const value = computed(() => props.modelValue);

    const change = value => {
      emit('update:modelValue', value);
      setProp(props.name, value);
    };

    return {
      value,
      change,
    };
  },
};
</script>
