import { PluginOption } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import purgeIcons from 'vite-plugin-purge-icons';
import UnoCSS from 'unocss/vite';
import { presetTypography, presetUno } from 'unocss';
import VitePluginCertificate from 'vite-plugin-mkcert';
// import vueSetupExtend from 'vite-plugin-vue-setup-extend-plus';
import { configStyleImportPlugin } from './styleImport';
import { configMockPlugin } from './mock';
import { configCompressPlugin } from './compress';
import { configVisualizerConfig } from './visualizer';
import { configThemePlugin } from './theme';
import { configSvgIconsPlugin } from './svgSprite';
import { viteStaticCopy } from 'vite-plugin-static-copy';

export function createVitePlugins(viteEnv: ViteEnv, isBuild: boolean) {
  const { VITE_USE_MOCK, VITE_BUILD_COMPRESS, VITE_BUILD_COMPRESS_DELETE_ORIGIN_FILE } = viteEnv;

  const vitePlugins: (PluginOption | PluginOption[])[] = [
    // have to
    vue(),
    // have to
    vueJsx(),
    // support name
    // vueSetupExtend(),
    VitePluginCertificate({
      source: 'coding',
    }),
    UnoCSS({
      presets: [presetUno(), presetTypography()],
    }),
  ];

  !isBuild &&
    vitePlugins.push(
      viteStaticCopy({
        targets: [
          {
            src: 'node_modules/swagger-ui-dist/*.{html,css,png,js}',
            dest: 'swagger-ui',
          },
          {
            src: 'dev/swagger-ui/index.html',
            dest: 'swagger-ui',
          },
        ],
      }),
    );

  // vite-plugin-svg-icons
  vitePlugins.push(configSvgIconsPlugin(isBuild));

  // vite-plugin-mock
  VITE_USE_MOCK && vitePlugins.push(configMockPlugin(isBuild));

  // vite-plugin-purge-icons
  vitePlugins.push(purgeIcons());

  // rollup-plugin-visualizer
  vitePlugins.push(configVisualizerConfig());

  // vite-plugin-theme
  vitePlugins.push(configThemePlugin(isBuild));

  // The following plugins only work in the production environment
  if (isBuild) {
    // rollup-plugin-gzip
    vitePlugins.push(configCompressPlugin(VITE_BUILD_COMPRESS, VITE_BUILD_COMPRESS_DELETE_ORIGIN_FILE));
  }

  return vitePlugins;
}
