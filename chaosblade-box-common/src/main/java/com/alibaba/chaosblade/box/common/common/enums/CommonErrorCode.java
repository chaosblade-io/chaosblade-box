package com.alibaba.chaosblade.box.common.common.enums;

import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;
import com.alibaba.chaosblade.box.common.common.util.Messages;

/**
 * 错误码规范,
 * S-1001,代表系统错误,取值范围是1000~1999
 * B-2000~2999,代表业务错误
 * P-3000~3001,代表参数错误
 *
 * @author haibin
 *
 *
 */
public enum CommonErrorCode implements IErrorCode {
    S_REQUEST_OUTER_SYSTEM_FAILED("请求外部系统失败", 1300),

    /**
     * P代表参数错误或者请求违法
     * 0,通用,3000~3099
     * 1.用户权限相关 3100~3199
     * 2.api相关:3200~-3299
     * 3.3300~3399 演练参数相关
     */
    P_ARGUMENT_ILLEGAL("参数填写有误", 3000),

    P_LOGIN_MISSED("缺少用户登录信息", 3001),

    P_REQUIRE_FIELD_MISSING("非法的参数", 3002),

    P_NAMESPACE_EXIST("namespace已经存在", 3003),

    P_STS_TOKEN_LOGIN_ILLEGAL("非法的免登信息", 3004),

    P_LOGIN_FORBINATION("账号密码错误，登录失败", 3005),

    P_NAMESPACE_MISSED("缺少namespace", 3304),

    P_USER_OPERATION_LIMITED("用户操作受限制", 3100),

    P_USER_PERMISSION_DENIED("用户权限不足", 3101),

    P_TOKEN_INVALID("无效token", 3200),

    P_TOKEN_PERMISSION_DENIED("Token权限不足", 3201),
    P_WORKSPACE_PERMISSION_DENIED("工作空间权限不足", 3203),

    P_ERROR_CODE_MINI_GROUP_ID_NOT_EXIST("微流程组ID不存在", 3204),
    P_ERROR_CODE_MINI_FLOW_NOT_EXIST("微流程不存在", 3205),
    P_ERROR_CODE_ACTIVITY_NOT_EXIST("小程序不存在", 3206),

    P_ERROR_LICENSE_NOT_FOUNT("未找到用户认证信息,请重新登录", 3207),

    P_EXPERIMENT_NOT_FOUND("演练不存在", 3301),

    P_EXPERIMENT_DEFINITION_ILLEGAL("演练定义格式有误", 3302),
    P_ACTIVITY_NOT_FOUND("演练活动不存在", 3303),

    P_EXPERIMENT_TASK_NOT_FOUND("演练任务未找到", 3304),

    P_ACTIVITY_TASK_NOT_FOUND("活动任务未找到", 3305),

    P_ACTIVITY_TASK_STATUS_ILLEGAL("任务状态不合法", 3306),
    P_WORKSPACE_NOT_FOUND("工作空间不存在", 3307),
    P_NOT_FOUND_EXPERTISE("专家经验不存在", 3308),
    P_OSS_CONFIG_NOT_EXIST("oss配置不存在", 3309),
    P_APPLICATION_NOT_FOUND("无法找到应用", 3310),
    P_APP_DEVICE_NOT_FOUND("应用机器未找到", 3311),

    //创建演练API接口
    P_ERROR_HOSTS_NOT_FOUND("无法找到对应的机器", 3311),
    P_ERROR_GROUP_HOSTS_SHOULD_IN_SAME_APP("同一个分组下面的机器应用是同一个", 3312),
    P_ERROR_EXPERIMENT_APP_SHOULD_IN_SAME_NAMESPACE("演练和应用要是同一个namespace", 3313),
    P_ERROR_FLOW_GROUP_ATTACK_ONLY_ONE("微流程组ATTACK有且只能有一个", 3314),
    P_ERROR_MINI_FLOW_ARGUMENT_REQUIRED_MISSED("微流程必填字段缺失", 3315),
    P_ERROR_MINI_FLOW_ARGUMENT_ONE_ONLY_MISSED("微流程多选一字段缺失", 3316),
    P_ERROR_MINI_FLOW_GROUP_PRE_CHECK_FAILURE("微流程组前置校验失败", 3317),
    P_ERROR_INIT_FLOW_GROUP_FAILURE("微流程初始化失败", 3318),
    P_ERROR_APP_SCOPE_TYPE_AND_CODE_NOT_MATH("应用机器类型与小程序支持类型不符", 3319),

    //创建演练提示使用，未选择应用下拉框直接选择应用分组时提示
    P_APPLICATION_ID_REQUIRED("请先选择应用", 3320),

