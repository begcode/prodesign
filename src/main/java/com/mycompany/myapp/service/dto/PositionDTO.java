package com.mycompany.myapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.domain.Position}的DTO。
 */
@Schema(description = "岗位\n")
@Data
@ToString
@EqualsAndHashCode
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionDTO implements Serializable {

    private Long id;

    /**
     * 岗位代码
     */
    @NotNull
    @Size(max = 50)
    @Schema(description = "岗位代码", required = true)
    private String code;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 50)
    @Schema(description = "名称", required = true)
    private String name;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sortNo;

    /**
     * 描述
     */
    @Size(max = 200)
    @Schema(description = "描述")
    private String description;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public PositionDTO id(Long id) {
        this.id = id;
        return this;
    }

    public PositionDTO code(String code) {
        this.code = code;
        return this;
    }

    public PositionDTO name(String name) {
        this.name = name;
        return this;
    }

    public PositionDTO sortNo(Integer sortNo) {
        this.sortNo = sortNo;
        return this;
    }

    public PositionDTO description(String description) {
        this.description = description;
        return this;
    }
    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

}
