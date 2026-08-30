package com.hmall.common.interceptor;

import com.hmall.common.utils.UserContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求头中的用户信息
        String userInfo = request.getHeader("user-info");
        // 2.判断是否为空
        if (userInfo != null){
            // 不为空，保存用户信息到ThreadLocal中
            UserContext.setUser(Long.valueOf(userInfo));
        }
        // 3.放行（仅用于获取用户信息，拦截功能在网关过滤器AuthGlobalFilter已实现）
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserContext.removeUser();
    }
}
