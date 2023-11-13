<template>
  <Popover trigger="hover" :open-delay="1000" popper-class="toolbar-right-popover" append-to-body content="预览页面">
    <span class="icon" @click="preview">
      <Icon :icon="icon"></Icon>
    </span>
  </Popover>
</template>

<script>
import { Popover } from 'ant-design-vue';
import { previewPage, previewBlock } from '@/common/js/preview';
import { getGlobalConfig, useBlock, useCanvas, useLayout, useNotify } from '@/controller';
import { getSchema } from '@/canvas';
import { Icon } from '@begcode/components';

export default {
  components: {
    Icon,
    Popover,
  },
  props: {
    icon: {
      type: String,
      default: 'ant-design:play-circle-outlined',
    },
  },
  setup() {
    const { isBlock, getCurrentPage } = useCanvas();
    const { getCurrentBlock } = useBlock();

    const preview = () => {
      if (useLayout().isEmptyPage()) {
        useNotify({
          type: 'warning',
          title: '请先创建页面',
        });
        return;
      }

      const params = {
        framework: getGlobalConfig()?.dslMode,
        platform: getGlobalConfig()?.platformId,
        pageInfo: {
          schema: getSchema(),
        },
      };

      if (isBlock()) {
        const block = getCurrentBlock();
        params.id = block?.id;
        params.pageInfo.name = block?.label;
        previewBlock(params);
      } else {
        const page = getCurrentPage();
        params.id = page?.id;
        params.pageInfo.name = page?.name;
        previewPage(params);
      }
    };

    return {
      preview,
    };
  },
};
</script>
