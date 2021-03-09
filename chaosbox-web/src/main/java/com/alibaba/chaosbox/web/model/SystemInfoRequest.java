package com.alibaba.chaosbox.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yefei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemInfoRequest {

    private String locale;

    private String version;
}
