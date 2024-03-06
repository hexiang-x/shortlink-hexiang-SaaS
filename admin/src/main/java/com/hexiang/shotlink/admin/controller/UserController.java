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

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String name){
        return Results.success(userService.getUserByUserName(name));
    }

    @GetMapping("/api/short-link/admin/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String name){
        return Results.success(BeanUtil.toBean(userService.getUserByUserName(name), UserActualRespDTO.class));
    }

    @GetMapping("/api/short-link/admin/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String name){
        System.out.println(name);
        return Results.success(userService.hasUsername(name));
    }

    @PostMapping("/api/short-link/admin/v1/user")
    public Result<Void> registerUser(@RequestBody UserRegisterReqDTO requestParm){
        userService.register(requestParm);
        return Results.success();
    }

    @PutMapping("/api/short-link/admin/v1/user")
    public Result<Boolean> updateUser(@RequestBody UserUpdateReqDTO requestParm){
        Boolean result = userService.updateUser(requestParm);
        return Results.success(result);
    }

    @PostMapping ("/api/short-link/admin/v1/user/login")
    public Result<UserLoginTokenRespDTO> loginUser(@RequestBody UserLoginReqDTO requestParm){
        UserLoginTokenRespDTO userLoginTokenRespDTO = userService.loginUser(requestParm);
        return Results.success(userLoginTokenRespDTO);
    }

    @GetMapping("/api/short-link/admin/v1/user/check-login")
    public Result<Boolean> checkLoginUser(@RequestParam("username") String username, @RequestParam("token") String token){
        return Results.success(userService.checkLoginUser(username,token));
    }

    @GetMapping("/api/short-link/admin/v1/user/logout")
    public Result<Void> logoutUser(@RequestParam("username") String username, @RequestParam("token") String token){
        userService.logoutUser(username,token);
        return Results.success();
    }

    @GetMapping("/api")
    public String getTest(){
        return "hello";
    }
}
