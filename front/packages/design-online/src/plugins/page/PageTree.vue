<template>
  <div class="app-manage-search">
    <InputSearch v-model:value="state.pageSearchValue" clearable placeholder="搜索页面" @search="searchPageData"></InputSearch>
  </div>

  <Collapse v-model:activeKey="state.collapseValue" class="page-manage-collapse lowcode-scrollbar" :bordered="false">
    <CollapsePanel v-for="(groupItem, index) in pageSettingState.pages" :key="groupItem.groupId" :style="{ padding: 0 }">
      <template #header>
        <span class="title">{{ groupItem.groupName }}</span>
      </template>
      <template #default>
        <div class="app-manage-tree" style="margin: -16px">
          <Tree
            :ref="getPageTreeRefs"
            :key="pageSettingState.pageTreeKey"
            :tree-data="groupItem.data"
            :field-names="{
              children: 'children',
              title: 'name',
            }"
            default-expand-all
            :filter-node-method="filterPageTreeData"
            :expand-on-click-node="false"
            :shrink-icon="shrinkIcon"
            :expand-icon="expandIcon"
            node-key="id"
            show-icon
          >
            <template #icon="{ key, selected, dataRef }">
              <Icon v-if="dataRef.isPage" icon="text-page-common|svg"></Icon>
              <Icon v-if="!dataRef.isPage && !dataRef.children" icon="text-page-folder-closed|svg"></Icon>
            </template>
            <template #title="{ name, key, dataRef }">
              <Popover>
                <template #content>
                  <p>{{ name }}</p>
                </template>
                <span
                  style="color: #1890ff; display: inline-block; width: 140px; text-overflow: ellipsis; overflow: hidden"
                  @click="nodeClick($event, dataRef)"
                  >{{ name }}</span
                >
              </Popover>
              <span class="icons">
                <Icon
                  v-if="dataRef.isPage && getCanvasStatus(dataRef.occupier).state === PAGE_STATUS.Lock"
                  class="page-edit-icon"
                  icon="locked-outline|svg"
                  @click.stop="openSettingPanel(dataRef, getCanvasStatus(dataRef.occupier).state === PAGE_STATUS.Lock)"
                ></Icon>
                <span class="home" v-if="dataRef.isHome">
                  <Icon class="page-edit-icon" icon="text-page-home|svg"></Icon>
                </span>
                <Icon
                  :size="18"
                  icon="setting|svg"
                  class="setting page-edit-icon"
                  @click.stop="openSettingPanel(dataRef, getCanvasStatus(dataRef.occupier).state === PAGE_STATUS.Lock)"
                ></Icon>
              </span>
            </template>
          </Tree>
        </div>
      </template>
    </CollapsePanel>
  </Collapse>
</template>

<script lang="jsx">
import { reactive, ref, watchEffect } from 'vue';
import { InputSearch, Tree, Collapse, CollapsePanel, Popover } from 'ant-design-vue';
import { IconFolderOpened, IconFolderClosed } from '@opentiny/vue-icon';
import { useCanvas, useApp, useModal, usePage, useBreadcrumb, useLayout } from '@/controller';
import { isEqual } from '@opentiny/vue-renderless/common/object';
import { getCanvasStatus } from '@/common/js/index';
import { constants } from '@/utils';
import { closePageSettingPanel } from './PageSetting.vue';
import { closeFolderSettingPanel } from './PageFolderSetting.vue';
import http from './http.js';
import { Icon } from '@begcode/components';

const { ELEMENT_TAG, PAGE_STATUS } = constants;

