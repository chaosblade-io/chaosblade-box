package com.alibaba.chaosblade.box.service.model.overview;

import lombok.Data;

import java.util.List;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class OverviewProduct {

    /**
     * 产品消息通知
     */
    private List<OverviewProductMessage> message;

    /**
     * 产品最佳实践
     */
    private List<OverviewProductMessage> practice;

}
