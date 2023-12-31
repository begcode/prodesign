package com.mycompany.myapp.settings.service;

import com.mycompany.myapp.settings.domain.SiteConfig;
import com.mycompany.myapp.settings.repository.SiteConfigRepository;
import com.mycompany.myapp.settings.service.base.SiteConfigBaseService;
import com.mycompany.myapp.settings.service.mapper.SiteConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link SiteConfig}.
 */
@Service
public class SiteConfigService extends SiteConfigBaseService<SiteConfigRepository, SiteConfig> {

    private final Logger log = LoggerFactory.getLogger(SiteConfigService.class);

    public SiteConfigService(SiteConfigRepository siteConfigRepository, CacheManager cacheManager, SiteConfigMapper siteConfigMapper) {
        super(siteConfigRepository, cacheManager, siteConfigMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
