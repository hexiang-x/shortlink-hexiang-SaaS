package com.hexiang.shotlink.admin.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hexiang.shotlink.admin.common.serialize.PhoneDensensizizationSerializer;
import lombok.Data;

@Data
public class UserRespDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    @JsonSerialize(using = PhoneDensensizizationSerializer.class)
    private String phone;

    /**
     * 邮箱
     */
    private String mail;
}
