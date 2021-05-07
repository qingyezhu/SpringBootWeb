package com.wangzhu.service.impl;

import com.wangzhu.bean.OptionBean;
import com.wangzhu.bean.QuestionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang.zhu on 2020-11-02 19:29.
 **/
@Service
public class HelloService implements InitializingBean, IHello {
    private static final Logger logger = LoggerFactory.getLogger(HelloService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("init-----");

        final List<OptionBean> optionBeans = new ArrayList<OptionBean>();
        optionBeans.add(OptionBean.builder("1", "专注"));
        optionBeans.add(OptionBean.builder("2", "低调"));
        optionBeans.add(OptionBean.builder("3", "勇敢"));
        optionBeans.add(OptionBean.builder("4", "超前"));

        final QuestionBean questionBean = QuestionBean.builder("1", "在人际交往方面最符合你的词", optionBeans);


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


    @Scheduled(fixedRate = 1000L)
    public void cronRateJob() {
        logger.info("RateStart");
        try {
            Thread.sleep(1500L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("RateEnd");
    }

    @Scheduled(fixedDelay = 1000L)
    public void cronDelayJob() {
        logger.info("DelayStart");
        try {
            Thread.sleep(1500L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("DelayEnd");
    }
}
