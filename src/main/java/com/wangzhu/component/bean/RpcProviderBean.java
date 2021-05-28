package com.wangzhu.component.bean;

import org.springframework.beans.factory.InitializingBean;

/**
 * Created by wang.zhu on 2021-05-28 20:10.
 **/
public class RpcProviderBean implements InitializingBean {

    public void setServiceInterface(String serviceInterface) {
        //todo
    }

    public void setServiceUri(String serviceUri) {
        //todo
    }

    public void setInstance(Object instance) {
        //todo
    }

    public void setTimeout(int timeout) {
        //todo
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
