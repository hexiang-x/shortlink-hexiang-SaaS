package com.hexiang.shotlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hexiang.shotlink.project.common.convention.exception.ClientException;
import com.hexiang.shotlink.project.dao.entity.ShortLinkDO;
import com.hexiang.shotlink.project.dao.mapper.ShortLinkMapper;
import com.hexiang.shotlink.project.dto.req.RecycleBinRecoverReqDTO;
import com.hexiang.shotlink.project.dto.req.RecycleBinRemoveReqDTO;
import com.hexiang.shotlink.project.dto.req.RecycleBinSaveReqDTO;
import com.hexiang.shotlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkPageRespDTO;
import com.hexiang.shotlink.project.service.RecycleBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.hexiang.shotlink.project.common.constant.RedisKeyConstant.GOTO_IS_NULL_SHORT_LINK_KEY;
import static com.hexiang.shotlink.project.common.constant.RedisKeyConstant.GOTO_SHORT_LINK_KEY;

@Service
public class RecycleBinServiceImpl implements RecycleBinService {
    @Autowired
    private ShortLinkMapper shortLinkMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveRecycleBin(RecycleBinSaveReqDTO requestParm) {
        LambdaQueryWrapper<ShortLinkDO> shortLinkDOLambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParm.getGid())
                .eq(ShortLinkDO::getFullShortUrl, requestParm.getFullShortUrl())
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getDelFlag, 0);
        ShortLinkDO updatesl = ShortLinkDO.builder()
                .enableStatus(1)
                .build();
        shortLinkMapper.update(updatesl, shortLinkDOLambdaQueryWrapper);
        stringRedisTemplate.delete(String.format(GOTO_SHORT_LINK_KEY, requestParm.getFullShortUrl()));
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> shortLinkDOLambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .in(ShortLinkDO::getGid, requestParam.getGidList())
                .eq(ShortLinkDO::getEnableStatus, 1)
                .eq(ShortLinkDO::getDelFlag, 0)
                .orderByDesc(ShortLinkDO::getUpdateTime);
        IPage<ShortLinkDO> result = shortLinkMapper.selectPage(requestParam, shortLinkDOLambdaQueryWrapper);

        return result.convert(each -> {
            ShortLinkPageRespDTO bean = BeanUtil.toBean(each, ShortLinkPageRespDTO.class);
            bean.setDomain("http://" + bean.getFullShortUrl());
            return bean;
        });
    }

    @Override
    public void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> shortLinkDOLambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getEnableStatus, 1)
                .eq(ShortLinkDO::getDelFlag, 0);
        ShortLinkDO updatesl = ShortLinkDO.builder()
                        .enableStatus(0)
                                .build();
        int reflact = shortLinkMapper.update(updatesl, shortLinkDOLambdaQueryWrapper);
        if(reflact > 0)
            stringRedisTemplate.delete(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
    }

    @Override
    public void removeRecycleBin(RecycleBinRemoveReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> shortLinkDOLambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getEnableStatus, 1)
                .eq(ShortLinkDO::getDelFlag, 0);
        ShortLinkDO deletesl = ShortLinkDO.builder().build();
        deletesl.setDelFlag(1);
        int reflact = shortLinkMapper.update(deletesl, shortLinkDOLambdaQueryWrapper);
    }
}