    P_HOST_REQUIRED("机器选择不能为空", 3321),

    /**
     * 系统级别的错误
     * 通用错误：1000~1099
     * 数据库错误:1100~1199
     * 缓存错误:1200~1299
     * 外部系统错误:1300~1399
     */
    S_SYSTEM_ERROR("系统内部错误", 1000, true),
    S_MINIAPP_INVOKE_COST_TOO_MANY_RESOURCE("执行失败,节点调用花费太多系统资源", 1002),
    S_ERR_OSS_CONFIG_PARSER_FAILED("解析OSS配置失败", 1003),

    S_DB_ERROR("数据库异常", 1100, true),

    S_DB_DOWN_ERROR("数据库无法连接,请等待dba处理", 1101, true),

    S_DATA_VIOLATION("数据异常", 1102, true),

    S_DATA_FIELD_MISSING("数据字段丢失", 1103, true),
    S_DATA_TRUNCATION("请求数据异常", 1104, true),
    S_ACTIVITY_EXECUTE_FAILED("当前节点执行失败,请稍后再试", 1105, true),

    //机器相关

    B_HOST_NOT_FOUND("机器不存在或者探针已卸载", 2201),

    B_HOST_NOT_ALIVE("当前节点心跳异常，无法访问", 2202),

    B_CLOUD_DEVICE_CLOSED("探针已失效或者机器已回收，会导致故障注入中断，请查看机器状态", 2203),

    B_APPLICATION_HOST_NOT_FOUND("应用下未找到设备,请确认设备存在或者所属应用未变化", 2204),

    B_ERR_UNKNOWN_SCOPE_TYPE("未知的机器类型", 2205),

    B_ERR_TARGET_PRE_CHECK("目标机器演练执行环境预校验失败", 2206),

    B_K8S_AGENT_CLUSTER_NOT_EQUAL("当前集群探针异常,请重新安装", 2207),

    B_K8S_AGENT_UNINSTALL("当前集群节点探针已下线", 2208),

    //应用相关

    B_HOST_APP_OR_GROUP_CHANGED("机器所属应用或者分组发生变化", 2302),

    B_UPDATE_APPLICATION_CONFIG_FAILED("更新应用配置失败", 2303),

    B_APPLICATION_HOST_NOT_ALIVE("应用机器心跳异常", 2307),

    B_K8S_AGENT_NOT_ALIVE("K8S演练探针状态异常,请稍后重试", 2306),
    B_APPLICATION_HOST_UNINSTALL("应用机器已下线,可能原因:1.目标机器不存在 2.探针已卸载", 2308),
    B_APPLICATION_HOST_INSTALL("应用机器探针安装中", 2308),

    B_EXPERIMENT_ALREADY_RUNNING("演练已有任务在运行中", 2401),

    B_EXPERIMENT_DRAFT_STATE("演练处于草稿状态", 2402),

    B_ACTIVITY_ALREADY_RUNNING("当前活动已有演练任务", 2403),

    B_EXPERIMENT_TASK_ALREADY_FINISHED("演练任务已经完结", 2404),

    B_ACTIVITY_TASK_HAS_CHECKED("不允许重复确认", 2405),

    B_ATTACK_ACTIVITY_NOT_FINISHED("未注入对应故障,无需执行恢复操作", 2406),

    B_EXPERIMENT_TASK_STOP_TIMEOUT("演练任务停止超时", 2407),

    B_EXPERIMENT_CREATE_FAILED("演练创建失败", 2408),

    B_EXPERIMENT_TASK_STOPPING("演练停止中", 2409),

    B_CREATE_EXPERIMENT_TASK_FAILED("创建演练任务失败", 2410),

    B_SAVE_EXPERIMENT_DEFINITION("保存演练定义失败", 2411),

    B_DUMP_EXPERIMENT_DEFINITION("重复创建演练定义", 2412),

    B_ONLY_SUPPORT_ATTACK("只支持注入阶段的小程序", 2413),

    B_EXPERIMENT_TASK_NOT_PUSHABLE("演练任务无法推进", 2414),

    B_EXPERIMENT_UPDATE("更新演练失败", 2415),

    B_EXPERIMENT_TASK_FEEDBACK_DENIED("不允许反馈", 2416),

    B_FEEDBACK_SUBMIT_FAILED("反馈异常", 2417),

    B_MINI_FLOW_PARAM_ERROR("微流程参数异常，缺少attack阶段", 2418),

    B_LITMUS_CHAOS_NOT_INSTALL_ERROR("当前集群未安装litmuschaos演练工具，无法使用litmuschaos相关场景", 2419),

    B_CHAOS_TOOLS_INSTALL_ERROR("演练工具安装失败", 2421),

