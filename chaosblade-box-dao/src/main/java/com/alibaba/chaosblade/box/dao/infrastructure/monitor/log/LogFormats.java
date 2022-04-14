package com.alibaba.chaosblade.box.dao.infrastructure.monitor.log;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.enums.UserCheckState;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChaosTraceUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import org.slf4j.helpers.MessageFormatter;

import java.util.Date;

/**
 * @author haibin
 *
 *
 */
public class LogFormats {

    public static String messageFormat(String messagePattern, Object... args) {
        return MessageFormatter.arrayFormat(messagePattern, args)
                .getMessage();
    }

    public static String buildThirdInterfaceRequestResult(String systemName, String interfaceName, Long cost,
                                                          boolean success, String error) {
        return MessageFormatter.arrayFormat("{}|{}|{}|{}|{}|{}",
                        new Object[]{ChaosTraceUtil.generateTraceId(), systemName.toLowerCase(), interfaceName, cost, success,
                                error})
                .getMessage();
    }

    /**
     * chaos-blade的表达式,appCode|host|requestId|success|errorCode|errorMessage|executionId|args|data
     *
     * @param miniAppInvokeContext
     * @param response
     * @return
     */
    public static String buildChaosBladeMessage(String appCode, MiniAppInvokeContext miniAppInvokeContext,
                                                Response response,
                                                String args) {
        return MessageFormatter.arrayFormat("{}|{}|{}|{}|{}|{}|{}|{}", new Object[]{appCode,
                        miniAppInvokeContext.getHost().getIp(), response.getRequestId(), response.isSuccess(),
                        response.getCode(),
                        removeNewLineFlag(JSON.toJSONString(response.getResult())), miniAppInvokeContext.getMiniAppTaskId(),
                        args})
                .getMessage();
    }

    private static String removeNewLineFlag(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return str;
        }
        return str.replace("\n", "");
    }

    public static class Activity {

        public static String buildBaseMessage(String messagePattern,
                                              Object... args) {
            return MessageFormatter.arrayFormat("ActivityTask={},Display={}," + messagePattern, args).getMessage();
        }

        public static String buildPauseBeforeMessage(String activityTaskId, long before) {
            return buildBaseMessage("运行前暂停:{} 毫秒", activityTaskId, true, before);
        }

        public static String buildPauseAfterMessage(String activityTaskId, long after) {
            return buildBaseMessage("运行后暂停:{} 毫秒", activityTaskId, true, after);
        }

        public static String buildUserConfirmedMessage(String activityTaskId, UserCheckState userCheckState) {
            return buildBaseMessage("用户确认结果:{}", activityTaskId, true,
                    UserCheckState.USER_CHECK_STATE_PASSED.equals(userCheckState) ? "继续执行" : "终止演练");
        }

        public static String buildStartRunActivity(String activityTaskId) {
            return buildBaseMessage("开始运行", activityTaskId, true);
        }

        public static String buildFinishedRunActivity(String activityTaskId) {
            return buildBaseMessage("运行结束", activityTaskId, true);
        }

        public static String buildStartRunOnTarget(String activityTaskId, Host host) {
            return buildBaseMessage("目标机器:{},执行开始", activityTaskId, true, host.getIp());
        }

        public static String buildFinishedRunOnTarget(String activityTaskId, Host host, boolean success,
                                                      String errorMessage) {
            return buildBaseMessage("目标机器:{},执行结束,结果:{}", activityTaskId, true, host.getIp(),
                    success ? "成功" : "失败:" + errorMessage);
        }

        public static String buildWaitingForUserChecked(String activityTaskId) {
            return buildBaseMessage("当前步骤运行结束，需要等待用户确认", activityTaskId, true);
        }

        public static String buildActivityRejected(String activityTaskId, String reason) {
            return buildBaseMessage("当前步骤跳过执行,因为演练正常结束或者手动终止", activityTaskId, true);
        }

        public static String buildMetricAppParamReplaces(String activityTaskId, Date from, Date to) {
            return buildBaseMessage("查询指标，时间点从:{}到{}", activityTaskId, true, from, to);
        }

        public static String buildK8sQueryResult(String activityTaskId, Integer value) {
            return buildBaseMessage("查询结果中.....等待{}秒", activityTaskId, true, value);
        }
    }

}
