<template>
  <div v-bind:class="[isActive ? 'canvas-mode' : '', 'jdl-editor']">
    <textarea id="textarea" ref="jdlcode" v-model="code"></textarea>
    <canvas
      id="canvas"
      ref="canvasElement"
      v-bind:style="{ top: canvasStyle.t + 'px', left: canvasStyle.l + 'px', width: canvasStyle.w + 'px', height: canvasStyle.h + 'px' }"
    ></canvas>
    <div
      id="canvas-panner"
      ref="canvasPanner"
      @mouseenter="isActive = true"
      @mousedown="mouseDown"
      @mouseup="mouseUp"
      @mouseleave="mouseUp"
      v-bind:style="{ width: pannerStyle.w }"
    ></div>
    <div class="tools">
      <a href="#" class="upload-input">
        <Icon icon="ant-design:cloud-upload-outlined" :size="20"></Icon>
        <input type="file" @change="importHandle" />
      </a>
      <a href="javascript:void(0);" @click="downHandle">
        <Icon icon="ant-design:cloud-download-outlined" :size="20"></Icon>
      </a>
      <a href="javascript:void(0);" @click="delHandle">
        <Icon icon="ant-design:delete-filled" :size="20"></Icon>
      </a>
    </div>
  </div>
</template>
<script setup>
import CodeMirror from './lib/codemirror/codemirror.custom';
import * as nomnoml from 'nomnoml';
import skanaar from './lib/nomnoml/shannar.custom';

import 'codemirror/theme/base16-dark.css';
import 'codemirror/lib/codemirror.css';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/addon/scroll/simplescrollbars.css';

import './css/show-hint-jdl.css';
import './css/solarized.jdl.css';

import { throttle, debounce, endsWith } from 'lodash-es';
// import saveAs from "file-saver";

import 'codemirror/addon/selection/active-line';
import 'codemirror/addon/edit/matchbrackets';
import 'codemirror/addon/hint/show-hint';
import 'codemirror/addon/scroll/simplescrollbars';
import { Icon } from '@begcode/components';
import { useJdlStoreWithOut } from '@/store/modules/jdl';
import { onMounted, reactive, ref } from 'vue';
import { jdlToNoml } from './jdlToNoml';
import { parse } from 'generator-jhipster/jdl';

defineOptions({
  name: 'JdlStudio',
  inheritAttrs: false,
});

const jdlStore = useJdlStoreWithOut();

const code = ref(jdlStore.code);

const cmOptions = reactive({
  // codemirror options
  tabSize: 4,
  mode: 'jdl',
  theme: 'solarized base16-dark',
  lineNumbers: true,
  line: true,
  matchBrackets: true,
  smartIndent: true,
  fullScreen: false,
  extraKeys: {
    Ctrl: 'autocomplete',
  },
  scrollbarStyle: 'simple',
});
const canvasElement = ref(null);
const canvasStyle = reactive({
  t: 0,
  l: 0,
  h: 0,
  w: 0,
});
const pannerStyle = reactive({
  w: '45%',
});
const isActive = ref(false);
const pannerwidth = ref('45%');
const canvasPanner = ref(null);
const vm = ref(null);
const zoomLevel = ref(1);
const offset = reactive({
  x: 0,
  y: 0,
});
const mouseDownPoint = ref({});

const jdlcode = ref(null);

onMounted(() => {
  let editor = CodeMirror.fromTextArea(jdlcode.value, cmOptions);
  let canvasPanner1 = document.getElementById('canvas-panner');
  editor.on('changes', debounce(sourceChanged, 300));
  editor.setSize(document.documentElement.clientWidth - 348, document.documentElement.clientHeight - 100);
  jdlStore.setEditor(editor);
  //更新jdlObject
  let jdlObject = parse(jdlStore.getEditorValue);
  jdlStore.setJdlObject(jdlObject);
  vm.value = skanaar.vector;
  window.addEventListener('resize', throttle(sourceChanged, 750, { leading: true }));
  canvasPanner1.addEventListener('wheel', throttle(magnify, 50));
  canvasPanner1.addEventListener('mousemove', throttle(mouseMove, 50));
});

