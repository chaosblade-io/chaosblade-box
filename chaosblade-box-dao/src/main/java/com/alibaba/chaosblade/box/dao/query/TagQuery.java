package com.alibaba.chaosblade.box.dao.query;

import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class TagQuery {

    private Integer tagType;

    private String partKey;

    private String userId;

    private List<String> tagNames;
}
