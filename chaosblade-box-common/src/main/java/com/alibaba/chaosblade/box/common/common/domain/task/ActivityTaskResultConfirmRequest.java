package com.alibaba.chaosblade.box.common.common.domain.task;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ActivityTaskResultConfirmRequest extends BaseRequest {

    /**
     * 需要确认的活动任务ID
     */
    private String activityTaskId;

    /**
     * 用户确认是否当前活动任务是否运行成功，
     * 如果true，会继续运行下一个结果，如果false，那么会停止并且恢复当前演练任务
     */
    private boolean success;

    private String experimentTaskId;

}
