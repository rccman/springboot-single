package com.rcc.test.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rcc.api.base.QueryCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * com.hzfh.noa.common.base
 *
 * @Project: noa
 * @Author: rencc
 * @Description:
 * @Date: 2018/11/19 16:17
 * @Source: Created with IntelliJ IDEA.
 */
public class BaseService<T, TC extends QueryCondition, M extends BaseDao> {

    @Autowired
    protected M m;

    public T findById(String id) {
        return (T) m.findById(id);
    }

    public void addEntity(T t) {
        m.addEntity(t);
    }

    public void editEntity(T t) {
        m.editEntity(t);
    }

    public void deleteEntity(String id) {
        m.deleteEntity(id);
    }

    public PageBean getPageBean(TC tc) {
        if (StringUtils.isEmpty(tc.getOrder()) || StringUtils.isEmpty(tc.getSort())) {
            PageHelper.startPage(tc.getPageIndex(), tc.getPageSize());
        } else {
            StringBuilder orderByBuilder = new StringBuilder();
            String[] order = tc.getOrder().split(",");
            String[] sort = tc.getSort().split(",");
            if (order.length > 0 && sort.length > 0 && order.length == sort.length) {
                for (int i = 0; i < order.length; i++) {
                    orderByBuilder.append(sort[i] + " " + order[i] + ",");
                }
            }
            String orderBy = orderByBuilder.toString();
            PageHelper.startPage(tc.getPageIndex(), tc.getPageSize(), orderBy.substring(0, orderBy.length() - 1));
        }
        List<T> tList = m.findByWhere(tc);
        PageInfo<T> pageInfo = new PageInfo<T>(tList);
        return new PageBean(tc.getPageIndex(), tc.getPageSize(), Math.toIntExact(pageInfo.getTotal()), tList);
    }
}
