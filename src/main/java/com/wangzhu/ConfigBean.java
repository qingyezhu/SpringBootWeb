package com.wangzhu;

import com.wangzhu.filter.CostFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Created by wang.zhu on 2020-11-02 20:32.
 **/
@Configuration
@EnableAutoConfiguration
public class ConfigBean {

    @Bean
    public Handler create() {
        return new Handler();
    }

    @Bean
    public CostFilter costFilter() {
        return new CostFilter();
    }

    /**
     * 开启webSocket支持
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
