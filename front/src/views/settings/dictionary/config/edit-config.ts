import { FormSchema } from '@begcode/components';
import { useI18n } from '@/hooks/web/useI18n';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (): FormSchema[] => {
  return [
    {
      label: 'ID',
      field: 'id',
      show: ({ values }) => {
        return values && values.id;
      },
      dynamicDisabled: true,
      component: 'InputNumber',
      componentProps: { placeholder: '请输入ID', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '字典名称',
      field: 'dictName',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入字典名称', style: 'width: 100%' },
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: '字典Key',
      field: 'dictKey',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入字典Key', style: 'width: 100%' },
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: '是否禁用',
      field: 'disabled',
      component: 'Switch',
      componentProps: { placeholder: '请选择是否禁用' },
      rules: [],
    },
    {
      label: '排序',
      field: 'sortValue',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入排序', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '是否内置',
      field: 'builtIn',
      show: false,
      component: 'Switch',
      componentProps: { placeholder: '请选择是否内置' },
      rules: [],
    },
    {
      label: '更新枚举',
      field: 'syncEnum',
      show: false,
      component: 'Switch',
      componentProps: { placeholder: '请选择更新枚举' },
      rules: [],
    },
  ];
};
const itemsColumns = () => {
  const { getEnumDict } = useI18n();
  return [
    {
      fixed: 'left',
      type: 'checkbox',
      width: 60,
    },
    {
      title: '名称',
      field: 'name',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: true },
    },
    {
      title: '字段值',
      field: 'value',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: true },
    },
    {
      title: '字段标题',
      field: 'label',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: true },
    },
    {
      title: '字段类型',
      field: 'valueType',
      minWidth: 100,
      visible: true,
      treeNode: false,
      params: { type: 'ENUM' },
      formatter: ({ cellValue }) => {
        return (getEnumDict('CommonFieldType').find(item => item.value === cellValue) || { label: cellValue }).label;
      },
      editRender: { name: 'ASelect', props: { options: getEnumDict('CommonFieldType') }, enabled: true },
    },
    {
      title: '说明',
      field: 'remark',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: true },
    },
    {
      title: '排序',
      field: 'sortValue',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
      editRender: { name: 'AInputNumber', enabled: true },
    },
    {
      title: '是否禁用',
      field: 'disabled',
      minWidth: 70,
      visible: true,
      treeNode: false,
      params: { type: 'BOOLEAN' },
      cellRender: { name: 'ASwitch', props: { disabled: true } },
    },
    {
      title: '实体名称',
      field: 'ownerEntityName',
      minWidth: 160,
      visible: false,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: true },
    },
    {
      title: '使用实体ID',
      field: 'ownerEntityId',
      minWidth: 160,
      visible: false,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: true },
    },
    {
      title: '操作',
      field: 'operation',
      fixed: 'right',
      width: 140,
      slots: { default: 'recordAction' },
    },
  ];
};
export default {
  fields,
  itemsColumns,
};
