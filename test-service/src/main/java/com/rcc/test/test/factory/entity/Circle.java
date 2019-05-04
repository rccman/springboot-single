package com.rcc.test.test.factory.entity;

import com.rcc.test.test.factory.Shape;

/**
 * 圆形
 */
public class Circle implements Shape {
    public Circle() {
        System.out.println("Circle");
    }
    @Override
    public void draw() {
        System.out.println("Draw Circle");
    }
}