package com.alibaba.chaosblade.box.common.app.sdk.annotations;

import java.lang.annotation.*;

/**
 * @author sunju
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Author {

    /**
     * alias of {@link #userId()}
     *
     * @return user id
     * @deprecated use {@link #userId()} instead
     */
    String empId() default "";

    /**
     * userId of author
     *
     * @return user id
     */
    String userId() default "";

    /**
     * mark the micro app for all users.
     * Micro app will be used by all users if set TRUE, otherwise it's used by author only.
     *
     * @return true or false
     */
    boolean forPublic() default false;
}
