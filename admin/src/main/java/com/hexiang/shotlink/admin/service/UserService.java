package com.hexiang.shotlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexiang.shotlink.admin.dao.entity.UserDO;
import com.hexiang.shotlink.admin.dto.req.UserLoginReqDTO;
import com.hexiang.shotlink.admin.dto.req.UserRegisterReqDTO;
import com.hexiang.shotlink.admin.dto.req.UserUpdateReqDTO;
import com.hexiang.shotlink.admin.dto.resp.UserLoginTokenRespDTO;
import com.hexiang.shotlink.admin.dto.resp.UserRespDTO;

public interface UserService extends IService<UserDO> {

    /**
     * 按名获取用户
     * @param username 用户名
     * @return 用户对象UserRepsDTO
     */
    UserRespDTO getUserByUserName(String username);

    /**
     * 检查用户名是否存在
     * @param username
     * @return
     */
    Boolean hasUsername(String username);

    /**
     * 注册用户
     */
    void register(UserRegisterReqDTO requestParm);

    /**
     * 更新用户信息
     * @param requestParm 用户信息
     * @return 是否成功
     */
    Boolean updateUser(UserUpdateReqDTO requestParm);

    /**
     * 用户登录
     *
     * @param requestParm 用户名密码
     * @return 登录状态
     */
    UserLoginTokenRespDTO loginUser(UserLoginReqDTO requestParm);

    /**
     * 检查用户是否登录
     * @param username
     * @param token
     * @return
     */
    Boolean checkLoginUser(String username, String token);

    /**
     * 用户登出
     * @param username
     * @param token
     */
    void logoutUser(String username, String token);

}
