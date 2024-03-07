package com.hexiang.shotlink.admin.dto.resp;

import lombok.Data;

@Data
public class GroupRespDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 分组中短链接数量
     */
    private Integer count;

    /**
     * 分组排序
     */
    private Integer sortOrder;
}
