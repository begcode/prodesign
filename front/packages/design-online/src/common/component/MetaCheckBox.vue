<template>
  <div>
    <tiny-checkbox v-model="checked"><slot></slot></tiny-checkbox>
  </div>
</template>

<script>
import { ref, watch } from 'vue';
import { Checkbox } from 'ant-design-vue';

export default {
  components: {
    TinyCheckbox: Checkbox,
  },
  props: {
    modelValue: { type: Boolean, default: false },
  },
  setup(props, context) {
    const checked = ref(props.modelValue);

    watch(checked, newValue => {
      context.emit('update:modelValue', newValue);
    });

    return { checked };
  },
};
</script>
