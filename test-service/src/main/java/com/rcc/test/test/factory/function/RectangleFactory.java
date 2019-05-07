package com.rcc.test.test.factory.function;

import com.rcc.test.test.factory.Shape;
import com.rcc.test.test.factory.entity.Rectangle;

/**
 * 长方形工厂
 */
public class RectangleFactory implements Factory{
    @Override
    public Shape getShape() {
        return new Rectangle();
    }

}
