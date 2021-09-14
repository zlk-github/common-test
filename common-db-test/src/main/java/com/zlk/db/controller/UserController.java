package com.zlk.db.controller;


import com.zlk.db.model.param.UserParam;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;

/**
* @author  likuan.zhou
* @title:  UserController
* @description: 用户API
* @date 2021-09-14
*/
@RestController
@RequestMapping("/user")
@ApiModel(value="用户API", description="用户API")
public class UserController {

    public Response<?> pageList(UserParam userParam){
        return null;
    }


}
