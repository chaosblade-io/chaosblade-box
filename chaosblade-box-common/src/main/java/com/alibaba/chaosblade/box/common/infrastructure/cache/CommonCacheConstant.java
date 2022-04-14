package com.alibaba.chaosblade.box.common.infrastructure.cache;

/**
 * @author haibin
 *
 *
 */
public final class CommonCacheConstant {

    /**
     * 演练活动的cachename,key是演练Id
     */
    public final static String CACHE_EXPERIMENT_ACTIVITIES = "experiment_activities";

    /**
     * 演练单个活动的cache，key是活动ID
     */
    public final static String CACHE_EXPERIMENT_ACTIVITITY = "experiment_activity";

    /**
     * 演练对象任务的cache,key是演练任务ID
     */
    public final static String CACHE_ACTIVITY_TARGET_TASKS = "experiment_activity_target_tasks";

    /**
     * 单个演练对象的cache，key是演练对象任务ID
     */
    public final static String CACHE_ACTIVITY_TARGET_TASK = "experiment_activity_target_task";

    /**
     * 演练任务包含的hosts,key是演练任务ID
     */
    public final static String CACHE_EXPERIMENT_TASK_HOSTS = "experiment_task_hosts";

    /**
     * 演练小程序的缓存,key是小程序的appCode
     */
    public final static String CACHE_SCENE_FUNCTION = "scene_function";

}
