package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async;

/**
 * @author sunpeng
 *
 *
 */
public interface ActivityAsyncCallback {

    /**
     * 回调处理
     * @param asyncCallBackContext
     */
    void execute(AsyncCallBackContext asyncCallBackContext);

    /**
     * 支持工具类型
     * @param toolType
     * @return
     */
    boolean support(String toolType);

    /**
     * 根据appCode判断是否支持
     * @param appCode
     * @return
     */
    boolean supportByAppCode(String appCode);

    /**
     * 演练过期处理
     * @param asyncCallBackContext
     */
    void expired(AsyncCallBackContext asyncCallBackContext);



}
