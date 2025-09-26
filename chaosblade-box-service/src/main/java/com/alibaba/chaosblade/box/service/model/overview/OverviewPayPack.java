package com.alibaba.chaosblade.box.service.model.overview;

import java.util.List;
import lombok.Data;

/** @author sunpeng */
@Data
public class OverviewPayPack {

  private List<OverviewPayPackInfo> overviewPayPackInfoList;

  public OverviewPayPack(List<OverviewPayPackInfo> overviewPayPackInfoList) {
    this.overviewPayPackInfoList = overviewPayPackInfoList;
  }
}
