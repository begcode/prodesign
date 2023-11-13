package com.mycompany.myapp.service;

import static tech.jhipster.service.mybatis.AggregateUtil.buildAggregate;
import static tech.jhipster.service.mybatis.AggregateUtil.buildGroupBy;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.Binder;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ResourceCategory;
import com.mycompany.myapp.repository.ResourceCategoryRepository;
import com.mycompany.myapp.service.criteria.ResourceCategoryCriteria;
import com.mycompany.myapp.service.dto.ResourceCategoryDTO;
import com.mycompany.myapp.service.mapper.ResourceCategoryMapper;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jhipster.service.aggregate.*;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.mybatis.CriteriaUtil;
import tech.jhipster.service.mybatis.QueryService;

/**
 * 用于对数据库中的{@link ResourceCategory}实体执行复杂查询的Service。
 * 主要输入是一个{@link ResourceCategoryCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link ResourceCategoryDTO}列表{@link List} 或 {@link ResourceCategoryDTO} 的分页列表 {@link Page}。
 */
@Service
public class ResourceCategoryQueryService implements QueryService<ResourceCategory> {

    private final Logger log = LoggerFactory.getLogger(ResourceCategoryQueryService.class);

    protected final ResourceCategoryRepository resourceCategoryRepository;

    protected final ResourceCategoryMapper resourceCategoryMapper;

    public ResourceCategoryQueryService(
        ResourceCategoryRepository resourceCategoryRepository,
        ResourceCategoryMapper resourceCategoryMapper
    ) {
        this.resourceCategoryRepository = resourceCategoryRepository;
        this.resourceCategoryMapper = resourceCategoryMapper;
    }

    /**
     * Return a {@link List} of {@link ResourceCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<ResourceCategoryDTO> findByCriteria(ResourceCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<ResourceCategory> queryWrapper = createQueryWrapper(criteria);
        return resourceCategoryMapper.toDto(resourceCategoryRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link ResourceCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<ResourceCategoryDTO> findByCriteria(ResourceCategoryCriteria criteria, Page<ResourceCategory> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<ResourceCategory> queryWrapper = createQueryWrapper(criteria);
        return resourceCategoryRepository.selectPage(page, queryWrapper).convert(resourceCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(ResourceCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<ResourceCategory> queryWrapper = createQueryWrapper(criteria);
        return resourceCategoryRepository.selectCount(queryWrapper);
    }

    /**
     * Get all the resourceCategories for parent is null.
     *
     * @param page the pagination information
     * @return the list of entities
     */
    public IPage<ResourceCategoryDTO> findAllTop(ResourceCategoryCriteria criteria, Page<ResourceCategory> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.parentId().setSpecified(false);
        final QueryWrapper<ResourceCategory> queryWrapper = createQueryWrapper(criteria);
        return resourceCategoryRepository
            .selectPage(page, queryWrapper)
            .convert(resourceCategory -> {
                Binder.bindRelations(resourceCategory, new String[] { "files", "images", "parent" });
                return resourceCategoryMapper.toDto(resourceCategory);
            });
    }

