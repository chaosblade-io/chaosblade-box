package com.alibaba.chaosblade.box.dao.infrastructure.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author haibin
 * 
 *
 */
public class Script implements Serializable {

    /**
     * 脚本语言类型
     */
    @Getter
    private String language;

    /**
     * 脚本内容
     */
    @Getter
    private String content;

    /**
     * 脚本名字
     */
    @Getter
    @Setter
    private String name;

    /**
     * 脚本唯一的id
     */
    @Getter
    private String id;

    /**
     * 脚本签名
     */
    @Getter
    private String signature;

    public Script(String id, String signature, String name, String content, String language) {
        this.id = id;
        this.signature = signature;
        this.content = content;
        this.language = language;
        this.name = name;
    }
}
