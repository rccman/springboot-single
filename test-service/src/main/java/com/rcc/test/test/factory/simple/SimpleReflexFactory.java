package com.rcc.test.test.factory.simple;

import com.rcc.test.test.factory.entity.Circle;
import com.rcc.test.test.factory.entity.Rectangle;
import com.rcc.test.test.factory.Shape;

/**
 * 简单工厂模式
 * 形状工厂 (反射)
 */
public class SimpleReflexFactory {
    public static Object getClass(Class<? extends Shape> clazz) {
        Object obj = null;

        try {
            obj = Class.forName(clazz.getName()).newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return obj;
    }



    public static void main(String[] args) {
        Circle circle = (Circle) SimpleReflexFactory.getClass(Circle.class);
        circle.draw();

        Rectangle rectangle = (Rectangle) SimpleReflexFactory.getClass(Rectangle.class);
        rectangle.draw();
    }


}
