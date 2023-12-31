import { FormSchema } from '@begcode/components';
import { useI18n } from '@/hooks/web/useI18n';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

const fields = (): FormSchema[] => {
  const { getEnumDict } = useI18n();
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
      label: '权限名称',
      field: 'text',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入权限名称', style: 'width: 100%' },
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: 'i18n主键',
      field: 'i18n',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入i18n主键', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '显示分组名',
      field: 'group',
      component: 'Switch',
      componentProps: { placeholder: '请选择显示分组名' },
      rules: [],
    },
    {
      label: '路由',
      field: 'link',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入路由', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '外部链接',
      field: 'externalLink',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入外部链接', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '链接目标',
      field: 'target',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择链接目标', options: getEnumDict('TargetType'), style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '图标',
      field: 'icon',
      component: 'IconPicker',
      componentProps: { placeholder: '请选择图标', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '禁用菜单',
      field: 'disabled',
      component: 'Switch',
      componentProps: { placeholder: '请选择禁用菜单' },
      rules: [],
    },
    {
      label: '隐藏菜单',
      field: 'hide',
      component: 'Switch',
      componentProps: { placeholder: '请选择隐藏菜单' },
      rules: [],
    },
    {
      label: '隐藏面包屑',
      field: 'hideInBreadcrumb',
      component: 'Switch',
      componentProps: { placeholder: '请选择隐藏面包屑' },
      rules: [],
    },
    {
      label: '快捷菜单项',
      field: 'shortcut',
      component: 'Switch',
      componentProps: { placeholder: '请选择快捷菜单项' },
      rules: [],
    },
    {
      label: '菜单根节点',
      field: 'shortcutRoot',
      component: 'Switch',
      componentProps: { placeholder: '请选择菜单根节点' },
      rules: [],
    },
    {
      label: '允许复用',
      field: 'reuse',
      component: 'Switch',
      componentProps: { placeholder: '请选择允许复用' },
      rules: [],
    },
    {
      label: '权限代码',
      field: 'code',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入权限代码', style: 'width: 100%' },
      rules: [{ required: true, message: '必填项' }],
    },
    {
      label: '权限描述',
      field: 'description',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入权限描述', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '权限类型',
      field: 'type',
      component: 'Select',
      componentProps: () => {
        return { placeholder: '请选择权限类型', options: getEnumDict('ViewPermissionType'), style: 'width: 100%' };
      },
      rules: [],
    },
    {
      label: '排序',
      field: 'order',
      component: 'InputNumber',
      componentProps: { placeholder: '请输入排序', style: 'width: 100%' },
      rules: [],
    },
    {
      label: 'api权限标识串',
      field: 'apiPermissionCodes',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入api权限标识串', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '组件名称',
      field: 'componentFile',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入组件名称', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '重定向路径',
      field: 'redirect',
      component: 'Input',
      componentProps: { type: 'text', clearable: true, placeholder: '请输入重定向路径', style: 'width: 100%' },
      rules: [],
    },
    {
      label: '上级',
      field: 'parent',
      component: 'ApiTreeSelect',
      componentProps: {
        api: null,
        style: 'width: 100%',
        labelInValue: true,
        numberToString: true,
        fieldNames: { children: 'children', value: 'id', label: 'text' },
        resultField: 'records',
        placeholder: '请选择上级',
      },
      rules: [],
    },
  ];
};
export default {
  fields,
};
