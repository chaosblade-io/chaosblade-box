package com.alibaba.chaosblade.box.common.infrastructure.domain.app;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class AppMembers {

  private String appName;
  private String businessLine;
  private List<AppMember> memberList = new ArrayList<>();

  public static AppMembers NullObject = new AppMembers();

  public boolean containsEmpId(String empId) {
    return memberList.stream().anyMatch(appMember -> appMember.getEmpId().equals(empId));
  }
}
