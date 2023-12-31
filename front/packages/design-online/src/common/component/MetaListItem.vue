<template>
  <div :class="['item-content', { 'active-item': currentIndex === index }]" @click="openEdit">
    <div class="option-input">
      <div class="left">
        <icon-more class="tiny-svg-size icon-more"></icon-more>
        <slot name="content" :data="item"></slot>
      </div>
      <div class="right">
        <slot name="operate" :data="item">
          <template v-for="item in dataScource.btnList" :key="item.type">
            <template v-if="item.type === 'edit'">
              <Popover
                :key="itemData.index"
                v-model:open="isVisible"
                placement="left"
                :overlay-class-name="`option-popper${expand ? ' fixed-left' : ''}`"
                :visible-arrow="!expand"
                title=""
                width="267"
                trigger="manual"
                @hide="hide(item)"
              >
                <template #content>
                  <template v-if="isVisible">
                    <div ref="addOptions" class="add-options" :style="`left:${itemData.left}px;top:${itemData.top}px`">
                      <icon-close class="tiny-svg-size icon-close" @click="closeEditOption"></icon-close>
                      <slot name="metaForm">
                        <meta-form
                          v-bind="itemData"
                          :footerbtnHide="true"
                          @changeItem="change"
                          @cancel="cancel"
                          @confirm="formConfirm"
                        ></meta-form>
                      </slot>
                      <slot name="footer"></slot></div
                  ></template>
                </template>
                <Tooltip class="item" effect="dark" :title="item.title" placement="top">
                  <icon-edit class="tiny-svg-size icon-edit" @click="btnClick($event, item.type)"></icon-edit>
                </Tooltip>
              </Popover>
            </template>
            <template v-else>
              <Tooltip class="item" effect="dark" :title="item.title" placement="top" @click="btnClick($event, item.type)">
                <span class="item-icon">
                  <component :is="item.icon"></component>
                </span>
              </Tooltip>
            </template>
          </template>
        </slot>
      </div>
    </div>
  </div>
  <!-- 删除页面弹窗 -->
  <Modal v-if="isShow" :append-to-body="true" :open="isShow" title="提示" width="20%" @update:open="isShow = $event">
    <span class="switch-tip">
      <span>
        <svg-icon name="warning"></svg-icon>
      </span>
      <span>确定删除吗？</span>
    </span>
    <template #footer>
      <Button @click="isShow = false">取消</Button>
      <Button type="danger" @click="confirm">删除</Button>
    </template>
  </Modal>
  <!-- 遮罩层 -->
  <mask-modal :visible="showMask && !expand" @close="closeMask"></mask-modal>
</template>

<script>
import { reactive, watchEffect, ref, onMounted } from 'vue';
import { Tooltip, Input, FormItem, Form, Popover, Modal, Button } from 'ant-design-vue';
import { iconDel, iconEdit, iconMore, iconClose } from '@opentiny/vue-icon';
import MaskModal from './MaskModal.vue';
import MetaForm from './MetaForm.vue';

export default {
  components: {
    Tooltip,
    Input,
    FormItem,
    Form,
    Popover,
    Modal,
    Button,
    MetaForm,
    MaskModal,
    IconDel: iconDel(),
    IconEdit: iconEdit(),
    IconMore: iconMore(),
    IconClose: iconClose(),
  },
  inheritAttrs: false,
  props: {
    item: {
      type: Object,
      default: () => {},
    },
    dataScource: {
      type: Object,
      default: () => {},
    },
    index: {
      type: Number,
      default: 0,
    },
    currentIndex: {
      type: Number,
      default: -1,
    },
    // 使用itemClick判断是否由双击触发弹出面板
    itemClick: {
      type: Boolean,
      default: false,
    },
    // 子级弹出层是否在左侧展开
    expand: {
      type: Boolean,
      default: false,
    },
  },
  emits: ['editItem', 'changeItem', 'deleteItem', 'hide'],
  setup(props, { emit }) {
    const itemData = reactive({});
    const isShow = ref(false);
    const isVisible = ref(false);
    const showMask = ref(false);
    let top = ref(0);

    const deleteItem = () => {
      isShow.value = true;
    };

    const editList = () => {
      showMask.value = true;
      isVisible.value = true;
    };

    const btnClick = ($event, action) => {
      switch (action) {
        case 'delete':
          deleteItem();
          break;
        case 'openModal':
          emit('editItem', { action: 'openModal', index: props.index });
          break;
        case 'edit':
          emit('editItem', { action: 'edit', index: props.index });
          editList();
          break;
        default:
          emit('editItem', { index: props.index });
          editList();
          break;
      }
    };

    const change = () => {
      emit('changeItem', itemData);
    };

    const confirm = () => {
      isShow.value = false;

      emit('deleteItem', itemData);
    };

    const closeEditOption = () => {
      isVisible.value = false;
      showMask.value = false;
    };

    const hide = () => {
      emit('hide');
    };

    const isShowModal = ref(false);

    const cancel = () => {
      isVisible.value = false;
    };

    const formConfirm = formData => {
      emit('changeItem', { data: formData, index: props.index });
      isVisible.value = false;
    };

    watchEffect(() => {
      itemData.option = props.item;
      itemData.textField = props.dataScource.textField;
      itemData.valueField = props.dataScource.valueField;
      itemData.label = props.dataScource.label;
      itemData.index = props.index;

      if (props.currentIndex !== props.index) {
        cancel();
      }
    });

    const openEdit = () => {
      if (props.itemClick && !isShow.value) {
        editList();
      }
    };

    const closeMask = () => {
      showMask.value = false;
      isVisible.value = false;
    };

    onMounted(() => {
      if (props.currentIndex === props.index) {
        editList();
      }
    });

    return {
      itemData,
      change,
      deleteItem,
      closeEditOption,
      btnClick,
      hide,
      isShow,
      confirm,
      editList,
      isVisible,
      showMask,
      closeMask,
      top,
      isShowModal,
      formConfirm,
      cancel,
      openEdit,
    };
  },
};
</script>

<style lang="less" scoped>
.item-content {
  border: 1px solid var(--ti-lowcode-meta-list-item-border-color);
  border-left: none;
  border-right: none;
  background: var(--ti-lowcode-meta-list-item-bg-color);
  margin-bottom: -1px;
  color: var(--ti-lowcode-toolbar-breadcrumb-color);
  &.active-item {
    background-color: var(--ti-lowcode-tabs-active-bg);
  }
  .option-input {
    display: flex;
    padding: 2px;
    & > div {
      overflow: hidden;
      .tiny-svg {
        margin-right: 5px;
        font-size: 12px;
        opacity: 0.4;
        color: var(--ti-lowcode-toolbar-breadcrumb-color);
        &:hover {
          cursor: pointer;
          opacity: 1;
        }
      }

      &.left {
        flex: 1;
        display: flex;
        align-items: center;
        .icon-more {
          font-size: 14px;
          flex-shrink: 0;
          cursor: move;
        }
        :deep(span) {
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
      &.right {
        float: left;
        text-align: right;
      }
    }
  }
}
.tiny-popover {
  .icon-close {
    float: right;
  }
}
.add-options {
  overflow-y: scroll;
  height: 100%;
  &::-webkit-scrollbar-track-piece {
    background: var(--ti-lowcode-toolbar-bg);
  }
  &::-webkit-scrollbar {
    width: 5px;
  }
}
</style>
