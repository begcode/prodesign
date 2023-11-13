<template>
  <a-input
    :disabled="disabled"
    readOnly
    :style="{ width }"
    :placeholder="t('component.icon.placeholder')"
    :class="prefixCls"
    v-model:value="currentSelect"
  >
    <template #addonAfter>
      <a-popover placement="bottomLeft" trigger="click" v-model="visible" :overlayClassName="`${prefixCls}-popover`">
        <template #title>
          <div class="flex justify-between">
            <a-input
              :placeholder="t('component.icon.search')"
              v-model:value="searchIconValue"
              @change="debounceHandleSearchChange"
              allowClear
            />
          </div>
        </template>

        <template #content>
          <div v-if="getPaginationList.length">
            <ScrollContainer class="border border-solid border-t-0">
              <ul class="flex flex-wrap px-2" style="padding-right: 0">
                <li
                  v-for="icon in getPaginationList"
                  :key="icon"
                  :class="currentSelect === icon ? 'border border-primary' : ''"
                  class="p-2 w-1/8 cursor-pointer mr-1 mt-1 flex justify-center items-center border border-solid hover:border-primary"
                  @click="handleClick(icon)"
                  :title="icon"
                >
                  <!-- <Icon :icon="icon" :prefix="prefix" /> -->
                  <SvgIcon v-if="isSvgMode" :name="icon" />
                  <Icon :icon="icon" v-else />
                </li>
              </ul>
            </ScrollContainer>
            <div class="flex py-2 items-center justify-center" v-if="getTotal >= pageSize">
              <a-pagination
                showLessItems
                v-model:current="current"
                :page-size-options="pageSizeOptions"
                size="small"
                v-model:pageSize="pageSize"
                v-model::total="getTotal"
                @change="handlePageChange"
              />
            </div>
          </div>
          <template v-else
            ><div class="p-5"><a-empty /></div>
          </template>
        </template>

        <span class="cursor-pointer px-2 py-1 flex items-center" v-if="isSvgMode && currentSelect">
          <SvgIcon :name="currentSelect" @click="currentSelectClick" />
        </span>
        <Icon :icon="currentSelect || 'ion:apps-outline'" class="cursor-pointer px-2 py-1" v-else @click="currentSelectClick" />
      </a-popover>
    </template>
  </a-input>
</template>
<script lang="ts" setup>
import { ref, watchEffect, watch } from 'vue';
import { Input, Popover, Pagination, Empty } from 'ant-design-vue';
import { useDesign } from '@/hooks/web/useDesign';
import { ScrollContainer } from '@/components/Container';
import Icon from '../Icon.vue';
import SvgIcon from './SvgIcon.vue';

import iconsData from '../data/icons.data';
import { usePagination } from '@/hooks/web/usePagination';
import { useDebounceFn } from '@vueuse/core';
import svgIcons from 'virtual:svg-icons-names';
import { copyText } from '@/utils/copyTextToClipboard';
import { useI18n } from '@/hooks/web/useI18nOut';

defineOptions({
  name: 'IconPicker',
});

const { t } = useI18n();

// 没有使用别名引入，是因为WebStorm当前版本还不能正确识别，会报unused警告
const AInput = Input;
const APopover = Popover;
const APagination = Pagination;
const AEmpty = Empty;

function getIcons() {
  const data = iconsData as any;
  const prefix: string = data?.prefix ?? '';
  let result: string[] = [];
  if (prefix) {
    result = (data?.icons ?? []).map(item => `${prefix}:${item}`);
  } else if (Array.isArray(iconsData)) {
    result = iconsData as string[];
  }
  return result;
}

function getSvgIcons() {
  return svgIcons.map((icon: string) => icon.replace('icon-', ''));
}

export interface Props {
  value?: string;
  width?: string;
  pageSize?: number;
  copy?: boolean;
  mode?: 'svg' | 'iconify';
  disabled?: boolean;
  clearSelect?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  value: '',
  width: '100%',
  pageSize: 140,
  copy: false,
  mode: 'iconify',
  disabled: true,
  clearSelect: false,
});

const emit = defineEmits(['change', 'update:value']);

const isSvgMode = props.mode === 'svg';
const icons = isSvgMode ? getSvgIcons() : getIcons();

const currentSelect = ref('');
const visible = ref(false);
const currentList = ref(icons);
const { prefixCls } = useDesign('icon-picker');

const debounceHandleSearchChange = useDebounceFn(handleSearchChange, 100);
//页数
const current = ref<number>(1);
//每页条数
const pageSize = ref<number>(140);
//下拉分页显示
const pageSizeOptions = ref<any>(['10', '20', '50', '100', '140']);
//下拉搜索值
const searchIconValue = ref<string>('');

const { getPaginationList, getTotal, setCurrentPage, setPageSize } = usePagination(currentList, pageSize.value);

watchEffect(() => {
  currentSelect.value = props.value;
});

watch(
  () => currentSelect.value,
  v => {
    emit('update:value', v);
    return emit('change', v);
  },
);

function handlePageChange(page: number, size: number) {
  current.value = page;
  pageSize.value = size;
  setPageSize(size);
  setCurrentPage(page);
}

function handleClick(icon: string) {
  if (props.clearSelect === true) {
    if (currentSelect.value === icon) {
      currentSelect.value = '';
    } else {
      currentSelect.value = icon;
    }
  } else {
    currentSelect.value = icon;
    if (props.copy) {
      copyText(icon, t('component.icon.copy'));
    }
  }
}

function handleSearchChange(e: Event) {
  const value = e.target.value;
  setCurrentPage(1);
  current.value = 1;
  if (!value) {
    currentList.value = icons;
    return;
  }
  currentList.value = icons.filter(item => item.includes(value));
}
function currentSelectClick() {
  setCurrentPage(1);
  setPageSize(140);
  current.value = 1;
  pageSize.value = 140;
  currentList.value = icons;
  searchIconValue.value = '';
}
</script>
<style>
.vben-icon-picker {
  .ant-input-group-addon {
    padding: 0;
  }

  &-popover {
    width: 400px;

    .ant-popover-inner-content {
      padding: 0;
    }

    .scrollbar {
      height: 220px;
    }
  }
}
</style>
