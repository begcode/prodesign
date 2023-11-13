package com.mycompany.myapp.system.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SmsConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SmsConfig getSmsConfigSample1() {
        return new SmsConfig()
            .id(1L)
            .smsCode("smsCode1")
            .templateId("templateId1")
            .accessKey("accessKey1")
            .secretKey("secretKey1")
            .regionId("regionId1")
            .signName("signName1")
            .remark("remark1");
    }

    public static SmsConfig getSmsConfigSample2() {
        return new SmsConfig()
            .id(2L)
            .smsCode("smsCode2")
            .templateId("templateId2")
            .accessKey("accessKey2")
            .secretKey("secretKey2")
            .regionId("regionId2")
            .signName("signName2")
            .remark("remark2");
    }

    public static SmsConfig getSmsConfigRandomSampleGenerator() {
        return new SmsConfig()
            .id(longCount.incrementAndGet())
            .smsCode(UUID.randomUUID().toString())
            .templateId(UUID.randomUUID().toString())
            .accessKey(UUID.randomUUID().toString())
            .secretKey(UUID.randomUUID().toString())
            .regionId(UUID.randomUUID().toString())
            .signName(UUID.randomUUID().toString())
            .remark(UUID.randomUUID().toString());
    }
}
