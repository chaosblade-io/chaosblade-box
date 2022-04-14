package com.alibaba.chaosblade.box.common.common.domain.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ChaosUser implements Serializable {
    /**
     * 系统管理员
     */
    public static final ChaosUser SYSTEM = new ChaosUser("-1");

    private Long id;
    /**
     * 用户ID
     */
    String userId;

    /**
     * 用户名字
     */
    String userName;

    String userNick;

    String password;

    String license;


    public ChaosUser(String userId) {
        this.userId = userId;
    }

    public ChaosUser(String userName, String password) {//, Role role
        this.userName = userName;
        this.password = password;
        this.license = genRandomId();
        this.userId = genRandomLong();
    }

    private String genRandomId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String genRandomLong() {
        int hash = UUID.randomUUID().hashCode();
        if (hash < 0) {
            hash = -hash;
        }
        return Integer.toString(hash);
    }

    public String getCurrentUserId() {
        return this.userId;
    }

    public Long getId() {
        return this.id;
    }


}
