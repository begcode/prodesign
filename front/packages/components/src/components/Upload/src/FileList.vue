<script lang="tsx">
import { defineComponent, CSSProperties, watch, nextTick } from 'vue';
import { fileListProps } from './props';
import { isFunction } from 'lodash-es';
import { useModalContext } from '@/components/Modal/src/hooks/useModalContext';

export default defineComponent({
  name: 'FileList',
  props: fileListProps,
  setup(props) {
    const modalFn = useModalContext();
    watch(
      () => props.dataSource,
      () => {
        nextTick(() => {
          modalFn?.redoModalHeight?.();
        });
      },
    );
    return () => {
      const { columns, actionColumn, dataSource } = props;
      const columnList = [...columns, actionColumn];
      return (
        <table class="file-table">
          <colgroup>
            {columnList.map(item => {
              const { width = 0, dataIndex } = item;
              const style: CSSProperties = {
                width: `${width}px`,
                minWidth: `${width}px`,
              };
              return <col style={width ? style : {}} key={dataIndex} />;
            })}
          </colgroup>
          <thead>
            <tr class="file-table-tr">
              {columnList.map(item => {
                const { title = '', align = 'center', dataIndex } = item;
                return (
                  <th class={['file-table-th', align]} key={dataIndex}>
                    {title}
                  </th>
                );
              })}
            </tr>
          </thead>
          <tbody>
            {dataSource.map((record = {}, index) => {
              return (
                <tr class="file-table-tr" key={`${index + record.name || ''}`}>
                  {columnList.map(item => {
                    const { dataIndex = '', customRender, align = 'center' } = item;
                    const render = customRender && isFunction(customRender);
                    return (
                      <td class={['file-table-td', align]} key={dataIndex}>
                        {render ? customRender?.({ text: record[dataIndex], record }) : record[dataIndex]}
                      </td>
                    );
                  })}
                </tr>
              );
            })}
          </tbody>
        </table>
      );
    };
  },
});
</script>
<style>
.file-table {
  width: 100%;
  border-collapse: collapse;
}
.file-table .center {
  text-align: center;
}
.file-table .left {
  text-align: left;
}
.file-table .right {
  text-align: right;
}
.file-table-th,
.file-table-td {
  padding: 12px 8px;
}
.file-table thead {
  background-color: v-bind('token["background-color-light"]');
}
.file-table table,
.file-table td,
.file-table th {
  border: 1px solid v-bind('token["colorBorder"]');
}
</style>
