package com.rcc.test.service;

import com.rcc.api.entity.UserParam;
import com.rcc.test.base.PageBean;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    PageBean findPageList(UserParam entity);
}
