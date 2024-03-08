package com.hexiang.shotlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hexiang.shotlink.project.common.convention.result.Result;
import com.hexiang.shotlink.project.common.convention.result.Results;
import com.hexiang.shotlink.project.dto.req.RecycleBinRecoverReqDTO;
import com.hexiang.shotlink.project.dto.req.RecycleBinRemoveReqDTO;
import com.hexiang.shotlink.project.dto.req.RecycleBinSaveReqDTO;
import com.hexiang.shotlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkPageRespDTO;
import com.hexiang.shotlink.project.service.RecycleBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecycleBinController {
    @Autowired
    private RecycleBinService recycleBinService;

    /**
     * 移入回收站
     * @param requestParm
     * @return
     */
    @PostMapping("/api/short-link/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParm){
        recycleBinService.saveRecycleBin(requestParm);
        return Results.success();
    }

    /**
     * 回收站分页查询
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/v1/recycle-bin/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        return Results.success(recycleBinService.pageShortLink(requestParam));
    }

    /**
     * 恢复短链接
     */
    @PostMapping("/api/short-link/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        recycleBinService.recoverRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 彻底删除短链接
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/v1/recycle-bin/remove")
    public Result<Void> removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam) {
        recycleBinService.removeRecycleBin(requestParam);
        return Results.success();
    }

}
