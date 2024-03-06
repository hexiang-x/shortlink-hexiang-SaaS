package com.hexiang.shotlink.admin.dto.resp;

import lombok.Data;

@Data
public class UserLoginTokenRespDTO {
    private String token;

    public UserLoginTokenRespDTO(String token) {
        this.token = token;
    }
}
