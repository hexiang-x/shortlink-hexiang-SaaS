package com.hexiang.shotlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.hexiang.shotlink.admin.common.convention.result.Result;
import com.hexiang.shotlink.admin.common.convention.result.Results;
import com.hexiang.shotlink.admin.dto.req.UserLoginReqDTO;
import com.hexiang.shotlink.admin.dto.req.UserRegisterReqDTO;
import com.hexiang.shotlink.admin.dto.req.UserUpdateReqDTO;
import com.hexiang.shotlink.admin.dto.resp.UserActualRespDTO;
import com.hexiang.shotlink.admin.dto.resp.UserLoginTokenRespDTO;
import com.hexiang.shotlink.admin.dto.resp.UserRespDTO;
import com.hexiang.shotlink.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 查询用户 按名
     * @param name
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String name){
        return Results.success(userService.getUserByUserName(name));
    }

    /**
     * 查询用户真实信息，去除加密
     * @param name
     * @return
     */

    @GetMapping("/api/short-link/admin/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String name){
        return Results.success(BeanUtil.toBean(userService.getUserByUserName(name), UserActualRespDTO.class));
    }

    /**
     * 判断用户名是否重复
     * @param name
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String name){
        System.out.println(name);
        return Results.success(userService.hasUsername(name));
    }

    /**
     * 注册新用户
     * @param requestParm
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/user")
    public Result<Void> registerUser(@RequestBody UserRegisterReqDTO requestParm){
        userService.register(requestParm);
        return Results.success();
    }

    /**
     * 更新用户信息
     * @param requestParm
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/user")
    public Result<Boolean> updateUser(@RequestBody UserUpdateReqDTO requestParm){
        Boolean result = userService.updateUser(requestParm);
        return Results.success(result);
    }

    /**
     * 登录用户，保存token
     * @param requestParm
     * @return
     */
    @PostMapping ("/api/short-link/admin/v1/user/login")
    public Result<UserLoginTokenRespDTO> loginUser(@RequestBody UserLoginReqDTO requestParm){
        UserLoginTokenRespDTO userLoginTokenRespDTO = userService.loginUser(requestParm);
        return Results.success(userLoginTokenRespDTO);
    }

    /**
     * 检查用户是否登录
     * @param username
     * @param token
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/user/check-login")
    public Result<Boolean> checkLoginUser(@RequestParam("username") String username, @RequestParam("token") String token){
        return Results.success(userService.checkLoginUser(username,token));
    }

    /**
     * 登出用户（删除redis中token）
     * @param username
     * @param token
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/user/logout")
    public Result<Void> logoutUser(@RequestParam("username") String username, @RequestParam("token") String token){
        userService.logoutUser(username,token);
        return Results.success();
    }

    /**
     * 测试用例
     * @return
     */
    @GetMapping("/api")
    public String getTest(){
        return "hello";
    }
}
