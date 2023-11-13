import { defineStore } from 'pinia';
import { store } from '@/store';

interface JdlState {
  filename: string;
  code: string;
  editor: any;
  jdlObject: any;
}

export const useJdlStore = defineStore({
  id: 'jdl',
  state: (): JdlState => ({
    filename: 'test.jdl',
    code: `
entity Region {
  regionName String
}
entity Country {
	countryName String
}
relationship OneToOne {
	Country{region} to Region
}
`,
    editor: {},
    jdlObject: {},
  }),
  getters: {
    getEditorValue(state) {
      if (state.editor.getValue) {
        return state.editor.getValue();
      } else {
        return '';
      }
    },
  },
  actions: {
    setEditor(value) {
      this.editor = value;
    },
    setFilename(value) {
      this.filename = value;
    },
    setEditorValue(value) {
      this.editor.setValue(value);
    },
    setJdlObject(value) {
      console.log(value);
      this.jdlObject = value;
    },
  },
});

export function useJdlStoreWithOut() {
  return useJdlStore(store);
}
