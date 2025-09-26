package com.alibaba.chaosblade.box.dao.query;

import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author sunpeng */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpertisePageQuery {

  String userId;

  List<String> tagNames;

  String partName;

  String namespace;

  Integer state;

  String scopeType;
}
