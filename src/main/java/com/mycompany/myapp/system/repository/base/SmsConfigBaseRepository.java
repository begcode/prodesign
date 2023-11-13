package com.mycompany.myapp.system.repository.base;

import com.diboot.core.mapper.BaseCrudMapper;
import com.mycompany.myapp.system.domain.SmsConfig;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the SmsConfig entity.
 */
@SuppressWarnings("unused")
@NoRepositoryBean
public interface SmsConfigBaseRepository<E extends SmsConfig> extends BaseCrudMapper<SmsConfig> {
    default List<SmsConfig> findAll() {
        return this.selectList(null);
    }

    default Optional<SmsConfig> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
