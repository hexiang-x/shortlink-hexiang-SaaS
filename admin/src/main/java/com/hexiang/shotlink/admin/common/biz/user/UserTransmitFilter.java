package com.hexiang.shotlink.admin.common.biz.user;



import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.hexiang.shotlink.admin.common.convention.exception.ClientException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static com.hexiang.shotlink.admin.common.constant.RedisCacheConstant.USER_LOGIN_KEY;

@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {
    private StringRedisTemplate stringRedisTemplate;

    private final static HashSet<String> IGNORE_URL = new HashSet<>(Lists.newArrayList(
            "/api/short-link/admin/v1/user/has-username",
            "/api/short-link/admin/v1/user/login"
    ));

    public UserTransmitFilter(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Collections.list(httpServletRequest.getHeaderNames())
                .forEach(headerName -> System.out.println(headerName + ": " + httpServletRequest.getHeader(headerName)));
        String realUri = httpServletRequest.getRequestURI();
        if(!IGNORE_URL.contains(realUri)){
            String method = httpServletRequest.getMethod();
            if(!(realUri.equals("/api/short-link/admin/v1/user/login") && method.equals("POST"))){
                String username = httpServletRequest.getHeader("username");
                String token = httpServletRequest.getHeader("token");
                Object userInfoJsonString = stringRedisTemplate.opsForHash().get(USER_LOGIN_KEY + username, token);
                if(userInfoJsonString != null){
                    UserInfoDTO userInfoDTO = JSON.parseObject(userInfoJsonString.toString(), UserInfoDTO.class);
                    UserContext.setUser(userInfoDTO);
                }
            }
        }
        try{
            filterChain.doFilter(servletRequest, servletResponse);
        }finally {
            UserContext.removeUser();
        }
    }
}
