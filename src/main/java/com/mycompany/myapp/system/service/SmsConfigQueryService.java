package com.mycompany.myapp.system.service;

import static tech.jhipster.service.mybatis.AggregateUtil.buildAggregate;
import static tech.jhipster.service.mybatis.AggregateUtil.buildGroupBy;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diboot.core.binding.query.dynamic.DynamicJoinQueryWrapper;
import com.mycompany.myapp.system.domain.*; // for static metamodels
import com.mycompany.myapp.system.domain.SmsConfig;
import com.mycompany.myapp.system.repository.SmsConfigRepository;
import com.mycompany.myapp.system.service.criteria.SmsConfigCriteria;
import com.mycompany.myapp.system.service.dto.SmsConfigDTO;
import com.mycompany.myapp.system.service.mapper.SmsConfigMapper;
import java.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.jhipster.service.aggregate.*;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.mybatis.CriteriaUtil;
import tech.jhipster.service.mybatis.QueryService;

/**
 * 用于对数据库中的{@link SmsConfig}实体执行复杂查询的Service。
 * 主要输入是一个{@link SmsConfigCriteria}，它被转换为{@link QueryWrapper}，
 * 所有字段过滤器都将应用到表达式中。
 * 它返回满足条件的{@link SmsConfigDTO}列表{@link List} 或 {@link SmsConfigDTO} 的分页列表 {@link Page}。
 */
@Service
public class SmsConfigQueryService implements QueryService<SmsConfig> {

    private final Logger log = LoggerFactory.getLogger(SmsConfigQueryService.class);

    protected final SmsConfigRepository smsConfigRepository;

    protected final SmsConfigMapper smsConfigMapper;

    public SmsConfigQueryService(SmsConfigRepository smsConfigRepository, SmsConfigMapper smsConfigMapper) {
        this.smsConfigRepository = smsConfigRepository;
        this.smsConfigMapper = smsConfigMapper;
    }

    /**
     * Return a {@link List} of {@link SmsConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */

    public List<SmsConfigDTO> findByCriteria(SmsConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final QueryWrapper<SmsConfig> queryWrapper = createQueryWrapper(criteria);
        return smsConfigMapper.toDto(smsConfigRepository.selectList(queryWrapper));
    }

