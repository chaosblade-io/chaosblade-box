package com.alibaba.chaosblade.box.service.model.agent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SettingQueryInstallRequest extends SettingRequest {
    String Mode;

    Integer OsType;

    String RegionId;

    String HelmVersion;

    String Lang;
}
