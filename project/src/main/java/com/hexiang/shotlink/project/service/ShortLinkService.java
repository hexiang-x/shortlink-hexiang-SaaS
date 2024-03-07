package com.hexiang.shotlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hexiang.shotlink.project.dao.entity.ShortLinkDO;
import com.hexiang.shotlink.project.dto.req.ShortLinkPageReqDTO;
import com.hexiang.shotlink.project.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkGroupCountResqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkPageRespDTO;

import java.util.List;

public interface ShortLinkService extends IService<ShortLinkDO> {
    /**
     * 创建短链接
     *
     * @param requestParm
     * @return
     */
    public ShortLinkCreateRespDTO createShortLink(ShotLinkCreateReqDTO requestParm);

    IPage<ShortLinkPageRespDTO> pageSelect(ShortLinkPageReqDTO shortLinkPageReqDTO);

    List<ShortLinkGroupCountResqDTO> groupCount(List<String> requestParm);
}
