<template>
  <div class="components-wrap">
    <tiny-search v-model="state.searchValue" placeholder="请输入关键字搜索" @update:modelValue="change"></tiny-search>
    <tiny-collapse v-model:activeKey="state.activeName" :bordered="false">
      <tiny-collapse-item v-for="(item, index) in state.components" :key="item.group" :header="item.group">
        <ul class="component-group" :style="{ gridTemplateColumns }">
          <template v-for="child in item.children" :key="child.component">
            <canvas-drag-item
              v-if="!child.hidden && (child.name?.zh_CN || child.name)"
              :data="generateNode({ component: child.snippetName || child.component })"
              @click="componentClick"
            >
              <li class="component-item">
                <div class="component-item-component">
                  <Icon :icon="child?.icon?.toLowerCase() || 'row'" :size="40"></Icon>
                </div>
                <span class="component-item-name" :title="child.name?.zh_CN || child.name">{{ child.name?.zh_CN || child.name }}</span>
              </li>
            </canvas-drag-item>
          </template>
        </ul>
      </tiny-collapse-item>
    </tiny-collapse>
  </div>
</template>

<script>
import { inject, onMounted, reactive, ref } from 'vue';
import { Collapse, CollapsePanel, InputSearch } from 'ant-design-vue';
import { iconSearch } from '@opentiny/vue-icon';
import { useResource } from '@/controller';
import { CanvasDragItem, addComponent } from '@/canvas';
import { Icon } from '@begcode/components';

export default {
  components: {
    Icon,
    TinySearch: InputSearch,
    IconSearch: iconSearch(),
    TinyCollapse: Collapse,
    TinyCollapseItem: CollapsePanel,
    CanvasDragItem,
  },
  setup() {
    const COMPONENT_PANEL_COLUMNS = '1fr 1fr 1fr';
    const SHORTCUT_PANEL_COLUMNS = '1fr 1fr 1fr 1fr 1fr 1fr';
    const { generateNode, resState } = useResource();
    const gridTemplateColumns = ref(COMPONENT_PANEL_COLUMNS);
    const panelState = inject('panelState', {});
    const { components } = resState;

    const fetchComponents = (components, name) => {
      if (!name) {
        return components;
      }

      const result = [];
      components.forEach(component => {
        const children = [];

        component.children.forEach(child => {
          if (child.name?.zh_CN?.toLowerCase().indexOf(name.toLowerCase()) > -1) {
            children.push(child);
          }
        });

        if (children.length > 0) {
          result.push({
            groupId: component.groupId,
            group: component.group,
            groupName: component.groupName,
            children: children,
          });
        }
      });

      return result;
    };

    const state = reactive({
      components,
      activeName: [...components.map(item => item.group)],
      searchValue: '',
    });

    const change = value => {
      state.components = fetchComponents(components, value);
    };

    const componentClick = data => {
      const { isShortcutPanel, emitEvent } = panelState;
      console.log('componentClick', data);
      console.log('componentClickpanelState', panelState);
      if (isShortcutPanel) {
        addComponent(data, isShortcutPanel);
        emitEvent('close');
      }
    };

    onMounted(() => {
      if (panelState.isShortcutPanel) {
        gridTemplateColumns.value = SHORTCUT_PANEL_COLUMNS;
      }
    });

    return {
      gridTemplateColumns,
      state,
      change,
      generateNode,
      componentClick,
    };
  },
};
</script>

<style lang="less" scoped>
.components-wrap {
  height: 100%;

  .ant-input-search {
    padding: 12px 8px;
  }

  .component-group {
    display: grid;
    width: 100%;
    color: var(--ti-lowcode-materials-component-list-color);

    .component-item {
      padding: 2px 0;
      border-right: 1px solid var(--ti-lowcode-material-component-list-border-color);
      border-bottom: 1px solid var(--ti-lowcode-material-component-list-border-color);
      text-align: center;
      user-select: none;
      cursor: move;
      background: var(--ti-lowcode-common-component-bg);

      &:hover {
        background: var(--ti-lowcode-material-component-list-hover-bg);
      }

      .component-item-component {
        margin-bottom: 8px;

        .anticon {
          font-size: 40px;
          vertical-align: middle;
          color: var(--ti-lowcode-component-icon-color);
          overflow: hidden;
        }
      }

      .component-item-name {
        max-width: 62px;
        display: inline-block;
        overflow: hidden;
        font-size: 12px;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }

  :deep(.tiny-svg, #tiny-engine .tiny-svg) {
    transform: rotate(270deg);
    &.is-active {
      transform: rotate(180deg);
    }
  }
}
</style>
