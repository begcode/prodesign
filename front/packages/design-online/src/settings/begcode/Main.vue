<template>
  <tiny-collapse v-model:activeKey="activeNames" :expand-icon-position="'right'">
    <tiny-collapse-item header="实体字段列表" key="jdl">
      <Suspense><JdlFieldList /></Suspense>
    </tiny-collapse-item>
    <tiny-collapse-item header="实体属性" key="other">
      <!--      <JdlEntityDetail />-->
    </tiny-collapse-item>
  </tiny-collapse>
</template>

<script setup>
import { ref } from 'vue';
import { Collapse as TinyCollapse, CollapsePanel as TinyCollapseItem } from 'ant-design-vue';
import JdlFieldList from '@/settings/begcode/components/jdl-field/jdl-field-list.vue';
import JdlEntityDetail from '@/settings/begcode/components/jdl-entity/jdl-entity-detail.vue';

const activeNames = ref(['jdl', 'other']);
</script>
