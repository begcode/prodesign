<template>
  <Toolbar v-if="showToolbar" />
  <Preview />
</template>

<script>
import Preview from './preview/Preview.vue';
import Toolbar from './Toolbar.vue';
import 'ant-design-vue/dist/reset.css';

export default {
  components: {
    Preview,
    Toolbar,
  },
  props: {
    showToolbar: {
      type: Boolean,
      default: true,
    },
  },
};
</script>

<style lang="less" scoped>
/* 一般类型的预览，顶部有工具条 */
#app > .vue-repl {
  --base-top-panel-border-bottom-width: 1px;
  height: calc(100vh - var(--base-top-panel-height) - var(--base-top-panel-border-bottom-width));
}
/* console 类型的预览，内容区域样式 */
#J_container > .vue-repl {
  height: 100%;
  width: 100%;
  position: static;
}
</style>
