package com.hexiang.shotlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hexiang.shotlink.admin.common.convention.result.Result;
import com.hexiang.shotlink.admin.common.convention.result.Results;
import com.hexiang.shotlink.admin.remote.ShortLinkActualRemoteService;
import com.hexiang.shotlink.admin.remote.ShortLinkRemoteService;
import com.hexiang.shotlink.admin.remote.dto.req.RecycleBinRecoverReqDTO;
import com.hexiang.shotlink.admin.remote.dto.req.RecycleBinRemoveReqDTO;
import com.hexiang.shotlink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import com.hexiang.shotlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.hexiang.shotlink.admin.service.RecycleBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecycleBinController {
    private ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };
    @Autowired
    private ShortLinkActualRemoteService shortLinkActualRemoteService;

    @Autowired
    private RecycleBinService recycleBinService;
    /**
     * 移入回收站
     * @param requestParm
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParm){
        return shortLinkActualRemoteService.saveRecycleBin(requestParm);
    }

    /**
     * 回收站分页查询
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/recycle-bin/page")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO shortLinkRecycleBinPageReqDTO) {
        return recycleBinService.pageRecycleBinShortLink(shortLinkRecycleBinPageReqDTO);
    }

    /**
     * 恢复短链接
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        return shortLinkActualRemoteService.recoverRecycleBin(requestParam);
    }

    /**
     * 删除短链接
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/remove")
    public Result<Void> removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam) {
        return shortLinkActualRemoteService.removeRecycleBin(requestParam);
    }
}
