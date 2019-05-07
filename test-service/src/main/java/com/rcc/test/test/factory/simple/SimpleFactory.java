package com.rcc.test.test.factory.simple;

import com.rcc.test.test.factory.entity.Circle;
import com.rcc.test.test.factory.entity.Rectangle;
import com.rcc.test.test.factory.Shape;

/**
 * 简单工厂模式
 * 形状工厂
 */
public class SimpleFactory {
    // 使用 getShape 方法获取形状类型的对象
    public static Shape getShape(String shapeType) {
        if (shapeType == null) {
            return null;
        }
        if (shapeType.equalsIgnoreCase("Circle")) {
            return new Circle();
        } else if (shapeType.equalsIgnoreCase("Rectangle")) {
            return new Rectangle();
        }
        return null;
    }


    public static void main(String[] args) {
        // 获取 Circle 的对象，并调用它的 draw 方法
        Shape circle = SimpleFactory.getShape("CIRCLE");
        circle.draw();

        // 获取 Rectangle 的对象，并调用它的 draw 方法
        Shape rectangle = SimpleFactory.getShape("RECTANGLE");
        rectangle.draw();
    }


}
