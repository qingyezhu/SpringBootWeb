package com.wangzhu.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * Created by wang.zhu on 2020-11-02 19:29.
 **/
@Service
public class HelloService implements InitializingBean, IHello {
    private static final Logger logger = LoggerFactory.getLogger(HelloService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("init-----");
    }

    @Override
    public void print() {
        logger.info("print {}", System.currentTimeMillis());
        calc();
    }

    @Override
    public void calc() {
        logger.info("calc {}", System.currentTimeMillis());
    }
}
