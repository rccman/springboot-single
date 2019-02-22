package com.rcc.test.utils;

import com.rcc.test.base.RestResult;

/**
 * com.hzfh.common.utils
 *
 * @Project: pem
 * @Author: rencc
 * @Description:
 * @Date: 2018/7/2 9:36
 * @Source: Created with IntelliJ IDEA.
 */
public class ResultUtil {
    /**
     * 返回成功，传入返回体具体出參
     * @param object
     * @return
     */
    public static RestResult success(Object object){
        RestResult result = RestResult.getInstance();
        result.setErrCode("0");
        result.setErrDesc("成功");
        result.setItems(object);
        return result;
    }

    /**
     * 提供给部分不需要出參的接口
     * @return
     */
    public static RestResult success(){
        return success(null);
    }

    /**
     * 自定义错误信息
     * @param code
     * @param msg
     * @return
     */
    public static RestResult error(Integer code,String msg){
        RestResult result = RestResult.getInstance();
        result.setErrCode(String.valueOf(code));
        result.setErrDesc(msg);
        result.setItems(null);
        return result;
    }


}
