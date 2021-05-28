package com.wangzhu.component.wrap;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by wang.zhu on 2021-05-28 20:21.
 **/
public class RpcConsumerBean implements FactoryBean<Object>, InitializingBean {

    private String serviceUri;
    private String serviceName;
    private int timeout;

    public RpcConsumerBean(String serviceUri, String serviceName, int timeout) {
        this.serviceUri = serviceUri;
        this.serviceName = serviceName;
        this.timeout = timeout;
    }

    @Override
    public Object getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
