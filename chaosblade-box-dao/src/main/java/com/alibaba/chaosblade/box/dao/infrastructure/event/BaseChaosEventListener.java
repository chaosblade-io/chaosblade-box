package com.alibaba.chaosblade.box.dao.infrastructure.event;

/**
 * @author haibin
 *
 *
 */
public abstract class BaseChaosEventListener implements ChaosEventListener {

    @Override
    public boolean interruptIfError() {
        return false;
    }

}
