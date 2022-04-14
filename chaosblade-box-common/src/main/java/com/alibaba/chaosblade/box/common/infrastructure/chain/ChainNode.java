package com.alibaba.chaosblade.box.common.infrastructure.chain;

/**
 * @author haibin
 *
 *
 */
public abstract class ChainNode<R, T extends ChainContext> {
    public ChainNode<R, T> getNext() {
        return next;
    }

    protected ChainNode<R, T> next;

    /**
     * invoke node
     *
     * @param context
     * @return
     * @throws Exception
     */
    public abstract R invoke(T context) throws Exception;

    public void setNextChainNode(ChainNode<R, T> chainNode) {
        this.next = chainNode;
    }

    public void init() {
        this.next.init();
    }

    public void destroy() {
        this.next.destroy();
    }

}
