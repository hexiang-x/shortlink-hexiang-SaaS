package com.hexiang.shotlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hexiang.shotlink.project.dto.req.RecycleBinRecoverReqDTO;
import com.hexiang.shotlink.project.dto.req.RecycleBinRemoveReqDTO;
import com.hexiang.shotlink.project.dto.req.RecycleBinSaveReqDTO;
import com.hexiang.shotlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkPageRespDTO;

public interface RecycleBinService {
    void saveRecycleBin(RecycleBinSaveReqDTO requestParm);

    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam);

    void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam);

    void removeRecycleBin(RecycleBinRemoveReqDTO requestParam);
}
