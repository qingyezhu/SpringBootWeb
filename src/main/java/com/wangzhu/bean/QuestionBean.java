package com.wangzhu.bean;

import java.util.List;

/**
 * Created by wang.zhu on 2021-01-28 17:22.
 **/
public class QuestionBean {
    private final String id;
    private final String question;
    private final List<OptionBean> optionBeans;

    private QuestionBean(final String id, final String question,
                         final List<OptionBean> optionBeans) {
        this.id = id;
        this.question = question;
        this.optionBeans = optionBeans;
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public List<OptionBean> getOptionBeans() {
        return optionBeans;
    }

    @Override
    public String toString() {
        return "QuestionBean{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", optionBeans=" + optionBeans +
                '}';
    }

    public static QuestionBean builder(final String id, final String question,
                                       final List<OptionBean> optionBeans) {
        return new QuestionBean(id, question, optionBeans);
    }
}
