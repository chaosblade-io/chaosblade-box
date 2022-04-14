package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentGuardMetricDataItem {

    private Long timestamp;

    private Number value;

    /**
     * //TODO ,2019.12.19,haibin.lhb
     * 本来考虑到机器会很多，如果按照机器维度来返回指定的话，前端不好展示，
     * 但是考虑到云上演练的机器数目其实不会很多，所以这里group的值还是用ip来展示，
     * 后续可以再改成group来展示
     */
    private String group;

}
