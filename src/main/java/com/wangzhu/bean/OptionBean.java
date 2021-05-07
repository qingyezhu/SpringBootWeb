package com.wangzhu.bean;

/**
 * Created by wang.zhu on 2021-01-28 17:18.
 **/
public class OptionBean {

    private final String id;
    private final String desc;

    private OptionBean(final String id, final String desc) {
        this.id = id;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "OptionBean{" +
                "id='" + id + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public static OptionBean builder(final String id, final String desc) {
        return new OptionBean(id, desc);
    }
}
