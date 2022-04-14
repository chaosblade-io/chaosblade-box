package com.alibaba.chaosblade.box.common.infrastructure.domain.tag;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class TagSearchRequest extends BaseRequest {

    /**
     * 部分关键词
     */
    private String key;

    /**
     * 标签类型
     */
    private Integer type;
}
