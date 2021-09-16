package com.zlk.db.controller;


import com.zlk.common.core.page.PageBean;
import com.zlk.common.core.response.Response;
import com.zlk.db.model.param.UserParam;
import com.zlk.db.model.vo.UserVO;
import com.zlk.db.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
* @author  likuan.zhou
* @title:  UserController
* @description: 用户API
* @date 2021-09-14
*/
@RestController
@RequestMapping("/user")
@Api(value="用户API", description="用户API")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/pageList")
    @ApiOperation(value="用户分页列表查询")
    public Response<?> pageList(UserParam userParam){
        PageBean<UserVO> pageList = userService.pageList(userParam);
        return Response.newSuccessResponse(pageList);
    }

}
