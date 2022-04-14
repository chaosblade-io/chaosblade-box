package com.alibaba.chaosblade.box.dao.infrastructure.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ChaosEventDispatcher implements InitializingBean {

    @Autowired
    private List<ChaosEventListener> chaosEventListeners;

    public void fireEvent(BaseChaosEvent chaosEvent) {
        for (ChaosEventListener chaosEventListener : chaosEventListeners) {
            if (chaosEventListener.support(chaosEvent)) {
                try {
                    chaosEventListener.onChaosEvent(chaosEvent);
                } catch (Exception ex) {
                    log.error("Exception on ChaosEventDispatcher.fireEvent:{}", chaosEvent.getClass(), ex);
                    if (chaosEventListener.interruptIfError()) {
                        throw new RuntimeException(ex.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AnnotationAwareOrderComparator.sort(chaosEventListeners);
    }
}
