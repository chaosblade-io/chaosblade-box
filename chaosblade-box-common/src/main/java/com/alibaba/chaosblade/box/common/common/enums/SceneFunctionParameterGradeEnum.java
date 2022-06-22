package com.alibaba.chaosblade.box.common.common.enums;

public enum SceneFunctionParameterGradeEnum implements IEnum<Integer> {

    HIDE(0, "Hidden Parameter(隐藏参数)",0, false),

    NORMAL(1, "Fault Configuration(故障配置)",1, true),

    RANGE(2, "Sphere of Influence(影响范围)",2, true),

    PRO(3, "General Configuration(通用配置)",3, false),

    ;

    private Integer type;

    private String name;

    private Integer order;

    private boolean open;

    SceneFunctionParameterGradeEnum(Integer type, String name, Integer order, boolean open) {
        this.type = type;
        this.name = name;
        this.order = order;
        this.open = open;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    public Integer getValue() {
        return this.type;
    }

    public static SceneFunctionParameterGradeEnum parse(Integer type) {
        if (null == type) {
            return null;
        } else {
            SceneFunctionParameterGradeEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                SceneFunctionParameterGradeEnum item = var1[var3];
                if (type.equals(item.getValue())) {
                    return item;
                }
            }
            return null;
        }
    }

    public static SceneFunctionParameterGradeEnum parse(String name) {
        if (null == name) {
            return null;
        } else {
            SceneFunctionParameterGradeEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                SceneFunctionParameterGradeEnum item = var1[var3];
                if (name.equalsIgnoreCase(item.name())) {
                    return item;
                }
            }
            return null;
        }
    }



}


