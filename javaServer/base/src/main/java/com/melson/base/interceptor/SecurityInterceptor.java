package com.melson.base.interceptor;

import com.melson.base.cache.CacheKey;
import com.melson.base.entity.StoreEmployee;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.melson.base.cache.CacheUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author Nelson
 * @Description 权限拦截器
 * @Date 2020/7/24
 */
public class SecurityInterceptor implements HandlerInterceptor {
    @Autowired
    private CacheUtil cacheUtil;
    @Override
    // 在目标方法执行前执行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token=request.getParameter("token");
        // 验证权限
        if (this.hasPermission(handler,token)) {
            return true;
        }
        //  null == request.getHeader("x-requested-with") TODO 暂时用这个来判断是否为ajax请求
        // 如果没有权限 则抛403异常 springboot会处理，跳转到 /error/403 页面
        response.sendError(HttpStatus.FORBIDDEN.value(), "无权限");
        return false;

    }
    private boolean hasPermission(Object handler,String userId) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法上的注解
            RequiredPermission requiredPermission = handlerMethod.getMethod().getAnnotation(RequiredPermission.class);
            // 如果方法上的注解为空 则获取类的注解
            if (requiredPermission == null) {
                requiredPermission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequiredPermission.class);
            }
            // 如果标记了注解，则判断权限
            if (requiredPermission != null && StringUtils.isNotBlank(requiredPermission.value())) {
                Integer value=Integer.parseInt(requiredPermission.value());
                if(StringUtils.isEmpty(userId)){
                    return false;
                }
                StoreEmployee employee=(StoreEmployee) cacheUtil.GetObjectValue(CacheKey.StoreEmployee, Map.class).get(userId);
                if(employee==null){
                    return false;
                }
                Integer permissionLev=employee.getPermission();
                return permissionLev>=value;
            }
        }
        return true;
    }

    // 在目标方法执行后执行，但在请求返回前，我们仍然可以对 ModelAndView进行修改
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // TODO
    }

    // 在请求已经返回之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // TODO
    }
}
