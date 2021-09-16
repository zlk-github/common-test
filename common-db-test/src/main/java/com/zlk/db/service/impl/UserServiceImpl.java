package com.zlk.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlk.common.core.page.PageBean;
import com.zlk.common.core.util.BeanExUtils;
import com.zlk.db.mapper.UserMapper;
import com.zlk.db.model.param.UserParam;
import com.zlk.db.model.po.User;
import com.zlk.db.model.vo.UserVO;
import com.zlk.db.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author  likuan.zhou
* @title:  UserServiceImpl
* @description: 用户业务接口实现
* @date 2021-09-14
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageBean<UserVO> pageList(UserParam userParam) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNoneBlank(userParam.getName())) {
            userLambdaQueryWrapper.like(User::getName , userParam.getName());
        }
        if (Objects.nonNull(userParam.getStatus())) {
            userLambdaQueryWrapper.eq(User::getStatus,userParam.getStatus());
        }
        Page<User> userPage = new Page<>(userParam.getPageNo() , userParam.getPageNo());
        IPage<User> userIPage = userMapper.selectPage(userPage, userLambdaQueryWrapper);
        PageBean<UserVO> pageBean = BeanExUtils.copyPageBean(new PageBean<>(userIPage.getRecords()), userDTO -> BeanExUtils.copyProperties(userDTO, UserVO.class));
        return pageBean;
    }
}
