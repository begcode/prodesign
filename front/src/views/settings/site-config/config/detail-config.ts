import { DescItem } from '@begcode/components';
import { h, resolveComponent } from 'vue';
import { Switch } from 'ant-design-vue';
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
    label: '分类名称',
    field: 'categoryName',
  },
  {
    label: '分类Key',
    field: 'categoryKey',
  },
  {
    label: '是否禁用',
    field: 'disabled',
    render: (value, data) =>
      h(Switch, {
        disabled: true,
        checked: value,
        onChange: checked => {
          data.disabled = checked;
        },
      }),
  },
  {
    label: '排序',
    field: 'sortValue',
  },
  {
    label: '是否内置',
    field: 'builtIn',
    render: (value, data) =>
      h(Switch, {
        disabled: true,
        checked: value,
        onChange: checked => {
          data.builtIn = checked;
        },
      }),
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
  {
    label: '配置项列表',
    field: 'items',
    render: (value, _data) => h(resolveComponent('vxe-grid'), { disabled: true, data: value, columns: {} }),
  },
];
const itemsColumns = [
  {
    label: 'ID',
    field: 'id',
    show: values => {
      return values && values.id;
    },
  },
  {
    label: '名称',
    field: 'name',
  },
  {
    label: '字段值',
    field: 'value',
  },
  {
    label: '字段标题',
    field: 'label',
  },
  {
    label: '字段类型',
    field: 'valueType',
    format: (value, _data) => {
      return getEnumDict('CommonFieldType').find(item => item.value === value) || value;
    },
  },
  {
    label: '说明',
    field: 'remark',
  },
  {
    label: '排序',
    field: 'sortValue',
  },
  {
    label: '是否禁用',
    field: 'disabled',
    render: (value, data) =>
      h(Switch, {
        disabled: true,
        checked: value,
        onChange: checked => {
          data.disabled = checked;
        },
      }),
  },
  {
    label: '实体名称',
    field: 'ownerEntityName',
  },
  {
    label: '使用实体ID',
    field: 'ownerEntityId',
  },
];

export default {
  fields,
  itemsColumns,
};
