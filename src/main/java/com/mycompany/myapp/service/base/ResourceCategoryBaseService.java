package com.mycompany.myapp.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.google.common.base.CaseFormat;
import com.mycompany.myapp.domain.ResourceCategory;
import com.mycompany.myapp.repository.ResourceCategoryRepository;
import com.mycompany.myapp.service.dto.ResourceCategoryDTO;
import com.mycompany.myapp.service.mapper.ResourceCategoryMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ResourceCategory}.
 */
public class ResourceCategoryBaseService<R extends ResourceCategoryRepository, E extends ResourceCategory>
    extends BaseServiceImpl<ResourceCategoryRepository, ResourceCategory> {

    private final Logger log = LoggerFactory.getLogger(ResourceCategoryBaseService.class);

    private final List<String> relationCacheNames = Arrays.asList(
        com.mycompany.myapp.domain.UploadFile.class.getName() + ".category",
        com.mycompany.myapp.domain.ResourceCategory.class.getName() + ".parent",
        com.mycompany.myapp.domain.UploadImage.class.getName() + ".category",
        com.mycompany.myapp.domain.ResourceCategory.class.getName() + ".children"
    );

    protected final ResourceCategoryRepository resourceCategoryRepository;

    protected final CacheManager cacheManager;

    protected final ResourceCategoryMapper resourceCategoryMapper;

    public ResourceCategoryBaseService(
        ResourceCategoryRepository resourceCategoryRepository,
        CacheManager cacheManager,
        ResourceCategoryMapper resourceCategoryMapper
    ) {
        this.resourceCategoryRepository = resourceCategoryRepository;
        this.cacheManager = cacheManager;
        this.resourceCategoryMapper = resourceCategoryMapper;
    }

    /**
     * Save a resourceCategory.
     *
     * @param resourceCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public ResourceCategoryDTO save(ResourceCategoryDTO resourceCategoryDTO) {
        log.debug("Request to save ResourceCategory : {}", resourceCategoryDTO);
        ResourceCategory resourceCategory = resourceCategoryMapper.toEntity(resourceCategoryDTO);
        clearChildrenCache();

        this.saveOrUpdate(resourceCategory);
        return findOne(resourceCategory.getId()).orElseThrow();
    }

    /**
     * Update a resourceCategory.
     *
     * @param resourceCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public ResourceCategoryDTO update(ResourceCategoryDTO resourceCategoryDTO) {
        log.debug("Request to update ResourceCategory : {}", resourceCategoryDTO);

        ResourceCategory resourceCategory = resourceCategoryMapper.toEntity(resourceCategoryDTO);

        resourceCategoryRepository.updateById(resourceCategory);
        return findOne(resourceCategoryDTO.getId()).orElseThrow();
    }

    /**
     * Partially update a resourceCategory.
     *
     * @param resourceCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<ResourceCategoryDTO> partialUpdate(ResourceCategoryDTO resourceCategoryDTO) {
        log.debug("Request to partially update ResourceCategory : {}", resourceCategoryDTO);

        return resourceCategoryRepository
            .findById(resourceCategoryDTO.getId())
            .map(existingResourceCategory -> {
                resourceCategoryMapper.partialUpdate(existingResourceCategory, resourceCategoryDTO);

                return existingResourceCategory;
            })
            .map(tempResourceCategory -> {
                resourceCategoryRepository.save(tempResourceCategory);
                return resourceCategoryMapper.toDto(resourceCategoryRepository.selectById(tempResourceCategory.getId()));
            });
    }

    /**
     * Get all the resourceCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<ResourceCategoryDTO> findAll(Page<ResourceCategory> pageable) {
        log.debug("Request to get all ResourceCategories");
        return this.page(pageable).convert(resourceCategoryMapper::toDto);
    }

    /**
     * Get one resourceCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ResourceCategoryDTO> findOne(Long id) {
        log.debug("Request to get ResourceCategory : {}", id);
        return Optional
            .ofNullable(resourceCategoryRepository.selectById(id))
            .map(resourceCategory -> {
                Binder.bindRelations(resourceCategory);
                return resourceCategory;
            })
            .map(resourceCategoryMapper::toDto);
    }

    /**
     * Delete the resourceCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ResourceCategory : {}", id);

        ResourceCategory resourceCategory = resourceCategoryRepository.selectById(id);
        if (resourceCategory.getChildren() != null) {
            resourceCategory.getChildren().forEach(subResourceCategory -> subResourceCategory.setParent(null));
            // todo 可能涉及到级联删除，需要手动处理，上述代码无效。
        }

        resourceCategoryRepository.deleteById(id);
    }

    /**
     * Update specified field by resourceCategory
     */
    @Transactional
    public void updateBatch(ResourceCategoryDTO changeResourceCategoryDTO, List<String> fieldNames, List<Long> ids) {
        UpdateWrapper<ResourceCategory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", ids);
        fieldNames.forEach(fieldName ->
            updateWrapper.set(
                CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                BeanUtil.getFieldValue(changeResourceCategoryDTO, fieldName)
            )
        );
        this.update(updateWrapper);
    }

    // 清除children缓存
    private void clearChildrenCache() {
        Objects.requireNonNull(cacheManager.getCache(com.mycompany.myapp.domain.ResourceCategory.class.getName() + ".children")).clear();
    }

    protected void clearRelationsCache() {
        this.relationCacheNames.forEach(cacheName -> Optional.ofNullable(cacheManager.getCache(cacheName)).ifPresent(Cache::clear));
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
