package com.alibaba.chaosblade.box.common.common.domain.experiment;

import com.alibaba.chaosblade.box.common.common.annotation.ApiParam;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class BaseExperimentRequest extends BaseRequest {

    /**
     * 演练名字
     */
    @ApiParam
    private String name;

    /**
     * 演练描述
     */
    private String description;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 小程序描述
     */
    private List<String> miniAppDesc;

    /**
     * 演练空间
     */
    private List<String> workspaces;

    /**
     * 外部ID
     */
    private String outerId;

    /**
     * 演练ID
     */
    private String experimentId;

    private List<ExperimentRelation> relations;

    private Integer source;

}
