package com.alibaba.chaosblade.box.service.model.device;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author hengyu
 * @version 1.0.0
 * @className SyncContainerResponse
 * @createTime 2021/11/23 10:35:00
 * @description
 */
@Data
@Builder
public class SyncContainerResponse {
    private boolean success;
    private Integer containerNum;
    private List<String> containerList;
}
