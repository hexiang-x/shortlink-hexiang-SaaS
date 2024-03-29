package com.hexiang.shotlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hexiang.shotlink.admin.common.convention.result.Result;
import com.hexiang.shotlink.admin.common.convention.result.Results;
import com.hexiang.shotlink.admin.remote.ShortLinkActualRemoteService;
import com.hexiang.shotlink.admin.remote.ShortLinkRemoteService;
import com.hexiang.shotlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.hexiang.shotlink.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import com.hexiang.shotlink.admin.remote.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkGroupCountResqDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接接口（远程调用中台接口）
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkController {
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};

    private final ShortLinkActualRemoteService shortLinkActualRemoteService;

    /**
     * 新增短链接
     * @param requestParm
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShotLinkCreateReqDTO requestParm){
        return shortLinkActualRemoteService.createShortLink(requestParm);
    }

    /**
     * 分页查询
     * @param shortLinkPageReqDTO
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<Page<ShortLinkPageRespDTO>> pageSelect(ShortLinkPageReqDTO shortLinkPageReqDTO){
        return shortLinkActualRemoteService.pageShortLink(shortLinkPageReqDTO.getGid(), shortLinkPageReqDTO.getCurrent(), shortLinkPageReqDTO.getSize());
    }

    /**
     * 查询分组数量
     * @param requestParm
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/count")
    public Result<List<ShortLinkGroupCountResqDTO>> groupShortLinkCount(@RequestParam List<String> requestParm){
        return shortLinkActualRemoteService.listGroupShortLinkCount(requestParm);
    }

    /**
     * 更新短链接
     * @param shortLinkUpdateReqDTO
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/update")
    public Result<Boolean> updateShortLink(@RequestBody ShortLinkUpdateReqDTO shortLinkUpdateReqDTO){
        return shortLinkActualRemoteService.updateShortLink(shortLinkUpdateReqDTO);
    }


}
