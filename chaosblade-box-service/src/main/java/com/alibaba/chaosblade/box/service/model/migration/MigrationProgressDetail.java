package com.alibaba.chaosblade.box.service.model.migration;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MigrationProgressDetail {
    private String name;

    private String type;

    private List<MigrationResultDetail> items;
}