    /**
     * Return a {@link IPage} of {@link SmsConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    public IPage<SmsConfigDTO> findByCriteria(SmsConfigCriteria criteria, Page<SmsConfig> page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final QueryWrapper<SmsConfig> queryWrapper = createQueryWrapper(criteria);
        return smsConfigRepository.selectPage(page, queryWrapper).convert(smsConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public long countByCriteria(SmsConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final QueryWrapper<SmsConfig> queryWrapper = createQueryWrapper(criteria);
        return smsConfigRepository.selectCount(queryWrapper);
    }

    public <T> List<T> getFieldByCriteria(Class<T> clazz, String fieldName, Boolean distinct, SmsConfigCriteria criteria) {
        return (List<T>) smsConfigRepository.selectObjs(createQueryWrapper(criteria).select(fieldName));
    }

    public long countByFieldNameAndCriteria(String fieldName, Boolean distinct, SmsConfigCriteria criteria) {
        return smsConfigRepository.selectCount(createQueryWrapper(criteria));
    }

    /**
     * Function to convert {@link SmsConfigCriteria} to a {@link QueryWrapper}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link QueryWrapper} of the entity.
     */
    public QueryWrapper<SmsConfig> createQueryWrapper(SmsConfigCriteria criteria) {
        QueryWrapper<SmsConfig> queryWrapper = new DynamicJoinQueryWrapper<>(SmsConfig.class, null);
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
                });
            } else {
                queryWrapper.and(q -> {
                    q.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "sms_code"));
                    q.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "template_id"));
                    q.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "access_key"));
                    q.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "secret_key"));
                    q.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "region_id"));
                    q.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "sign_name"));
                    q.or(buildStringSpecification(new StringFilter().setContains(criteria.getJhiCommonSearchKeywords()), "remark"));
                });
            }
        }
        return queryWrapper;
    }

    private QueryWrapper<SmsConfig> createQueryWrapper(QueryWrapper<SmsConfig> queryWrapper, Boolean useOr, SmsConfigCriteria criteria) {
        if (criteria != null) {
            if (useOr == null) {
                useOr = false;
            }
            if (criteria.getId() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildRangeSpecification(criteria.getId(), "id", useOr));
            }
            if (criteria.getProvider() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildSpecification(criteria.getProvider(), "provider", useOr));
            }
            if (criteria.getSmsCode() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildStringSpecification(criteria.getSmsCode(), "sms_code", useOr));
            }
            if (criteria.getTemplateId() != null) {
                queryWrapper =
                    CriteriaUtil.build(useOr, queryWrapper, buildStringSpecification(criteria.getTemplateId(), "template_id", useOr));
            }
            if (criteria.getAccessKey() != null) {
                queryWrapper =
                    CriteriaUtil.build(useOr, queryWrapper, buildStringSpecification(criteria.getAccessKey(), "access_key", useOr));
            }
            if (criteria.getSecretKey() != null) {
                queryWrapper =
                    CriteriaUtil.build(useOr, queryWrapper, buildStringSpecification(criteria.getSecretKey(), "secret_key", useOr));
            }
            if (criteria.getRegionId() != null) {
                queryWrapper =
                    CriteriaUtil.build(useOr, queryWrapper, buildStringSpecification(criteria.getRegionId(), "region_id", useOr));
            }
            if (criteria.getSignName() != null) {
                queryWrapper =
                    CriteriaUtil.build(useOr, queryWrapper, buildStringSpecification(criteria.getSignName(), "sign_name", useOr));
            }
            if (criteria.getRemark() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildStringSpecification(criteria.getRemark(), "remark", useOr));
            }
            if (criteria.getEnabled() != null) {
                queryWrapper = CriteriaUtil.build(useOr, queryWrapper, buildSpecification(criteria.getEnabled(), "enabled", useOr));
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
     * Return a {@link IPage} of {@link SmsConfigDTO} which matches the criteria from the database.
     * @param queryWrapper The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    public IPage<SmsConfigDTO> findByQueryWrapper(QueryWrapper<SmsConfig> queryWrapper, Page<SmsConfig> page) {
        log.debug("find by criteria : {}, page: {}", queryWrapper, page);
        return smsConfigRepository.selectPage(page, queryWrapper).convert(smsConfigMapper::toDto);
    }

    public List<Map<String, Object>> statsByAggregateCriteria(SmsConfigCriteria criteria) {
        QueryWrapper<SmsConfig> queryWrapper = createQueryWrapper(criteria);
        List<String> selectFields = new ArrayList<>();
        List<String> groupByFields = new ArrayList<>();
        if (criteria.getId() != null) {
            getAggregateAndGroupBy(criteria.getId(), "id", selectFields, groupByFields);
        }
        if (criteria.getProvider() != null) {
            getAggregateAndGroupBy(criteria.getProvider(), "provider", selectFields, groupByFields);
        }
        if (criteria.getSmsCode() != null) {
            getAggregateAndGroupBy(criteria.getSmsCode(), "sms_code", selectFields, groupByFields);
        }
        if (criteria.getTemplateId() != null) {
            getAggregateAndGroupBy(criteria.getTemplateId(), "template_id", selectFields, groupByFields);
        }
        if (criteria.getAccessKey() != null) {
            getAggregateAndGroupBy(criteria.getAccessKey(), "access_key", selectFields, groupByFields);
        }
        if (criteria.getSecretKey() != null) {
            getAggregateAndGroupBy(criteria.getSecretKey(), "secret_key", selectFields, groupByFields);
        }
        if (criteria.getRegionId() != null) {
            getAggregateAndGroupBy(criteria.getRegionId(), "region_id", selectFields, groupByFields);
        }
        if (criteria.getSignName() != null) {
            getAggregateAndGroupBy(criteria.getSignName(), "sign_name", selectFields, groupByFields);
        }
        if (criteria.getRemark() != null) {
            getAggregateAndGroupBy(criteria.getRemark(), "remark", selectFields, groupByFields);
        }
        if (criteria.getEnabled() != null) {
            getAggregateAndGroupBy(criteria.getEnabled(), "enabled", selectFields, groupByFields);
        }
        if (CollectionUtils.isNotEmpty(selectFields)) {
            queryWrapper.select(selectFields.toArray(new String[0])).groupBy(CollectionUtils.isNotEmpty(groupByFields), groupByFields);
            return smsConfigRepository.selectMaps(queryWrapper);
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
