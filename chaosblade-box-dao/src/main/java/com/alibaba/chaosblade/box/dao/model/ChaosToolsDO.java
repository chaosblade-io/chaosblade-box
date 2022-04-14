package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * t_chaos_tools
 * @author
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_chaos_tools")
public class ChaosToolsDO extends BaseDO {

    private String configurationId;

    private String clusterId;

    /**
     * tools name
     */
    private String name;

    /**
     * version
     */
    private String version;

    /**
     * download url
     */
    private String url;

    /**
     * device type
     */
    private Byte deviceType;

}
