package com.alibaba.chaosblade.box.dao.infrastructure.app.function;

import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import lombok.Getter;

/**
 * @author haibin
 *
 *
 */
@Getter
public class SceneFunctionAddedEvent extends BaseSceneFunctionEvent {

    public SceneFunctionAddedEvent(SceneFunctionDO sceneFunctionDO, boolean system) {
        super(sceneFunctionDO, system);
    }
}
