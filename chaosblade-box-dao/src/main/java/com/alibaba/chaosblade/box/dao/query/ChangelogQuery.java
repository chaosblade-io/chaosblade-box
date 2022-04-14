package com.alibaba.chaosblade.box.dao.query;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Deprecated
public class ChangelogQuery {

    String targetId;

    String operatorType;

    String operatorId;

    String targetType;

    String actionType;

    Date from;

    Date to;

}
