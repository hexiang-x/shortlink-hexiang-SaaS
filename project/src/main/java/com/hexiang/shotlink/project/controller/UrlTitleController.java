package com.hexiang.shotlink.project.controller;

import com.hexiang.shotlink.project.common.convention.result.Result;
import com.hexiang.shotlink.project.common.convention.result.Results;
import com.hexiang.shotlink.project.service.UrlTitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UrlTitleController {

    private final UrlTitleService urlTitleService;

    /**
     * 根据 URL 获取对应网站的标题
     */
    @GetMapping("/api/short-link/v1/title")
    public Result<String> getTitleByUrl(@RequestParam("url") String url) throws IOException {
        return Results.success(urlTitleService.getUrlTitle(url));
    }
}
