package com.wangzhu.bean;

/**
 * Created by wang.zhu on 2021-01-28 14:57.
 **/
public enum OptionEnum {
    A("1", "专注"),
    B("2", "低调"),
    C("3", "勇敢"),
    D("4", "超前"),
    ;
    private final String id;
    private final String desc;

    OptionEnum(String id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }
}
