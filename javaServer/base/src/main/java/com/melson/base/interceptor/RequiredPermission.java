package com.melson.base.interceptor;

import java.lang.annotation.*;

/**
 * Created by Nelson on 2020/7/24.
 * 自定义注解 和拦截器一起使用校验权限
 * ElementType.TYPE，ElementType.METHOD表示注解可以标记类和方法
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequiredPermission {
    String value();
}
