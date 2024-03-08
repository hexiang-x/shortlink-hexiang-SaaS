package com.hexiang.shotlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ShortLinkUpdateReqDTO {
    /**
     * domain
     */
    private String domain;

    /**
     * short_url
     */
    private String shortUrl;

    /**
     * full_short_url
     */
    private String fullShortUrl;

    /**
     * origin_url
     */
    private String originUrl;

    /**
     * gid
     */
    private String gid;

    /**
     * 原始GID
     */
    private String originalGid;


    /**
     * valid_data_type
     */
    private Integer validDataType;

    /**
     * valid_data
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validData;

    /**
     * describe
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 网站标识
     */
    private String favicon;

}
