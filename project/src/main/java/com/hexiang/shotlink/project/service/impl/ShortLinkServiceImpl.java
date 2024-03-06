package com.hexiang.shotlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexiang.shotlink.project.common.convention.exception.ClientException;
import com.hexiang.shotlink.project.dao.entity.ShortLinkDO;
import com.hexiang.shotlink.project.dao.mapper.ShortLinkMapper;
import com.hexiang.shotlink.project.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.project.service.ShortLinkService;
import com.hexiang.shotlink.project.toolkit.HashUtil;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;


@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    @Autowired
    private RBloomFilter<String> shortlinkCreateRBloomFilter;
    @Override
    public ShortLinkCreateRespDTO createShortLink(ShotLinkCreateReqDTO requestParm) {
        //TODO 增加

        String shortLink = generateShortLinkHashString(requestParm);
//        ShortLinkDO shortLinkDO = BeanUtil.toBean(requestParm, ShortLinkDO.class);
//        shortLinkDO.setFullShortUrl(requestParm.getDomain() + '/' + shortLink);
//        shortLinkDO.setShortUrl(shortLink);
//        shortLinkDO.setEnableStatus(0);
//        shortLinkDO.setValidDataType(2);
        ShortLinkDO shortLinkDO = ShortLinkDO.builder().domain(requestParm.getDomain())
                .originUrl(requestParm.getOriginUrl())
                .gid(requestParm.getGid())
                .createType(requestParm.getCreateType())
                .describe(requestParm.getDescribe())
                .shortUrl(shortLink)
                .fullShortUrl(requestParm.getDomain() + '/' + shortLink)
                .enableStatus(0)
                .build();
        try {
            baseMapper.insert(shortLinkDO);
        } catch (DuplicateKeyException e) {
            throw new ClientException("短链接重复，请重试");
        }
        shortlinkCreateRBloomFilter.add(shortLinkDO.getFullShortUrl());
        return ShortLinkCreateRespDTO.builder()
                .gid(requestParm.getGid())
                .shortUrl(shortLinkDO.getShortUrl())
                .fullShortUrl(shortLinkDO.getFullShortUrl())
                .build();
    }

    private String generateShortLinkHashString(ShotLinkCreateReqDTO requestParm){
        String shortLink = HashUtil.hashToBase62(requestParm.getOriginUrl());
        String fullLink = requestParm.getDomain() + "/" + shortLink;
        for(int i = 0; i < 10 && shortlinkCreateRBloomFilter.contains(fullLink); i ++){
            shortLink = HashUtil.hashToBase62(requestParm.getOriginUrl()+System.currentTimeMillis());
            fullLink = requestParm.getDomain() + "/" + shortLink;
        }
        if(shortlinkCreateRBloomFilter.contains(fullLink))
            throw new ClientException("短链接重复，请稍后再试");
        return shortLink;
    }
}
