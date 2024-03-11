package com.hexiang.shotlink.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hexiang.shotlink.admin.common.convention.result.Result;
import com.hexiang.shotlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkPageRespDTO;

public interface RecycleBinService {

    public Result<Page<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO shortLinkRecycleBinPageReqDTO);
}
