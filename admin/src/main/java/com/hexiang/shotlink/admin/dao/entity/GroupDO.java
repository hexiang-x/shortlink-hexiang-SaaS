package com.hexiang.shotlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hexiang.shotlink.admin.common.database.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("t_group")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDO extends BaseDO {
    private int id;
    private String gid;
    private String name;
    private String username;
    private int sortOrder;
//    private Date createTime;
//    private Date updateTime;
//    private int delFlag;
}
