package com.rcc.test.base;

import com.rcc.api.base.QueryCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * com.hzfh.noa.common.base
 *
 * @Project: noa
 * @Author: rencc
 * @Description:
 * @Date: 2018/11/19 16:21
 * @Source: Created with IntelliJ IDEA.
 */
public interface BaseDao <T ,TC extends QueryCondition>{
    String findNextId();

    void addEntity(T var1);

    void editEntity(T var1);

    void deleteEntity(@Param("id") String var1);

    T findById(@Param("id") String var1);

    List<T> findByWhere(TC var1);

    List<T> getPagingListNoPlugin(TC var1);

    long getTotalCount(TC var1);

}