function magnifyViewport(diff) {
  zoomLevel.value = Math.min(10, zoomLevel.value + diff);
  sourceChanged();
}
function resetViewport() {
  zoomLevel.value = 0;
  Object.assign(offset, {
    x: 0,
    y: 0,
  });
  sourceChanged();
}
function magnify(e) {
  zoomLevel.value = Math.min(10, zoomLevel.value - (e.deltaY < 0 ? -1 : 1));
  sourceChanged();
}
function positionCanvas(rect, superSampling, offset) {
  var w = rect.width / superSampling;
  var h = rect.height / superSampling;
  var t = 300 * (1 - h / document.documentElement.clientHeight) + offset.y;
  var l = 150 + (document.documentElement.clientWidth - w) / 2 + offset.x;
  Object.assign(canvasStyle, { t: t, l: l, w: w, h: h });
}
function sourceChanged() {
  try {
    let superSampling = window.devicePixelRatio || 1;
    let canvas1 = document.getElementById('canvas');
    let scale = superSampling * Math.exp(zoomLevel.value / 10);
    let model = nomnoml.draw(canvas1, jdlToNoml(jdlStore.getEditorValue), scale);
    positionCanvas(canvas1, superSampling, offset);
  } catch (e) {
    console.error(e);
  }
}
function mouseDown(e) {
  pannerStyle.w = '100%';
  mouseDownPoint.value = vm.value.diff(
    {
      x: e.pageX,
      y: e.pageY,
    },
    offset,
  );
}
function mouseMove(e) {
  if (Object.keys(mouseDownPoint.value).length > 0) {
    Object.assign(
      offset,
      vm.value.diff(
        {
          x: e.pageX,
          y: e.pageY,
        },
        mouseDownPoint.value,
      ),
    );
    sourceChanged();
  }
}
function mouseUp() {
  isActive.value = false;
  mouseDownPoint.value = {};
  pannerStyle.w = '45%';
}
function downHandle() {
  let blob = new Blob([this.editorValue], {
    type: 'text/plain;charset=utf-8',
  });
  // saveAs(blob, this.filename);
}
function delHandle() {
  // this.$store.commit("setEditorValue", "");
  // this.$store.commit("setJdlObject", {});
}
function handleClose() {
  this.settingdialogVisiable = false;
}
function importHandle(event) {
  let file = event.target.files[0];
  var reader = new FileReader();
  reader.readAsText(file, 'UTF-8');
  reader.onload = e => {
    let data = e.currentTarget.result;
    // this.$store.commit("setFilename", file.name);
    if (endsWith(file.name, '.json')) {
      console.log(data);
    }
    if (endsWith(file.name, '.jdl')) {
      // this.$store.commit("setEditorValue", data);
      //data->jdlobject
      data = data.replace(/\/\/[^\n\r]*/gm, '');
      // let jdlObject = parser.parse(data);
      // this.$store.commit("setJdlObject", jdlObject);
    }
  };
}
</script>

<style lang="less" scoped>
.isFixed {
  position: fixed;
  height: 25px;
  left: 33%;
  top: 5px;
  transform: translate(-50%, -50%);
  z-index: 999;
  background: rgba(0, 0, 0, 0);
  transition: all 1s;
  color: #657b83;
}
#textarea {
  outline: none;
  position: absolute;
  z-index: 1;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0);
  box-sizing: border-box;
  border: 0;
  font-family: Consolas, Menlo, monospace;
  color: #657b83;
  resize: none;
  opacity: 1;
  margin: 60px 0 20px 0;
  transition: opacity 0.3s;
}
.CodeMirror {
  width: calc(100% - 240px - 82px);
  height: calc(100% - 48px);
  border: 0;
  margin: 60px 0 20px 0;
  line-height: 25px;
  font-size: 16px;
  font-family: Consolas, Monaco, monospace;
  font-weight: 500;
  opacity: 1;
  transition: opacity 0.3s;
}

.CodeMirror .CodeMirror-linenumber {
  color: #3e4350;
  padding-left: 12px;
}
.canvas-mode .CodeMirror {
  opacity: 0;
  transition: opacity 1s;
}
.tools i {
  color: #fdf6e3;
  font-size: 1.2em;
}
.tools .tool-btn {
  padding-left: 2px;
}
.canvas-tools i {
  font-size: 1.5em;
}
.canvas-tools a {
  line-height: 2.5em;
}

#canvas-panner {
  position: absolute;
  z-index: 3;
  width: 33%;
  height: calc(100% - 58px);
  background: rgba(0, 0, 0, 0);
  top: 54px;
  right: 14px;
  bottom: 0;
  cursor: move;
}
#canvas {
  position: absolute;
  z-index: 2;
  cursor: move;
}
.canvas-mode .CodeMirror {
  opacity: 0;
  transition: opacity 1s;
}
.canvas-mode .canvas-tools {
  opacity: 1;
  transition: opacity 0.3s;
}

.tools {
  display: block;
  position: absolute;
  z-index: 4;
  top: 15px;
  right: 20px;
  font-family: Calibri, Helvetica, sans-serif;
  font-weight: bold;
  background: transparent;
  border-radius: 5px;
}
.tools.left {
  left: 20px;
}
.tools .canvas-tools {
  display: block;
  position: absolute;
  top: 60px;
  right: 0;
  width: 24px;
  opacity: 0;
  transition: opacity 1s;
}
.canvas-mode .canvas-tools {
  opacity: 1;
  transition: opacity 0.3s;
}
.tools i {
  width: 24px;
  height: 24px;
  margin-bottom: 5px;
}

.tools > .logo {
  color: #fdf6e3;
  font-size: 150%;
}
.tools > #tooltip {
  font-size: 90%;
  color: #807c72;
  position: fixed;
  top: 7px;
  right: 20px;
  text-align: right;
  background: rgb(40, 44, 52);
  border-radius: 2px;
  padding: 0 5px;
}
.tools > #storage-status {
  color: #fdf6e3;
  opacity: 0.5;
  font-style: italic;
  position: fixed;
  top: 7px;
  right: 0;
  width: 100%;
  text-align: center;
}
.tools > #storage-status a {
  border: 2px solid rgba(255, 246, 223, 0.63);
  padding: 0 5px;
  border-radius: 4px;
  font-style: normal;
  opacity: 1;
}

.tools .seperator {
  color: white;
  font-size: 25px;
  margin: 0 10px;
}
.el-upload-list {
  display: none;
}
.upload-input {
  position: relative;
  padding: 2px 2px;
  overflow: hidden;
  text-decoration: none;
  text-indent: 0;
  line-height: 20px;
}
.upload-input input {
  position: absolute;
  font-size: 100px;
  left: 0;
  top: 0;
  width: 20px;
  height: 20px;
  opacity: 0;
}
</style>
