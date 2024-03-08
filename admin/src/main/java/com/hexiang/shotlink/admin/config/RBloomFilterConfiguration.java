package com.hexiang.shotlink.admin.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 布隆过滤器：过滤注册用户名
 */
@Configuration
public class RBloomFilterConfiguration {

    @Bean
    public RBloomFilter<String> userRegisterCachePenetrationBloomFilter(RedissonClient redissonClient){
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("userRegisterCachePenetrationBloomFilter");
        cachePenetrationBloomFilter.tryInit(100000000L, 0.001);
        return cachePenetrationBloomFilter;
    }
}
