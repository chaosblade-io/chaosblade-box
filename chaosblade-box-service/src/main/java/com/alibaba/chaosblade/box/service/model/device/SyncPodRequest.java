package com.alibaba.chaosblade.box.service.model.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hengyu
 * @version 1.0.0
 * @className SyncPodRequest
 * @createTime 2021/11/23 10:35:00
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncPodRequest {
    private String nodeName;
}
