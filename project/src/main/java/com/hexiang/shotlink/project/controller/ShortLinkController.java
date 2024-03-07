package com.hexiang.shotlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hexiang.shotlink.project.common.convention.result.Result;
import com.hexiang.shotlink.project.common.convention.result.Results;
import com.hexiang.shotlink.project.dto.req.ShortLinkPageReqDTO;
import com.hexiang.shotlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.hexiang.shotlink.project.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkGroupCountResqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkPageRespDTO;
import com.hexiang.shotlink.project.service.ShortLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ShortLinkController {
    @Autowired
    private ShortLinkService shortLinkService;

    @GetMapping("/{shortlink}")
    public void restoreUrl(@PathVariable String shortlink, HttpServletRequest request, HttpServletResponse response) throws IOException {
        shortLinkService.restoreUrl(shortlink, request, response);
    }

    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShotLinkCreateReqDTO requestParm){
        ShortLinkCreateRespDTO result = shortLinkService.createShortLink(requestParm);
        return Results.success(result);
    }

    @GetMapping("/api/short-link/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageSelect(ShortLinkPageReqDTO shortLinkPageReqDTO){
        return Results.success(shortLinkService.pageSelect(shortLinkPageReqDTO));
    }

    @GetMapping("/api/short-link/v1/groupcount")
    public Result<List<ShortLinkGroupCountResqDTO>> groupCount(@RequestParam List<String> requestParm){
        return Results.success(shortLinkService.groupCount(requestParm));
    }

    @PutMapping("/api/short-link/v1/update")
    public Result<Boolean> updateShortLink(@RequestBody ShortLinkUpdateReqDTO shortLinkUpdateReqDTO){
        return Results.success(shortLinkService.updateShortLink(shortLinkUpdateReqDTO));
    }
}
