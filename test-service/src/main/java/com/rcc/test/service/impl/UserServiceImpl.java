package com.rcc.test.service.impl;

import com.rcc.api.entity.UserParam;
import com.rcc.test.base.BaseService;
import com.rcc.test.base.PageBean;
import com.rcc.test.dao.UserDao;
import com.rcc.test.entity.UserEntity;
import com.rcc.test.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseService<UserEntity,UserParam, UserDao> implements UserService {
    @Override
    public PageBean findPageList(UserParam entity) {
        return this.getPageBean(entity);
    }
}
