package com.testing.class14;

import com.testing.inter.InterKeyWord;

public class TestparamRep {
    public static void main(String[] args) {
        InterKeyWord inter=new InterKeyWord();
        inter.saveParam("Roy","java");
        inter.saveParam("will","python");
        inter.saveParam("土匪","性能");

        String origin="{\"最胖的人教的课\":\"{Roy}\",\"最帅的教的课\":\"{will}\",\"客服老师\":\"{kaka}\",\"最爱吃肉的\":\"{Roy}\"}";
        String result=inter.toParam(origin);
        System.out.println(result);

    }
}
