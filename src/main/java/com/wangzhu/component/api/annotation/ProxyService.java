package com.wangzhu.component.api.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 代理服务<br/>
 * Created by wang.zhu on 2021-05-28 19:27.
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface ProxyService {

    String value() default "";
}
