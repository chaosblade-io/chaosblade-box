package com.alibaba.chaosblade.box.dao.query;

import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class TagQuery {

  private Integer tagType;

  private String partKey;

  private String userId;

  private List<String> tagNames;
}
