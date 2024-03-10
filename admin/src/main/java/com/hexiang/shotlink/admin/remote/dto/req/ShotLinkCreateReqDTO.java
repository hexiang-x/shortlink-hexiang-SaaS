package com.hexiang.shotlink.admin.remote.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

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

    /**
     * valid_data_type
     */
    private Integer validDataType;

    /**
     * valid_data
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validData;
}
