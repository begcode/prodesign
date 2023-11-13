/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

import * as Vue from 'vue';
import * as VueI18n from 'vue-i18n';
import { addScript, addStyle, dynamicImportComponents, updateDependencies } from '../common';
import * as TinyVueIcon from '@opentiny/vue-icon';
import {
  Button,
  ButtonGroup,
  Calendar,
  Card,
  CardGrid,
  CardMeta,
  Collapse,
  CollapsePanel,
  Carousel,
  Cascader,
  Checkbox,
  CheckboxGroup,
  Col,
  Comment,
  ConfigProvider,
  DatePicker,
  MonthPicker,
  WeekPicker,
  RangePicker,
  QuarterPicker,
  Descriptions,
  DescriptionsItem,
  Divider,
  Dropdown,
  DropdownButton,
  Drawer,
  Empty,
  BackTop,
  Form,
  FormItem,
  FormItemRest,
  Grid,
  Input,
  InputGroup,
  InputPassword,
  InputSearch,
  Textarea,
  Image,
  ImagePreviewGroup,
  InputNumber,
  Layout,
  LayoutHeader,
  LayoutSider,
  LayoutFooter,
  LayoutContent,
  List,
  ListItem,
  ListItemMeta,
  Menu,
  MenuDivider,
  MenuItem,
  MenuItemGroup,
  SubMenu,
  Mentions,
  MentionsOption,
  Modal,
  Statistic,
  StatisticCountdown,
  message,
  notification,
  PageHeader,
  Pagination,
  Popconfirm,
  Popover,
  Progress,
  Radio,
  RadioButton,
  RadioGroup,
  Rate,
  Result,
  Row,
  Select,
  SelectOptGroup,
  SelectOption,
  Skeleton,
  SkeletonButton,
  SkeletonAvatar,
  SkeletonInput,
  SkeletonImage,
  SkeletonTitle,
  Slider,
  Space,
  Spin,
  Steps,
  Step,
  Switch,
  Transfer,
  Tree,
  TreeNode,
  DirectoryTree,
  TreeSelect,
  TreeSelectNode,
  Tabs,
  TabPane,
  Tag,
  CheckableTag,
  TimePicker,
  TimeRangePicker,
  Timeline,
  TimelineItem,
  Tooltip,
  Typography,
  TypographyLink,
  TypographyParagraph,
  TypographyText,
  TypographyTitle,
  Upload,
  UploadDragger,
  LocaleProvider,
  Affix,
  Anchor,
  AnchorLink,
  AutoComplete,
  AutoCompleteOptGroup,
  AutoCompleteOption,
  Alert,
  Avatar,
  AvatarGroup,
  Badge,
  BadgeRibbon,
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbSeparator,
} from 'ant-design-vue';
import {
  AccountBookFilled,
  AccountBookOutlined,
  AccountBookTwoTone,
  AimOutlined,
  AlertFilled,
  AlertOutlined,
  AlertTwoTone,
} from '@ant-design/icons-vue';
import {
  // 全局对象
  VXETable,

  // 表格功能
  // Filter,
  // Edit,
  // Menu,
  // Export,
  // Keyboard,
  // Validator,

  // 可选组件
  Icon,
  Column,
  // Colgroup,
  Grid as VxeGrid,
  // Tooltip,
  Toolbar,
  Pager,
  // Form,
  // FormItem,
  // FormGather,
  // Checkbox,
  // CheckboxGroup,
  // Radio,
  // RadioGroup,
  // RadioButton,
  // Switch,
  // Input,
  Select as VxeSelect,
  // Optgroup,
  // Option,
  // Textarea,
  Button as VxeButton,
  // Modal,
  // List,
  // Pulldown,

  // 表格
  Table,
} from 'vxe-table';
import { camelize, capitalize } from '@vue/shared';

const dispatch = (name, data) => {
  window.parent.document.dispatchEvent(new CustomEvent(name, data));
};
dispatch('beforeCanvasReady');

