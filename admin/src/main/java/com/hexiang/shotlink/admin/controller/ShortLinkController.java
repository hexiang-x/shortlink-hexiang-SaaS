package com.hexiang.shotlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hexiang.shotlink.admin.common.convention.result.Result;
import com.hexiang.shotlink.admin.common.convention.result.Results;
import com.hexiang.shotlink.admin.remote.ShortLinkRemoteService;
import com.hexiang.shotlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.hexiang.shotlink.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import com.hexiang.shotlink.admin.remote.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkGroupCountResqDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/api/short-link/admin/v1/groupcount")
    public Result<List<ShortLinkGroupCountResqDTO>> groupCount(@RequestParam List<String> requestParm){
        return shortLinkRemoteService.groupCount(requestParm);
    }

    @PutMapping("/api/short-link/admin/v1/update")
    public Result<Boolean> updateShortLink(@RequestBody ShortLinkUpdateReqDTO shortLinkUpdateReqDTO){
        return shortLinkRemoteService.updateShortLink(shortLinkUpdateReqDTO);
    }


}
