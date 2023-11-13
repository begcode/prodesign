import { defineComponent, reactive, ref } from 'vue';
import { Card, Col, Divider, Dropdown, Input, Menu, MenuItem, message, Modal, Row, Space, Tree } from 'ant-design-vue';
import { DownOutlined, UpOutlined, UserOutlined } from '@ant-design/icons-vue';
import { VxeGrid, VxeGridInstance, VxeGridListeners, VxeGridProps } from 'vxe-table';
import { SearchForm, Icon, useModalInner, BasicModal, useDrawerInner, BasicDrawer, Button } from '@begcode/components';
import SmsConfigEdit from './jdl-field-edit.vue';
import SmsConfigDetail from './jdl-field-detail.vue';
import config from './config/list-config';

// begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！

export default defineComponent({
  name: 'JdlFieldList',
  components: {
    'a-card': Card,
    'a-button': Button,
    'a-space': Space,
    'a-divider': Divider,
    'a-tree': Tree,
    'a-col': Col,
    'a-input': Input,
    'a-menu-item': MenuItem,
    'a-menu': Menu,
    'a-dropdown': Dropdown,
    'a-row': Row,
    'vxe-grid': VxeGrid,
    SearchForm,
    DownOutlined,
    UpOutlined,
    UserOutlined,
    Icon,
    BasicModal,
    BasicDrawer,
    SmsConfigEdit,
    SmsConfigDetail,
  },
  props: {
    query: {
      type: Object,
      default: () => ({}),
    },
    editIn: {
      type: String,
      default: '',
    },
    baseData: {
      type: Object,
      default: () => ({}),
    },
    gridOptions: {
      type: Object,
      default: () => ({
        toolbarConfig: {
          import: true,
          print: true,
          export: true,
        },
      }),
    },
    searchFormOptions: {
      type: Object,
      default: () => ({
        disabled: false,
      }),
    },
    gridCustomConfig: {
      type: Object,
      default: () => ({
        tools: [''],
        hideOperations: false,
        hideSlots: [],
        columnsName: '',
      }),
    },
  },
  async setup(props) {
    const [registerModal, { closeModal, setModalProps }] = useModalInner(data => {
      console.log(data);
    });
    const [registerDrawer, { closeDrawer, setDrawerProps }] = useDrawerInner(data => {
      console.log(data);
    });
    const modalComponentRef = ref<any>(null);
    const drawerComponentRef = ref<any>(null);
    const relationshipApis: any = {};
    const apis = {};
    const pageConfig = {
      title: '短信配置列表',
      baseRouteName: 'systemSmsConfig',
    };
    const columns = config.columns();
    const searchFormFields = config.searchForm();
    if (columns && columns.length && Object.keys(relationshipApis).length) {
      columns
        .filter(item => item.field && Object.keys(relationshipApis).includes(item.field))
        .forEach(item => {
          item.editRender = {
            ...item.editRender,
            props: {
              ...item.editRender?.props,
              api: relationshipApis[item.field!],
            },
          };
        });
    }
    const showRecycleBin = ref<Boolean>(false);
    const xGrid = ref({} as VxeGridInstance);
    const searchInput = ref(null);
    const searchFormConfig = reactive(
      Object.assign(
        {
          fieldList: searchFormFields,
          toggleSearchStatus: false,
          useOr: false,
          disabled: false,
          allowSwitch: true,
        },
        props.searchFormOptions,
      ),
    );
    const batchOperations = [];
    const rowOperations = [
      {
        disabled: false,
        name: 'save',
        type: 'icon',
      },
      {
        name: 'delete',
        type: 'icon',
      },
      {
        title: '详情',
        name: 'detail',
        containerType: 'drawer',
        type: 'icon',
      },
    ];
    const tableRowOperations = reactive<any[]>([]);
    const tableRowMoreOperations = reactive<any[]>([]);
    const saveOperation = rowOperations.find(operation => operation.name === 'save');
    if (rowOperations.length > 4 || (saveOperation && rowOperations.length > 3)) {
      if (saveOperation) {
        tableRowOperations.push(...rowOperations?.slice(0, 2));
        tableRowMoreOperations.push(...rowOperations.slice(3));
      } else {
        tableRowOperations.push(...rowOperations?.slice(0, 3));
        tableRowMoreOperations.push(...rowOperations.slice(4));
      }
    } else {
      tableRowOperations.push(...rowOperations);
    }
    const selectedRows = reactive<any>([]);
    const loading = ref(false);
    const searchFormRef = ref<any>(null);
    const searchValue = ref('');
    const mapOfFilter = reactive({});
    const mapOfSort: { [key: string]: any } = reactive({});
    columns?.forEach(column => {
      if (column.sortable && column.field) {
        mapOfSort[column.field] = false;
      }
    });
    const sort = () => {
      const result: any[] = [];
      Object.keys(mapOfSort).forEach(key => {
        if (mapOfSort[key] && mapOfSort[key] !== false) {
          if (mapOfSort[key] === 'asc') {
            result.push(key + ',asc');
          } else if (mapOfSort[key] === 'desc') {
            result.push(key + ',desc');
          }
        }
      });
      return result;
    };
    const treeFilterData = [];
    const filterTreeConfig = reactive({
      filterTreeSpan: treeFilterData && treeFilterData.length > 0 ? 6 : 0,
      treeFilterData,
      expandedKeys: [],
      checkedKeys: [],
      selectedKeys: [],
      autoExpandParent: true,
    });
    const modalConfig = reactive({
      componentName: '',
      entityId: '',
      containerType: 'modal',
      baseData: props.baseData,
      width: '80%',
      destroyOnClose: true,
    });
    const drawerConfig = reactive({
      componentName: '',
      containerType: 'drawer',
      entityId: '',
      baseData: props.baseData,
      width: '30%',
      destroyOnClose: true,
    });
    const gridOptions = reactive<VxeGridProps>({
      ...config.baseGridOptions(),
      customConfig: {
        storage: true,
        checkMethod({ column }) {
          return !['nickname', 'role'].includes(column.field);
        },
      },
      proxyConfig: {
        enabled: true,
        autoLoad: true,
        seq: true,
        sort: true,
        filter: true,
        props: {
          result: 'records',
          total: 'total',
        },
        ajax: {
          query: async ({ filters, page, sort, sorts }) => {
            return new Promise<any>(resolve => {
              resolve([
                {
                  fieldName: 'username',
                  fieldTitle: '用户名',
                },
              ]);
            });
          },
          queryAll: async () => await apis.find({ size: -1 }),
          delete: async records => {
            if (showRecycleBin.value) {
              return await apis.reallyDelete(records.body.removeRecords.map(record => record.id));
            } else {
              return await apis.deleteByIds(records.body.removeRecords.map(record => record.id));
            }
          },
        },
      },
      toolbarConfig: {
        custom: false,
        import: false,
        print: false,
        export: false,
        slots: {
          buttons: 'toolbar_buttons',
        },
        // 表格右上角自定义按钮
        tools: [{ code: 'new', name: '新增', circle: false, icon: 'vxe-icon-add' }],
      },
      columns,
    });
    const gridEvents = reactive<VxeGridListeners>({
      checkboxAll: () => {
        const $grid = xGrid.value;
        selectedRows.length = 0;
        selectedRows.push(...$grid.getCheckboxRecords());
      },
      checkboxChange: () => {
        const $grid = xGrid.value;
        selectedRows.length = 0;
        selectedRows.push(...$grid.getCheckboxRecords());
      },
      pageChange({ currentPage, pageSize }) {
        if (gridOptions.pagerConfig) {
          gridOptions.pagerConfig.currentPage = currentPage;
          gridOptions.pagerConfig.pageSize = pageSize;
        }
      },
      radioChange() {
        const $grid = xGrid.value;
        selectedRows.length = 0;
        selectedRows.push($grid.getRadioRecord());
      },
      // 表格左上角按钮事件
      toolbarButtonClick({ code }) {
        switch (code) {
          case 'myInsert': {
            break;
          }
          case 'mySave': {
            break;
          }
          case 'myExport': {
            break;
          }
        }
      },
      // 表格右上角自定义按钮事件
      toolbarToolClick({ code }) {
        switch (code) {
          case 'new':
            if (props.editIn === 'modal') {
              modalConfig.componentName = 'sms-config-edit';
              modalConfig.entityId = '';
              setModalProps({ open: true });
            } else if (props.editIn === 'drawer') {
              drawerConfig.componentName = 'sms-config-edit';
              drawerConfig.entityId = '';
              setDrawerProps({ open: true });
            } else {
              if (pageConfig.baseRouteName) {
              } else {
                console.log('未定义方法');
              }
            }
            break;
        }
      },
      editClosed({ row, column }) {
        const field = column.property;
        const cellValue = row[field];
        // 判断单元格值是否被修改
        if (xGrid.value.isUpdateByRow(row, field)) {
          const entity = { id: row.id };
          entity[field] = cellValue;
          apis
            .update(entity, [row.id], [field])
            .then(() => {
              message.success({
                content: `信息更新成功。 ${field}=${cellValue}`,
                duration: 1,
              });
              xGrid.value.reloadRow(row, null, field);
            })
            .catch(error => {
              console.log('error', error);
              message.error({
                content: `信息保存可能存在问题！ ${field}=${cellValue}`,
                onClose: () => {},
              });
            });
        }
      },
    });
    const okModal = () => {
      if (modalConfig.containerType === 'modal') {
        if (modalComponentRef.value) {
          modalComponentRef.value.saveOrUpdate();
          formSearch();
        }
      }
    };
    const okDrawer = () => {
      if (drawerConfig.containerType === 'drawer') {
        if (drawerComponentRef.value) {
          drawerComponentRef.value.saveOrUpdate();
          formSearch();
        }
      }
    };
    const formSearch = () => {
      xGrid.value.commitProxy('reload');
    };
    return {
      sort,
      searchFormConfig,
      selectedRows,
      loading,
      mapOfFilter,
      mapOfSort,
      filterTreeConfig,
      searchValue,
      xGrid,
      searchInput,
      tableRowOperations,
      tableRowMoreOperations,
      modalConfig,
      registerModal,
      closeModal,
      closeDrawer,
      setModalProps,
      drawerConfig,
      registerDrawer,
      setDrawerProps,
      gridEvents,
      gridOptions,
      batchOperations,
      apis,
      pageConfig,
      searchFormRef,
      showRecycleBin,
      okDrawer,
      okModal,
      formSearch,
      modalComponentRef,
      drawerComponentRef,
    };
  },
  methods: {
    handleToggleSearch(): void {
      this.searchFormConfig.toggleSearchStatus = !this.searchFormConfig.toggleSearchStatus;
    },
    onCheck(checkedKeys) {
      console.log('onCheck', checkedKeys);
      this.filterTreeConfig.checkedKeys = checkedKeys;
    },
    showSearchFormSetting() {
      if (this.searchFormRef) {
        this.searchFormRef.showSettingModal();
      }
    },
    onSelect(selectedKeys, info) {
      const filterData = info.node.dataRef;
      if (filterData.type === 'filterGroup') {
        this.mapOfFilter[info.node.dataRef.key].value = [];
      } else if (filterData.type === 'filterItem') {
        this.mapOfFilter[info.node.dataRef.filterName].value = [info.node.dataRef.filterValue];
      }
      this.formSearch();
      this.filterTreeConfig.selectedKeys = selectedKeys;
    },
    switchFilterTree() {
      this.filterTreeConfig.filterTreeSpan = this.filterTreeConfig.filterTreeSpan > 0 ? 0 : 6;
    },
    rowMoreClick(e, row) {
      const { key } = e;
      const operation = this.tableRowMoreOperations.find(operation => operation.name === key);
      this.rowClickHandler(name, operation, row);
    },
    rowClick(name, row) {
      const operation = this.tableRowOperations.find(operation => operation.name === name);
      this.rowClickHandler(name, operation, row);
    },
    rowClickHandler(name, operation, row) {
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      const _this = this;
      switch (name) {
        case 'save':
          break;
        case 'edit':
          if (operation) {
            if (operation.click) {
              operation.click(row);
            } else {
              const containerType = _this.editIn || operation.containerType;
              switch (containerType) {
                case 'modal':
                  this.modalConfig.componentName = 'sms-config-edit';
                  this.modalConfig.entityId = row.id;
                  this.setModalProps({ open: true });
                  break;
                case 'drawer':
                  this.drawerConfig.componentName = 'sms-config-edit';
                  this.drawerConfig.entityId = row.id;
                  this.setDrawerProps({ open: true });
                  break;
                case 'route':
                default:
                  if (this.pageConfig.baseRouteName) {
                  } else {
                    console.log('未定义方法');
                  }
              }
            }
          } else {
            switch (_this.editIn) {
              case 'modal':
                this.modalConfig.componentName = 'sms-config-edit';
                this.modalConfig.entityId = row.id;
                this.setModalProps({ open: true });
                break;
              case 'drawer':
                this.drawerConfig.componentName = 'sms-config-edit';
                this.drawerConfig.entityId = row.id;
                this.setDrawerProps({ open: true });
                break;
              case 'route':
              default:
                if (this.pageConfig.baseRouteName) {
                } else {
                  console.log('未定义方法');
                }
            }
          }
          break;
        case 'detail':
          if (operation) {
            if (operation.click) {
              operation.click(row);
            } else {
              switch (operation.containerType) {
                case 'modal':
                  this.modalConfig.componentName = 'sms-config-detail';
                  this.modalConfig.entityId = row.id;
                  this.setModalProps({ open: true });
                  break;
                case 'drawer':
                  this.drawerConfig.componentName = 'sms-config-detail';
                  this.drawerConfig.entityId = row.id;
                  this.setDrawerProps({ open: true });
                  break;
                case 'route':
                default:
                  if (this.pageConfig.baseRouteName) {
                  } else {
                    console.log('未定义方法');
                  }
              }
            }
          } else {
            if (this.pageConfig.baseRouteName) {
            } else {
              console.log('未定义方法');
            }
          }
          break;
        case 'delete':
          Modal.confirm({
            title: `操作提示`,
            content: `是否确认删除ID为${row.id}的记录？`,
            onOk() {
              if (operation.click) {
                operation.click(row);
              } else {
                _this.apis.deleteById(row.id).then(() => {
                  _this.formSearch();
                });
              }
            },
          });
          break;
        default:
          if (operation) {
            if (operation.click) {
              operation.click(row);
            } else {
              console.log('error', `click方法未定义`);
            }
          } else {
            console.log('error', `${name}未定义`);
          }
      }
    },
  },
});
