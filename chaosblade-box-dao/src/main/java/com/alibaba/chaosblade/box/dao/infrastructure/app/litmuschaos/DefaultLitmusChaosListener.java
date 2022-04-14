package com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos;

import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yefei
 */
@Component
public class DefaultLitmusChaosListener implements LitmusChaosListener {

    @Autowired
    private LitmusChaosFunctionLoader litmusChaosFunctionLoader;

    @Override
    public ChaosBladeMetaData onCompleted(LitmusChaosInvoker litmusChaosInvoker) throws Exception {
        litmusChaosFunctionLoader.load(litmusChaosInvoker, false);
        return ChaosBladeMetaData.getInstance();
    }
}
