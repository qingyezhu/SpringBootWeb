package com.wangzhu.controller;

/**
 * Created by wang.zhu on 2021-02-07 17:04.
 **/
public class TestInit {

    public static void main(String[] args) {
        System.out.println("爸爸的岁数:" + Son.factor);    //入口
    }

}

class Grandpa {
    static {
        System.out.println("爷爷在静态代码块");
    }
    public static int factor = 21;
}

class Father extends Grandpa {
    static {
        System.out.println("爸爸在静态代码块");
    }

    public static  int factor = 22;

    public Father() {
        System.out.println("我是爸爸~");
    }
}

class Son extends Father {
    static {
        System.out.println("儿子在静态代码块");
    }
//    public static  int factor = 23;

    public Son() {
        System.out.println("我是儿子~");
    }
}