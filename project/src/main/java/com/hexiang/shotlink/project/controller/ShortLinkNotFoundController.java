package com.hexiang.shotlink.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class ShortLinkNotFoundController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/page/notfound")
    public String notfound(){
        return "notfound";
    }


    @GetMapping("/api/short-link/v1/redisclean")
    public Map<String, Object> cleanRedis() {
        Map<String, Object> map = new HashMap<>();
        try {
            // 获取所有key
            Set<String> keys = stringRedisTemplate.keys("*");
            assert keys != null;
            // 迭代
            Iterator<String> it1 = keys.iterator();
            while (it1.hasNext()) {
                // 循环删除
                stringRedisTemplate.delete(it1.next());
            }
            map.put("code", 1);
            map.put("msg", "清理全局缓存成功");
            return map;
        } catch (Exception e) {
            map.put("code", -1);
            map.put("msg", "清理全局缓存失败");
            return map;
        }
    }
}
