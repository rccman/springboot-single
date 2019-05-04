package com.rcc.test.test.factory.entity;

import com.rcc.test.test.factory.Shape;

/**
 * 长方形
 */
public class Rectangle implements Shape {
    public Rectangle() {
        System.out.println("Rectangle");
    }
    @Override
    public void draw() {
        System.out.println("Draw Rectangle");
    }
}
