package com.rcc.test.exception;

import com.rcc.test.base.FieldErrorItem;
import com.rcc.test.base.RestResult;
import com.rcc.test.utils.JsonUtils;
import com.rcc.test.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 全局异常管理
 * @Author: rencc
 * @Description:
 * @Date: 2019/2/21 9:43
 * @Source: Created with IntelliJ IDEA.
 */
@ControllerAdvice
public class ExceptionHandle {
    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);


    @Autowired
    private ResourceBundleMessageSource errorMessageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public RestResult handleIllegalParamException(MethodArgumentNotValidException e, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return ResultUtil.error(400, JsonUtils.objtoJson(getErrorList(e.getBindingResult().getAllErrors())));
    }

    /**
     * 取出錯誤信息
     *
     * @param list
     * @return
     */
    protected List<FieldErrorItem> getErrorList(List<ObjectError> list) {
        List<FieldErrorItem> errorList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ObjectError error = list.get(i);
            FieldError fe = (FieldError) error;
            FieldErrorItem errorItem = new FieldErrorItem(fe.getField(), fe.getDefaultMessage());
            errorList.add(errorItem);
        }
        return errorList;
    }

    /**
     * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public RestResult runtimeExceptionHandler(Exception e) {
        if (e instanceof RestException) {
            //自定义异常
            RestException pingjiaException = (RestException) e;
            Integer code = pingjiaException.getCode();
            String message = pingjiaException.getMessage();
            if (StringUtils.isEmpty(message)) {
                message = getResult(code).getErrDesc();
            }
            logger.error("\033[0;31m【自定义异常】RestException => {}:{}\033[0m", code, message);
            return ResultUtil.error(code, message);
        }
        //位置异常
        logger.error("\033[0;31m【系统异常】:\033[0m", e);
        return getResult(9999);
    }

    /**
     * 根据code取
     *
     * @param code
     * @param <T>
     * @return
     */
    public <T> RestResult getResult(Integer code) {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale == null) {
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        String message = errorMessageSource.getMessage(code + "", null, locale);
        return ResultUtil.error(code, message);
    }
}
