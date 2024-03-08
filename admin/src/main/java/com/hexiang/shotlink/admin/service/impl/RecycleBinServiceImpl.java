package com.hexiang.shotlink.admin.service.impl;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hexiang.shotlink.admin.common.biz.user.UserContext;
import com.hexiang.shotlink.admin.common.convention.result.Result;
import com.hexiang.shotlink.admin.dao.entity.GroupDO;
import com.hexiang.shotlink.admin.dao.mapper.GroupMapper;
import com.hexiang.shotlink.admin.remote.ShortLinkRemoteService;
import com.hexiang.shotlink.admin.remote.dto.req.RecycleBinRecoverReqDTO;
import com.hexiang.shotlink.admin.remote.dto.req.RecycleBinRemoveReqDTO;
import com.hexiang.shotlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.hexiang.shotlink.admin.service.RecycleBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecycleBinServiceImpl implements RecycleBinService {
    @Autowired
    private GroupMapper groupMapper;

    private ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };
    @Override
    public Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO shortLinkRecycleBinPageReqDTO) {
        LambdaQueryWrapper<GroupDO> eq = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);
        List<GroupDO> groupDOList = groupMapper.selectList(eq);
        List<String> gidList = groupDOList.stream().map(GroupDO::getGid).toList();
        shortLinkRecycleBinPageReqDTO.setGidList(gidList);
        return shortLinkRemoteService.pageShortLink(shortLinkRecycleBinPageReqDTO);
    }
}