TinyI18nHost.lowcode = lowcode;

// 目前先兼容老区块发布的代码，后期区块发布整改后再删除
window.React = {};
window.React.createElement = Vue.h;

// 不能采用new Proxy代理Vue的方案，在编译后的vue会报错警告，采用一下方案扩展用于注入一些区块加载逻辑
window.Vue = {
  ...Vue,
  resolveComponent(...args) {
    // 此处先执行vue内部的解析组件的方法，如果可以拿到组件对象则直接返回，反之则去注册区块
    const component = Vue.resolveComponent(args[0]);
    if (component && typeof component === 'string') {
      return getComponent(capitalize(camelize(args[0])));
    } else {
      return component;
    }
  },
  // renderSlot方法第三个参数是作用域插槽传递的数据，格式{ data: vue.unref(state).componentData }
  renderSlot(...args) {
    // 获取当前vue的实例
    const instance = Vue.getCurrentInstance();

    // 获取当前区块名称
    const blockName = instance.attrs.dataTag;

    const [, slotName, slotData] = args;

    // 如果是作用域插槽，则获取作用域插槽传递过来的参数
    if (slotData) {
      if (blockSlotDataMap[blockName]) {
        blockSlotDataMap[blockName][slotName] = slotData;
      } else {
        blockSlotDataMap[blockName] = { [slotName]: slotData };
      }
    }

    /**
     * vue源码中的renderSlot会忽略default插槽的名称，所以这里必须手动添加args第三个参数的name值
     * vue源码如右所示：if (name !== 'default') props.name = name; return createVNode('slot', props, fallback && fallback());
     **/
    if (slotName === 'default') {
      args[2] = args[2] || {};
      args[2].name = slotName;
    }

    return Vue.renderSlot(...args);
  },
};

window.VueI18n = VueI18n;
window.TinyVue = {};
window.TinyVueIcon = TinyVueIcon;
window.TinyWebcomponentCore = TinyWebcomponentCore;
window.TinyI18nHost = TinyI18nHost;
window.TinyLowcodeComponent = {};
window.TinyComponentLibs = {};

