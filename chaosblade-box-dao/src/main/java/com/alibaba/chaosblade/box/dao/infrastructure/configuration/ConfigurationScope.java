package com.alibaba.chaosblade.box.dao.infrastructure.configuration;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author haibin.lhb
 *
 *
 */
@Data
public class ConfigurationScope implements Serializable {

    /**
     * 适用分组
     */
    private List<String> nodeGroups = new ArrayList<>();

    /**
     * 支持的小程序场景
     */
    private List<String> appCodes = new ArrayList<>();

}
