package com.hexiang.shotlink.project.dto.resp;

import lombok.Data;

@Data
public class ShortLinkGroupCountResqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组中短链接数量
     */
    private Integer shortLinkCount;

}
