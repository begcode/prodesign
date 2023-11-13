<template>
  <div :class="getClass">
    <template v-if="canFullscreen">
      <Tooltip v-if="fullScreen" :title="t('component.modal.restore')" placement="bottom">
        <FullscreenExitOutlined role="full" @click="handleFullScreen" />
      </Tooltip>
      <Tooltip v-else :title="t('component.modal.maximize')" placement="bottom">
        <FullscreenOutlined role="close" @click="handleFullScreen" />
      </Tooltip>
    </template>
    <Tooltip :title="t('component.modal.close')" placement="bottom">
      <CloseOutlined @click="handleCancel" />
    </Tooltip>
  </div>
</template>
<script lang="ts">
import { defineComponent, computed } from 'vue';
import { Tooltip, theme } from 'ant-design-vue';
import { FullscreenExitOutlined, FullscreenOutlined, CloseOutlined } from '@ant-design/icons-vue';
import { useDesign } from '@/hooks/web/useDesign';
import { useI18n } from '@/hooks/web/useI18nOut';

export default defineComponent({
  name: 'ModalClose',
  components: { Tooltip, FullscreenExitOutlined, FullscreenOutlined, CloseOutlined },
  props: {
    canFullscreen: { type: Boolean, default: true },
    fullScreen: { type: Boolean },
  },
  emits: ['cancel', 'fullscreen'],
  setup(props, { emit }) {
    const { prefixCls } = useDesign('basic-modal-close');
    const { t } = useI18n();

    const getClass = computed(() => {
      return [
        prefixCls,
        `${prefixCls}--custom`,
        {
          [`${prefixCls}--can-full`]: props.canFullscreen,
        },
      ];
    });

    function handleCancel(e: Event) {
      emit('cancel', e);
    }

    function handleFullScreen(e: Event) {
      e?.stopPropagation();
      e?.preventDefault();
      emit('fullscreen');
    }

    const { useToken } = theme;
    const token = useToken();

    return {
      t,
      getClass,
      prefixCls,
      handleCancel,
      handleFullScreen,
      token,
    };
  },
});
</script>
<style>
.vben-basic-modal-close {
  display: flex;
  height: 95%;
  align-items: center;

  > span {
    margin-left: 48px;
    font-size: 16px;
  }

  &--can-full {
    > span {
      margin-left: 12px;
    }
  }

  &:not(&--can-full) {
    > span:nth-child(1) {
      &:hover {
        font-weight: 700;
      }
    }
  }

  & span:nth-child(1) {
    display: inline-block;
    padding: 10px;

    &:hover {
      color: v-bind('token.colorPrimary');
    }
  }

  & span:last-child {
    padding: 10px 10px 10px 0;
    &:hover {
      color: v-bind('token.colorError');
    }
  }
}
</style>
