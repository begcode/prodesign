import { VxeGridProps } from 'vxe-table';
import { VxeGridPropTypes } from 'vxe-table/types/grid';
import dayjs from 'dayjs';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！-->

const searchForm = (): any[] => {
  return [
    {
      title: 'ID',
      field: 'id',
      componentType: 'Text',
      value: '',
      type: 'Long',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '分类名称',
      field: 'categoryName',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '分类Key',
      field: 'categoryKey',
      componentType: 'Text',
      value: '',
      type: 'String',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '是否禁用',
      field: 'disabled',
      componentType: 'Switch',
      value: '',
      operator: '',
      span: 8,
      type: 'Boolean',
      componentProps: {},
    },
    {
      title: '排序',
      field: 'sortValue',
      componentType: 'Text',
      value: '',
      type: 'Integer',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '是否内置',
      field: 'builtIn',
      componentType: 'Switch',
      value: '',
      operator: '',
      span: 8,
      type: 'Boolean',
      componentProps: {},
    },
    {
      title: '创建者Id',
      field: 'createdBy',
      componentType: 'Text',
      value: '',
      type: 'Long',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '创建时间',
      field: 'createdDate',
      componentType: 'DateTime',
      operator: '',
      span: 8,
      type: 'ZonedDateTime',
      componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
    },
    {
      title: '修改者Id',
      field: 'lastModifiedBy',
      componentType: 'Text',
      value: '',
      type: 'Long',
      operator: '',
      span: 8,
      componentProps: {},
    },
    {
      title: '修改时间',
      field: 'lastModifiedDate',
      componentType: 'DateTime',
      operator: '',
      span: 8,
      type: 'ZonedDateTime',
      componentProps: { type: 'date', format: 'YYYY-MM-DD hh:mm:ss', style: 'width: 100%' },
    },
  ];
};

const columns = (): VxeGridPropTypes.Columns => {
  return [
    {
      fixed: 'left',
      type: 'checkbox',
      width: 60,
    },
    {
      title: 'ID',
      field: 'id',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'LONG' },
      editRender: { name: 'AInputNumber', enabled: false },
    },
    {
      title: '分类名称',
      field: 'categoryName',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '分类Key',
      field: 'categoryKey',
      minWidth: 160,
      visible: true,
      treeNode: false,
      params: { type: 'STRING' },
      editRender: { name: 'AInput', enabled: false },
    },
    {
      title: '是否禁用',
      field: 'disabled',
      minWidth: 70,
      visible: true,
      treeNode: false,
      params: { type: 'BOOLEAN' },
      cellRender: { name: 'ASwitch', props: { disabled: false } },
    },
    {
      title: '排序',
      field: 'sortValue',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'INTEGER' },
      editRender: { name: 'AInputNumber', enabled: false },
    },
    {
      title: '是否内置',
      field: 'builtIn',
      minWidth: 70,
      visible: true,
      treeNode: false,
      params: { type: 'BOOLEAN' },
      cellRender: { name: 'ASwitch', props: { disabled: false } },
    },
    {
      title: '创建者Id',
      field: 'createdBy',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'LONG' },
      editRender: { name: 'AInputNumber', enabled: false },
    },
    {
      title: '创建时间',
      field: 'createdDate',
      minWidth: 140,
      visible: true,
      treeNode: false,
      params: { type: 'ZONED_DATE_TIME' },
      formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
    },
    {
      title: '修改者Id',
      field: 'lastModifiedBy',
      minWidth: 80,
      visible: true,
      treeNode: false,
      params: { type: 'LONG' },
      editRender: { name: 'AInputNumber', enabled: false },
    },
    {
      title: '修改时间',
      field: 'lastModifiedDate',
      minWidth: 140,
      visible: true,
      treeNode: false,
      params: { type: 'ZONED_DATE_TIME' },
      formatter: ({ cellValue }) => (cellValue ? dayjs(cellValue).format('YYYY-MM-DD hh:mm:ss') : ''),
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

const baseGridOptions = (): VxeGridProps => {
  return {
    rowConfig: {
      keyField: 'id',
      isHover: true,
    },
    border: true,
    showHeaderOverflow: true,
    showOverflow: true,
    keepSource: true,
    id: 'full_edit_1',
    height: 600,
    printConfig: {
      columns: [
        // { field: 'name' },
      ],
    },
    filterConfig: {
      remote: true,
    },
    columnConfig: {
      resizable: true,
    },
    sortConfig: {
      trigger: 'cell',
      remote: true,
      orders: ['asc', 'desc', null],
      multiple: true,
      defaultSort: {
        field: 'id',
        order: 'desc',
      },
    },
    pagerConfig: {
      layouts: ['Sizes', 'PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'FullJump', 'Total'],
      pageSize: 15,
      pageSizes: [5, 10, 15, 20, 30, 50],
      total: 0,
      pagerCount: 5,
      currentPage: 1,
    },
    importConfig: {},
    exportConfig: {},
    checkboxConfig: {
      // labelField: 'id',
      reserve: true,
      highlight: true,
    },
    editRules: {},
    editConfig: {
      trigger: 'click',
      mode: 'row',
      showStatus: true,
    },
  };
};

export default {
  searchForm,
  columns,
  baseGridOptions,
};
