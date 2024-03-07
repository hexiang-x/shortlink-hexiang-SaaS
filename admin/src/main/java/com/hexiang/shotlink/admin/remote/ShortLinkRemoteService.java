package com.hexiang.shotlink.admin.remote;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hexiang.shotlink.admin.common.convention.result.Result;
import com.hexiang.shotlink.admin.common.convention.result.Results;
import com.hexiang.shotlink.admin.remote.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkGroupCountResqDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.hexiang.shotlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public interface ShortLinkRemoteService {

    default Result<ShortLinkCreateRespDTO> createShortLink(ShotLinkCreateReqDTO requestParm){
        String responseBodyString = HttpUtil.post("http://localhost:8001/api/short-link/v1/create", JSON.toJSONString(requestParm));
        return JSON.parseObject(responseBodyString, new TypeReference<Result<ShortLinkCreateRespDTO>>() {});
    }

    default Result<IPage<ShortLinkPageRespDTO>> pageSelect(ShortLinkPageReqDTO shortLinkPageReqDTO){
        HashMap<String ,Object> map = new HashMap<>();
        map.put("gid", shortLinkPageReqDTO.getGid());
        map.put("current", shortLinkPageReqDTO.getCurrent());
        map.put("size", shortLinkPageReqDTO.getSize());
        String responseParm = HttpUtil.get("http://localhost:8001/api/short-link/v1/page", map);
        return JSON.parseObject(responseParm, new TypeReference<>() {});
    }


    default Result<List<ShortLinkGroupCountResqDTO>> groupCount(List<String> requestParm){
        HashMap<String, Object> map = new HashMap<>();
        map.put("requestParm", requestParm);
        String responseParm = HttpUtil.get("http://localhost:8001/api/short-link/v1/groupcount", map);
        return JSON.parseObject(responseParm, new TypeReference<>() {});
    }
}
