package com.hexiang.shotlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hexiang.shotlink.project.dao.entity.ShortLinkDO;
import com.hexiang.shotlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.hexiang.shotlink.project.dto.req.ShortLinkPageReqDTO;
import com.hexiang.shotlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.hexiang.shotlink.project.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkGroupCountResqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkPageRespDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
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

    Boolean updateShortLink(ShortLinkUpdateReqDTO shortLinkUpdateReqDTO);

    void restoreUrl(String shortlink, HttpServletRequest request, HttpServletResponse response) throws IOException;

    void shortLinkStats(String fullShortUrl, String gid, ShortLinkStatsRecordDTO statsRecord);
}
