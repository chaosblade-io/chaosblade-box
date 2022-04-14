package com.alibaba.chaosblade.box.dao.infrastructure.app;

import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ChaosAppLoaderConfig {

    private List<MiniAppProcessor> miniAppProcessors;
}
