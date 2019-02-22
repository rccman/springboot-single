package com.rcc.api.base;

import java.io.Serializable;

public abstract class QueryCondition extends PagingInfo implements Serializable {

    /**
     * 排序字段
     **/
//    @ApiModelProperty(value = "排序字段(如需多个以 , 分隔)",name ="排序字段",example = "time")
    private String sort;
    /**
     * 排序方式
     **/
//    @ApiModelProperty(value = "排序方式(如需多个以 , 分隔)",name ="排序方式",example = "DESC")
    private String order;




    public QueryCondition() {
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
