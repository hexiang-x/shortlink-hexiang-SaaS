package com.hexiang.shotlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;

@Data
public class ShortLinkPageReqDTO extends Page {
    String gid;
}
