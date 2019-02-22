package com.rcc.test.exception;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义异常
 *
 * @Project:
 * @Author: rencc
 * @Description:
 * @Date: 2018/7/2 9:44
 * @Source: Created with IntelliJ IDEA.
 */
public class RestException extends Exception {
    private Integer code;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 继承exception，加入错误状态值
     */
    public RestException(Integer code) {
        super();
        this.code = code;
    }
    /**
     * 继承exception，加入错误状态值
     */
    public RestException(Integer code, Exception e) {
        super();
        logger.error("【系统异常】",e);
        this.code = code;
    }

    /**
     * 在调用其它接口报错直接返回错误信息
     * @param message
     * @param code
     */
    public RestException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
