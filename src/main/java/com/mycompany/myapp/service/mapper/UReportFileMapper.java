package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.UReportFile;
import com.mycompany.myapp.service.dto.UReportFileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UReportFile} and its DTO {@link UReportFileDTO}.
 */
@Mapper(componentModel = "spring")
public interface UReportFileMapper extends EntityMapper<UReportFileDTO, UReportFile> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
