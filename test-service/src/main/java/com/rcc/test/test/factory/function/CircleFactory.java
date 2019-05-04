package com.rcc.test.test.factory.function;

import com.rcc.test.test.factory.Shape;
import com.rcc.test.test.factory.entity.Circle;

public class CircleFactory implements Factory {

    @Override
    public Shape getShape() {
        return new Circle();
    }

}
