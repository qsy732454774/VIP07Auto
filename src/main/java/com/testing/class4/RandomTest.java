package com.testing.class4;

import javax.sound.midi.Soundbank;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomTest {
    public static void main(String[] args) {
//        String[] goods={"米粉","蒸菜","黄焖鸡","炒菜","煲仔饭"};
//        Random random=new Random();
//        int id=random.nextInt(goods.length);
//        System.out.println("今天吃点啥"+goods[id]);

        Date now=new Date();
        System.out.println(now);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd-HHmmss");
        String nowtime=sdf.format(now);
        System.out.println(nowtime);

    }
}
