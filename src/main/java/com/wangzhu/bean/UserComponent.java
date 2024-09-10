package com.wangzhu.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by wangz on 2021/6/7 19:48.
 **/
@Component
@ConfigurationProperties(prefix = "demo.user")
public class UserComponent {
    private String name;
    private Integer age;
    private String desc;
    //private String nick;
    //private Integer level;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "UserComponent{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", desc='" + desc +
                '}';
    }
}
