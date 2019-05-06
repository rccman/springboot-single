package com.rcc.test.test.factory.function;

import com.rcc.test.test.factory.Shape;
import com.rcc.test.test.factory.entity.Circle;

/**
 * 圆形工厂
 */
public class CircleFactory implements Factory {

    @Override
    public Shape getShape() {
        return new Circle();
    }

}
