package com.hexiang.shotlink.admin.remote;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hexiang.shotlink.admin.common.convention.result.Result;
import com.hexiang.shotlink.admin.common.convention.result.Results;
import com.hexiang.shotlink.admin.remote.dto.req.*;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkGroupCountResqDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

public interface ShortLinkRemoteService {

    /**
     * 远程调用中台
     * @param requestParm
     * @return
     */
    default Result<ShortLinkCreateRespDTO> createShortLink(ShotLinkCreateReqDTO requestParm){
        String responseBodyString = HttpUtil.post("http://localhost:8001/api/short-link/v1/create", JSON.toJSONString(requestParm));
        return JSON.parseObject(responseBodyString, new TypeReference<Result<ShortLinkCreateRespDTO>>() {});
    }
    /**
     * 远程调用中台
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageSelect(ShortLinkPageReqDTO shortLinkPageReqDTO){
        HashMap<String ,Object> map = new HashMap<>();
        map.put("gid", shortLinkPageReqDTO.getGid());
        map.put("current", shortLinkPageReqDTO.getCurrent());
        map.put("size", shortLinkPageReqDTO.getSize());
        String responseParm = HttpUtil.get("http://localhost:8001/api/short-link/v1/page", map);
        return JSON.parseObject(responseParm, new TypeReference<>() {});
    }

    /**
     * 远程调用中台
     */
    default Result<List<ShortLinkGroupCountResqDTO>> groupCount(List<String> requestParm){
        HashMap<String, Object> map = new HashMap<>();
        map.put("requestParm", requestParm);
        String responseParm = HttpUtil.get("http://localhost:8001/api/short-link/v1/groupcount", map);
        return JSON.parseObject(responseParm, new TypeReference<>() {});
    }
    /**
     * 远程调用中台
     */
    default Result<Boolean> updateShortLink(ShortLinkUpdateReqDTO shortLinkUpdateReqDTO){
        String responseBodystring = HttpRequest.put("http://localhost:8001/api/short-link/v1/update").body(JSON.toJSONString(shortLinkUpdateReqDTO)).execute().body();
        // String responseBodyString = HttpUtil.("http://localhost:8001/api/short-link/v1/update", JSON.toJSONString(shortLinkUpdateReqDTO));
        return JSON.parseObject(responseBodystring, new TypeReference<Result<Boolean>>() {});
    }

    /**
     * 保存回收站
     */
    default Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam){
        String responseBodyString = HttpUtil.post("http://localhost:8001/api/short-link/v1/recycle-bin/save", JSON.toJSONString(requestParam));
        return JSON.parseObject(responseBodyString, new TypeReference<Result<Void>>() {});
    }

    /**
     * 分页查询回收站短链接
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        Map<String, Object> map = new HashMap<>();
        map.put("gidList", requestParam.getGidList());
        map.put("current", requestParam.getCurrent());
        map.put("size", requestParam.getSize());
        String responseBodyString = HttpUtil.get("http://localhost:8001/api/short-link/v1/recycle-bin/page", map);
        return JSON.parseObject(responseBodyString, new TypeReference<Result<IPage<ShortLinkPageRespDTO>>>() {
        });
    }

    /**
     * 恢复短链接
     */

    default Result<Void> recoverRecycleBin(RecycleBinRecoverReqDTO requestParam) {
        String responseJsonString = HttpUtil.post("http://localhost:8001/api/short-link/v1/recycle-bin/recover",
                JSON.toJSONString(requestParam)
        );
        return JSON.parseObject(responseJsonString, new TypeReference<Result<Void>>() {
        });
    }

    /**
     * 彻底删除短链接
     */
    default Result<Void> removeRecycleBin(RecycleBinRemoveReqDTO requestParam) {
        String responseJsonString = HttpUtil.post("http://localhost:8001/api/short-link/v1/recycle-bin/remove",
                JSON.toJSONString(requestParam)
        );
        return JSON.parseObject(responseJsonString, new TypeReference<Result<Void>>() {
        });
    }

}
