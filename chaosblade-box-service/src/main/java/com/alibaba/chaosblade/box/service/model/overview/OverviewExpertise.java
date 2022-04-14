package com.alibaba.chaosblade.box.service.model.overview;

import lombok.Data;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class OverviewExpertise {

    /**
     * 经验ID
     */
    private String expertiseId;

    /**
     * 经验名称
     */
    private String expertiseName;

    /**
     * 经验描述
     */
    private String expertiseDescription;

    /**
     * 经验目标资源类型
     */
    private String expertiseTargetType;

    /**
     * 页面显示的icon
     */
    private String expertiseTargetIcon;



}
