<template>
  <div id="typography-more">
    <tiny-collapse expand-icon-position="right">
      <tiny-collapse-item :header="title" name="1">
        <slot></slot>
      </tiny-collapse-item>
    </tiny-collapse>
  </div>
</template>

<script>
import { Collapse, CollapsePanel } from 'ant-design-vue';

export default {
  components: {
    TinyCollapse: Collapse,
    TinyCollapseItem: CollapsePanel,
  },
  props: {
    title: {
      type: String,
      default: '更多类型选项',
    },
  },
  setup() {
    return {};
  },
};
</script>

<style lang="less" scoped>
#typography-more {
  .tiny-collapse {
    :deep(.tiny-collapse-item) {
      .tiny-collapse-item__header {
        height: 24px;
        line-height: 24px;
        border: none;
        justify-content: center;
        transition: 0.3s;

        &:hover {
          color: var(--ti-lowcode-toolbar-icon-color);

          svg {
            color: var(--ti-lowcode-toolbar-icon-color);
          }
        }
      }

      .tiny-collapse-item__content {
        padding: 0;
      }
    }
  }
}
</style>
