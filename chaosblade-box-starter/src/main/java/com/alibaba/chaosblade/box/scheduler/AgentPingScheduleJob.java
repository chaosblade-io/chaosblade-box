package com.alibaba.chaosblade.box.scheduler;

import com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent.ThreadPoolExecutors;
import com.alibaba.chaosblade.box.common.infrastructure.constant.DeviceStatus;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.query.CloudDeviceQuery;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerJob;
import com.alibaba.chaosblade.box.dao.scheduler.quartz.BaseJob;
import com.alibaba.chaosblade.box.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@DisallowConcurrentExecution
@SchedulerJob(name = "AgentPingScheduleJob", cronExpression = "*/10 * * * * ?")
public class AgentPingScheduleJob extends BaseJob implements Job, InitializingBean, DisposableBean {

    /**
     * 数据分组大小，放入并行线程执行
     */
    private static int GROUP_SIZE = 10;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SettingService settingService;


    static private final ThreadPoolExecutor rejectedThreadPool = new ThreadPoolExecutor(
            3,
            3,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            ThreadPoolExecutors.defaultThreadFactory("AgentPingScheduleJob"));

    static private final ExecutorService countDeviceExecutor = new ThreadPoolExecutor(
            3,
            5,
            0,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(300),
            Executors.defaultThreadFactory(),
            new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
                    //目前的队列长度配置足够，加个保底策略，触发了warn的时候说明数据量上来了，需要考虑在调整配置或优化了
                    log.warn("[AgentPingScheduleJob] BlockingQueue is full, RejectedExecutionHandler is running");
                    try {
                        rejectedThreadPool.execute(runnable);
                    } catch (Exception e) {
                        log.error("[AgentPingScheduleJob] execute error", e);
                    }
                }
            });

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long time = System.currentTimeMillis();
        log.info("[AgentPingScheduleJob] start");
        //查询所有禁用的应用，验证应用下是否有机器，若存在机器，则将disabled置为false
        List<Future<Boolean>> futureList = new ArrayList<>();
        for(int page = 1;;page ++) {
            //改分页，一次查10条
            CloudDeviceQuery cloudDeviceQuery = new CloudDeviceQuery();
            PageableQueryWrapper<CloudDeviceQuery> pageableQueryWrapper = PageableQueryWrapper.of(cloudDeviceQuery);
            pageableQueryWrapper.pageNumber(page);
            pageableQueryWrapper.pageSize(GROUP_SIZE);
            PageableResponse<DeviceDO> deviceDOPageableResponse = deviceRepository.getAliveDevices(pageableQueryWrapper);

            deviceDOPageableResponse.getData().stream().map(
                    deviceDO -> futureList.add(countDeviceExecutor.submit(() -> pingAgent(deviceDO)))).
                    collect(Collectors.toList());


            log.info("[AgentPingScheduleJob] page:{}, totalPage: {}, total: {}",
                    page,deviceDOPageableResponse.getPages(), deviceDOPageableResponse.getTotal());
            if(page >= deviceDOPageableResponse.getPages()) {
                break;
            }
        }

        for(Future<Boolean> future : futureList) {
            try {
                Boolean result = future.get();
                if(!result) {
                    log.info("[AgentPingScheduleJob] future return false");
                }
            } catch (InterruptedException | ExecutionException e) {
                log.info("[AgentPingScheduleJob] future error", e);
            }
        }
        log.info("[AgentPingScheduleJob] time: {} ", System.currentTimeMillis() - time);
    }

    private Boolean pingAgent (DeviceDO deviceDO) {
        if (settingService.pingAgent(deviceDO)) {
            return true;
        }

        deviceDO.setStatus(DeviceStatus.OFFLINE.getStatus());
        return deviceRepository.update(deviceDO);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rejectedThreadPool.allowCoreThreadTimeOut(true);
    }

    @Override
    public void destroy() {
        try {
            //先关闭主线程池
            if (!countDeviceExecutor.isTerminated()) {
                countDeviceExecutor.shutdown();
            }
            //在关闭兜底线程池
            if (!rejectedThreadPool.isTerminated()) {
                rejectedThreadPool.shutdown();
            }
        } catch (Exception ignored) {}
    }
}
