package com.alibaba.chaosblade.box.common.infrastructure.domain.workspace;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @Author yinyansheng
 * @create 2020/10/20
 */
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ExperimentSummaryInfo {

    public ExperimentSummaryInfo(String date, Integer resultState, Integer size) {
        this.date = date;
        this.resultState = resultState;
        this.size = size;
    }

    String date;

    @JSONField(serialize = false)
    Integer resultState;

    @JSONField(serialize = false)
    Integer size;

    Integer totalSize;
    Integer successSize;
    Integer unexpectedSize;

}
