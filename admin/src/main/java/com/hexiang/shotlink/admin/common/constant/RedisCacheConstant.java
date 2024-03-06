package com.hexiang.shotlink.admin.common.constant;

/**
 * 后台管理 Redis 缓存常量类
 */
public class RedisCacheConstant {
    /**
     * 用户注册分布式锁
     */
    public static final String LOCK_USER_REGISTER_KEY = "short-link:lock_user-register";

    /**
     * 用户登录标识
     */
    public static final String USER_LOGIN_KEY = "short-link:login:";

}
