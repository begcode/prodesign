import { resolve } from 'path';
import * as path from 'path';
import type { UserConfig, ConfigEnv } from 'vite';
import monacoEditorPlugin from 'vite-plugin-monaco-editor';
import dayjs from 'dayjs';
import { loadEnv } from 'vite';
// import { generateModifyVars } from './build/generate/generateModifyVars';
// import { createProxy } from './build/vite/proxy';
// import { wrapperEnv } from './build/utils';
// import { createVitePlugins } from './build/vite/plugin';
// import { OUTPUT_DIR } from './build/constant';
import nodePolyfill from 'rollup-plugin-polyfill-node';
import nodeGlobalsPolyfillPlugin from '@esbuild-plugins/node-globals-polyfill';
import nodeModulesPolyfillPlugin from '@esbuild-plugins/node-modules-polyfill';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons';
import visualizer from 'rollup-plugin-visualizer';
import pkg from './package.json';

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

  const monacoPublicPath = {
    local: 'editor/monaco-workers',
    alpha: 'https://tinyengine-assets.obs.cn-north-4.myhuaweicloud.com/files/monaco-assets',
    prod: 'https://tinyengine-assets.obs.cn-north-4.myhuaweicloud.com/files/monaco-assets',
  };
  let monacoEditorPluginInstance = monacoEditorPlugin({ publicPath: monacoPublicPath.local });

  // The boolean type read by loadEnv is a string. This function can be converted to boolean type
  // const viteEnv = wrapperEnv(env);

  // const { VITE_PORT, VITE_PUBLIC_PATH, VITE_PROXY, VITE_DROP_CONSOLE } = viteEnv;

  // const isBuild = command === 'build';

  return {
    base: '/',
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
      port: 3200,
      open: '/?type=app&id=prodesign&tenant=1',
      hmr: {
        overlay: false,
      },
      // Load proxy configuration from .env
      proxy: {},
    },
    esbuild: {
      pure: [],
    },
    build: {
      target: 'es2015',
      minify: 'esbuild',
      cssTarget: 'chrome80',
      outDir: 'dist',
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
      rollupOptions: {
        plugins: [nodePolyfill({ include: null })], // 使用@rollup/plugin-inject的默认值{include: null}, 即在所有代码中生效
        input: {
          index: resolve(__dirname, './index.html'),
          canvas: resolve(__dirname, './canvas.html'),
          setting: resolve(__dirname, './setting.html'),
          preview: resolve(__dirname, './preview.html'),
          previewApp: resolve(__dirname, './previewApp.html'),
        },
        // output: {
        //   manualChunks: (id) => {
        //     const chunksMap = {
        //       monaco: ['node_modules/monaco-editor'],
        //       prettier: ['node_modules/prettier'],
        //       vendor: ['node_modules']
        //     }
        //     for (const [chunkName, sourcePaths] of Object.entries(chunksMap)) {
        //       if (sourcePaths.some((item) => id.indexOf(item) > -1)) {
        //         return chunkName
        //       }
        //     }
        //     return undefined
        //   }
        // }
      },
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
          // modifyVars: generateModifyVars(),
          javascriptEnabled: true,
        },
        scss: { charset: false },
      },
    },

    // The vite plugin used by the project. The quantity is large, so it is separately extracted and managed
    plugins: [
      monacoEditorPluginInstance,
      visualizer({
        filename: 'tmp/report.html',
        title: 'Bundle Analyzer',
      }),
      vue({
        reactivityTransform: path.resolve(__dirname, 'src'),
        template: {
          compilerOptions: {
            isCustomElement: tag => tag.startsWith('tiny-i18n-host') || tag.startsWith('ng'),
          },
        },
      }),
      vueJsx(),
      createSvgIconsPlugin({
        iconDirs: [
          path.resolve(__dirname, './src/assets/rf-resources/'), // 脚手架执行构建时将图元图片拷贝到此目录
          path.resolve(__dirname, './src/assets/'),
        ],
        symbolId: 'icon-[dir]-[name]',
      }),
    ],

    optimizeDeps: {
      esbuildOptions: {
        plugins: [
          nodeGlobalsPolyfillPlugin({
            process: true,
            buffer: true,
          }),
          nodeModulesPolyfillPlugin(),
        ],
      },
      // @iconify/iconify: The dependency is dynamically and virtually loaded by @purge-icons/generated, so it needs to be specified explicitly
      include: ['@vue/runtime-core', '@vue/shared', '@iconify/iconify', 'ant-design-vue/es/locale/zh_CN', 'ant-design-vue/es/locale/en_US'],
    },
  };
};
