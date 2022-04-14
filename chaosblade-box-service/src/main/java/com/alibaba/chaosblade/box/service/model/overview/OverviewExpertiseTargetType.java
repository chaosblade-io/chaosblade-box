package com.alibaba.chaosblade.box.service.model.overview;

/**
 * @author sunpeng
 *
 *
 */
public enum OverviewExpertiseTargetType {

    CPU("CPU","icon-cpu"),
    MEM("内存","icon-neicun"),
    NETWORK("网络","icon-wangluo"),
    DISK("磁盘","icon-cipan"),
    PROCESS("进程","icon-a-jincheng1"),
    JVM("JVM","icon-java"),
    KUBERNETES("Kubernetes","icon-K8S"),

    ;


    String name;

    String icon;

    OverviewExpertiseTargetType(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }


    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }


}
