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
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.service.criteria.AuthorityCriteria;
import com.mycompany.myapp.service.dto.AuthorityDTO;
import com.mycompany.myapp.service.mapper.AuthorityMapper;
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
 * 用于对数据库中的{@link Authority}实体执行复杂查询的Service。
 * 主要输入是一个{@link AuthorityCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link AuthorityDTO}列表{@link List} 或 {@link AuthorityDTO} 的分页列表 {@link Page}。
 */
@Service
public class AuthorityQueryService implements QueryService<Authority> {

    private final Logger log = LoggerFactory.getLogger(AuthorityQueryService.class);

    protected final AuthorityRepository authorityRepository;

    protected final AuthorityMapper authorityMapper;

    public AuthorityQueryService(AuthorityRepository authorityRepository, AuthorityMapper authorityMapper) {
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
    }

    /**
     * Return a {@link List} of {@link AuthorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<AuthorityDTO> findByCriteria(AuthorityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return authorityMapper.toDto(authorityRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link AuthorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<AuthorityDTO> findByCriteria(AuthorityCriteria criteria, Page<Authority> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return authorityRepository.selectPage(page, queryWrapper).convert(authorityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(AuthorityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return authorityRepository.selectCount(queryWrapper);
    }

    /**
     * Get all the authorities for parent is null.
     *
     * @param page the pagination information
     * @return the list of entities
     */
    public IPage<AuthorityDTO> findAllTop(AuthorityCriteria criteria, Page<Authority> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.parentId().setSpecified(false);
        final QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        return authorityRepository
            .selectPage(page, queryWrapper)
            .convert(authority -> {
                Binder.bindRelations(authority, new String[] { "viewPermissions", "apiPermissions", "parent", "users", "department" });
                return authorityMapper.toDto(authority);
            });
    }

    /**
     * Get all the authorities for parent is parentId.
     *
     * @param parentId the Id of parent
     * @return the list of entities
     */
    public List<AuthorityDTO> findChildrenByParentId(Long parentId) {
        log.debug("Request to get all Authorities for parent is parentId");
        return authorityRepository
            .selectList(new LambdaUpdateWrapper<Authority>().eq(Authority::getParentId, parentId))
            .stream()
            .map(authority -> {
                Binder.bindRelations(authority, new String[] { "viewPermissions", "apiPermissions", "parent", "users", "department" });
                return authorityMapper.toDto(authority);
            })
            .collect(Collectors.toList());
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, AuthorityCriteria criteria) {
        return (List<T>) authorityRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, AuthorityCriteria criteria) {
        return authorityRepository.selectCount(createQueryWrapper(criteria));
    }

    /**
     * Function to convert {@link AuthorityCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<Authority> createQueryWrapper(AuthorityCriteria criteria) {
        QueryWrapper<Authority> queryWrapper = new DynamicJoinQueryWrapper<>(Authority.class, null);
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
                            "order"
                        )
                    );
                });
            } else {
                queryWrapper.and(q -> {
                    q.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "name"));
                    q.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "code"));
                    q.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "info"));
                });
            }
        }
        return queryWrapper;
    }

    private QueryWrapper<Authority> createQueryWrapper(QueryWrapper<Authority> queryWrapper, Boolean useOr, AuthorityCriteria criteria) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            if (criteria.getId() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildRangeSpecification(criteria.getId(), "id", useOr));
            }
            if (criteria.getName() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildStringSpecification(criteria.getName(), "name", useOr));
            }
            if (criteria.getCode() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildStringSpecification(criteria.getCode(), "code", useOr));
            }
            if (criteria.getInfo() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildStringSpecification(criteria.getInfo(), "info", useOr));
            }
            if (criteria.getOrder() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildRangeSpecification(criteria.getOrder(), "order", useOr));
            }
            if (criteria.getDisplay() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildSpecification(criteria.getDisplay(), "display", useOr));
            }
            if (criteria.getChildrenId() != null) {
                // todo 未实现
            }
            if (criteria.getChildrenName() != null) {
                // todo 未实现 one-to-many;name
            }
            if (criteria.getViewPermissionsId() != null) {
                queryWrapper =
                    CriteriaUtil.build(
                        useOr,
                        queryWrapper,
                        buildRangeSpecification(criteria.getViewPermissionsId(), "view_permission_left_join.id", useOr)
                    );
            }
            if (criteria.getViewPermissionsText() != null) {
                queryWrapper =
                    CriteriaUtil.build(
                        useOr,
                        queryWrapper,
                        buildStringSpecification(criteria.getViewPermissionsText(), "view_permission_left_join.text", useOr)
                    );
            }
            if (criteria.getApiPermissionsId() != null) {
                queryWrapper =
                    CriteriaUtil.build(
                        useOr,
                        queryWrapper,
                        buildRangeSpecification(criteria.getApiPermissionsId(), "api_permission_left_join.id", useOr)
                    );
            }
            if (criteria.getApiPermissionsName() != null) {
                queryWrapper =
                    CriteriaUtil.build(
                        useOr,
                        queryWrapper,
                        buildStringSpecification(criteria.getApiPermissionsName(), "api_permission_left_join.name", useOr)
                    );
            }
            if (criteria.getParentId() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildRangeSpecification(criteria.getParentId(), "parent_id", useOr));
            }
            if (criteria.getParentName() != null) {
                queryWrapper =
                    CriteriaUtil.build(
                        useOr,
                        queryWrapper,
                        buildStringSpecification(criteria.getParentName(), "jhi_authority_left_join.name", useOr)
                    );
            }
            if (criteria.getUsersId() != null) {
                queryWrapper =
                    CriteriaUtil.build(useOr, queryWrapper, buildRangeSpecification(criteria.getUsersId(), "jhi_user_left_join.id", useOr));
            }
            if (criteria.getUsersFirstName() != null) {
                queryWrapper =
                    CriteriaUtil.build(
                        useOr,
                        queryWrapper,
                        buildStringSpecification(criteria.getUsersFirstName(), "jhi_user_left_join.first_name", useOr)
                    );
            }
            if (criteria.getDepartmentId() != null) {
                queryWrapper =
                    CriteriaUtil.build(
                        useOr,
                        queryWrapper,
                        buildRangeSpecification(criteria.getDepartmentId(), "department_left_join.id", useOr)
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
     * Return a {@link IPage} of {@link AuthorityDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<AuthorityDTO> findByQueryWrapper(QueryWrapper<Authority> queryWrapper, Page<Authority> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return authorityRepository.selectPage(page, queryWrapper).convert(authorityMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(AuthorityCriteria criteria) {
        QueryWrapper<Authority> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        if (criteria.getId() != null) {
            getAggregateAndGroupBy(criteria.getId(), "id", selectFields, groupByFields);
        }
        if (criteria.getName() != null) {
            getAggregateAndGroupBy(criteria.getName(), "name", selectFields, groupByFields);
        }
        if (criteria.getCode() != null) {
            getAggregateAndGroupBy(criteria.getCode(), "code", selectFields, groupByFields);
        }
        if (criteria.getInfo() != null) {
            getAggregateAndGroupBy(criteria.getInfo(), "info", selectFields, groupByFields);
        }
        if (criteria.getOrder() != null) {
            getAggregateAndGroupBy(criteria.getOrder(), "order", selectFields, groupByFields);
        }
        if (criteria.getDisplay() != null) {
            getAggregateAndGroupBy(criteria.getDisplay(), "display", selectFields, groupByFields);
        }
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return authorityRepository.selectMaps(queryWrapper);
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
