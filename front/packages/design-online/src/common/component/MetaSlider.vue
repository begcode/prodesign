<template>
  <div class="slider-container">
    <input v-model="value" type="range" @change="onSliderStop" />
    <meta-number
      :min="0"
      :max="100"
      :controls="controls"
      :modelValue="value"
      :selectedUnit="unitRef"
      :showUnit="showUnit"
      :disabled="disabled"
      :unit="[
        { value: 'px', label: 'px' },
        { value: '%', label: '%' },
      ]"
      @numberChange="onNumberCompChange"
      @unitChange="onUnitChange"
    ></meta-number>
  </div>
</template>

<script>
import { ref, watch } from 'vue';
import MetaNumber from './MetaNumber.vue';

export default {
  components: {
    MetaNumber,
  },
  props: {
    modelValue: {
      type: Number,
      default: 0,
    },
    controls: {
      type: Boolean,
      default: false,
    },
    // 默认选中的单位
    selectedUnit: {
      type: String,
      default: '%',
    },
    // 是否显示单位
    showUnit: {
      type: Boolean,
      default: false,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  setup(props, { emit }) {
    const value = ref(props.modelValue);
    const unitRef = ref(props.selectedUnit || 'px');

    watch(
      () => props.modelValue,
      val => {
        value.value = val;
      },
    );

    const triggerUpdate = (value, unitValue = unitRef.value) => {
      emit('update:modelValue', value, unitValue);
    };

    const onSliderStop = event => {
      triggerUpdate(Number(event.target.value));
    };

    const onNumberCompChange = val => {
      value.value = val;
      triggerUpdate(val);
    };

    const onUnitChange = val => {
      unitRef.value = val;
      value.value = 0;
      triggerUpdate(0, val);
    };

    return { value, unitRef, onNumberCompChange, onUnitChange, onSliderStop };
  },
};
</script>

<style lang="less" scoped>
.slider-container {
  display: grid;
  grid-template-columns: minmax(80px, 50%) minmax(0, 1fr); // 最后一个元素自适应布局
  column-gap: 6%;
  :deep(.tiny-slider) {
    height: 3px;
    width: 100%;
    border-radius: 2px;
    background-color: var(--ti-lowcode-canvas-wrap-bg);
    .tiny-slider__handle {
      width: 10px;
      height: 10px;
      margin: -4px -5px 0 -5px;
      border-radius: 50%;
      border: none;
      cursor: ew-resize;
      background-color: var(--ti-lowcode-toolbar-breadcrumb-color);
      box-shadow:
        rgba(0, 0, 0, 0) 0px 0px 0px 1px,
        rgba(0, 0, 0, 0.3) 0px 0px 0px 0.5px;
    }
    .tiny-slider__range {
      height: 3px;
      border-radius: 3px;
      margin-top: 0;
      background: var(--ti-lowcode-canvas-wrap-bg);
    }

    .tiny-slider__tips {
      color: var(--ti-lowcode-breadcrumb-color);
      background: var(--ti-lowcode-breadcrumb-bg);
      padding: 4px 6px;
      border-radius: 2px;
      box-shadow: rgb(0 0 0 / 15%) 0px 5px 10px;
      margin-top: -8px;
      &::before,
      &::after {
        border-color: var(--ti-lowcode-breadcrumb-bg) transparent;
      }
    }
  }
  input[type='range'] {
    height: 4px;
    border-radius: 8px;
    margin: 0.8em 0;
    padding: 0;
    cursor: pointer;
    border: 0;
    background: -webkit-linear-gradient(#fff, #fff) no-repeat #999999;
    background-size: 0% 100%;
    position: relative;
    outline: 0;
    top: -1px;
    -webkit-appearance: none !important;
  }
  input[type='range']::-webkit-slider-thumb {
    width: 10px;
    height: 10px;
    border-radius: 50%;
    border: 0;
    background-color: #fff;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.21);
    -webkit-transition:
      border-color 0.15s,
      background-color 0.15s;
    transition:
      border-color 0.15s,
      background-color 0.15s;
    cursor: pointer;
    background-clip: padding-box;
    box-sizing: border-box;
    -webkit-appearance: none !important;
  }
  input[type='range']::-webkit-slider-thumb:active {
    border: 0;
    background-color: #fff;
  }
}
</style>
