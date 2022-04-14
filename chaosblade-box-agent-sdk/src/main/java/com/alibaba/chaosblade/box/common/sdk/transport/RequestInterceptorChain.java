package com.alibaba.chaosblade.box.common.sdk.transport;

/**
 * Thread Safe
 *
 * @author changjun.xcj
 */
public abstract class RequestInterceptorChain implements RequestInterceptor {
    private RequestInterceptor interceptor;

    ///**
    // * Reverse chain
    // *
    // * @param requestInterceptor
    // * @return
    // */
    //public static RequestInterceptor reverse(RequestInterceptor requestInterceptor) {
    //    if (requestInterceptor == null) {
    //        return requestInterceptor;
    //    }
    //    if (!(requestInterceptor instanceof RequestInterceptorChain)) {
    //        return requestInterceptor;
    //    }
    //    RequestInterceptorChain requestInterceptorChain = (RequestInterceptorChain)requestInterceptor;
    //    if (requestInterceptorChain.getInterceptor() == null) {
    //        return requestInterceptor;
    //    }
    //    RequestInterceptorChain head = requestInterceptorChain;
    //    Stack<RequestInterceptorChain> stack = new Stack<RequestInterceptorChain>();
    //    stack.push(head);
    //
    //    RequestInterceptor next = null;
    //    while ((next = head.getInterceptor()) != null) {
    //        if (next instanceof RequestInterceptorChain) {
    //            head = (RequestInterceptorChain)next;
    //            stack.push(head);
    //        } else {
    //            break;
    //        }
    //    }
    //
    //    RequestInterceptorChain newHead = stack.pop();
    //    RequestInterceptorChain newNext = newHead;
    //    while (!stack.empty()) {
    //        RequestInterceptorChain pop = stack.pop();
    //        newNext.setInterceptor(pop);
    //        newNext = pop;
    //    }
    //    newNext.setInterceptor(next);
    //    return newHead;
    //}

    public RequestInterceptor setInterceptor(RequestInterceptor interceptor) {
        this.interceptor = interceptor;
        return interceptor;
    }

    public RequestInterceptor getInterceptor() {
        return interceptor;
    }

    @Override
    public void handle(Request request) throws RequestException {
        doHandle(request);
        if (interceptor != null) {
            interceptor.handle(request);
        }
    }

    @Override
    public void invoke(Request request) throws RequestException {
        doInvoke(request);
        if (interceptor != null) {
            interceptor.invoke(request);
        }
    }

    /**
     * Real handle method implementation of The interceptor
     *
     * @param request
     * @throws RequestException
     */
    public abstract void doHandle(Request request) throws RequestException;

    /**
     * Real invoke method implementation of The interceptor
     *
     * @param request
     * @throws RequestException
     */
    public abstract void doInvoke(Request request) throws RequestException;
}
