package com.alibaba.chaosblade.box.auth;

import com.alibaba.chaosblade.box.annotation.AdminOperation;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author haibin
 *
 *
 */
public class AdminOperationHandlerInterceptor implements HandlerInterceptor {
//    private AdministratorManager administratorManager;

    private LoginUserResolver loginUserResolver;

    public AdminOperationHandlerInterceptor(LoginUserResolver loginUserResolver) {
        this.loginUserResolver = loginUserResolver;
    }
//    public AdminOperationHandlerInterceptor(AdministratorManager administratorManager,
//        LoginUserResolver loginUserResolver) {
//        this.administratorManager = administratorManager;
//        this.loginUserResolver = loginUserResolver;
//    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        Object handler)
        throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            boolean hasMethodAnnotation = handlerMethod.hasMethodAnnotation(AdminOperation.class);
            boolean hasClassAnnotation = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(),
                AdminOperation.class) != null;
            if (hasMethodAnnotation || hasClassAnnotation) {
                ChaosUser chaosUser = loginUserResolver.resolve(httpServletRequest);
                if (chaosUser == null) {
                    throw new ChaosException(CommonErrorCode.P_USER_PERMISSION_DENIED, "无权限");
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        Object handler,
        ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        Object o, Exception e) throws Exception {

    }

}
