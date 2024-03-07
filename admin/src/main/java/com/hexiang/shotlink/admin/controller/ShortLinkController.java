package com.hexiang.shotlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hexiang.shotlink.admin.common.convention.result.Result;
import com.hexiang.shotlink.admin.common.convention.result.Results;
import com.hexiang.shotlink.admin.remote.ShortLinkRemoteService;
import com.hexiang.shotlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.hexiang.shotlink.admin.remote.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortLinkController {
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};

    @PostMapping("/api/short-link/admin/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShotLinkCreateReqDTO requestParm){
        return shortLinkRemoteService.createShortLink(requestParm);
    }
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageSelect(ShortLinkPageReqDTO shortLinkPageReqDTO){
        return shortLinkRemoteService.pageSelect(shortLinkPageReqDTO);
    }
}
