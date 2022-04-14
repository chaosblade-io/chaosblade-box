package com.alibaba.chaosblade.box.service.model.migration;

import com.alibaba.chaosblade.box.common.common.enums.MigrationStateEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MigrationResultDetail {
    private MigrationStateEnum status;

    private String name;

    private String type;

}
