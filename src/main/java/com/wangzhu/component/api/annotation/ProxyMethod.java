package com.wangzhu.component.api.annotation;

import java.lang.annotation.*;

/**
 * 代理方法<br/>
 * Created by wang.zhu on 2021-05-28 19:28.
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProxyMethod {

    boolean kibana() default true;

    boolean log() default true;
}
