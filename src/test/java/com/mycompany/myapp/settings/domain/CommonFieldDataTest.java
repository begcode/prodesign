package com.mycompany.myapp.settings.domain;

import static com.mycompany.myapp.settings.domain.CommonFieldDataTestSamples.*;
import static com.mycompany.myapp.settings.domain.DictionaryTestSamples.*;
import static com.mycompany.myapp.settings.domain.SiteConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommonFieldDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonFieldData.class);
        CommonFieldData commonFieldData1 = getCommonFieldDataSample1();
        CommonFieldData commonFieldData2 = new CommonFieldData();
        assertThat(commonFieldData1).isNotEqualTo(commonFieldData2);

        commonFieldData2.setId(commonFieldData1.getId());
        assertThat(commonFieldData1).isEqualTo(commonFieldData2);

        commonFieldData2 = getCommonFieldDataSample2();
        assertThat(commonFieldData1).isNotEqualTo(commonFieldData2);
    }

    @Test
    void siteConfigTest() throws Exception {
        CommonFieldData commonFieldData = getCommonFieldDataRandomSampleGenerator();
        SiteConfig siteConfigBack = getSiteConfigRandomSampleGenerator();

        commonFieldData.setSiteConfig(siteConfigBack);
        assertThat(commonFieldData.getSiteConfig()).isEqualTo(siteConfigBack);

        commonFieldData.siteConfig(null);
        assertThat(commonFieldData.getSiteConfig()).isNull();
    }

    @Test
    void dictionaryTest() throws Exception {
        CommonFieldData commonFieldData = getCommonFieldDataRandomSampleGenerator();
        Dictionary dictionaryBack = getDictionaryRandomSampleGenerator();

        commonFieldData.setDictionary(dictionaryBack);
        assertThat(commonFieldData.getDictionary()).isEqualTo(dictionaryBack);

        commonFieldData.dictionary(null);
        assertThat(commonFieldData.getDictionary()).isNull();
    }
}
