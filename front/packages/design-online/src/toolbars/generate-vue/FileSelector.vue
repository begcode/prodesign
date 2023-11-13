<template>
  <Modal
    class="dialog-box"
    :open="visible"
    :close-on-click-modal="false"
    :append-to-body="true"
    width="800"
    title="请选择生成到本地的文件"
    @close="$emit('cancel')"
    @open="openDialog"
  >
    <div class="dialog-grid">
      <Table
        :dataSource="tableData"
        :columns="columns"
        :max-height="500"
        :tree-config="{ children: 'children' }"
        :auto-resize="true"
        :row-selection="rowSelection"
        row-key="filePath"
        ref="gridRef"
        size="mini"
      ></Table>
    </div>
    <template #footer>
      <Button type="primary" @click="confirm">确定</Button>
      <Button @click="$emit('cancel')">取消</Button>
    </template>
  </Modal>
</template>

<script>
import { Modal, Button, Table, TableColumn } from 'ant-design-vue';
import { reactive, computed, ref, nextTick } from 'vue';
import { useNotify } from '@/controller';

export default {
  components: {
    Modal,
    Button,
    Table,
    TinyGridColumn: TableColumn,
  },
  props: {
    visible: { type: Boolean, default: false },
    data: {
      type: Array,
      default: () => [],
    },
  },
  emits: ['cancel', 'confirm'],
  setup(props, { emit }) {
    const getTableTreeData = data => {
      const dataMap = {};
      data.forEach(item => {
        if (!dataMap[item.fileType]) {
          dataMap[item.fileType] = { fileType: item.fileType, children: [] };
        }
        dataMap[item.fileType].children.push(item);
      });

      return Object.values(dataMap);
    };

    const tableData = computed(() => getTableTreeData(props.data));
    const gridRef = ref(null);

    const state = reactive({
      selectedRows: [],
    });

    const confirm = () => {
      const selectedData = state.selectedRows.filter(item => !item.children);
      if (!selectedData?.length) {
        useNotify({
          type: 'error',
          title: '生成代码失败',
          message: '选中列表为空，请先选择需要生成到本地的文件再点击确定按钮.',
        });
        return;
      }

      emit('confirm', selectedData);
    };

    const openDialog = () => {
      nextTick(() => {
        gridRef.value.setAllTreeExpansion(true);
        gridRef.value.setAllSelection(true);
      });
    };

    const rowSelection = ref({
      checkStrictly: true,
      onChange: (selectedRowKeys, selectedRows) => {
        state.selectedRows = selectedRows;
      },
      onSelect: (record, selected, selectedRows) => {
        state.selectedRows = selectedRows;
      },
      onSelectAll: (selected, selectedRows, changeRows) => {
        state.selectedRows = selectedRows;
      },
    });

    const columns = ref([
      {
        title: '文件类型',
        dataIndex: 'fileType',
        key: 'fileType',
        width: 180,
      },
      {
        title: '文件路径',
        dataIndex: 'filePath',
        key: 'filePath',
        width: 250,
      },
      {
        title: '文件内容',
        dataIndex: 'fileContent',
        key: 'fileContent',
        ellipsis: true,
      },
    ]);

    return {
      state,
      tableData,
      gridRef,
      confirm,
      openDialog,
      rowSelection,
      columns,
    };
  },
};
</script>

<style lang="less" scoped>
.dialog-box {
  :deep(.tiny-dialog-box__content) {
    background-color: var(--ti-lowcode-common-component-bg);

    .tiny-dialog-box__header {
      background-color: var(--ti-lowcode-common-component-bg);

      .tiny-dialog-box__title {
        color: var(--ti-lowcode-toolbar-breadcrumb-color);
      }

      .tiny-dialog-box__headerbtn .tiny-dialog-box__close {
        fill: var(--ti-lowcode-toolbar-breadcrumb-color) !important;

        &:hover {
          fill: var(--ti-lowcode-common-primary-text-color) !important;
        }
      }
    }

    .tiny-dialog-box__footer {
      .tiny-button--primary {
        background-color: var(--ti-lowcode-common-danger-color, #191919;);
        border: none;
      }
    }
  }

  .dialog-grid {
    :deep(.tiny-grid-tree__indent) {
      width: 0 !important;
    }

    :deep(.tiny-grid-cell) {
      position: relative;
    }

    :deep(.tiny-grid-tree-wrapper) {
      position: relative;
      right: -35px;
      top: 2px;
    }

    :deep(.tiny-grid) {
      .tiny-grid__header-wrapper {
        background-color: var(--ti-lowcode-toolbar-view-hover-bg);

        .tiny-grid-header__column {
          color: var(--ti-lowcode-toolbar-breadcrumb-color);
          height: 30px;

          .tiny-grid-resizable.is__line:before {
            background-color: var(--ti-lowcode-common-component-bg);
          }
        }

        .tiny-grid__repair {
          border-color: var(--ti-lowcode-tabs-border-color);
        }

        .tiny-grid-checkbox__icon {
          svg {
            color: var(--ti-lowcode-common-primary-color);
          }
        }
      }

      .tiny-grid__body-wrapper {
        &::-webkit-scrollbar {
          height: 10px;
        }

        &::-webkit-scrollbar-track-piece {
          background: unset;
        }

        .tiny-grid-tree-wrapper {
          margin-left: -13px;
          padding-right: 5px;
        }

        .tiny-grid-body__column {
          height: 32px;
          padding-left: 11px;
        }

        .tiny-grid-body__row {
          background-color: var(--ti-lowcode-common-component-bg);
        }

        .tiny-grid-body__row,
        .tiny-grid-body__row:not(.row__hover):nth-child(2n) {
          background-image: linear-gradient(
            -180deg,
            var(--ti-lowcode-new-table-row-sepline-background),
            var(--ti-lowcode-new-table-row-sepline-background)
          );
          background-repeat: no-repeat;
          background-size: 100% 1px;
          background-position: 100% 100%;

          &.row__current {
            background-color: var(--ti-lowcode-toolbar-view-hover-bg);
          }
        }

        .tiny-grid-body__row {
          &.row__selected {
            .tiny-grid-checkbox__icon {
              svg {
                color: var(--ti-lowcode-common-primary-color);
                width: 100%;
                height: 100%;
              }
            }
          }
        }
      }

      .tiny-grid__empty-text {
        color: var(--ti-lowcode-toolbar-breadcrumb-color);
      }
    }
  }
}
</style>
