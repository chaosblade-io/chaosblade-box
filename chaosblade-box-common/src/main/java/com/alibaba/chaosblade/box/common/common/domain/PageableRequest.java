package com.alibaba.chaosblade.box.common.common.domain;

import lombok.Data;

/** @author haibin */
@Data
public class PageableRequest extends BaseRequest {

  private Integer page = 1;

  private int size = 10;
}
