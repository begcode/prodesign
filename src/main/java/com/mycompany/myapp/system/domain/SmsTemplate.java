package com.mycompany.myapp.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.mycompany.myapp.domain.AbstractAuditingEntity;
import com.mycompany.myapp.domain.enumeration.MessageSendType;
import java.io.Serializable;
import lombok.*;

/**
 * 消息模板
 */
@TableName(value = "sms_template")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SmsTemplate extends AbstractAuditingEntity<Long, SmsTemplate> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @TableField(value = "id")
    private Long id;

    /**
     * 模板标题
     */
    @TableField(value = "name")
    private String name;

    /**
     * 模板CODE
     */
    @TableField(value = "code")
    private String code;

    /**
     * 模板类型
     */
    @TableField(value = "type")
    private MessageSendType type;

    /**
     * 模板内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 模板测试json
     */
    @TableField(value = "test_json")
    private String testJson;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public SmsTemplate id(Long id) {
        this.id = id;
        return this;
    }

    public SmsTemplate name(String name) {
        this.name = name;
        return this;
    }

    public SmsTemplate code(String code) {
        this.code = code;
        return this;
    }

    public SmsTemplate type(MessageSendType type) {
        this.type = type;
        return this;
    }

    public SmsTemplate content(String content) {
        this.content = content;
        return this;
    }

    public SmsTemplate testJson(String testJson) {
        this.testJson = testJson;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsTemplate)) {
            return false;
        }
        return getId() != null && getId().equals(((SmsTemplate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}
