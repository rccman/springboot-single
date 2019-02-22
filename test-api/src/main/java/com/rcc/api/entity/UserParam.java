package com.rcc.api.entity;

import com.rcc.api.base.QueryCondition;

public class UserParam extends QueryCondition {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserParam(String name) {
        this.name = name;
    }

    public UserParam() {
    }
}