    B_CHAOS_TOOLS_UNINSTALL_ERROR("演练工具卸载失败", 2422),

    B_CHAOS_TOOLS_INSTALL_NOT_SUPPORT_HOST("Host主机暂不支持安装拓展工具", 2423),

    B_PERCENT_HOST_APP_OR_GROUP_NOT_FOUND("百分比模式执行必须有应用和分组信息", 2424),

    B_ADD_TAG_FAILED("添加标签失败", 2501),

    B_CHAOS_AGENT_VERSION_LOW("当前机器探针版本过低，请到探针管理页面升级到最新版本", 2601),

    B_PERCENT_HOST_NOT_ALIVE("应用下无符合条件的机器", 2205),

    //javaAgent
    B_JAVA_AGENT_REVOKED("JavaAgent已经被卸载", 2601),

    B_JAVA_AGENT_CREATED_TIMEOUT("JavaAgent安装超时", 2602),

    B_JAVA_AGENT_CREATED_ERROR("JavaAgent安装异常", 2603),

    B_CHAOS_BLADE_ASYNC_ERROR("异步执行回调异常", 2611),
    B_CHAOS_BLADE_ASYNC_TIMEOUT("异步执行超时", 2612),
    B_CHAOS_BLADE_ASYNC_REVOKED("异步执行场景已被撤销", 2613),

    // miniapp
    B_UPDATE_SCRIPT("更新小程序脚本失败", 2701),

    B_SCRIPT_NOT_EXIST("脚本不存在", 2702),

    B_SCRIPT_CHANGED_BEFORE_UPDATE("脚本已经发生了变化", 2703),

    B_DELETE_APP_FORBIDDEN_BY_TYPE("该类型小程序不允许删除", 2704),

    B_MINIAPP_NOT_EXIST("小程序不存在或已删除", 2705),

    B_MINIAPP_CODE_EXIST("小程序code已经存在", 2706),

    B_CREATE_MINIAPP_FAILED("小程序创建失败", 2707),

    B_UPDATE_MINIAPP_FAILED("小程序更新失败", 2708),

    B_DELETE_MINIAPP_FAILED("小程序删除失败", 2709),

    B_FISSION_MINIAPP_FAILED("裂变小程序失败", 2710),

    B_ADD_MINIAPP_PARAMETER("添加小程序参数失败", 2711),

    B_UPDATE_MINIAPP_PARAMETER("更新小程序参数失败", 2712),

    B_DELETE_MINIAPP_PARAMETER("添加小程序参数失败", 2713),

    B_CREATE_MINIAPP_RELATION("添加小程序关联关系失败", 2714),

    B_CREATE_MINIAPP_PARAMETER_RELATION("添加小程序参数关联关系失败", 2715),

    B_ERROR_CREATE_MINIAPP_CATEGORY("添加小程序类目失败", 2716),

    B_UPDATE_APP_CATEGORY("更新小程序类目失败", 2717),

    B_MINIAPP_CATEGORY_NOT_EXIST("小程序类目不存在或已删除", 2718),

    B_ERROR_COMPILE_SCRIPT("编译脚本失败", 2719),

    B_ERROR_APP_DISABLED("小程序已经下架，无法执行", 2720),

    /**
     * 工作空间相关
     */
    B_ERROR_SCENE_AUTHORIZED_UPDATE("小程序更新授权失败", 2721),
    B_INVOKE_MINI_APP_FAILED("小程序调用失败", 2722),
    B_INVOKE_METRIC_APP_FAILED("数据查询异常", 2723),

    /**
     * 概览页
     */
    B_OVERVIEW_USER_SCENE_QUERY_ERROR("用户常用场景查询异常", 2901),
    B_OVERVIEW_USER_EXPERTISE_QUERY_ERROR("用户演练经验查询异常", 2902),
    B_TOPOLOGY_NODE_NOT_SUPPORTED("当前节点类型暂不支持演练",2401);

    ;

    private String readableMessage;

    private boolean log;

    private Integer statusCode;

    CommonErrorCode(String readableMessage, int statusCode) {
        this.readableMessage = readableMessage;
        this.log = false;
        this.statusCode = statusCode;
    }

    CommonErrorCode(String readableMessage, int statusCode, boolean log) {
        this.readableMessage = readableMessage;
        this.log = log;
        this.statusCode = statusCode;
    }

    public String getFullMessage() {
        return this.readableMessage;
    }

    @Override
    public Integer status() {
        return this.statusCode;
    }

    @Override
    public String getReadableMessage() {
        return Messages.getMessage(this.name(), this.readableMessage);
    }

    @Override
    public boolean logWhenThrowable() {
        return this.log;
    }

}
