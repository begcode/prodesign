<template>
  <div class="clearfix">
    <Upload :file-list="fileList" :before-upload="beforeUpload" @remove="handleRemove" :custom-request="requestMethod">
      <Button>
        <upload-outlined />
        选择文件
      </Button>
    </Upload>
    <Modal :open="previewOpen" :footer="null" @cancel="handleCancel()">
      <img alt="example" style="width: 100%" :src="previewImage" />
    </Modal>
  </div>
</template>
<script lang="ts">
import { Upload, Button, Modal } from 'ant-design-vue';
import { UploadOutlined } from '@ant-design/icons-vue';
import { defineComponent, ref } from 'vue';
import type { UploadProps } from 'ant-design-vue';

export default defineComponent({
  name: 'SelectFile',
  components: {
    UploadOutlined,
    Upload,
    Button,
    Modal,
  },
  emits: ['update:value', 'selectFile'],
  setup(props, { emit, refs }) {
    const fileList = ref<UploadProps['fileList']>([]);
    const uploading = ref<boolean>(false);
    //预览图
    const previewImage = ref<string | undefined>('');
    //预览框状态
    const previewOpen = ref<boolean>(false);

    const handleRemove: UploadProps['onRemove'] = file => {
      const index = fileList.value.indexOf(file);
      const newFileList = fileList.value.slice();
      newFileList.splice(index, 1);
      fileList.value = newFileList;
      emit('update:value', fileList.value[0]);
    };

    const beforeUpload: UploadProps['beforeUpload'] = file => {
      fileList.value = [...fileList.value, file];
      emit('selectFile', file);
    };
    const requestMethod: UploadProps['customRequest'] = ({ file, onSuccess }) => {
      console.log('requestMethod', file);
    };

    return {
      requestMethod,
      fileList,
      uploading,
      handleRemove,
      beforeUpload,
      previewImage,
      previewOpen,
    };
  },
});
</script>
<style scoped>
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
