<template class="inline-block">
  <Select
    @dropdown-visible-change="handleFetch"
    v-bind="attrs_"
    @change="handleChange"
    :options="getOptions"
    :show-search="showSearch"
    @search="searchHandle"
    v-model:value="state"
  >
    <template #[item]="data" v-for="item in Object.keys($slots)">
      <slot :name="item" v-bind="data || {}"></slot>
    </template>
    <template #suffixIcon v-if="loading || showAdd">
      <LoadingOutlined spin v-if="loading" />
      <plus-outlined @click="emitAdd" v-if="showAdd" />
    </template>
    <template #notFoundContent v-if="loading">
      <span>
        <LoadingOutlined spin class="mr-1" />
        请等待数据加载完成...
      </span>
    </template>
  </Select>
</template>
<script lang="ts">
import { defineComponent, PropType, ref, computed, unref, watch } from 'vue';
import { Select } from 'ant-design-vue';
import type { SelectValue } from 'ant-design-vue/es/select';
import { useRuleFormItem } from '@/hooks/component/useFormItem';
import { useAttrs } from '@/hooks/vben/useAttrs';
import { get, omit, isFunction } from 'lodash-es';
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons-vue';
import { propTypes } from '@/utils/propTypes';

type OptionsItem = { label: string; value: string; disabled?: boolean };

export default defineComponent({
  name: 'ApiSelect',
  components: {
    Select,
    LoadingOutlined,
    PlusOutlined,
  },
  inheritAttrs: false,
  props: {
    value: { type: [Array, Object, String, Number] as PropType<SelectValue> },
    numberToString: propTypes.bool,
    api: {
      type: Function as PropType<(arg?: Recordable) => Promise<OptionsItem[]>>,
      default: null,
    },
    // api params
    params: {
      type: Object as PropType<Recordable>,
      default: () => ({}),
    },
    searchParams: {
      type: Object as PropType<Recordable>,
      default: () => ({}),
    },
    showSearch: propTypes.bool.def(false),
    // support xxx.xxx.xx
    resultField: propTypes.string.def(''),
    labelField: propTypes.string.def('label'),
    valueField: propTypes.string.def('value'),
    immediate: propTypes.bool.def(true),
    alwaysLoad: propTypes.bool.def(false),
    showAdd: propTypes.bool.def(false),
    options: {
      type: Array<OptionsItem>,
      default: [],
    },
  },
  emits: ['options-change', 'change', 'show-add'],
  setup(props, { emit }) {
    const options = ref<OptionsItem[]>([]);
    const loading = ref(false);
    // 首次是否加载过了
    const isFirstLoaded = ref(false);
    const emitData = ref<OptionsItem[]>([]);
    const attrs = useAttrs();
    const searchKeyword = ref('');
    const searchParamValues = computed(() => {
      const res: Recordable = { ...props.searchParams };
      Object.keys(props.searchParams)
        .filter(param => param !== 'useOr')
        .forEach(key => {
          if (!res[key]) {
            res[key] = searchKeyword.value;
          }
        });
      return res;
    });

    // Embedded in the form, just use the hook binding to perform form verification
    const [state, setState] = useRuleFormItem(props, 'value', 'change', emitData);
    let vModalValue: any;
    const attrs_ = computed(() => {
      let obj: any = unref(attrs) || {};
      if (obj && obj['onUpdate:value']) {
        vModalValue = obj['onUpdate:value'];
        delete obj['onUpdate:value'];
      }
      if (obj['filterOption'] === undefined) {
        obj['filterOption'] = (inputValue, option) => {
          if (typeof option['label'] === 'string') {
            return option['label'].toLowerCase().indexOf(inputValue.toLowerCase()) != -1;
          } else {
            return true;
          }
        };
      }
      return obj;
    });
    const getOptions = computed(() => {
      const { labelField, valueField, numberToString } = props;

      let data = unref(options).reduce((prev, next: Recordable) => {
        if (next) {
          const value = next[valueField];
          prev.push({
            ...omit(next, [labelField, valueField]),
            label: next[labelField],
            value: numberToString ? `${value}` : value,
          });
        }
        return prev;
      }, [] as OptionsItem[]);
      return data.length > 0 ? data : props.options;
    });

    watch(
      () => props.params,
      () => {
        console.log('watch:props.params', props.params);
        !unref(isFirstLoaded) && fetch();
      },
      { deep: true, immediate: props.immediate },
    );
    watch(
      () => searchKeyword.value,
      () => {
        !unref(isFirstLoaded) && fetch();
      },
      { deep: true },
    );

    async function fetch() {
      const api = props.api;
      if (!api || !isFunction(api) || loading.value) return;
      options.value = [];
      try {
        loading.value = true;
        const res = await api({ ...props.params, ...unref(searchParamValues) });
        isFirstLoaded.value = true;
        if (Array.isArray(res)) {
          options.value = res;
          emitChange();
          return;
        }
        if (props.resultField) {
          options.value = get(res, props.resultField) || [];
        }
        emitChange();
      } catch (error) {
        console.warn(error);
      } finally {
        loading.value = false;
        unref(attrs).mode == 'multiple' && !Array.isArray(unref(state)) && setState([]);
        initValue();
      }
    }

    function initValue() {
      let value = props.value;
      if (value && typeof value === 'string' && value != 'null' && value != 'undefined') {
        state.value = value.split(',');
      }
    }

    async function handleFetch(visible: boolean) {
      if (visible) {
        if (props.alwaysLoad) {
          await fetch();
        } else if (!props.immediate && !unref(isFirstLoaded)) {
          await fetch();
        }
      }
    }

    function emitChange() {
      emit('options-change', unref(getOptions));
    }

    function handleChange(_, ...args) {
      vModalValue && vModalValue(_);
      // 需要还原为原始值
      const { valueField, labelField } = props;
      if (attrs?.labelInValue) {
        args.forEach((item: any) => {
          if (valueField !== 'value') {
            item[valueField] = item.value;
          }
          if (labelField !== 'label') {
            item[labelField] = item.label;
          }
        });
        Object.assign(_, args[0].option);
      }
      emitData.value = args;
    }

    function searchHandle(value) {
      searchKeyword.value = value;
      fetch();
    }

    const emitAdd = () => {
      emit('show-add');
    };
    return { state, attrs_, attrs, getOptions, loading, handleFetch, handleChange, searchHandle, emitAdd };
  },
});
</script>
