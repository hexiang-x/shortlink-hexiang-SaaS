package com.hexiang.shotlink.project.controller;

import com.hexiang.shotlink.project.common.convention.result.Result;
import com.hexiang.shotlink.project.common.convention.result.Results;
import com.hexiang.shotlink.project.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.project.service.ShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortLinkController {
    @Autowired
    private ShortLinkService shortLinkService;
    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShotLinkCreateReqDTO requestParm){
        ShortLinkCreateRespDTO result = shortLinkService.createShortLink(requestParm);
        return Results.success(result);
    }
}
