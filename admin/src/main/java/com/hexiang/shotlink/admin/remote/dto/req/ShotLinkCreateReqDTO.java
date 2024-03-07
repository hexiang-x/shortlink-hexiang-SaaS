package com.hexiang.shotlink.admin.remote.dto.req;

import lombok.Data;

@Data
public class ShotLinkCreateReqDTO {
    /**
     * domain
     */
    private String domain;

    /**
     * gid
     */
    private String gid;

    /**
     * origin_url
     */
    private String originUrl;

    /**
     * create_type
     */
    private int createType;

    /**
     * describe
     */
    private String describe;
}
