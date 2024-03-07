package com.hexiang.shotlink.project.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ShortLinkPageRespDTO {
    /**
     * id
     */
    private Long id;

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
     * 有效期类型 0：永久有效 1：自定义
     */
    private Integer validDateType;

    /**
     * 有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 描述
     */
    private String describe;

    /**
     * 网站标识
     */
    private String favicon;
}
