package com.alibaba.chaosblade.box.dao.query;

import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentTagQuery {

  private String relationId;

  private String tagId;

  private Integer tagType;

  private String key;

  private List<String> tagNames;

  private String userId;

  private List<String> experimentIds;
}
