package com.rcc.test.test.factory.function;

import com.rcc.test.test.factory.Shape;

/**
 * 方法工厂测试类
 */
public class FunctionFactoryTest {
    public static void main(String[] args) {
        Factory circleFactory = new CircleFactory();
        Shape circleShape = circleFactory.getShape();
        circleShape.draw();

        Factory rectangleFactory = new RectangleFactory();
        Shape rectangleShape = rectangleFactory.getShape();
        rectangleShape.draw();
    }
}
