<template>
  <div :class="[prefixCls, { fullscreen }]">
    <Upload
      name="file"
      multiple
      @change="handleChange"
      :action="uploadUrl"
      :showUploadList="false"
      :data="getBizData()"
      :headers="getheader()"
      accept=".jpg,.jpeg,.gif,.png,.webp"
    >
      <a-button type="primary" v-bind="{ ...getButtonProps }">
        {{ t('component.upload.imgUpload') }}
      </a-button>
    </Upload>
  </div>
</template>
<script lang="ts">
import { defineComponent, computed } from 'vue';

import { Upload } from 'ant-design-vue';
import { useDesign } from '@/hooks/web/useDesign';
import { useI18n } from '@/hooks/web/useI18nOut';
import { getFileAccessHttpUrl } from '@/utils';

export default defineComponent({
  name: 'TinymceImageUpload',
  components: { Upload },
  props: {
    fullscreen: {
      type: Boolean,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    token: {
      type: String,
      default: '',
    },
    uploadUrl: {
      type: String,
      default: '',
    },
  },
  emits: ['uploading', 'done', 'error'],
  setup(props, { emit }) {
    let uploading = false;

    function getheader() {
      return { Authorization: `Bearer ${props.token}` };
      // return getHeaders();
    }

    function getBizData() {
      return {
        biz: 'jeditor',
        jeditor: '1',
      };
    }

    const { uploadUrl } = props;

    const { t } = useI18n();
    const { prefixCls } = useDesign('tinymce-img-upload');

    const getButtonProps = computed(() => {
      const { disabled } = props;
      return {
        disabled,
      };
    });

    function handleChange(info: Record<string, any>) {
      const file = info.file;
      const status = file?.status;
      // const url = file?.response?.url;
      const name = file?.name;

      if (status === 'uploading') {
        if (!uploading) {
          emit('uploading', name);
          uploading = true;
        }
      } else if (status === 'done') {
        let realUrl = getFileAccessHttpUrl(file.response.message);
        emit('done', name, realUrl);
        uploading = false;
      } else if (status === 'error') {
        emit('error');
        uploading = false;
      }
    }

    return {
      t,
      prefixCls,
      handleChange,
      uploadUrl,
      getheader,
      getBizData,
      getButtonProps,
    };
  },
});
</script>
<style scoped>
.vben-tinymce-img-upload {
  position: absolute;
  top: 4px;
  right: 10px;
  z-index: 20;

  &.fullscreen {
    position: fixed;
    z-index: 10000;
  }
}
</style>
