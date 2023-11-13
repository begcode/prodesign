import path from 'path';
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
    createSvgIconsPlugin({
      iconDirs: [path.resolve(process.cwd(), 'src/assets/svg')],
      // default
      symbolId: 'icon-[dir]-[name]',
    }),
  ],
  publicDir: false,
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
      '#': path.resolve(__dirname, './types'),
    },
  },
  build: {
    minify: false,
    cssCodeSplit: true,
    lib: {
      entry: {
        index: path.resolve(__dirname, './src/index.ts'),
      },
      name: 'begcode-components',
      fileName: (format, entryName) => `${entryName}.${format}.js`,
      formats: ['es', 'umd', 'cjs'],
    },
    commonjsOptions: {
      transformMixedEsModules: true,
    },
    rollupOptions: {
      external: ['vue', 'vue-i18n', 'ant-design-vue', 'virtual:svg-icons-names', 'vxe-table'],
      output: {
        banner: chunk => {
          if (chunk.name === 'index') {
            return 'import "./index.css"';
          }
          return '';
        },
        globals: {
          vue: 'Vue',
          'vue-i18n': 'VueI18n',
          'ant-design-vue': 'AntDesignVue',
        },
      },
    },
  },
});
