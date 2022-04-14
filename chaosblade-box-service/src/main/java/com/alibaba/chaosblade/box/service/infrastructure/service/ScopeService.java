package com.alibaba.chaosblade.box.service.infrastructure.service;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.service.model.param.HostExperimentTask;

import java.util.List;

/**
 * @author haibin
 *
 * 
 */
public interface ScopeService {

    /**
     * 根据进程名字判断进程是否存在
     *
     * @param host
     * @param processName
     * @return
     */
    public Response<String[]> checkProcessExistByName(Host host, String processName);

    /**
     * 根据进程号判断进程是否存在
     *
     * @param host
     * @param pid
     * @return
     */
    public Response<String[]> checkProcessExistByPid(Host host, String pid);

    /**
     * 检查机器是否属于某个演练当中
     *
     * @param host
     * @return 返回演练任务信息
     */
    public List<HostExperimentTask> checkHostInRunningExperiment(Host host);

}
