package com.wangzhu.controller;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created by wang.zhu on 2021-02-16 16:48.
 **/
public class TestVector {

    public static void main(String[] args) {
        Vector vector = new Vector();
        vector.add(1);

        Iterator iterator = vector.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Object num = iterator.next();
            System.out.println("num=" + num);
            if (i++ < 5) {
                vector.add(iterator.next());
            }
        }
        System.out.println(vector);
    }
}
