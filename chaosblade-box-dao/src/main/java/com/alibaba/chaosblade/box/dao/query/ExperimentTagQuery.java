package com.alibaba.chaosblade.box.dao.query;

import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
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
