package com.alibaba.chaosblade.box.common.infrastructure.excel;

import java.io.Serializable;
import lombok.Data;

/** @author haibin */
@Data
public class SceneFunctionParamsExcelRow implements Serializable {

  private String code;

  private String description;
  private String codeDesc;

  private String name;

  private String alias;

  private boolean required = false;

  private String defaultValue;

  private boolean isNumber = false;
}
