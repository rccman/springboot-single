package com.rcc.test.service.impl;

import com.rcc.api.entity.UserParam;
import com.rcc.test.base.BaseService;
import com.rcc.test.base.PageBean;
import com.rcc.test.base.RestResult;
import com.rcc.test.dao.UserDao;
import com.rcc.test.entity.UserEntity;
import com.rcc.test.service.UserService;
import com.rcc.test.utils.PubArithUtil;
import com.rcc.test.utils.ResultUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.*;

@Service
public class UserServiceImpl extends BaseService<UserEntity,UserParam, UserDao> implements UserService {
    private static ExecutorService executorService = Executors.newFixedThreadPool(2);
    @Override
    public PageBean findPageList(UserParam entity) {
        return this.getPageBean(entity);
    }

    @Override
    public RestResult testFutureTask() throws ExecutionException, InterruptedException {
        long currentTimeMillis = System.currentTimeMillis();
        Future<Object> submit1 = executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                int time = 5000;
                System.out.println("Thread1 线程开始");
                Thread.sleep(time);
                System.out.println("Thread1 线程结束");
                return time;
            }
        });
        Future<Object> submit2 = executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                int time = 8000;
                System.out.println("Thread2 线程开始");
                Thread.sleep(time);
                System.out.println("Thread2 线程结束");
                return time;
            }
        });
        System.out.println("Thread1 休眠时间："+ submit1.get());
        System.out.println("Thread2 休眠时间："+ submit2.get());
        double sub = PubArithUtil.sub(System.currentTimeMillis(), currentTimeMillis);
        return ResultUtil.success(sub);
    }
}
