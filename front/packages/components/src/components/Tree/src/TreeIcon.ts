import type { VNode } from 'vue';

import { h } from 'vue';
import { isString } from '@vue/shared';
import Icon from '@/components/Icon/Icon.vue';

export const TreeIcon = ({ icon }: { icon: VNode | string }) => {
  if (!icon) return null;
  if (isString(icon)) {
    return h(Icon, { icon, class: 'mr-1' });
  }
  return h(Icon);
};
