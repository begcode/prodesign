package com.mycompany.myapp.settings.service.mapper;

import com.mycompany.myapp.service.mapper.EntityMapper;
import com.mycompany.myapp.settings.domain.CommonFieldData;
import com.mycompany.myapp.settings.domain.Dictionary;
import com.mycompany.myapp.settings.domain.SiteConfig;
import com.mycompany.myapp.settings.service.dto.CommonFieldDataDTO;
import com.mycompany.myapp.settings.service.dto.DictionaryDTO;
import com.mycompany.myapp.settings.service.dto.SiteConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommonFieldData} and its DTO {@link CommonFieldDataDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommonFieldDataMapper extends EntityMapper<CommonFieldDataDTO, CommonFieldData> {
    @Mapping(target = "siteConfig", source = "siteConfig", qualifiedByName = "siteConfigId")
    @Mapping(target = "dictionary", source = "dictionary", qualifiedByName = "dictionaryId")
    CommonFieldDataDTO toDto(CommonFieldData s);

    @Named("siteConfigId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SiteConfigDTO toDtoSiteConfigId(SiteConfig siteConfig);

    @Named("dictionaryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DictionaryDTO toDtoDictionaryId(Dictionary dictionary);
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
