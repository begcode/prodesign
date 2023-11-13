package com.mycompany.myapp.settings.domain;

import static com.mycompany.myapp.settings.domain.CommonFieldDataTestSamples.*;
import static com.mycompany.myapp.settings.domain.DictionaryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.ArrayList;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DictionaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dictionary.class);
        Dictionary dictionary1 = getDictionarySample1();
        Dictionary dictionary2 = new Dictionary();
        assertThat(dictionary1).isNotEqualTo(dictionary2);

        dictionary2.setId(dictionary1.getId());
        assertThat(dictionary1).isEqualTo(dictionary2);

        dictionary2 = getDictionarySample2();
        assertThat(dictionary1).isNotEqualTo(dictionary2);
    }

    @Test
    void itemsTest() throws Exception {
        Dictionary dictionary = getDictionaryRandomSampleGenerator();
        CommonFieldData commonFieldDataBack = getCommonFieldDataRandomSampleGenerator();

        // dictionary.addItems(commonFieldDataBack);
        assertThat(dictionary.getItems()).containsOnly(commonFieldDataBack);
        assertThat(commonFieldDataBack.getDictionary()).isEqualTo(dictionary);

        // dictionary.removeItems(commonFieldDataBack);
        assertThat(dictionary.getItems()).doesNotContain(commonFieldDataBack);
        assertThat(commonFieldDataBack.getDictionary()).isNull();

        dictionary.items(new ArrayList<>(Set.of(commonFieldDataBack)));
        assertThat(dictionary.getItems()).containsOnly(commonFieldDataBack);
        assertThat(commonFieldDataBack.getDictionary()).isEqualTo(dictionary);

        dictionary.setItems(new ArrayList<>());
        assertThat(dictionary.getItems()).doesNotContain(commonFieldDataBack);
        assertThat(commonFieldDataBack.getDictionary()).isNull();
    }
}
