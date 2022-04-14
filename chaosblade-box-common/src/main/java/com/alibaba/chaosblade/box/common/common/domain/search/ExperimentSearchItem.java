package com.alibaba.chaosblade.box.common.common.domain.search;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author haibin
 * 
 *
 */
@Data
public class ExperimentSearchItem implements Serializable {

    private String experimentId;

    private String name;

    private String description;

    private Date gmtCreate;

    private List<String> tags;

    private String userId;
}
