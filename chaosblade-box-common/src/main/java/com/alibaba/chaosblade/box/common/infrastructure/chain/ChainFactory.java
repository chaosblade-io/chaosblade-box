package com.alibaba.chaosblade.box.common.infrastructure.chain;

import com.alibaba.chaosblade.box.common.infrastructure.util.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.Iterator;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
public class ChainFactory {

    /**
     * 生成chain，顺序越小，调用越靠前
     *
     * @param chainClass
     * @param <T>
     * @return
     */
    public static <T extends ChainNode> T createSpringChain(ApplicationContext applicationContext,
        Class<T> chainClass) {
        List<T> chainNodes = SpringUtils.getBeans(applicationContext, chainClass);
        AnnotationAwareOrderComparator.sort(chainNodes);
        return createChain(chainNodes);
    }

    public static <T extends ChainNode> T createChain(List<T> chainNodes) {
        T chainNode = null;
        Iterator<T> iterator = chainNodes.iterator();
        T preNode = null;
        while (iterator.hasNext()) {
            T node = iterator.next();
            if (chainNode == null) {
                chainNode = node;
            } else {
                preNode.setNextChainNode(node);
            }
            preNode = node;
        }
        return chainNode;
    }

}
