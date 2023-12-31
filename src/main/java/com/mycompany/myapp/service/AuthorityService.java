package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.service.base.AuthorityBaseService;
import com.mycompany.myapp.service.mapper.AuthorityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Authority}.
 */
@Service
public class AuthorityService extends AuthorityBaseService<AuthorityRepository, Authority> {

    private final Logger log = LoggerFactory.getLogger(AuthorityService.class);

    public AuthorityService(AuthorityRepository authorityRepository, CacheManager cacheManager, AuthorityMapper authorityMapper) {
        super(authorityRepository, cacheManager, authorityMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
