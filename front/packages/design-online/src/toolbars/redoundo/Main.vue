<template>
  <span class="redo-undo-wrap">
    <tiny-popover
      trigger="hover"
      :open-delay="1000"
      popper-class="toolbar-right-popover"
      append-to-body
      :content="historyState.back ? '撤销' : '没有要撤销的'"
    >
      <span :class="['icon-wrap', 'undo', { disabled: !historyState.back }]" @click="back">
        <Icon :icon="undoIcon"></Icon>
      </span>
    </tiny-popover>
    <tiny-popover
      trigger="hover"
      :open-delay="1000"
      popper-class="toolbar-right-popover"
      append-to-body
      :content="historyState.forward ? '恢复' : '没有要恢复的'"
    >
      <span :class="['icon-wrap', 'redo', !historyState.forward ? 'disabled' : '']" @click="forward">
        <Icon :icon="redoIcon"></Icon>
      </span>
    </tiny-popover>
  </span>
</template>

<script>
import { Popover } from 'ant-design-vue';
import { useHistory } from '@/controller';
import { Icon } from '@begcode/components';

export default {
  components: {
    Icon,
    TinyPopover: Popover,
  },
  props: {
    undoIcon: {
      type: String,
      default: 'ant-design:undo-outlined',
    },
    redoIcon: {
      type: String,
      default: 'ant-design:redo-outlined',
    },
  },
  setup() {
    return useHistory();
  },
};
</script>

<style lang="less" scoped>
.redo-undo-wrap {
  display: flex;

  :deep(.icon-wrap) {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    height: 32px;
    width: 32px;
    border-radius: 6px;
    font-size: 20px;
    svg {
      color: var(--ti-lowcode-toolbar-title-color);
    }
    &.disabled {
      cursor: not-allowed;
      svg {
        color: var(--ti-lowcode-disabled-color);
      }
    }
    &:not(.disabled):hover {
      background: var(--ti-lowcode-toolbar-view-active-bg);
      svg {
        color: var(--ti-lowcode-toolbar-active-color);
      }
    }
    &.redo {
      margin-left: -5px;
    }
  }
}
</style>
