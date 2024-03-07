package com.hexiang.shotlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexiang.shotlink.project.common.convention.exception.ClientException;
import com.hexiang.shotlink.project.dao.entity.ShortLinkDO;
import com.hexiang.shotlink.project.dao.mapper.ShortLinkMapper;
import com.hexiang.shotlink.project.dto.req.ShortLinkPageReqDTO;
import com.hexiang.shotlink.project.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkGroupCountResqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkPageRespDTO;
import com.hexiang.shotlink.project.service.ShortLinkService;
import com.hexiang.shotlink.project.toolkit.HashUtil;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    @Autowired
    private RBloomFilter<String> shortlinkCreateRBloomFilter;
    @Override
    public ShortLinkCreateRespDTO createShortLink(ShotLinkCreateReqDTO requestParm) {

        String shortLink = generateShortLinkHashString(requestParm);
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

    @Override
    public IPage<ShortLinkPageRespDTO> pageSelect(ShortLinkPageReqDTO shortLinkPageReqDTO) {
        LambdaQueryWrapper<ShortLinkDO> shortLinkDOLambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, shortLinkPageReqDTO.getGid())
                .eq(ShortLinkDO::getDelFlag, 0);
        IPage<ShortLinkDO> results = baseMapper.selectPage(shortLinkPageReqDTO, shortLinkDOLambdaQueryWrapper);
        return results.convert(each -> BeanUtil.toBean(each, ShortLinkPageRespDTO.class));
    }

    @Override
    public List<ShortLinkGroupCountResqDTO> groupCount(List<String> requestParm) {
        QueryWrapper<ShortLinkDO> objectQueryWrapper = Wrappers.query(new ShortLinkDO()).select("gid as gid", "count(0) as count")
                .in("gid", requestParm)
                .eq("enable_status", 0)
                .groupBy("gid");
        List<Map<String, Object>> shortLinkDOList = baseMapper.selectMaps(objectQueryWrapper);
        return BeanUtil.copyToList(shortLinkDOList, ShortLinkGroupCountResqDTO.class);
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