const allAntvComponents = {
  Button,
  ButtonGroup,
  Calendar,
  Card,
  CardGrid,
  CardMeta,
  Collapse,
  CollapsePanel,
  Carousel,
  Cascader,
  Checkbox,
  CheckboxGroup,
  Col,
  Comment,
  ConfigProvider,
  DatePicker,
  MonthPicker,
  WeekPicker,
  RangePicker,
  QuarterPicker,
  Descriptions,
  DescriptionsItem,
  Divider,
  Dropdown,
  DropdownButton,
  Drawer,
  Empty,
  BackTop,
  Form,
  FormItem,
  FormItemRest,
  Grid,
  Input,
  InputGroup,
  InputPassword,
  InputSearch,
  Textarea,
  Image,
  ImagePreviewGroup,
  InputNumber,
  Layout,
  LayoutHeader,
  LayoutSider,
  LayoutFooter,
  LayoutContent,
  List,
  ListItem,
  ListItemMeta,
  Menu,
  MenuDivider,
  MenuItem,
  MenuItemGroup,
  SubMenu,
  Mentions,
  MentionsOption,
  Modal,
  Statistic,
  StatisticCountdown,
  message,
  notification,
  PageHeader,
  Pagination,
  Popconfirm,
  Popover,
  Progress,
  Radio,
  RadioButton,
  RadioGroup,
  Rate,
  Result,
  Row,
  Select,
  SelectOptGroup,
  SelectOption,
  Skeleton,
  SkeletonButton,
  SkeletonAvatar,
  SkeletonInput,
  SkeletonImage,
  SkeletonTitle,
  Slider,
  Space,
  Spin,
  Steps,
  Step,
  Switch,
  Transfer,
  Tree,
  TreeNode,
  DirectoryTree,
  TreeSelect,
  TreeSelectNode,
  Tabs,
  TabPane,
  Tag,
  CheckableTag,
  TimePicker,
  TimeRangePicker,
  Timeline,
  TimelineItem,
  Tooltip,
  Typography,
  TypographyLink,
  TypographyParagraph,
  TypographyText,
  TypographyTitle,
  Upload,
  UploadDragger,
  LocaleProvider,
  Affix,
  Anchor,
  AnchorLink,
  AutoComplete,
  AutoCompleteOptGroup,
  AutoCompleteOption,
  Alert,
  Avatar,
  AvatarGroup,
  Badge,
  BadgeRibbon,
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbSeparator,
};
const allAntvIcons = {
  AccountBookFilled,
  AccountBookOutlined,
  AccountBookTwoTone,
  AimOutlined,
  AlertFilled,
  AlertOutlined,
  AlertTwoTone,
};
import {
  PageWrapper,
  Description,
  SearchForm,
  BasicForm,
  BasicDrawer,
  BasicModal,
  Button as BButton,
  Icon as BIcon,
} from '@begcode/components';
import lowcode from '../../lowcode';
import { getComponent, blockSlotDataMap } from './render';
import Main, { api } from './RenderMain';
import * as TinyWebcomponentCore from '@/webcomponent/lib';
import TinyI18nHost, { I18nInjectionKey } from '@/common/js/i18n';
const allVxeComponents = {
  // 全局对象
  VXETable,
  Icon,
  Column,
  Table,
  VxeGrid,
  Toolbar,
  Pager,
  VxeSelect,
  VxeButton,
};
const allVbenComponents = {
  PageWrapper,
  Description,
  SearchForm,
  BasicForm,
  BasicDrawer,
  BasicModal,
  BButton,
  BIcon,
};
Object.entries(allVbenComponents).forEach(([_key, component]) => {
  const { name } = component;
  if (name) {
    window.TinyLowcodeComponent[name] = component;
  }
});
Object.entries(allAntvComponents).forEach(([_key, component]) => {
  const { name } = component;
  if (name) {
    window.TinyLowcodeComponent[name] = component;
  }
});
Object.entries(allAntvIcons).forEach(([_key, component]) => {
  const { name } = component;
  if (name) {
    window.TinyLowcodeComponent[name] = component;
  }
});

Object.entries(allVxeComponents).forEach(([_key, component]) => {
  const { name } = component;
  if (name) {
    window.TinyLowcodeComponent[name] = component;
  }
});
let App = null;

const renderer = {
  getApp() {
    return App;
  },
  getI18n() {
    return TinyI18nHost;
  },
  ...api,
};

const create = () => {
  App && App.unmount();
  App = null;

  document.body.remove();
  document.body = document.createElement('body');
  dispatch('canvasReady', { detail: renderer });

  App = Vue.createApp(Main).use(TinyI18nHost).provide(I18nInjectionKey, TinyI18nHost);
  App.use(VxeSelect);
  App.use(Table);
  App.use(Pager);
  App.use(Toolbar);
  App.use(VxeButton);
  App.config.globalProperties.lowcodeConfig = window.parent.TinyGlobalConfig;
  App.mount(document.body);

  new ResizeObserver(() => {
    dispatch('canvasResize');
  }).observe(document.body);

  App.config.errorHandler = () => {};
};

export const createRender = config => {
  const { dslMode, canvasOptions } = config;
  const { styles = [], scripts = [] } = canvasOptions[dslMode];
  const { styles: thirdStyles = [], scripts: thirdScripts = [] } = window.thirdPartyDeps || {};

  Promise.all([
    ...thirdScripts.map(dynamicImportComponents),
    ...scripts.map(src => addScript(src)).concat([...thirdStyles, ...styles].map(src => addStyle(src))),
  ]).finally(create);
};

document.addEventListener('updateDependencies', updateDependencies);