export default {
  components: {
    InputSearch,
    Tree,
    Collapse,
    CollapsePanel,
    Icon,
    Popover,
  },
  props: {
    isFolder: {
      type: Boolean,
      default: false,
    },
  },
  emits: ['openSettingPanel', 'add'],
  setup(props, { emit }) {
    const { appInfoState } = useApp();
    const { confirm } = useModal();
    const { initData, pageState, isBlock, isSaved } = useCanvas();
    const { pageSettingState, changeTreeData, isCurrentDataSame } = usePage();
    const { fetchPageList, fetchPageDetail } = http;
    const { setBreadcrumbPage } = useBreadcrumb();
    const pageTreeRefs = ref([]);
    const ROOT_ID = pageSettingState.ROOT_ID;
    const pagesDataRef = ref([]);

    const state = reactive({
      pageSearchValue: '',
      collapseValue: [0, 1],
      currentNodeData: {},
    });

    const formatTreeData = (data, parentId, id) => {
      const orginObj = { 0: { id: ROOT_ID, name: '站点根目录', children: [] } };
      const treeArr = [];

      data.forEach(item => {
        orginObj[item[id]] = item;
        if (item.parentId === ROOT_ID) {
          orginObj[ROOT_ID].children.push(item);
        }
      });

      data.forEach(item => {
        let parentObj = orginObj[item[parentId]];
        if (parentObj && parentObj.id !== ROOT_ID) {
          parentObj.children = parentObj.children || [];
          parentObj.children.push(item);
        } else if (parentObj && parentObj.id === ROOT_ID) {
          treeArr.push(item);
        }
      });

      pageSettingState.treeDataMapping = orginObj;

      return pageSettingState.treeDataMapping;
    };

    const refreshPageList = async (appId, data) => {
      const pagesData = data ? data : await fetchPageList(appId);

      const firstGroupData = { groupName: '静态页面', groupId: 0, data: [] };
      const secondGroupData = { groupName: '公共页面', groupId: 1, data: [] };

      pagesData.forEach(item => {
        if (item.parentId === undefined || item.parentId === null || item.parentId === '') {
          item.parentId = ROOT_ID;
        }
        const node = item.meta
          ? {
              ...item,
              ...item.meta,
              name: item.fileName,
              isPage: true,
              isBody: item.meta.rootElement === ELEMENT_TAG.Body,
            }
          : item.name
          ? item
          : { ...item, name: item.folderName, group: 'staticPages' };

        const { children, ...other } = node;

        if (node.group === 'staticPages') {
          firstGroupData.data.push(other);
        } else {
          secondGroupData.data.push(other);
        }
      });

      const firstGroupTreeData = formatTreeData([...firstGroupData.data], 'parentId', 'id');
      firstGroupData.data = firstGroupTreeData[ROOT_ID].children;
      pageSettingState.pages = [firstGroupData, secondGroupData];
      pagesDataRef.value = pageSettingState.pages;
      return pageSettingState.pages;
    };

    pageSettingState.updateTreeData = async () => {
      const pageList = await refreshPageList(appInfoState.selectedId);
      return pageList;
    };

    const clearCurrentState = () => {
      pageState.currentVm = null;
      pageState.hoverVm = null;
      pageState.properties = {};
      pageState.pageSchema = null;
    };

    const updateUrlPageId = id => {
      const url = new URL(window.location);

      url.searchParams.delete('blockid');
      url.searchParams.set('pageid', id);
      window.history.pushState({}, '', url);
    };

    const getPageDetail = pageId => {
      fetchPageDetail(pageId).then(data => {
        updateUrlPageId(pageId);
        closePageSettingPanel();
        closeFolderSettingPanel();
        useLayout().closePlugin();
        useLayout().layoutState.pageStatus = getCanvasStatus(data.occupier);
        initData(data['page_content'], data);
      });
    };

    const switchPage = data => {
      pageState.hoverVm = null;
      state.currentNodeData = data;
      let pageName = 'untitle';
      if (data.isPage) {
        pageName = data?.name || 'untitle';
      }
      setBreadcrumbPage([pageName]);

      // 切换页面时清空 选中节点信息状态
      clearCurrentState();
      getPageDetail(data.id);
    };

    const nodeClick = (e, nodeData) => {
      console.log('e', nodeData);
      e.stopPropagation();

      const { id, isPage } = nodeData;

      // 区块切换回页面需要重新加载页面
      if ((!isBlock() && id === state?.currentNodeData?.id) || !isPage) {
        return;
      }

      if (isSaved() && isCurrentDataSame()) {
        switchPage(nodeData);
      } else {
        confirm({
          title: '提示',
          message: `${isBlock() ? '区块' : '页面'}尚未保存，是否要继续切换?`,
          exec: () => {
            changeTreeData(pageSettingState.oldParentId, pageSettingState.currentPageData.parentId);
            Object.assign(pageSettingState.currentPageData, pageSettingState.currentPageDataCopy);
            switchPage(nodeData);
          },
        });
      }
    };

    const openSettingPanel = (nodeData, isPageLocked) => {
      console.log('openSettingPanel', nodeData);

      if (isPageLocked && nodeData.isPage) {
        const username = nodeData.occupier?.username || '';

        useModal().message({
          message: `您点击的页面被${username}锁定，暂时无法编辑，请联系解锁`,
          status: 'info',
        });

        return;
      }

      if (isEqual(pageSettingState.currentPageData, pageSettingState.currentPageDataCopy)) {
        emit('openSettingPanel', nodeData);
      } else {
        confirm({
          title: '提示',
          message: `当前页面或文件夹${pageSettingState.currentPageData.name}尚未保存，是否要继续切换?`,
          exec: () => {
            changeTreeData(pageSettingState.oldParentId, pageSettingState.currentPageData.parentId);
            Object.assign(pageSettingState.currentPageData, pageSettingState.currentPageDataCopy);
            emit('openSettingPanel', nodeData);
          },
        });
      }
    };

    watchEffect(() => {
      if (appInfoState.selectedId) {
        refreshPageList(appInfoState.selectedId);
      }
    });

    const filterTree = (treeData, value) => {
      if (!value) return treeData;
      if (!Array.isArray(treeData)) return [];
      return treeData
        .map(node => {
          if (node.children) {
            const children = filterTree(node.children, value);
            if (children.length) {
              return { ...node, children };
            }
          }
          return node.name.toLowerCase().indexOf(value.toLowerCase()) !== -1 ? node : null;
        })
        .filter(node => node);
    };

    const searchPageData = value => {
      if (!value) {
        pageSettingState.pages = pagesDataRef.value;
        return;
      }
      console.log('pagesDataRef.value', pagesDataRef.value);
      if (Array.isArray(pagesDataRef?.value)) {
        pageSettingState.pages = pagesDataRef.value.map(item => {
          item.data = filterTree(item.data, value);
          return item;
        });
      }
    };

    const filterPageTreeData = (value, data) => {
      if (!value) return true;
      console.log('value', value, data);
      return data.name?.toLowerCase().indexOf(value?.toLowerCase()) !== -1;
    };

    const getPageTreeRefs = el => {
      if (el) {
        pageTreeRefs.value.push(el);
      }
    };

    const createPublicPage = e => {
      e.stopPropagation();
      e.preventDefault();
      emit('add');
    };

    const expandIcon = <SvgIcon name="text-page-folder-closed" class="folder-icon"></SvgIcon>;

    const shrinkIcon = <SvgIcon name="text-page-folder" class="folder-icon"></SvgIcon>;

    return {
      createPublicPage,
      state,
      switchPage,
      pageSettingState,
      searchPageData,
      refreshPageList,
      filterPageTreeData,
      getPageTreeRefs,
      IconFolderOpened: IconFolderOpened(),
      IconFolderClosed: IconFolderClosed(),
      shrinkIcon,
      expandIcon,
      getCanvasStatus,
      PAGE_STATUS,
      nodeClick,
      openSettingPanel,
    };
  },
};
</script>

