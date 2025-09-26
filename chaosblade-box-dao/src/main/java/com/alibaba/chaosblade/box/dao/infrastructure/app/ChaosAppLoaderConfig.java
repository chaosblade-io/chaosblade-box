package com.alibaba.chaosblade.box.dao.infrastructure.app;

import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ChaosAppLoaderConfig {

  private List<MiniAppProcessor> miniAppProcessors;
}
