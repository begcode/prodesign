<template>
  <tiny-select v-model="appInfoState.selectedId" placeholder="应用名称" @change="appChange">
    <tiny-option v-for="item in appInfoState.list" :key="`app-${item.name}-${item.id}`" :label="item.name" :value="item.id"></tiny-option>
  </tiny-select>
</template>

<script>
import { useApp } from '@/controller';
import { Select, SelectOption } from 'ant-design-vue';

export default {
  components: {
    TinySelect: Select,
    TinyOption: SelectOption,
  },
  setup() {
    const { appInfoState } = useApp();

    const appChange = appId => {
      appInfoState.selectedId = appId;
    };

    return {
      appInfoState,
      appChange,
    };
  },
};
</script>
