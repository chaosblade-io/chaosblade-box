package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskFinishedInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.loadtest.LoadTestTaskManager;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.model.LoadTestStrategyDO;
import com.alibaba.chaosblade.box.dao.model.LoadTestTaskDO;
import com.alibaba.chaosblade.box.dao.repository.LoadTestStrategyRepository;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 压测演练任务完成拦截器
 * 在演练任务完成时处理关联的压测任务
 *
 * @author ZhengMingZhuo
 */
@Slf4j
@Component
public class LoadTestExperimentTaskFinishedInterceptor implements ExperimentTaskFinishedInterceptor {

    @Resource
    private LoadTestStrategyRepository loadTestStrategyRepository;

    @Resource
    private LoadTestTaskManager loadTestTaskManager;

    @Override
    public void beforeTurnToFinishedState(ExperimentTaskDO experimentTaskDO) {
        try {
            String experimentId = experimentTaskDO.getExperimentId();
            String experimentTaskId = experimentTaskDO.getTaskId();
            
            log.info("演练任务完成，检查关联的压测任务: experimentTaskId={}", experimentTaskId);
            
            // 查询是否有关联的压测策略
            Optional<LoadTestStrategyDO> strategyOptional = 
                    loadTestStrategyRepository.findByExperimentId(experimentId);
            
            if (!strategyOptional.isPresent()) {
                log.debug("演练任务 {} 未关联压测策略，无需处理压测任务", experimentTaskId);
                return;
            }

            LoadTestStrategyDO strategy = strategyOptional.get();
            
            // 查询关联的压测任务状态
            Response<LoadTestTaskDO> taskResponse = 
                    loadTestTaskManager.getLoadTestTaskStatusByExperimentTaskId(experimentTaskId);
            
            if (!taskResponse.isSuccess()) {
                log.debug("演练任务 {} 未找到关联的压测任务", experimentTaskId);
                return;
            }

            LoadTestTaskDO loadTestTask = taskResponse.getResult();
            log.info("找到关联的压测任务: taskId={}, status={}", 
                    loadTestTask.getTaskId(), loadTestTask.getStatus());

            // 根据演练任务的完成方式决定是否停止压测
            if (shouldStopLoadTest(experimentTaskDO, strategy, loadTestTask)) {
                log.info("停止压测任务: taskId={}, reason={}", 
                        loadTestTask.getTaskId(), getStopReason(experimentTaskDO));
                
                Response<Boolean> stopResponse = 
                        loadTestTaskManager.stopLoadTestTask(loadTestTask.getTaskId());
                
                if (stopResponse.isSuccess()) {
                    log.info("压测任务停止成功: taskId={}", loadTestTask.getTaskId());
                } else {
                    log.error("压测任务停止失败: taskId={}, error={}", 
                            loadTestTask.getTaskId(), stopResponse.getError().getErrorMessage());
                }
            } else {
                log.info("压测任务将继续运行: taskId={}, reason={}", 
                        loadTestTask.getTaskId(), getContinueReason(experimentTaskDO));
            }
            
        } catch (Exception e) {
            log.error("处理演练任务完成时的压测清理失败: experimentTaskId={}", 
                    experimentTaskDO.getTaskId(), e);
            // 不抛出异常，避免影响演练任务完成流程
        }
    }

    /**
     * 判断是否应该停止压测任务
     */
    private boolean shouldStopLoadTest(ExperimentTaskDO experimentTaskDO, 
                                       LoadTestStrategyDO strategy, 
                                       LoadTestTaskDO loadTestTask) {
        
        // 如果压测任务已经完成，无需停止
        if (loadTestTask.isFinished()) {
            return false;
        }

        // 如果演练任务是被强制停止的（手动停止），则停止压测
        if (experimentTaskDO.isStopping()) {
            return true;
        }

        // 如果演练任务是异常结束的，则停止压测
        if (experimentTaskDO.isFinished() && !isTaskSuccess(experimentTaskDO)) {
            return true;
        }

        // 如果演练任务是正常完成的，压测继续运行（根据策略配置）
        // 这里可以根据具体业务需求调整策略
        return false;
    }

    /**
     * 获取停止压测的原因
     */
    private String getStopReason(ExperimentTaskDO experimentTaskDO) {
        if (experimentTaskDO.isStopping()) {
            return "演练任务被手动停止";
        } else if (experimentTaskDO.isFinished() && !isTaskSuccess(experimentTaskDO)) {
            return "演练任务异常结束";
        } else {
            return "演练任务完成";
        }
    }

    /**
     * 获取压测继续运行的原因
     */
    private String getContinueReason(ExperimentTaskDO experimentTaskDO) {
        if (isTaskSuccess(experimentTaskDO)) {
            return "演练任务正常完成，压测按策略继续运行";
        } else {
            return "压测任务已完成或其他原因";
        }
    }

    /**
     * 判断演练任务是否成功
     */
    private boolean isTaskSuccess(ExperimentTaskDO experimentTaskDO) {
        return experimentTaskDO.isFinished() &&
               ResultEnum.SUCCESS.getValue().equals(experimentTaskDO.getResult());
    }
}
