package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.dao.infrastructure.event.BaseChaosEvent;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class BaseSceneFunctionEvent extends BaseChaosEvent {
    protected SceneFunctionDO sceneFunctionDO;
    protected boolean system;

    public BaseSceneFunctionEvent(SceneFunctionDO sceneFunctionDO, boolean system) {
        super();
        this.sceneFunctionDO = sceneFunctionDO;
        this.system = system;
    }
}
