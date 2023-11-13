<template>
  <plugin-panel :title="shortcut ? '' : '物料'" @close="$emit('close')">
    <template #header>
      <svg-button
        class="item icon-sidebar"
        :class="[fixedPanels?.includes(PLUGIN_NAME.Materials) && 'active']"
        name="fixed|svg"
        :tips="!fixedPanels?.includes(PLUGIN_NAME.Materials) ? '固定面板' : '解除固定面板'"
        @click="$emit('fixPanel', PLUGIN_NAME.Materials)"
      ></svg-button>
    </template>
    <template #content>
      <tiny-tabs v-model:activeKey="activeName" tab-style="button-card" class="full-width-tabs" @click="tabClick">
        <tiny-tab-item key="components" tab="组件">
          <component-panel></component-panel>
        </tiny-tab-item>
        <tiny-tab-item key="blocks" tab="区块">
          <block-panel></block-panel>
        </tiny-tab-item>
      </tiny-tabs>
      <block-group-panel></block-group-panel>
      <block-version-select></block-version-select>
    </template>
  </plugin-panel>
</template>

<script>
import { ref, reactive, provide } from 'vue';
import { Tabs, TabPane } from 'ant-design-vue';
import { PluginPanel, SvgButton } from '@/common';
import ComponentPanel from './component/Main.vue';
import BlockPanel from './block/Main.vue';
import BlockGroupPanel from './block/BlockGroupPanel.vue';
import BlockVersionSelect from './block/BlockVersionSelect.vue';
import { setBlockPanelVisible, setBlockVersionPanelVisible } from './block/js/usePanel';
import { useLayout } from '@/controller';
import { fetchGroups } from './block/http';

export const api = {
  fetchGroups,
};

export default {
  components: {
    TinyTabs: Tabs,
    TinyTabItem: TabPane,
    PluginPanel,
    ComponentPanel,
    BlockPanel,
    BlockGroupPanel,
    BlockVersionSelect,
    SvgButton,
  },
  props: {
    shortcut: [Boolean, String],
    fixedPanels: {
      type: Array,
    },
  },
  emits: ['close', 'fixPanel'],
  setup(props, { emit }) {
    const activeName = ref('components');

    const panelState = reactive({
      isShortcutPanel: props.shortcut,
      isBlockGroupPanel: false,
      isBlockList: false,
      emitEvent: emit,
    });

    provide('panelState', panelState);

    const tabClick = tabs => {
      if (tabs.name === 'components') {
        setBlockPanelVisible(false);
        setBlockVersionPanelVisible(false);
      }
    };
    const { PLUGIN_NAME } = useLayout();

    return {
      activeName,
      tabClick,
      PLUGIN_NAME,
    };
  },
};
</script>

<style lang="less" scoped>
.ant-tabs {
  height: calc(100% - 48px);
  :deep(.ant-tabs-nav) {
    padding: 8px;
    margin: 0;
  }
}

:deep(.tiny-tabs__content) {
  height: calc(100% - 48px);
  padding: 0;
  & > div {
    height: 100%;
  }
}

:deep(.ant-tabs-content-holder) {
  height: calc(100% - 56px);
  overflow-y: scroll;
  .tiny-collapse-item.is-active + .tiny-collapse-item {
    margin-top: 0;
  }

  .components-items {
    .item {
      cursor: pointer;
    }
  }
}

.tiny-collapse {
  height: calc(100% - 56px);
  overflow-y: scroll;
}
</style>
