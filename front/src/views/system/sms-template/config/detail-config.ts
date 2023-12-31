import { DescItem } from '@begcode/components';
import dayjs from 'dayjs';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields: DescItem[] = [
  {
    label: 'ID',
    field: 'id',
    show: values => {
      return values && values.id;
    },
  },
  {
    label: '模板标题',
    field: 'name',
  },
  {
    label: '模板CODE',
    field: 'code',
  },
  {
    label: '模板类型',
    field: 'type',
    format: (value, _data) => {
      return getEnumDict('MessageSendType').find(item => item.value === value) || value;
    },
  },
  {
    label: '模板内容',
    field: 'content',
  },
  {
    label: '模板测试json',
    field: 'testJson',
  },
  {
    label: '创建者Id',
    field: 'createdBy',
  },
  {
    label: '创建时间',
    field: 'createdDate',
    format: (value, _data) => {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
    },
  },
  {
    label: '修改者Id',
    field: 'lastModifiedBy',
  },
  {
    label: '修改时间',
    field: 'lastModifiedDate',
    format: (value, _data) => {
      return value ? dayjs(value).format('YYYY-MM-DD HH:mm:ss') : '';
    },
  },
];

export default {
  fields,
};
