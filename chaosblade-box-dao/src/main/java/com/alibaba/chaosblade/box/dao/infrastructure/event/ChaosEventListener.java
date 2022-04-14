package com.alibaba.chaosblade.box.dao.infrastructure.event;

import java.util.EventListener;

/**
 * @author haibin.lhb
 *
 * 
 */
public interface ChaosEventListener extends EventListener {

    public boolean support(BaseChaosEvent event);

    public void onChaosEvent(BaseChaosEvent event);

    public boolean interruptIfError();
}
