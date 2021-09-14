package com.zlk.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlk.db.model.param.UserParam;
import com.zlk.db.model.po.User;

import java.util.List;

/**
* @author  likuan.zhou
* @title:  IUserService
* @description: 用户业务接口
* @date 2021-09-14
*/
public interface IUserService extends IService<User> {

    /**
     * 分页查询用户列表
     * @param userParam 查询参数
     * @return 用户列表
     */
    List<User> pageList(UserParam userParam);

}
