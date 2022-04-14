package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author haibin
 *
 * 
 */
@TableName("t_chaos_experiment_host_relation")
@Data
public class ExperimentHostRelationDO extends BaseDO {

    public static final Integer RELATION_EXPERIMENT = 0;
    public static final Integer RELATION_EXPERIMENT_TASK = 1;
    public static final String COLUMN_SCOPE_TYPE = "scope_type";
    public static final String COLUMN_NAMESPACE = "namespace";
    public static final String COLUMN_CLUSTER_ID = "cluster_id";
    public static final String COLUMN_KUB_NAMESPACE = "kub_namespace";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_APP_CONFIGURATION_ID = "app_configuration_id";
    public static final String COLUMN_EXPERIMENT_TASK_STATE = "experiment_task_state";
    public static final String NAME = "name";

    public static final String COLUMN_INJECTION_TARGET_TYPE = "injection_target_type";

    public static final String INJECTION__TYPE_HOST = "host";

    public static final String INJECTION_TYPE_CONTAINER = "container";

    public static final String INJECTION_TYPE_PROCESS = "process";

    public static final String INJECTION_TYPE_POD = "pod";
    public static final String COLUMN_INJECTION_TARGET_NAME = "injection_target_name";
    public static final String COLUMN_RELATION_TYPE = "relation_type";
    public static final String COLUMN_APP_NAME = "app_name";
    public static final String COLUMN_OUTER_ID = "outer_id";
    public static final String COLUMN_CONFIGURATION_ID = "configuration_id";
    public static final String COLUMN_APP_ID = "app_id";
    public static final String CONDITION_APP_ID_COUNT_DISTINCT_OUTER_ID_AS_TOTAL
        = "app_id,count(distinct(outer_id)) as total";
    public static final String RESULT_FIELD_TOTAL = "total";

    @TableField("host_ip")
    private String hostIp;

    @TableField("device_id")
    private String deviceId;

    @TableField(COLUMN_CONFIGURATION_ID)
    private String configurationId;

    @TableField(COLUMN_OUTER_ID)
    private String outerId;

    @TableField(COLUMN_APP_NAME)
    private String appName;

    @TableField(COLUMN_RELATION_TYPE)
    private Integer relationType;

    /**
     * 演练任务的运行状态
     */
    @TableField(COLUMN_EXPERIMENT_TASK_STATE)
    protected Integer taskState;

    @TableField(COLUMN_APP_CONFIGURATION_ID)
    private String appConfigurationId;

    /**
     * 用户ID
     */
    @TableField(COLUMN_USER_ID)
    private String userId;

    /**
     * K8S namespace
     */
    @TableField(COLUMN_KUB_NAMESPACE)
    private String kubNamespace;

    /**
     * 用户环境
     */
    @TableField(COLUMN_NAMESPACE)
    private String namespace;

    @TableField(COLUMN_CLUSTER_ID)
    private String clusterId;

    @TableField(COLUMN_SCOPE_TYPE)
    private Integer scopeType;

    /**
     * 注入的目标名称,比如容器名,进程名,POD名称
     */
    @TableField(COLUMN_INJECTION_TARGET_NAME)
    private String injectionTargetName;

    /**
     * 注入的目标类型
     */
    @TableField(COLUMN_INJECTION_TARGET_TYPE)
    private String injectionTargetType;

    @TableField(COLUMN_APP_ID)
    private String appId;

    public static ExperimentHostRelationDO build(Scope scope, ExperimentTaskDO experimentTaskDO) {
        ExperimentHostRelationDO experimentHostRelationDO = new ExperimentHostRelationDO();
        experimentHostRelationDO.setAppName(scope.getApp());
        experimentHostRelationDO.setAppId(scope.getAppId());
        experimentHostRelationDO.setClusterId(scope.getClusterId());
        experimentHostRelationDO.setKubNamespace(scope.getKubNamespace());
        experimentHostRelationDO.setConfigurationId(scope.getDeviceConfigurationId());
        experimentHostRelationDO.setAppConfigurationId(scope.getAppConfigurationId());
        experimentHostRelationDO.setDeviceId(scope.getDeviceId());
        experimentHostRelationDO.setScopeType(scope.getScopeType());
        experimentHostRelationDO.setHostIp(scope.getIp());
        experimentHostRelationDO.setUserId(experimentTaskDO.getUserId());
        experimentHostRelationDO.setNamespace(experimentTaskDO.getNamespace());
        experimentHostRelationDO.setRelationType(
            ExperimentHostRelationDO.RELATION_EXPERIMENT_TASK);
        experimentHostRelationDO.setOuterId(experimentTaskDO.getTaskId());
        experimentHostRelationDO.setTaskState(StateEnum.RUNNING.getValue());
        return experimentHostRelationDO;
    }

}
