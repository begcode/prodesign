package com.mycompany.myapp.settings.domain;

import static com.mycompany.myapp.settings.domain.SysFillRuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SysFillRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SysFillRule.class);
        SysFillRule sysFillRule1 = getSysFillRuleSample1();
        SysFillRule sysFillRule2 = new SysFillRule();
        assertThat(sysFillRule1).isNotEqualTo(sysFillRule2);

        sysFillRule2.setId(sysFillRule1.getId());
        assertThat(sysFillRule1).isEqualTo(sysFillRule2);

        sysFillRule2 = getSysFillRuleSample2();
        assertThat(sysFillRule1).isNotEqualTo(sysFillRule2);
    }
}
