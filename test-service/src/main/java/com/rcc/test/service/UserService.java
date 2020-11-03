package com.rcc.test.service;

import com.rcc.api.entity.UserParam;
import com.rcc.test.base.PageBean;
import com.rcc.test.base.RestResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public interface UserService {
    PageBean findPageList(UserParam entity);

    RestResult testFutureTask() throws ExecutionException, InterruptedException;

}
