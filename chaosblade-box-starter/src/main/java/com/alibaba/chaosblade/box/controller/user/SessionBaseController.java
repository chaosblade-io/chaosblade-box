package com.alibaba.chaosblade.box.controller.user;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping(value = "/chaos/")
public class SessionBaseController {

    protected HttpServletRequest getServletRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest();
    }

    protected HttpSession getSession() {
        return getServletRequest().getSession(false);
    }

    protected void invalidateSession() {
        getSession().invalidate();
    }
    protected void refreshSession(ChaosUser user) {
        HttpSession session = getServletRequest().getSession();
        session.setAttribute("uid", user.getUserId());
        session.setAttribute("name", user.getUserName());
        session.setAttribute("license", user.getLicense());
    }

}
