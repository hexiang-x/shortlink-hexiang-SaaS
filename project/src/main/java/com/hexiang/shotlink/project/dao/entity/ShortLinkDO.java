package com.hexiang.shotlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hexiang.shotlink.project.common.database.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("t_link")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkDO extends BaseDO {

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
     * click_num
     */
    private Integer clickNum;

    /**
     * gid
     */
    private String gid;

    /**
     * enable_status
     */
    private int enableStatus;

    /**
     * create_type
     */
    private int createType;

    /**
     * valid_data_type
     */
    private int validDataType;

    /**
     * valid_data
     */
    private Date validData;

    /**
     * describe
     */
    @TableField("`describe`")
    private String describe;


}
