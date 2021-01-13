package com.wangzhu.service.impl;

import org.springframework.scheduling.annotation.Async;

/**
 * Created by wang.zhu on 2020-11-02 20:43.
 **/
public interface IHello {

    @Async
    void print();

    @Async
    void calc();
}
