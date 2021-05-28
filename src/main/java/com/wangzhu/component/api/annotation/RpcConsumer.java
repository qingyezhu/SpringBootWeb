package com.wangzhu.component.api.annotation;

import java.lang.annotation.*;

/**
 * Created by wang.zhu on 2021-05-28 19:48.
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcConsumer {
    /**
     * 服务别名
     *
     * @return
     */
    String name();

    /**
     * 服务地址
     *
     * @return
     */
    String uri();

    /**
     * 客户端超时时间
     *
     * @return
     */
    int timeout() default 500;

}
