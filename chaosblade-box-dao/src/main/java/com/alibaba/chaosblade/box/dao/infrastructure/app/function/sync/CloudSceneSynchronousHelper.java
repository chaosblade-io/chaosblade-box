package com.alibaba.chaosblade.box.dao.infrastructure.app.function.sync;

import com.alibaba.chaosblade.box.dao.infrastructure.app.function.DefaultOnlineAppLoadFactory;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.SceneSynchronousHelper;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CloudSceneSynchronousHelper extends SceneSynchronousHelper {

    @Autowired
    private DefaultOnlineAppLoadFactory defaultOnlineAppLoadFactory;

    @Override
    protected Map<String, SceneFunctionDO> loadSceneFunctionConfig() {
        return new HashMap<>();
    }

    @Override
    protected List<String> loadDefaultOnlineApps() {
        return defaultOnlineAppLoadFactory.getDefaultFcuntions();
    }
}
