package com.hexiang.shotlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexiang.shotlink.project.dao.entity.ShortLinkDO;
import com.hexiang.shotlink.project.dao.mapper.ShortLinkMapper;
import com.hexiang.shotlink.project.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.project.service.ShortLinkService;
import com.hexiang.shotlink.project.toolkit.HashUtil;
import org.springframework.stereotype.Service;


@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShotLinkCreateReqDTO requestParm) {
        //TODO 增加
        String shortLink = generateShortLinkHashString(requestParm);
        ShortLinkDO shortLinkDO = BeanUtil.toBean(requestParm, ShortLinkDO.class);
        shortLinkDO.setFullShortUrl(requestParm.getDomain() + '/' + shortLink);
        shortLinkDO.setShortUrl(shortLink);
        shortLinkDO.setEnableStatus(0);
        shortLinkDO.setValidDataType(2);
        baseMapper.insert(shortLinkDO);
        return ShortLinkCreateRespDTO.builder()
                .gid(requestParm.getGid())
                .shortUrl(shortLinkDO.getShortUrl())
                .fullShortUrl(shortLinkDO.getFullShortUrl())
                .build();
    }

    private String generateShortLinkHashString(ShotLinkCreateReqDTO requestParm){
        return HashUtil.hashToBase62(requestParm.getOriginUrl());
    }
}
