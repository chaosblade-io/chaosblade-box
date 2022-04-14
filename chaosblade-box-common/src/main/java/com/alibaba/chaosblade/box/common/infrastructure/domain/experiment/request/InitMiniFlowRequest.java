package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class InitMiniFlowRequest extends BaseRequest {
	
	public static Integer SOURCE_NON_APP = 0;
	
	public static Integer SOURCE_APP = 1;
	
	private String appCode;
	
	private PhaseType phase;
	
	/**
	 * 来源,默认是0，表示非应用
	 * 1表示应用
	 */
	@Nullable
	private Integer source = InitMiniFlowRequest.SOURCE_NON_APP;
	
	/**
	 * 应用ID
	 */
	@Nullable
	private Long appId;
	
	private List<String> nodeGroups = new ArrayList<>();
	
}
