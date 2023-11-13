package com.mycompany.myapp.system.domain;

import static com.mycompany.myapp.system.domain.SmsConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SmsConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsConfig.class);
        SmsConfig smsConfig1 = getSmsConfigSample1();
        SmsConfig smsConfig2 = new SmsConfig();
        assertThat(smsConfig1).isNotEqualTo(smsConfig2);

        smsConfig2.setId(smsConfig1.getId());
        assertThat(smsConfig1).isEqualTo(smsConfig2);

        smsConfig2 = getSmsConfigSample2();
        assertThat(smsConfig1).isNotEqualTo(smsConfig2);
    }
}
