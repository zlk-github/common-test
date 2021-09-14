package com.zlk.db.mapper;

import com.zlk.db.model.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;

/**
* @author  likuan.zhou
* @title:  UserMapper
* @description: 用户mapper
* @date 2021-09-14
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
