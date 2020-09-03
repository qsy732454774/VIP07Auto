package com.testing.class4;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class RobotTest {

    public static void main(String[] args) {
        try {
            Robot rb = new Robot();
            //鼠标移动到指定坐标位置
            rb.mouseMove(940, 300);
            //鼠标点击左键
            rb.mousePress(InputEvent.BUTTON1_MASK);
            //松开左键
            rb.mouseRelease(InputEvent.BUTTON1_MASK);
//            rb.mouseMove(1478,636);
//            rb.mousePress(InputEvent.BUTTON1_MASK);
//            rb.mouseRelease(InputEvent.BUTTON1_MASK);
            //按下键盘上的+号
            rb.keyPress(KeyEvent.VK_ADD);
            //松开
            rb.keyRelease(KeyEvent.VK_ADD);
            rb.keyPress(KeyEvent.VK_G);
            rb.keyRelease(KeyEvent.VK_G);
            rb.keyPress(KeyEvent.VK_O);
            rb.keyRelease(KeyEvent.VK_O);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

}
