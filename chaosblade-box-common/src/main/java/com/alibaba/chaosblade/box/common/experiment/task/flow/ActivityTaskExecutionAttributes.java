package com.alibaba.chaosblade.box.common.experiment.task.flow;

/**
 * @author haibin
 *
 * 
 */
public class ActivityTaskExecutionAttributes {

    /**
     * activity的运行策略
     */
    public static String ATTRIBUTE_KEY_ACTIVITY_RUNNABLE_STRATEGY = "activity_run_strategy";

    /**
     * 批量运行的大小
     */
    public static String ATTRIBUTE_KEY_ACTIVITY_RUNNABLE_BATCH_SIZE = "activity_run_batch_size";

    /**
     * 标明当前的活动任务是重试引起的
     */
    public static String ATTRIBUTE_KEY_RETRY = "retry";

    /**
     * 如果只是查询metric，那么是不需要走常规的activity task流程的
     */
    public static String ATTRIBUTE_KEY_METRIC_RELOAD = "metric_reload";

    public static String ATTRIBUTE_KEY_USER_ID = "userId";

    public static String ATTRIBUTE_KEY_SUB_USER_ID = "subUserId";

    public static String ATTRIBUTE_KEY_STS_TOKEN = "stsToken";

    public static String ATTRIBUTE_KEY_PUSH_EXPERIMENT_TASK = "push_experiment_task";

    public static String ATTRIBUTE_KEY_ACTIVITY_TASK_DO = "activity_task";

    public static String ATTRIBUTE_KEY_USER = "user";

    public static String ATTRIBUTE_KEY_EXPERIMENT_ACTIVITY = "experiment_activity";

    public static String ATTRIBUTE_KEY_EXPERIMENT = "experiment";

    public static String ATTRIBUTE_KEY_EXPERIMENT_ACTIVITY_ID = "experiment_activity_id";

    public static String ATTRIBUTE_KEY_EXPERIMENT_ACTIVITY_DEFINITION = "experiment_activity_definition";

    public static String ATTRIBUTE_KEY_EXPERIMENT_TASK = "experiment_task";

    public static String ATTRIBUTE_KEY_INTERRUPTED_EXPERIMENT_TASK_NOW = "experiment_task_interrupted_now";

    public static String ATTRIBUTE_KEY_NAMESPACE = "namespace";

    public static String ATTRIBUTE_KEY_MINI_APP_CONTEXT = "mini_app_context";

    public static String ATTRIBUTE_KEY_SCRIPT = "script";

    public static String ATTRIBUTE_KEY_PHASE = "phase";

    public static String ATTRIBUTE_VALUE_TRUE = "true";

    public static String ATTRIBUTE_VALUE_ACTIVITY_RUNNABLE_STRATEGY_BATCH = "batch";

    public static String ATTRIBUTE_VALUE_FALSE = "false";

    public static String ATTRIBUTE_ACTIVITY_SUPPORT_REPERTITION_EXECUTION = "activity_support_repetition_execution";

    public static String ATTRIBUTE_REQUEST_ID = "request_id";

    public static String ATTRIBUTE_JEXL_PARSER_RESULT = "jexl_args_result";

    public static String ATTRIBUTE_INVOKE_ONCE_HOST_RESPONSE = "invoke_one_host_response";

    public static String ATTRIBUTE_INVOKE_MODE = "invoke_mode";

    public static String ATTRIBUTE_ASYNC_MODE = "async";


}
