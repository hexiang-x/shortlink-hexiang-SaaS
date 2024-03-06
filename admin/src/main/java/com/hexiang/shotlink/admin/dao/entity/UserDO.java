package com.hexiang.shotlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hexiang.shotlink.admin.common.database.BaseDO;
import lombok.Data;


import java.util.Date;

@Data
@TableName("t_user")
public class UserDO extends BaseDO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 注销时间戳
     */
    private Long deletionTime;

//    /**
//     * 注册时间
//     */
//    @TableField(fill = FieldFill.INSERT)
//    private Date createTime;
//
//    /**
//     * 最近更新时间
//     */
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private Date updateTime;
//
//    /**
//     * 删除标记
//     */
//    @TableField(fill = FieldFill.INSERT)
//    private Integer delFlag;
}
