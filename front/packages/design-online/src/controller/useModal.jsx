import { h, render } from 'vue'
import { Modal } from 'ant-design-vue'

const confirm = ({ title, status, message, exec, cancel, showFooter = true }) => {
  Modal.confirm({
    title,
    status,
    content: () => {
      return (
          <div class="modal-content">
            <div class="wrap">
              {typeof message === 'string' ? message : <message />}
            </div>
          </div>
      )
    },
    onOk() {
      if (typeof exec === 'function') {
        exec()
      }
    },
    onCancel() {
      if (typeof cancel === 'function') {
        cancel()
      }
    },
    okText: '确认',
    cancelText: '取消',
  });
}

const message = ({ title, status, message, exec }) => {
  Modal.alert({
    title,
    status,
    content: () => {
      return (
          <div class="modal-content" >
            <div class="wrap">
              {typeof message === 'string' ? message : <message />}
            </div>
          </div >
      )
    },
    onOk() {
      if (typeof exec === 'function') {
        exec()
      }
    }
  });
}

const topbox = (options) => {
  const props = { ...options, modelValue: true }
  let TopBox = h(Modal, props)
  const modalEl = document.createElement('div')

  const close = () => {
    TopBox.el.remove()
    TopBox = null
  }

  render(TopBox, modalEl)

  return {
    TopBox,
    close
  }
}

window.topbox = topbox
window.message = message

export default () => {
  return {
    confirm,
    message,
    topbox
  }
}