    /**
     * Get all the resourceCategories for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<ResourceCategoryDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all ResourceCategories for parent is parentId");
        return resourceCategoryRepository
            .selectList(new LambdaUpdateWrapper<ResourceCategory>().eq(ResourceCategory::getParentId, parentId))
            .stream()
            .map(resourceCategory -> {
                Binder.bindRelations(resourceCategory, new String[] { "files", "images", "parent" });
                return resourceCategoryMapper.toDto(resourceCategory);
            })
            .collect(Collectors.toList());
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, ResourceCategoryCriteria criteria) {
        return (List<T>) resourceCategoryRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, ResourceCategoryCriteria criteria) {
        return resourceCategoryRepository.selectCount(createQueryWrapper(criteria));
    }

    /**
     * Function to convert {@link ResourceCategoryCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<ResourceCategory> createQueryWrapper(ResourceCategoryCriteria criteria) {
        QueryWrapper<ResourceCategory> queryWrapper = new DynamicJoinQueryWrapper<>(ResourceCategory.class, null);
        queryWrapper = createQueryWrapper(queryWrapper, criteria.getUseOr(), criteria);
        if (StringUtils.isNotEmpty(criteria.getJhiCommonSearchKeywords())) {
            if (StringUtils.isNumeric(criteria.getJhiCommonSearchKeywords())) {
                queryWrapper.and(q -> {
                    q.or(buildSpecification(new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())), "id"));
                    q.or(
                        buildRangeSpecification(
                            (LongFilter) new LongFilter().setEquals(Long.valueOf(criteria.getJhiCommonSearchKeywords())),
                            "id"
                        )
                    );
                    q.or(
                        buildRangeSpecification(
                            (IntegerFilter) new IntegerFilter().setEquals(Integer.valueOf(criteria.getJhiCommonSearchKeywords())),
                            "order_number"
                        )
                    );
                });
            } else {
                queryWrapper.and(q -> {
                    q.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "title"));
                    q.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "code"));
                });
            }
        }
        return queryWrapper;
    }

    private QueryWrapper<ResourceCategory> createQueryWrapper(
        QueryWrapper<ResourceCategory> queryWrapper,
        Boolean useOr,
        ResourceCategoryCriteria criteria
    ) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            if (criteria.getId() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildRangeSpecification(criteria.getId(), "id", useOr));
            }
            if (criteria.getTitle() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildStringSpecification(criteria.getTitle(), "title", useOr));
            }
            if (criteria.getCode() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildStringSpecification(criteria.getCode(), "code", useOr));
            }
            if (criteria.getOrderNumber() != null) {
                queryWrapper =
                    CriteriaUtil.build(useOr, queryWrapper, buildRangeSpecification(criteria.getOrderNumber(), "order_number", useOr));
            }
            if (criteria.getFilesId() != null) {
                // todo 未实现
            }
            if (criteria.getFilesUrl() != null) {
                // todo 未实现 one-to-many;url
            }
            if (criteria.getChildrenId() != null) {
                // todo 未实现
            }
            if (criteria.getChildrenTitle() != null) {
                // todo 未实现 one-to-many;title
            }
            if (criteria.getImagesId() != null) {
                // todo 未实现
            }
            if (criteria.getImagesUrl() != null) {
                // todo 未实现 one-to-many;url
            }
            if (criteria.getParentId() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildRangeSpecification(criteria.getParentId(), "parent_id", useOr));
            }
            if (criteria.getParentTitle() != null) {
                queryWrapper =
                    CriteriaUtil.build(
                        useOr,
                        queryWrapper,
                        buildStringSpecification(criteria.getParentTitle(), "resource_category_left_join.title", useOr)
                    );
            }
            if (criteria.getAnd() != null) {
                Map<String, Object> stringObjectMap = BeanUtil.beanToMap(criteria.getAnd(), false, true);
                if (
                    !((stringObjectMap.containsKey("useOr") && stringObjectMap.keySet().size() == 1) ||
                        ObjectUtils.isEmpty(stringObjectMap))
                ) {
                    queryWrapper.and(q -> createQueryWrapper(q, criteria.getAnd().getUseOr(), criteria.getAnd()));
                }
            } else {
                if (criteria.getOr() != null) {
                    Map<String, Object> stringObjectMap = BeanUtil.beanToMap(criteria.getOr(), false, true);
                    if (
                        !((stringObjectMap.containsKey("useOr") && stringObjectMap.keySet().size() == 1) ||
                            ObjectUtils.isEmpty(stringObjectMap))
                    ) {
                        queryWrapper.or(q -> createQueryWrapper(q, criteria.getOr().getUseOr(), criteria.getOr()));
                    }
                }
            }
        }
        return queryWrapper;
    }

    /**
     * Return a {@link IPage} of {@link ResourceCategoryDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<ResourceCategoryDTO> findByQueryWrapper(QueryWrapper<ResourceCategory> queryWrapper, Page<ResourceCategory> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return resourceCategoryRepository.selectPage(page, queryWrapper).convert(resourceCategoryMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(ResourceCategoryCriteria criteria) {
        QueryWrapper<ResourceCategory> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        if (criteria.getId() != null) {
            getAggregateAndGroupBy(criteria.getId(), "id", selectFields, groupByFields);
        }
        if (criteria.getTitle() != null) {
            getAggregateAndGroupBy(criteria.getTitle(), "title", selectFields, groupByFields);
        }
        if (criteria.getCode() != null) {
            getAggregateAndGroupBy(criteria.getCode(), "code", selectFields, groupByFields);
        }
        if (criteria.getOrderNumber() != null) {
            getAggregateAndGroupBy(criteria.getOrderNumber(), "order_number", selectFields, groupByFields);
        }
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return resourceCategoryRepository.selectMaps(queryWrapper);
        }
        return Collections.emptyList();
    }

    private void getAggregateAndGroupBy(Filter<?> filter, String fieldName, List<String> selects, List<String> groupBys) {
        if (filter.getAggregate() != null) {
            if (filter.getAggregate() instanceof NumberAggregate) {
                buildAggregate((NumberAggregate) filter.getAggregate(), fieldName, selects);
            } else {
                buildAggregate(filter.getAggregate(), fieldName, selects);
            }
        }
        if (filter.getGroupBy() != null) {
            if (filter.getGroupBy() instanceof DateTimeGroupBy) {
                buildGroupBy((DateTimeGroupBy) filter.getGroupBy(), fieldName, groupBys, selects);
            } else {
                buildGroupBy(filter.getGroupBy(), fieldName, groupBys, selects);
            }
        }
    }
}