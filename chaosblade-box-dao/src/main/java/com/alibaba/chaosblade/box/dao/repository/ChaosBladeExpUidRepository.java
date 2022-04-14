package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.ChaosBladeExpUidMapper;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author haibin
 *
 * 
 */
@Component
public class ChaosBladeExpUidRepository implements IRepository<String, ChaosBladeExpUidDO> {
    protected static final String EXP_UID = "exp_uid";
    protected static final String EXPERIMENT_TASK_ID = "experiment_task_id";
    protected static final String APP_CODE = "app_code";
    protected static final String EXPIRED = "expired";
    protected static final String HOST = "host";
    protected static final String ACTIVITY_TASK_ID = "activity_task_id";
    protected static final String APP_EXECUTION_ID = "app_execution_id";
    @Autowired
    private ChaosBladeExpUidMapper chaosBladeExpUidMapper;

    @Override
    public Optional<ChaosBladeExpUidDO> findById(String s) {
        return Optional.ofNullable(chaosBladeExpUidMapper.selectById(s));
    }

    public ChaosBladeExpUidDO createChaosBladeExpRecord(String expId, ExperimentMiniAppTaskDO experimentMiniAppTaskDO,
                                                        String hostDeviceIp, boolean persistent) {
        ChaosBladeExpUidDO chaosBladeExpUidDO = new ChaosBladeExpUidDO();
        chaosBladeExpUidDO.setExpUid(expId);
        chaosBladeExpUidDO.setAppExecutionId(experimentMiniAppTaskDO.getTaskId());
        chaosBladeExpUidDO.setExperimentTaskId(experimentMiniAppTaskDO.getExperimentTaskId());
        String configurationId = Strings.isNullOrEmpty(experimentMiniAppTaskDO.getAppConfigurationId())
                ? experimentMiniAppTaskDO.getDeviceConfigurationId() : experimentMiniAppTaskDO.getAppConfigurationId();
        chaosBladeExpUidDO.setConfigurationId(configurationId);
        chaosBladeExpUidDO.addAttribute(ChaosBladeExpUidDO.ATTRIBUTE_DEVICE_CONFIGURATION_ID,
                experimentMiniAppTaskDO.getDeviceConfigurationId());
        chaosBladeExpUidDO.addAttribute(ChaosBladeExpUidDO.ATTRIBUTE_DEVICE_IP, hostDeviceIp);
        chaosBladeExpUidDO.setAppCode(experimentMiniAppTaskDO.getAppCode());
        chaosBladeExpUidDO.setActivityTaskId(experimentMiniAppTaskDO.getActivityTaskId());
        chaosBladeExpUidDO.setHost(experimentMiniAppTaskDO.getHostIp());
        if (persistent) {
            chaosBladeExpUidMapper.insert(chaosBladeExpUidDO);
        }
        return chaosBladeExpUidDO;
    }

    public Optional<ChaosBladeExpUidDO> findForAsyncCallback(String expId, String requestId) {
        ChaosBladeExpUidDO chaosBladeExpUidDO = findByExpUid(expId);
        if (chaosBladeExpUidDO != null) {
            return Optional.of(chaosBladeExpUidDO);
        }
        chaosBladeExpUidDO = findByAppExecutionId(requestId);
        return Optional.ofNullable(chaosBladeExpUidDO);
    }

    public List<ChaosBladeExpUidDO> findByActivityTaskId(String activityTaskId) {
        ChaosBladeExpUidDO chaosBladeExpUidDO = new ChaosBladeExpUidDO();
        chaosBladeExpUidDO.setActivityTaskId(activityTaskId);
        return chaosBladeExpUidMapper.selectList(new QueryWrapper<>(chaosBladeExpUidDO));
    }

    @Override
    public boolean update(ChaosBladeExpUidDO chaosBladeExpUidDO) {
        return chaosBladeExpUidMapper.updateById(chaosBladeExpUidDO) > 0;
    }

    @Override
    public boolean add(ChaosBladeExpUidDO chaosBladeExpUidDO) {
        return chaosBladeExpUidMapper.insert(chaosBladeExpUidDO) > 0;
    }

    public ChaosBladeExpUidDO findLastByExpUid(String expUid) {
        QueryWrapper<ChaosBladeExpUidDO> chaosBladeExpUidDOQueryWrapper = new QueryWrapper<>();
        chaosBladeExpUidDOQueryWrapper.eq(EXP_UID, expUid);
        wrapperLast(chaosBladeExpUidDOQueryWrapper);
        return chaosBladeExpUidMapper.selectOne(chaosBladeExpUidDOQueryWrapper);
    }

    public ChaosBladeExpUidDO findLastByExperimentTaskIdAndHostAndAppCodeAndNotExpired(String experimentTaskId,
                                                                                       String host,
                                                                                       String appCode) {
        QueryWrapper<ChaosBladeExpUidDO> chaosBladeExpUidDOQueryWrapper = new QueryWrapper<>();
        chaosBladeExpUidDOQueryWrapper.eq(EXPERIMENT_TASK_ID, experimentTaskId)
                .eq(HOST, host)
                .eq(APP_CODE, appCode)
                .eq(EXPIRED, false);
        wrapperLast(chaosBladeExpUidDOQueryWrapper);
        return chaosBladeExpUidMapper.selectOne(chaosBladeExpUidDOQueryWrapper);
    }

