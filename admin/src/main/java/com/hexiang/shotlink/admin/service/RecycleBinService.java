package com.hexiang.shotlink.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hexiang.shotlink.admin.common.convention.result.Result;
import com.hexiang.shotlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkPageRespDTO;

import java.util.List;

public interface RecycleBinService {

    public Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO shortLinkRecycleBinPageReqDTO);
}
