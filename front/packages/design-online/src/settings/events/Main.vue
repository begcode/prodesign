<template>
  <tiny-collapse v-model:activeKey="activeNames" :expand-icon-position="'right'">
    <tiny-collapse-item header="事件绑定" key="bindEvent">
      <bind-events></bind-events>
    </tiny-collapse-item>
    <tiny-collapse-item header="高级配置" key="advancedConfig">
      <advance-config></advance-config>
    </tiny-collapse-item>
  </tiny-collapse>
</template>

<script setup>
import { ref } from 'vue';
import { Collapse as TinyCollapse, CollapsePanel as TinyCollapseItem } from 'ant-design-vue';
import BindEvents from './components/BindEvents.vue';
import AdvanceConfig from './components/AdvanceConfig.vue';

const activeNames = ref(['bindEvent', 'advancedConfig']);
</script>
