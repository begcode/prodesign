<template>
  <InputGroup>
    <Input :value="beginValue" style="width: calc(50% - 15px)" placeholder="最小值" @change="handleChangeBegin" />
    <Input style="width: 30px; border-left: 0; pointer-events: none; background-color: #fff" placeholder="~" disabled />
    <Input :value="endValue" style="width: calc(50% - 15px); border-left: 0" placeholder="最大值" @change="handleChangeEnd" />
  </InputGroup>
</template>

<script>
/**
 * 查询条件用-数值范围查询
 */
import { ref, watch } from 'vue';
import { Form, Input, InputGroup } from 'ant-design-vue';
import { propTypes } from '@/utils/propTypes';

export default {
  name: 'RangeNumber',
  components: {
    Input,
    InputGroup,
  },
  props: {
    value: propTypes.oneOfType([propTypes.string, propTypes.array]),
  },
  emits: ['change', 'update:value'],
  setup(props, { emit }) {
    const beginValue = ref('');
    const endValue = ref('');
    const formItemContext = Form.useInjectFormItemContext();

    function handleChangeBegin(e) {
      beginValue.value = e.target.value;
      emitArray();
    }

    function handleChangeEnd(e) {
      endValue.value = e.target.value;
      emitArray();
    }

    function emitArray() {
      let arr = [];
      let begin = beginValue.value || '';
      let end = endValue.value || '';
      arr.push(begin);
      arr.push(end);
      emit('change', arr);
      emit('update:value', arr);
      formItemContext.onFieldChange();
    }

    watch(
      () => props.value,
      val => {
        if (val && val.length == 2) {
          beginValue.value = val[0];
          endValue.value = val[1];
        } else {
          beginValue.value = '';
          endValue.value = '';
        }
      },
      { immediate: true },
    );

    return {
      beginValue,
      endValue,
      handleChangeBegin,
      handleChangeEnd,
    };
  },
};
</script>
