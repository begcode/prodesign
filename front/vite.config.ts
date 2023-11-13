import { resolve } from 'path';
import type { UserConfig, ConfigEnv } from 'vite';
import dayjs from 'dayjs';
import { loadEnv } from 'vite';
import pkg from './package.json';
import { generateModifyVars } from './build/generate/generateModifyVars';
import { createProxy } from './build/vite/proxy';
import { wrapperEnv } from './build/utils';
import { createVitePlugins } from './build/vite/plugin';
import { OUTPUT_DIR } from './build/constant';

function pathResolve(dir: string) {
  return resolve(process.cwd(), '.', dir);
}

const { dependencies, devDependencies, name, version } = pkg;
const __APP_INFO__ = {
  pkg: { dependencies, devDependencies, name, version },
  lastBuildTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
};

export default ({ command, mode }: ConfigEnv): UserConfig => {
  const root = process.cwd();

  const env = loadEnv(mode, root);

  // The boolean type read by loadEnv is a string. This function can be converted to boolean type
  const viteEnv = wrapperEnv(env);

  const { VITE_PORT, VITE_PUBLIC_PATH, VITE_PROXY } = viteEnv;

  const isBuild = command === 'build';

  return {
    base: VITE_PUBLIC_PATH,
    root: root,
    publicDir: 'public',
    resolve: {
      alias: [
        {
          find: 'vue-i18n',
          replacement: 'vue-i18n/dist/vue-i18n.cjs.js',
        },
        // @/xxxx => src/xxxx
        {
          find: /@\//,
          replacement: pathResolve('src') + '/',
        },
        // /#/xxxx => types/xxxx
        {
          find: /\/#\//,
          replacement: pathResolve('src/types') + '/',
        },
        // #/xxxx => types/xxxx
        {
          find: /#\//,
          replacement: pathResolve('src/types') + '/',
        },
      ],
    },
    server: {
      https: false,
      // Listening on all local IPs
      host: true,
      port: VITE_PORT,
      // Load proxy configuration from .env
      proxy: createProxy(VITE_PROXY),
    },
    esbuild: {
      drop: isBuild ? ['console', 'debugger'] : [],
    },
    build: {
      target: 'es2015',
      minify: 'esbuild',
      cssTarget: 'chrome80',
      outDir: OUTPUT_DIR,
      rollupOptions: {
        output: {
          chunkFileNames: 'js/[name]-[hash].js', // 引入文件名的名称
          entryFileNames: 'js/[name]-[hash].js', // 包的入口文件名称
          // manualChunks配置 (依赖包从大到小排列)
          manualChunks: {
            'tinymce-vendor': ['tinymce'],
            'echarts-vendor': ['echarts'],
            'antd-vue-vendor': ['ant-design-vue', '@ant-design/icons-vue', '@ant-design/colors'],
            'vxe-table-vendor': ['vxe-table', 'xe-utils'],
            'codemirror-vendor': ['codemirror'],
            // 将 Lodash 库的代码单独打包
            'lodash-es-vendor': ['lodash-es'],
            'html2canvas-vendor': ['html2canvas'],
            // vue vue-router合并打包
            vue: ['vue', 'vue-router'],
            'cron-parser-vendor': ['cron-parser'],
          },
        },
      },
      // minify: 'terser',
      /**
       * 当 minify=“minify:'terser'” 解开注释
       * Uncomment when minify="minify:'terser'"
       */
      // terserOptions: {
      //   compress: {
      //     keep_infinity: true,
      //     drop_console: VITE_DROP_CONSOLE,
      //     drop_debugger: true,
      //   },
      // },
      // Turning off brotliSize display can slightly reduce packaging time
      reportCompressedSize: false,
      chunkSizeWarningLimit: 2000,
    },
    define: {
      // setting vue-i18-next
      // Suppress warning
      __INTLIFY_PROD_DEVTOOLS__: false,
      __APP_INFO__: JSON.stringify(__APP_INFO__),
    },

    css: {
      preprocessorOptions: {
        less: {
          modifyVars: generateModifyVars(),
          javascriptEnabled: true,
        },
        scss: { charset: false },
      },
    },

    // The vite plugin used by the project. The quantity is large, so it is separately extracted and managed
    plugins: createVitePlugins(viteEnv, isBuild),

    optimizeDeps: {
      // @iconify/iconify: The dependency is dynamically and virtually loaded by @purge-icons/generated, so it needs to be specified explicitly
      include: [
        '@vue/runtime-core',
        '@vue/shared',
        '@iconify/iconify',
        'ant-design-vue/es/locale/zh_CN',
        'ant-design-vue/es/locale/en_US',
        '@ant-design/colors',
        '@ant-design/icons-vue',
        '@vueuse/core',
        '@vueuse/shared',
        '@zxcvbn-ts/core',
        'ant-design-vue',
        'axios',
        'china-area-data',
        'clipboard',
        'codemirror',
        'cron-parser',
        'cropperjs',
        'crypto-js/aes',
        'crypto-js/enc-base64',
        'crypto-js/enc-utf8',
        'crypto-js/md5',
        'crypto-js/mode-ecb',
        'crypto-js/pad-pkcs7',
        'dom-align',
        'echarts',
        'echarts/charts',
        'echarts/components',
        'echarts/core',
        'echarts/renderers',
        'intro.js',
        'lodash-es',
        'md5',
        'nprogress',
        'path-to-regexp',
        'pinia',
        'print-js',
        'qs',
        'resize-observer-polyfill',
        'showdown',
        'sortablejs',
        'tinymce/icons/default/icons',
        'tinymce/plugins/advlist',
        'tinymce/plugins/anchor',
        'tinymce/plugins/autolink',
        'tinymce/plugins/autosave',
        'tinymce/plugins/code',
        'tinymce/plugins/codesample',
        'tinymce/plugins/contextmenu',
        'tinymce/plugins/directionality',
        'tinymce/plugins/fullscreen',
        'tinymce/plugins/hr',
        'tinymce/plugins/image',
        'tinymce/plugins/insertdatetime',
        'tinymce/plugins/link',
        'tinymce/plugins/lists',
        'tinymce/plugins/media',
        'tinymce/plugins/nonbreaking',
        'tinymce/plugins/noneditable',
        'tinymce/plugins/pagebreak',
        'tinymce/plugins/paste',
        'tinymce/plugins/preview',
        'tinymce/plugins/print',
        'tinymce/plugins/save',
        'tinymce/plugins/searchreplace',
        'tinymce/plugins/spellchecker',
        'tinymce/plugins/tabfocus',
        'tinymce/plugins/table',
        'tinymce/plugins/template',
        'tinymce/plugins/textcolor',
        'tinymce/plugins/textpattern',
        'tinymce/plugins/visualblocks',
        'tinymce/plugins/visualchars',
        'tinymce/plugins/wordcount',
        'tinymce/themes/silver',
        'tinymce/tinymce',
        'vditor',
        'vue',
        'vue-i18n',
        'vue-print-nb-jeecg/src/printarea',
        'vue-router',
        'vue-types',
        'vxe-table',
        'vxe-table-plugin-antd',
        'xe-utils',
        'xss',
      ],
    },
  };
};
