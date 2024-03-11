package com.hexiang.shotlink.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(value = "com.hexiang.shotlink.admin.remote")
@MapperScan("com.hexiang.shotlink.admin.dao.mapper")
public class ShotLinkAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShotLinkAdminApplication.class, args);
    }
}
