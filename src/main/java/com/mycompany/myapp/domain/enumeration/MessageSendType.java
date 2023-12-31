package com.mycompany.myapp.domain.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 通知类型
 */
public enum MessageSendType {
    EMAIL("EMAIL", "EMAIL"),
    SMS("SMS", "SMS"),
    WECHAT("WECHAT", "WECHAT");

    @EnumValue
    @JsonValue
    private String value;

    private String desc;

    MessageSendType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static MessageSendType getByValue(String value) {
        for (MessageSendType enumMessageSendType : MessageSendType.values()) {
            if (enumMessageSendType.getValue().equals(value)) {
                return enumMessageSendType;
            }
        }
        return null;
    }

    public static MessageSendType getByDesc(String desc) {
        for (MessageSendType enumMessageSendType : MessageSendType.values()) {
            if (enumMessageSendType.getDesc().equals(desc)) {
                return enumMessageSendType;
            }
        }
        return null;
    }
}
