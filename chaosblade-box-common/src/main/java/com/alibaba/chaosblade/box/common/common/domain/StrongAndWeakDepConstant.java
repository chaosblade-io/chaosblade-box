package com.alibaba.chaosblade.box.common.common.domain;

import com.alibaba.chaosblade.box.common.common.enums.IEnum;

/**
 * 强弱依赖状态
 *
 * @author haibin.lhb
 *
 *
 */
public class StrongAndWeakDepConstant {

    /**
     * 强弱依赖状态
     */
    public static enum StrongAndWeakStatus implements IEnum<Integer> {
        /**
         * 强依赖
         */
        Strong(1),

        /**
         * 弱依赖
         */
        Weak(2),

        /**
         * 未知
         */
        UNKNOWN(0);

        private Integer value;

        StrongAndWeakStatus(Integer value) {
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return value;
        }
    }
}
