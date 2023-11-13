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

import { createApp } from 'vue';
import App from './App.vue';
import globalConfig from './config/lowcode.config';
import initSvgs from '@/svgs';
import { setGlobalConfig } from '@/controller';
import i18n from '@/common/js/i18n';
import { initMonitor } from '@/common/js/monitor';
import { isDevelopEnv } from '@/common/js/environments';
import 'virtual:svg-icons-register';
import 'ant-design-vue/dist/reset.css';

// import TinyThemeTool from '@opentiny/vue-theme/theme-tool'
// import { tinySmbTheme } from '@opentiny/vue-theme/theme' // SMB 主题

// eslint-disable-next-line no-new
// new TinyThemeTool(tinySmbTheme, 'smbtheme') // 初始化主题

if (!isDevelopEnv) {
  initMonitor();
}
import VXETable from 'vxe-table';
import 'vxe-table/lib/style.css';

window.TinyGlobalConfig = globalConfig;
setGlobalConfig(globalConfig);

const app = createApp(App);
app.use(VXETable);
initSvgs(app);
window.lowcodeI18n = i18n;
app.use(i18n).mount('#app');
