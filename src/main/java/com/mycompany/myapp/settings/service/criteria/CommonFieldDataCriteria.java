package com.mycompany.myapp.settings.service.criteria;

import com.mycompany.myapp.domain.enumeration.CommonFieldType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.settings.domain.CommonFieldData} entity. This class is used
 * in {@link com.mycompany.myapp.settings.web.rest.CommonFieldDataResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /common-field-data?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommonFieldDataCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private Boolean useOr;

    private CommonFieldDataCriteria and;

    private CommonFieldDataCriteria or;

    /**
     * Class for filtering CommonFieldType
     */
    public static class CommonFieldTypeFilter extends Filter<CommonFieldType> {

        public CommonFieldTypeFilter() {}

        public CommonFieldTypeFilter(String value) {
            CommonFieldType enumValue = CommonFieldType.getByValue(value);
            if (enumValue != null) {
                setEquals(enumValue);
            } else {
                enumValue = CommonFieldType.getByDesc(value);
                if (enumValue != null) {
                    setEquals(enumValue);
                }
            }
        }

        public CommonFieldTypeFilter(CommonFieldTypeFilter filter) {
            super(filter);
        }

        @Override
        public CommonFieldTypeFilter copy() {
            return new CommonFieldTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter value;

    private StringFilter label;

    private CommonFieldTypeFilter valueType;

    private StringFilter remark;

    private IntegerFilter sortValue;

    private BooleanFilter disabled;

    private StringFilter ownerEntityName;

    private StringFilter ownerEntityId;

    private LongFilter siteConfigId;

    private LongFilter dictionaryId;

    private Boolean distinct;

    public CommonFieldDataCriteria() {}

    public CommonFieldDataCriteria(CommonFieldDataCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.label = other.label == null ? null : other.label.copy();
        this.valueType = other.valueType == null ? null : other.valueType.copy();
        this.remark = other.remark == null ? null : other.remark.copy();
        this.sortValue = other.sortValue == null ? null : other.sortValue.copy();
        this.disabled = other.disabled == null ? null : other.disabled.copy();
        this.ownerEntityName = other.ownerEntityName == null ? null : other.ownerEntityName.copy();
        this.ownerEntityId = other.ownerEntityId == null ? null : other.ownerEntityId.copy();
        this.siteConfigId = other.siteConfigId == null ? null : other.siteConfigId.copy();
        this.dictionaryId = other.dictionaryId == null ? null : other.dictionaryId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CommonFieldDataCriteria copy() {
        return new CommonFieldDataCriteria(this);
    }

    public void setAnd(CommonFieldDataCriteria and) {
        this.and = and;
    }

    public CommonFieldDataCriteria getAnd() {
        return and;
    }

    public CommonFieldDataCriteria and() {
        if (and == null) {
            and = new CommonFieldDataCriteria();
        }
        return and;
    }

    public void setOr(CommonFieldDataCriteria or) {
        this.or = or;
    }

    public CommonFieldDataCriteria getOr() {
        return or;
    }

    public CommonFieldDataCriteria or() {
        if (or == null) {
            or = new CommonFieldDataCriteria();
        }
        return or;
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getValue() {
        return value;
    }

    public StringFilter value() {
        if (value == null) {
            value = new StringFilter();
        }
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public StringFilter getLabel() {
        return label;
    }

    public StringFilter label() {
        if (label == null) {
            label = new StringFilter();
        }
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public CommonFieldTypeFilter getValueType() {
        return valueType;
    }

    public CommonFieldTypeFilter valueType() {
        if (valueType == null) {
            valueType = new CommonFieldTypeFilter();
        }
        return valueType;
    }

    public void setValueType(CommonFieldTypeFilter valueType) {
        this.valueType = valueType;
    }

    public StringFilter getRemark() {
        return remark;
    }

    public StringFilter remark() {
        if (remark == null) {
            remark = new StringFilter();
        }
        return remark;
    }

    public void setRemark(StringFilter remark) {
        this.remark = remark;
    }

    public IntegerFilter getSortValue() {
        return sortValue;
    }

    public IntegerFilter sortValue() {
        if (sortValue == null) {
            sortValue = new IntegerFilter();
        }
        return sortValue;
    }

    public void setSortValue(IntegerFilter sortValue) {
        this.sortValue = sortValue;
    }

    public BooleanFilter getDisabled() {
        return disabled;
    }

    public BooleanFilter disabled() {
        if (disabled == null) {
            disabled = new BooleanFilter();
        }
        return disabled;
    }

    public void setDisabled(BooleanFilter disabled) {
        this.disabled = disabled;
    }

    public StringFilter getOwnerEntityName() {
        return ownerEntityName;
    }

    public StringFilter ownerEntityName() {
        if (ownerEntityName == null) {
            ownerEntityName = new StringFilter();
        }
        return ownerEntityName;
    }

    public void setOwnerEntityName(StringFilter ownerEntityName) {
        this.ownerEntityName = ownerEntityName;
    }

    public StringFilter getOwnerEntityId() {
        return ownerEntityId;
    }

    public StringFilter ownerEntityId() {
        if (ownerEntityId == null) {
            ownerEntityId = new StringFilter();
        }
        return ownerEntityId;
    }

    public void setOwnerEntityId(StringFilter ownerEntityId) {
        this.ownerEntityId = ownerEntityId;
    }

    public LongFilter getSiteConfigId() {
        return siteConfigId;
    }

    public LongFilter siteConfigId() {
        if (siteConfigId == null) {
            siteConfigId = new LongFilter();
        }
        return siteConfigId;
    }

    public void setSiteConfigId(LongFilter siteConfigId) {
        this.siteConfigId = siteConfigId;
    }

    public LongFilter getDictionaryId() {
        return dictionaryId;
    }

    public LongFilter dictionaryId() {
        if (dictionaryId == null) {
            dictionaryId = new LongFilter();
        }
        return dictionaryId;
    }

    public void setDictionaryId(LongFilter dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    public String getJhiCommonSearchKeywords() {
        return jhiCommonSearchKeywords;
    }

    public void setJhiCommonSearchKeywords(String jhiCommonSearchKeywords) {
        this.jhiCommonSearchKeywords = jhiCommonSearchKeywords;
    }

    public Boolean getUseOr() {
        return useOr;
    }

    public void setUseOr(Boolean useOr) {
        this.useOr = useOr;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommonFieldDataCriteria that = (CommonFieldDataCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(value, that.value) &&
            Objects.equals(label, that.label) &&
            Objects.equals(valueType, that.valueType) &&
            Objects.equals(remark, that.remark) &&
            Objects.equals(sortValue, that.sortValue) &&
            Objects.equals(disabled, that.disabled) &&
            Objects.equals(ownerEntityName, that.ownerEntityName) &&
            Objects.equals(ownerEntityId, that.ownerEntityId) &&
            Objects.equals(siteConfigId, that.siteConfigId) &&
            Objects.equals(dictionaryId, that.dictionaryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            value,
            label,
            valueType,
            remark,
            sortValue,
            disabled,
            ownerEntityName,
            ownerEntityId,
            siteConfigId,
            dictionaryId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonFieldDataCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (label != null ? "label=" + label + ", " : "") +
            (valueType != null ? "valueType=" + valueType + ", " : "") +
            (remark != null ? "remark=" + remark + ", " : "") +
            (sortValue != null ? "sortValue=" + sortValue + ", " : "") +
            (disabled != null ? "disabled=" + disabled + ", " : "") +
            (ownerEntityName != null ? "ownerEntityName=" + ownerEntityName + ", " : "") +
            (ownerEntityId != null ? "ownerEntityId=" + ownerEntityId + ", " : "") +
            (siteConfigId != null ? "siteConfigId=" + siteConfigId + ", " : "") +
            (dictionaryId != null ? "dictionaryId=" + dictionaryId + ", " : "") +
            (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
            "useOr=" + useOr +
            (and != null ? "and=" + and + ", " : "") +
            (or != null ? "or=" + or + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
