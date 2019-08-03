package com.rcc.test.service.impl;

import com.google.common.collect.Lists;
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
import java.util.List;
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
        Future<UserEntity> submit1 = executorService.submit(new Callable<UserEntity>() {
            @Override
            public UserEntity call() throws Exception {
                UserEntity userEntity = new UserEntity();
                userEntity.setId(1);
                userEntity.setName("Thread1");
                int time = 5000;
                System.out.println("Thread1 线程开始");
                Thread.sleep(time);
                System.out.println("Thread1 线程结束");
                return userEntity;
            }
        });
        Future<UserEntity> submit2 = executorService.submit(new Callable<UserEntity>() {
            @Override
            public UserEntity call() throws Exception {
                UserEntity userEntity = new UserEntity();
                userEntity.setId(2);
                userEntity.setName("Thread2");
                int time = 8000;
                System.out.println("Thread2 线程开始");
                Thread.sleep(time);
                System.out.println("Thread2 线程结束");
                return userEntity;
            }
        });
        System.out.println("Thread1 休眠时间："+ submit1.get());
        System.out.println("Thread2 休眠时间："+ submit2.get());
        double sub = PubArithUtil.sub(System.currentTimeMillis(), currentTimeMillis);
        return ResultUtil.success(sub);
    }

    public static void main(String[] args) {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setId(3);
        userEntity1.setName("Thread3");
        userEntity1.setValue(3L);
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setId(2);
        userEntity2.setName("Thread2");
        userEntity2.setValue(2L);
        List<UserEntity> list = Lists.newArrayList();
        list.add(userEntity1);
        list.add(userEntity2);
        list.sort((user1,user2) -> user1.getId()>user2.getId()?1:-1);
//        list.sort((user1,user2) -> user1.getValue().compareTo(user2.getValue()));
        for (UserEntity userEntity : list) {
            System.out.println(userEntity);
        }
    }
}
