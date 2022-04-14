package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 * 
 */
@Slf4j
@Component
public class ChaosBladeListenerImpl implements ChaosBladeListener {

    @Autowired
    private ChaosBladeFunctionLoader chaosBladeFunctionLoader;

    @Override
    public ChaosBladeMetaData onCompleted(ChaosBladeInvoker chaosBladeInvoker) throws Exception {
        chaosBladeFunctionLoader.load(chaosBladeInvoker, false);
        return ChaosBladeMetaData.getInstance();
    }

}
