<template>
  <config-render :data="properties">
    <template #prefix="{ data }">
      <block-link-field v-if="isBlock" :data="data"></block-link-field>
    </template>
  </config-render>
  <block-description v-if="isBlock" class="block-description"> </block-description>
  <empty :showEmptyTips="showEmptyTips"></empty>
</template>

<script>
import { computed, watchEffect, ref } from 'vue';
import { ConfigRender, BlockDescription, BlockLinkField } from '@/common';
import { useCanvas, useProperty } from '@/controller';
import Empty from './components/Empty.vue';

export default {
  components: {
    ConfigRender,
    BlockLinkField,
    BlockDescription,
    Empty,
  },
  setup() {
    const { pageState } = useCanvas();
    const { properties } = useProperty({ pageState });
    const showEmptyTips = ref(false);

    const isBlock = computed(() => pageState.isBlock);

    watchEffect(() => {
      showEmptyTips.value = !properties.value?.length;
    });

    return {
      isBlock,
      properties,
      showEmptyTips,
    };
  },
};
</script>

<style lang="less" scoped>
.block-description {
  margin: 10px;
}
:deep(.ant-tabs-content-holder) {
  overflow-y: scroll;
}
</style>
