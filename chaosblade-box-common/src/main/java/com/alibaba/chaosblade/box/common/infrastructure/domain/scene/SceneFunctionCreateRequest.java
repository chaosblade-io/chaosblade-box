package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class SceneFunctionCreateRequest {

    /**
     * 用户
     */
    ChaosUser user;

    /**
     * 函数code，全局唯一
     */
    String code;

    /**
     * 函数名字
     */
    String name;

    /**
     * 函数描述
     */
    String description;

    /**
     * 阶段标志位,支持哪个阶段
     */
    private Integer phaseFlag;

    /**
     * 是否生效，影响是否对用户可见
     *
     * 0: 下架
     * 1: 上架
     * 2: 待发布
     *
     */
    private Integer enabled;

    /**
     * 包含系统列表
     */
    private List<SceneFunctionSystemVersion> systemVersions;

    /**
     * 版本信息
     */
    private String version;
}
