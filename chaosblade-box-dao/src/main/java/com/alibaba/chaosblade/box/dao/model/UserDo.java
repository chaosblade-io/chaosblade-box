package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("t_chaos_user")
@Data
//@Deprecated
public class UserDo extends BaseDO {
    private String userId;

    private String userName;

    private String userPassword;

    private String license;

    private Date lastLoginTime;

    private String secretKey;

    private int isDeleted;
}
