package com.mycompany.myapp.settings.service;

import com.mycompany.myapp.settings.domain.RegionCode;
import com.mycompany.myapp.settings.repository.RegionCodeRepository;
import com.mycompany.myapp.settings.service.base.RegionCodeBaseService;
import com.mycompany.myapp.settings.service.mapper.RegionCodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link RegionCode}.
 */
@Service
public class RegionCodeService extends RegionCodeBaseService<RegionCodeRepository, RegionCode> {

    private final Logger log = LoggerFactory.getLogger(RegionCodeService.class);

    public RegionCodeService(RegionCodeRepository regionCodeRepository, CacheManager cacheManager, RegionCodeMapper regionCodeMapper) {
        super(regionCodeRepository, cacheManager, regionCodeMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
