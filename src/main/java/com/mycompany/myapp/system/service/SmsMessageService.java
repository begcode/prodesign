package com.mycompany.myapp.system.service;

import com.mycompany.myapp.system.domain.SmsMessage;
import com.mycompany.myapp.system.repository.SmsMessageRepository;
import com.mycompany.myapp.system.service.base.SmsMessageBaseService;
import com.mycompany.myapp.system.service.mapper.SmsMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link SmsMessage}.
 */
@Service
public class SmsMessageService extends SmsMessageBaseService<SmsMessageRepository, SmsMessage> {

    private final Logger log = LoggerFactory.getLogger(SmsMessageService.class);

    public SmsMessageService(SmsMessageRepository smsMessageRepository, CacheManager cacheManager, SmsMessageMapper smsMessageMapper) {
        super(smsMessageRepository, cacheManager, smsMessageMapper);
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
