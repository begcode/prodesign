package com.mycompany.myapp.settings.service.dto;

import com.mycompany.myapp.domain.enumeration.FieldParamType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.settings.domain.FillRuleItem}的DTO。
 */
@Schema(description = "填充规则条目")
@Data
@ToString
@EqualsAndHashCode
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FillRuleItemDTO implements Serializable {

    private Long id;

    /**
     * 排序值
     */
    @Schema(description = "排序值")
    private Integer sortValue;

    /**
     * 字段参数类型
     */
    @Schema(description = "字段参数类型")
    private FieldParamType fieldParamType;

    /**
     * 字段参数值
     */
    @Schema(description = "字段参数值")
    private String fieldParamValue;

    /**
     * 日期格式
     */
    @Schema(description = "日期格式")
    private String datePattern;

    /**
     * 序列长度
     */
    @Schema(description = "序列长度")
    private Integer seqLength;

    /**
     * 序列增量
     */
    @Schema(description = "序列增量")
    private Integer seqIncrement;

    /**
     * 序列起始值
     */
    @Schema(description = "序列起始值")
    private Integer seqStartValue;

    /**
     * 填充规则
     */
    @Schema(description = "填充规则")
    private SysFillRuleDTO fillRule;

    private Long fillRuleId;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public FillRuleItemDTO id(Long id) {
        this.id = id;
        return this;
    }

    public FillRuleItemDTO sortValue(Integer sortValue) {
        this.sortValue = sortValue;
        return this;
    }

    public FillRuleItemDTO fieldParamType(FieldParamType fieldParamType) {
        this.fieldParamType = fieldParamType;
        return this;
    }

    public FillRuleItemDTO fieldParamValue(String fieldParamValue) {
        this.fieldParamValue = fieldParamValue;
        return this;
    }

    public FillRuleItemDTO datePattern(String datePattern) {
        this.datePattern = datePattern;
        return this;
    }

    public FillRuleItemDTO seqLength(Integer seqLength) {
        this.seqLength = seqLength;
        return this;
    }

    public FillRuleItemDTO seqIncrement(Integer seqIncrement) {
        this.seqIncrement = seqIncrement;
        return this;
    }

    public FillRuleItemDTO seqStartValue(Integer seqStartValue) {
        this.seqStartValue = seqStartValue;
        return this;
    }

    public FillRuleItemDTO fillRule(SysFillRuleDTO fillRule) {
        this.fillRule = fillRule;
        return this;
    }
    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

}
