package com.alibaba.chaosblade.box.service.model.overview;

import lombok.Data;

import java.util.List;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class OverviewPayPack {

    private List<OverviewPayPackInfo> overviewPayPackInfoList;


    public OverviewPayPack(List<OverviewPayPackInfo> overviewPayPackInfoList) {
        this.overviewPayPackInfoList = overviewPayPackInfoList;
    }
}
