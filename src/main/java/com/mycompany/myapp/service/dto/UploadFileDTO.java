package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.AbstractAuditingEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.domain.UploadFile}的DTO。
 */
@Schema(description = "上传文件")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UploadFileDTO extends AbstractAuditingEntity<Long, UploadFileDTO> {

    private Long id;

    /**
     * 完整文件名
     * 不含路径
     */
    @Schema(description = "完整文件名\n不含路径")
    private String fullName;

    /**
     * 文件名
     * 不含扩展名
     */
    @Schema(description = "文件名\n不含扩展名")
    private String name;

    /**
     * 缩略图Url地址
     */
    @Schema(description = "缩略图Url地址")
    private String thumb;

    /**
     * 扩展名
     */
    @Schema(description = "扩展名")
    private String ext;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型")
    private String type;

    /**
     * Url地址
     */
    @Schema(description = "Url地址")
    private String url;

    /**
     * 本地路径
     */
    @Schema(description = "本地路径")
    private String path;

    /**
     * 存储目录
     */
    @Schema(description = "存储目录")
    private String folder;

    /**
     * 实体名称
     */
    @Schema(description = "实体名称")
    private String ownerEntityName;

    /**
     * 使用实体ID
     */
    @Schema(description = "使用实体ID")
    private String ownerEntityId;

    /**
     * 业务标题
     */
    @Schema(description = "业务标题")
    private String businessTitle;

    /**
     * 业务自定义描述内容
     */
    @Schema(description = "业务自定义描述内容")
    private String businessDesc;

    /**
     * 业务状态
     */
    @Schema(description = "业务状态")
    private String businessStatus;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private ZonedDateTime createAt;

    /**
     * 文件大小
     */
    @Schema(description = "文件大小")
    private Long fileSize;

    /**
     * 被引次数
     */
    @Schema(description = "被引次数")
    private Long referenceCount;

    /**
     * 创建者Id
     */
    @Schema(description = "创建者Id")
    private Long createdBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private ZonedDateTime createdDate;

    /**
     * 修改者Id
     */
    @Schema(description = "修改者Id")
    private Long lastModifiedBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private ZonedDateTime lastModifiedDate;

    /**
     * 所属分类
     */
    @Schema(description = "所属分类")
    private ResourceCategoryDTO category;

    private Long categoryId;

    private MultipartFile file;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public UploadFileDTO id(Long id) {
        this.id = id;
        return this;
    }

    public UploadFileDTO fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public UploadFileDTO name(String name) {
        this.name = name;
        return this;
    }

    public UploadFileDTO thumb(String thumb) {
        this.thumb = thumb;
        return this;
    }

    public UploadFileDTO ext(String ext) {
        this.ext = ext;
        return this;
    }

    public UploadFileDTO type(String type) {
        this.type = type;
        return this;
    }

    public UploadFileDTO url(String url) {
        this.url = url;
        return this;
    }

    public UploadFileDTO path(String path) {
        this.path = path;
        return this;
    }

    public UploadFileDTO folder(String folder) {
        this.folder = folder;
        return this;
    }

    public UploadFileDTO ownerEntityName(String ownerEntityName) {
        this.ownerEntityName = ownerEntityName;
        return this;
    }

    public UploadFileDTO ownerEntityId(String ownerEntityId) {
        this.ownerEntityId = ownerEntityId;
        return this;
    }

    public UploadFileDTO businessTitle(String businessTitle) {
        this.businessTitle = businessTitle;
        return this;
    }

    public UploadFileDTO businessDesc(String businessDesc) {
        this.businessDesc = businessDesc;
        return this;
    }

    public UploadFileDTO businessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
        return this;
    }

    public UploadFileDTO createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public UploadFileDTO fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public UploadFileDTO referenceCount(Long referenceCount) {
        this.referenceCount = referenceCount;
        return this;
    }

    public UploadFileDTO createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public UploadFileDTO createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public UploadFileDTO lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public UploadFileDTO lastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public UploadFileDTO category(ResourceCategoryDTO category) {
        this.category = category;
        return this;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

}