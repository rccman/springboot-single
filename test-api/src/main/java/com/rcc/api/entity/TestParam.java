package com.rcc.api.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class TestParam {
    @NotBlank
    /**消息内容**/
    @ApiModelProperty(value = "消息内容",name = "消息内容",example = "")
    private String message;
    private Date time;

    public TestParam() {
    }

    public TestParam(String message) {
        this.message = message;
    }

    public TestParam(String message, Date time) {
        this.message = message;
        this.time = time;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