    public ChaosBladeExpUidDO findLastByActivityTaskIdAndHost(String activityTaskId, String host) {
        QueryWrapper<ChaosBladeExpUidDO> chaosBladeExpUidDOQueryWrapper = new QueryWrapper<>();
        chaosBladeExpUidDOQueryWrapper.eq(ACTIVITY_TASK_ID, activityTaskId)
                .eq(HOST, host);
        wrapperLast(chaosBladeExpUidDOQueryWrapper);
        return chaosBladeExpUidMapper.selectOne(chaosBladeExpUidDOQueryWrapper);
    }

    private void wrapperLast(QueryWrapper<ChaosBladeExpUidDO> queryWrapper) {
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.last("limit 1");
    }

    public ChaosBladeExpUidDO findByExpUid(String expUid) {
        QueryWrapper<ChaosBladeExpUidDO> chaosBladeExpUidDOQueryWrapper = new QueryWrapper<>();
        chaosBladeExpUidDOQueryWrapper.eq(EXP_UID, expUid);
        return chaosBladeExpUidMapper.selectOne(chaosBladeExpUidDOQueryWrapper);
    }

    public ChaosBladeExpUidDO findByAppExecutionId(String appExecutionId) {
        ChaosBladeExpUidDO chaosBladeExpUidDO = new ChaosBladeExpUidDO();
        chaosBladeExpUidDO.setAppExecutionId(appExecutionId);
        QueryWrapper<ChaosBladeExpUidDO> queryWrapper = new QueryWrapper<>(chaosBladeExpUidDO);
        wrapperLast(queryWrapper);
        return chaosBladeExpUidMapper.selectOne(queryWrapper);
    }

    public ChaosBladeExpUidDO findByExpUidAndExperimentTaskId(String expUid, String experimentTaskId) {
        QueryWrapper<ChaosBladeExpUidDO> chaosBladeExpUidDOQueryWrapper = new QueryWrapper<>();
        chaosBladeExpUidDOQueryWrapper.eq(EXP_UID, expUid);
        chaosBladeExpUidDOQueryWrapper.eq(EXPERIMENT_TASK_ID, experimentTaskId);
        return chaosBladeExpUidMapper.selectOne(chaosBladeExpUidDOQueryWrapper);
    }

    public ChaosBladeExpUidDO findByActivityTargetTaskId(String activityTargetTaskId) {
        QueryWrapper<ChaosBladeExpUidDO> chaosBladeExpUidDOQueryWrapper = new QueryWrapper<>();
        chaosBladeExpUidDOQueryWrapper.eq(APP_EXECUTION_ID, activityTargetTaskId);
        wrapperLast(chaosBladeExpUidDOQueryWrapper);
        return chaosBladeExpUidMapper.selectOne(chaosBladeExpUidDOQueryWrapper);
    }

    public List<ChaosBladeExpUidDO> findByExperimentTaskId(String experimentTaskId) {
        QueryWrapper<ChaosBladeExpUidDO> chaosBladeExpUidDOQueryWrapper = new QueryWrapper<>();
        chaosBladeExpUidDOQueryWrapper.eq(EXPERIMENT_TASK_ID, experimentTaskId);
        return chaosBladeExpUidMapper.selectList(chaosBladeExpUidDOQueryWrapper);
    }

    public List<ChaosBladeExpUidDO> findByExperimentTaskIdAndNotExpired(String experimentTaskId) {
        ChaosBladeExpUidDO chaosBladeExpUidDO = new ChaosBladeExpUidDO();
        chaosBladeExpUidDO.setExperimentTaskId(experimentTaskId);
        chaosBladeExpUidDO.setExpired(false);
        return chaosBladeExpUidMapper.selectList(new QueryWrapper<>(chaosBladeExpUidDO));
    }

    public List<ChaosBladeExpUidDO> findWithNotExpireAndExperimentTaskFinished() {
        QueryWrapper<ChaosBladeExpUidDO> chaosBladeExpUidDOQueryWrapper = new QueryWrapper<>();
        chaosBladeExpUidDOQueryWrapper.eq(EXPIRED, false)
                .inSql(EXPERIMENT_TASK_ID, "SELECT task_id "
                        + "  from t_chaos_experiment_task "
                        + " where run_state= 4");
        return chaosBladeExpUidMapper.selectList(chaosBladeExpUidDOQueryWrapper);
    }

    public ChaosBladeExpUidDO findLastByExpUidOrSubExpId(String expUid) {
        QueryWrapper<ChaosBladeExpUidDO> chaosBladeExpUidDOQueryWrapper = new QueryWrapper<>();
        chaosBladeExpUidDOQueryWrapper.and(wrapper -> wrapper.eq(EXP_UID, expUid).or().eq("sub_exp_uid", expUid));
        wrapperLast(chaosBladeExpUidDOQueryWrapper);
        return chaosBladeExpUidMapper.selectOne(chaosBladeExpUidDOQueryWrapper);
    }

}
