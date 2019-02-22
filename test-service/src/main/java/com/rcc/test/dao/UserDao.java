package com.rcc.test.dao;

import com.rcc.api.entity.UserParam;
import com.rcc.test.base.BaseDao;
import com.rcc.test.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseDao<UserEntity, UserParam> {
}
