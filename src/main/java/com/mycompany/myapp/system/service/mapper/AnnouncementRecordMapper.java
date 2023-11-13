package com.mycompany.myapp.system.service.mapper;

import com.mycompany.myapp.service.mapper.EntityMapper;
import com.mycompany.myapp.system.domain.AnnouncementRecord;
import com.mycompany.myapp.system.service.dto.AnnouncementRecordDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AnnouncementRecord} and its DTO {@link AnnouncementRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnnouncementRecordMapper extends EntityMapper<AnnouncementRecordDTO, AnnouncementRecord> {
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    AnnouncementRecord toEntity(AnnouncementRecordDTO announcementRecordDTO);
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}