<style lang="less" scoped>
.ant-collapse .ant-collapse-content > .ant-collapse-content-box {
  padding: 0;
}

.app-manage-search {
  padding: 8px 10px;
  border-bottom: 1px solid var(--ti-lowcode-page-manage-search-border-color);
}

.page-manage-collapse {
  height: calc(100% - 95px);
  overflow-y: auto;
  .app-manage-public-page {
    position: absolute;
    right: 0;
    cursor: pointer;
    svg {
      font-size: 22px;
    }
  }
  :deep(.tiny-collapse-item__header) {
    &,
    &.is-active {
      &::before {
        border: none;
      }
    }
    .title {
      margin-left: 6px;
    }
  }
}

.app-manage-tree {
  :deep(.label) {
    margin-right: 10px;
    margin-left: 20px;
  }
  :deep(.ant-tree) {
    background: var(--ti-lowcode-page-manage-tree-node-background-color);
    color: var(--ti-lowcode-page-manage-tree-color);

    .ant-tree-treenode {
      &.ant-tree-node-selected {
        .tiny-tree-node-content-wrapper {
          background-color: var(--ti-lowcode-page-manage-page-tree-background-color);
          &:hover {
            background-color: var(--ti-lowcode-page-manage-page-tree-background-hover-color);
          }
        }
      }
    }
    .ant-tree-node-content-wrapper {
      height: 32px;
      &::before {
        content: '';
        width: 12px;
        height: 100%;
        // 页面拖拽功能没上，先不显示拖拽图标
        // background-image: url(data:image/svg+xml;base64,PHN2ZyB2ZXJzaW9uPSIxLjEiIGlkPSLlm77lsYJfMSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayIgeD0iMHB4IiB5PSIwcHgiIHZpZXdCb3g9IjAgMCAxNiAxNiIgc3R5bGU9ImVuYWJsZS1iYWNrZ3JvdW5kOm5ldyAwIDAgMTYgMTY7IiB4bWw6c3BhY2U9InByZXNlcnZlIiB3aWR0aD0iMTMycHgiIGhlaWdodD0iMTMycHgiPgo8c3R5bGUgdHlwZT0idGV4dC9jc3MiPgoJLnN0MHtmaWxsOiNhZGIwYjg7fQo8L3N0eWxlPgo8cGF0aCBpZD0iaWNvbi3np7vliqgiIGNsYXNzPSJzdDAiIGQ9Ik0xMCwzQzkuNCwzLDksMi42LDksMnMwLjQtMSwxLTFjMC42LDAsMSwwLjQsMSwxUzEwLjYsMywxMCwzeiBNNiwzQzUuNCwzLDUsMi42LDUsMiYjMTA7JiM5O3MwLjQtMSwxLTFzMSwwLjQsMSwxUzYuNiwzLDYsM3ogTTEwLDdDOS40LDcsOSw2LjYsOSw2czAuNC0xLDEtMWMwLjYsMCwxLDAuNCwxLDFTMTAuNiw3LDEwLDd6IE02LDdDNS40LDcsNSw2LjYsNSw2czAuNC0xLDEtMSYjMTA7JiM5O3MxLDAuNCwxLDFTNi42LDcsNiw3eiBNMTAsMTFjLTAuNiwwLTEtMC40LTEtMWMwLTAuNiwwLjQtMSwxLTFjMC42LDAsMSwwLjQsMSwxQzExLDEwLjYsMTAuNiwxMSwxMCwxMXogTTYsMTFjLTAuNiwwLTEtMC40LTEtMSYjMTA7JiM5O2MwLTAuNiwwLjQtMSwxLTFzMSwwLjQsMSwxQzcsMTAuNiw2LjYsMTEsNiwxMXogTTEwLDE1Yy0wLjYsMC0xLTAuNC0xLTFzMC40LTEsMS0xYzAuNiwwLDEsMC40LDEsMVMxMC42LDE1LDEwLDE1eiBNNiwxNSYjMTA7JiM5O2MtMC42LDAtMS0wLjQtMS0xczAuNC0xLDEtMXMxLDAuNCwxLDFTNi42LDE1LDYsMTV6Ii8+Cjwvc3ZnPg==);
        background-size: 12px 32px;
        background-repeat: no-repeat;
        background-position: 3px 3px;
        cursor: ns-resize;
        opacity: 0.35;
        visibility: hidden;
      }
      &:hover {
        background-color: var(--ti-lowcode-page-manage-page-tree-background-color) !important;
        border-radius: 0;
        .icons {
          .setting {
            display: inline-block;
            font-size: 16px;
          }
        }

        &::before {
          visibility: visible;
        }
      }
      .folder-icon {
        color: var(--ti-lowcode-page-manage-content-tips-color);
      }
    }

    .tiny-tree-node__expand-icon:not(.is-leaf) {
      margin-left: 12px;
      margin-right: 6px;
    }
  }
}

:deep(.tiny-svg, #tiny-engine .tiny-svg) {
  transform: rotate(270deg);
  &.is-active {
    transform: rotate(180deg);
  }
}
</style>
