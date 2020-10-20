package com.testing.class15;

import com.testing.inter.InterKeyWord;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RefletTest {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        InterKeyWord inter = new InterKeyWord();
        //基于方法的名字和参数列表的类型查找方法
        Method targetPost = inter.getClass().getDeclaredMethod("doPost", String.class, String.class, String.class);
        targetPost.invoke(inter,"http://www.testingedu.com.cn:8081/inter/REST/auth","","text");
    }
}
