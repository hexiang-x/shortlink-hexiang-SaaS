package com.hexiang.shotlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexiang.shotlink.project.dao.entity.ShortLinkDO;
import com.hexiang.shotlink.project.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkCreateRespDTO;

public interface ShortLinkService extends IService<ShortLinkDO> {
    /**
     * 创建短链接
     *
     * @param requestParm
     * @return
     */
    public ShortLinkCreateRespDTO createShortLink(ShotLinkCreateReqDTO requestParm);
}
