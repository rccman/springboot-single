package com.rcc.test.config.aop;

import com.rcc.test.base.RestResult;
import com.rcc.test.exception.ExceptionHandle;
import com.rcc.test.utils.HttpUtils;
import com.rcc.test.utils.JsonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * AOP用户检测请求信息和返回信息
 *
 * @Author: rencc
 * @Description:
 * @Date: 2018/7/2 9:47
 * @Source: Created with IntelliJ IDEA.
 */
@Aspect
@Component
public class HttpAspect {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpAspect.class);

    @Autowired
    private ExceptionHandle exceptionHandle;

    @Pointcut("execution(public * com.rcc.*.controller.*.*(..))")
    public void log(){

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //url
        LOGGER.info("\033[0;34m==> url:\033[0m {}",request.getRequestURL());
        //method
        LOGGER.info("\033[0;34m==> method:  {}\033[0m",request.getMethod());
        //ip
        LOGGER.info("\033[0;34m==> IP:  {}\033[0m", HttpUtils.getClientIpAddr(request));
        //class_method
        LOGGER.info("\033[0;34m==> class_method:  {}\033[0m",joinPoint.getSignature().getDeclaringTypeName() + "," + joinPoint.getSignature().getName());
        //args[]
        LOGGER.info("\033[0;34m==> args:  {}\033[0m", JsonUtils.objtoJson(joinPoint.getArgs()));
    }

    @Around("log()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RestResult result = null;
        try {

        } catch (Exception e) {
            return exceptionHandle.runtimeExceptionHandler(e);
        }
        if(result == null){
            return proceedingJoinPoint.proceed();
        }else {
            return result;
        }
    }

    @AfterReturning(pointcut = "log()",returning = "object")//打印输出结果
    public void doAfterReturing(Object object){
        if (object != null){
            LOGGER.info("\033[0;34m==> response: {}\033[0m",JsonUtils.objtoJson(object));
        }
    }
}
