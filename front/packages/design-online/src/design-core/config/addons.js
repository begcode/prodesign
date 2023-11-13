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

import Breadcrumb from '@/toolbars/breadcrumb/index';
import Fullscreen from '@/toolbars/fullscreen/index';
import Lang from '@/toolbars/lang';
import Checkinout from '@/toolbars/lock';
import Logo from '@/toolbars/logo';
import Media from '@/toolbars/media';
import Redoundo from '@/toolbars/redoundo';
import Save from '@/toolbars/save';
import Clean from '@/toolbars/clean';
import Preview from '@/toolbars/preview';
import GenerateVue from '@/toolbars/generate-vue';
import Refresh from '@/toolbars/refresh';
import Collaboration from '@/toolbars/collaboration';
import Setting from '@/toolbars/setting';

import Materials from '@/plugins/materials';
import Data from '@/plugins/data';
import Script from '@/plugins/script';
import Tree from '@/plugins/tree';
import Help from '@/plugins/help';
import Schema from '@/plugins/schema';
import Page from '@/plugins/page';
import I18n from '@/plugins/i18n';
import Bridge from '@/plugins/bridge';
import Block from '@/plugins/block';
import Datasource from '@/plugins/datasource';

import Props from '@/settings/props';
import Events from '@/settings/events/index';
import Styles from '@/settings/styles/index';
import JdlSetting from '@/settings/begcode/index';

import '@/theme/light/index.less';

const addons = {
  plugins: [Materials, Tree, Page, Block, Datasource, Bridge, I18n, Script, Data, Schema, Help],
  toolbars: [
    Logo,
    Breadcrumb,
    Media,
    Collaboration,
    Clean,
    Refresh,
    Save,
    GenerateVue,
    Preview,
    Redoundo,
    Fullscreen,
    Checkinout,
    Setting,
    Lang,
  ],
  settings: [Props, Styles, Events, JdlSetting],
};

export default addons;
