package com.hexiang.shotlink.admin.config;

import com.hexiang.shotlink.admin.common.biz.user.UserTransmitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 配置用户过滤器
 * 过滤所有请求
 */

@Configuration
public class UserConfig {

    @Bean
    public FilterRegistrationBean<UserTransmitFilter> globalUserTransmitFilter(StringRedisTemplate stringRedisTemplate){
        UserTransmitFilter userTransmitFilter = new UserTransmitFilter(stringRedisTemplate);
        FilterRegistrationBean<UserTransmitFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(userTransmitFilter);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        filterFilterRegistrationBean.setOrder(0);
        return filterFilterRegistrationBean;
    }
}
