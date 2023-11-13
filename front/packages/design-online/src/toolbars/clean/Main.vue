<template>
  <div class="toolbar-itm-clean">
    <tiny-popover trigger="hover" :open-delay="1000" popper-class="toolbar-right-popover" append-to-body content="清除屏幕">
      <span :class="['icon', { disabled: isLock }]" @click="clean">
        <Icon :icon="icon"></Icon>
      </span>
    </tiny-popover>
  </div>
</template>

<script lang="jsx">
import { ref, watch, getCurrentInstance } from 'vue';
import { Popover, Modal } from 'ant-design-vue';
import { useCanvas, useLayout } from '@/controller';
import { constants } from '@/utils';
import { Icon } from '@begcode/components';

const { PAGE_STATUS } = constants;
export default {
  components: {
    Icon,
    TinyPopover: Popover,
  },
  props: {
    icon: {
      type: String,
      default: 'ant-design:clear-outlined',
    },
  },
  setup() {
    const app = getCurrentInstance();
    const SvgIcon = app.appContext.components.SvgIcon;
    const { pageState, clearCanvas } = useCanvas();
    const isLock = ref(pageState.isLock);

    watch(
      () => pageState.isLock,
      // eslint-disable-next-line no-return-assign
      value => (isLock.value = value),
    );

    const clean = () => {
      if (![PAGE_STATUS.Occupy, PAGE_STATUS.Guest].includes(useLayout().layoutState?.pageStatus?.state)) {
        return;
      }

      if (!isLock.value) {
        Modal.confirm({
          title: '提示',
          message: () => {
            return [
              <div class="modal-content">
                {
                  <div class="wrap">
                    <SvgIcon name="warning"></SvgIcon>
                    <span>{`您确定要清除屏幕吗？`}</span>
                  </div>
                }
              </div>,
            ];
          },
        }).then(res => {
          if (res === 'confirm') {
            clearCanvas();
          }
        });
      }
    };

    return {
      clean,
      isLock,
    };
  },
};
</script>
