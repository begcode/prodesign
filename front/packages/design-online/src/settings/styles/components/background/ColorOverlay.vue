<template>
  <div class="background-row">
    <label class="row-label">Color</label>
    <meta-color :modelValue="modelValue" @change="changeColor" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { MetaColor } from '@/common';
import { BACKGROUND_PROPERTY } from '../../js/styleProperty';

const props = defineProps({
  style: {
    type: Object,
    default: () => {},
  },
});

const emit = defineEmits(['updateStyle']);

const modelValue = ref('');

const updateStyle = property => {
  emit('updateStyle', property);
};

const changeColor = val => {
  updateStyle({ [BACKGROUND_PROPERTY.BackgroundImage]: `linear-gradient(${val}, ${val})` });
};

onMounted(() => {
  modelValue.value = props.style.text ?? '#000';
});
</script>
