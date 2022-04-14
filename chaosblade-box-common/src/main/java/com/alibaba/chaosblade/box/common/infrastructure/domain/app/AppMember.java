package com.alibaba.chaosblade.box.common.infrastructure.domain.app;

import lombok.Data;

/**
 * aone app memebr
 *
 * @author haibin
 *
 *
 */
@Data
public class AppMember {

    private String empId;

    private boolean bizOps;

    private boolean devTel;

    private boolean appOps;

    private boolean pe;

}
