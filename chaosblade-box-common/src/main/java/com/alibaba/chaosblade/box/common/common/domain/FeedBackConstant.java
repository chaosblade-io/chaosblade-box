package com.alibaba.chaosblade.box.common.common.domain;

import com.alibaba.chaosblade.box.common.common.enums.IEnum;

/**
 * 反馈相关的状态集合
 *
 * @author haibin.lhb
 *
 *
 */
public class FeedBackConstant {

    /**
     * 反馈阶段
     */
    public static enum FeedbackStatus implements IEnum<Integer> {

        /**
         * 等待反馈
         */
        FEEDBACK_STATUS_WAITING(0),

        /**
         * 已经反馈
         */
        FEEDBACK_STATUS_FINISHED(1);

        private Integer value;

        FeedbackStatus(int value) {
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return value;
        }
    }

    /**
     * 反馈是否符合预期
     */
    public static enum ExpectationStatus implements IEnum<Integer> {
        /**
         * 符合预期
         */
        EXPECTATION_STATUS_YES(1),
        /**
         * 不符合预期
         */
        EXPECTATION_STATUS_NO(0);

        private Integer value;

        ExpectationStatus(int value) {
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return value;
        }
    }



    /**
     * 反馈是否符合业务
     */
    public static enum BusinessImpactStatus implements IEnum<Integer> {
        /**
         * 不影响业务
         */
        BUSINESS_IMPACT_STATUS_NO(0),

        /**
         * 影响业务
         */
        BUSINESS_IMPACT_STATUS_YES(1);

        private Integer value;

        BusinessImpactStatus(int value) {
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return value;
        }
    }

}
