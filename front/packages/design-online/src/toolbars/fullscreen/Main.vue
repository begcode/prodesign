<template>
  <tiny-popover
    trigger="hover"
    :open-delay="1000"
    popper-class="toolbar-right-popover"
    append-to-body
    :content="!isFullscreen ? '全屏' : '退出全屏'"
  >
    <span class="icon" @click="fullscreen">
      <Icon :icon="iconName"></Icon>
    </span>
  </tiny-popover>
</template>

<script>
import { ref } from 'vue';
import { Popover } from 'ant-design-vue';
import { PublicIcon } from '@/common';
import { Icon } from '@begcode/components';

export default {
  components: {
    Icon,
    TinyPopover: Popover,
    PublicIcon,
  },
  props: {
    icon: {
      type: String,
      default: 'ant-design:fullscreen-outlined',
    },
  },
  setup(props, { emit }) {
    const isFullscreen = ref(false);
    const iconName = ref(props.icon);

    const fullscreen = () => {
      isFullscreen.value = !isFullscreen.value;
      iconName.value = isFullscreen.value ? 'ant-design:fullscreen-exit-outlined' : 'ant-design:fullscreen-outlined';
      document.webkitFullscreenElement ? document.webkitExitFullscreen() : document.documentElement.webkitRequestFullScreen();
    };

    return {
      fullscreen,
      isFullscreen,
      iconName,
    };
  },
};
</script>
