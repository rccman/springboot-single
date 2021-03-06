package com.rcc.test.controller;

import com.rcc.api.entity.TestParam;
import com.rcc.api.entity.UserParam;
import com.rcc.test.base.PageBean;
import com.rcc.test.base.RestResult;
import com.rcc.test.config.stream.StreamProducer;
import com.rcc.test.exception.RestException;
import com.rcc.test.service.UserService;
import com.rcc.test.utils.RedisService;
import com.rcc.test.utils.ResultUtil;
import com.rcc.test.utils.ThreadPoolUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


@Api(tags = "测试控制器")
@RestController
public class TestController {

    @Value("${my.hello}")
    private String message;

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private StreamProducer streamProducer;

    @GetMapping("/")
    public String home() {
        TestParam testBean = new TestParam();
        return message;
    }
    @ApiOperation(value = "测试Post请求校验参数", tags = {"测试控制器"}, notes = "测试Post请求校验参数", response = TestParam.class, code = 0)
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功", response = TestParam.class)
    })
    @PostMapping("/test")
    @ResponseBody
    public TestParam test(@RequestBody @Validated TestParam testParam) {
        testParam.setTime(new Date());
        return testParam;
    }
    @GetMapping("/user")
    public String findPageList() {
        UserParam userParam = new UserParam();
        userParam.setPageSize(2);
        userParam.setPageIndex(1);
        userParam.setSort("id");
        userParam.setOrder("DESC");
        userParam.setName("任");
        PageBean pageList = userService.findPageList(userParam);
        return String.valueOf(pageList.getTotalPage());
    }
    @GetMapping("/redis/{key}/{value}")
    public String testRedis(@PathVariable("key") String key,@PathVariable("value") String value) {
        try{
            redisService.put(key,new TestParam(value), 500);
            redisService.put(key+":str",value, 500);
        }catch (Exception e){
            System.out.println(e);
            return "failure";
        }
        return "success";
    }

    @GetMapping("/redis/hash/{key}/{hashKey}/{value}")
    public String testRedisHash(@PathVariable("key") String key,@PathVariable("hashKey") String hashKey,@PathVariable("value") String value) {
        try{
            redisService.put(key,hashKey,new TestParam(value), 500);
//            redisService.put(key+":str",hashKey,value, 500);
        }catch (Exception e){
            System.out.println(e);
            return "failure";
        }
        return "success";
    }
    @GetMapping("/redis/get")
    public String redisGet() {
//        redisService.get("common:Hash","haseKey");
        TestParam common = redisService.get("hashCommon","hashKey", TestParam.class);
        return common.getMessage();
    }

    @GetMapping("/redis/del")
    public String redisDel() {
//        redisService.del("test:redis","key");
//        redisService.clearRedisCache("test:hash", Collections.singletonList("hashKey"));
        redisService.clearRedisCache();
        return "";
    }


    @ApiOperation(value = "测试自定义异常", tags = {"测试控制器"}, notes = "测试自定义异常", response = RestResult.class, code = 0)
    @PostMapping("/exception")
    @ResponseBody
    public RestResult exception(@RequestBody @Validated TestParam testParam) throws RestException {
        throw new RestException(990104);
    }

    @ApiOperation(value = "测试消息队列发送消息", tags = {"测试控制器"}, notes = "测试消息队列存放消息", response = RestResult.class, code = 0)
    @PostMapping("/sendMessageMQ")
    @ResponseBody
    public RestResult sendMessageMQ(@RequestBody @Validated TestParam testParam) throws RestException {
        boolean send = streamProducer.produceMsg(testParam);
        return ResultUtil.success(send);
    }

    @PostMapping("/testFutureTask")
    @ResponseBody
    public RestResult testFutureTask(@RequestBody @Validated TestParam testParam) throws RestException, ExecutionException, InterruptedException {
        return userService.testFutureTask();
    }

    public static void main(String[] args) {
        String s1 = new String("计算机");
        String s2 = s1.intern();
        String s3 = "计算机";
        System.out.println(s2);//计算机
        System.out.println(s1 == s2);//false，因为一个是堆内存中的 String 对象一个是常量池中的 String 对象，
        System.out.println(s3 == s2);//true，因为两个都是常量池中的 String 对象
        Runnable runnable = () -> System.out.println(System.currentTimeMillis());
        runnable.run();
        ThreadPoolUtil.threadPool.submit(() ->{
            System.out.println("当前系统时间是：");
            System.out.println(System.currentTimeMillis());
        });
        Runnable runnable1 = System::currentTimeMillis;
        runnable1.run();

        final int num = 1;
        Converter<Integer, String> stringConverter =
                (from) -> String.valueOf(from + num);

        stringConverter.convert(2);     // 3


    }
}
