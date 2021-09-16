package com.zlk.db.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlk.common.core.page.PageBean;
import com.zlk.db.model.param.UserParam;
import com.zlk.db.model.po.User;
import com.zlk.db.model.vo.UserVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * @author likuan.zhou
 * @title: IUserServiceTest
 * @projectName common-test
 * @description: 用户业务测试
 * @date 2021/9/15/015 19:37
 */
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
// 注 类和方法必须为public才能运行
@SpringBootTest
@RunWith(SpringRunner.class)
public class IUserServiceTest {
    @Autowired
    private IUserService userService;

    @Test
    public void testPageList1() {
        UserParam userParam = new UserParam();
        userParam.setPageNo(1L);
        userParam.setPageSize(10);
        PageBean<UserVO> pageList = userService.pageList(userParam);
        System.out.println(pageList.getTotal());
        assertNotNull(pageList);
    }

    @Test
    public void testPageList() {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.like(User::getName , "name");
        userLambdaQueryWrapper.eq(User::getStatus,1);
        Page<User> userPage = new Page<>(1 , 10);
        IPage<User> userIPage = userService.page(userPage, userLambdaQueryWrapper);
        System.out.println(userIPage);
        assertNotNull(userIPage);
    }

    @Test
    public void testQueryList() {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.like(User::getName , "name");
        userLambdaQueryWrapper.eq(User::getStatus,1);
        //查询列表
        List<User> list = userService.list(userLambdaQueryWrapper);
        System.out.println(list);
        assertNotNull(list);
    }

    @Test
    public void testGetById() {
        //id查询
        User user = userService.getById(1L);
        System.out.println(user);
        assertNotNull(user);
    }

    @Test
    public void testSave() {
        User user = new User();
        user.setName("名字");
        user.setStatus(1);
        //新增
        userService.save(user);

       //(需要返回主键id需要自定义sql) 需要自己封装SQL
      /*<insert id="insertUser" parameterType="cn.bear.neckmassager.api.entity.Userinfo" useGeneratedKeys="true" keyProperty="id">
                insert into userinfo(user_name,create_time) values (#{userName},#{createTime});
        </insert>*/

        //更新活保存 userService.saveOrUpdate(user)
        //批量新增  userService.saveBatch(userList);
    }

    @Test
    public void testUpdateById() {
        User user = new User();
        user.setId(1L);
        user.setName("名字");
        user.setStatus(1);
        //更新
        userService.updateById(user);
    }

    @Test
    public void testRemoveById() {
        //id删除
        userService.removeById(1L);
    }
}
