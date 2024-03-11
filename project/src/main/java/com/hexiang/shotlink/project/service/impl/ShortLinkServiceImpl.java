package com.hexiang.shotlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexiang.shotlink.project.common.convention.exception.ClientException;
import com.hexiang.shotlink.project.common.convention.exception.ServiceException;
import com.hexiang.shotlink.project.common.enmus.VailDateTypeEnum;
import com.hexiang.shotlink.project.dao.entity.ShortLinkDO;
import com.hexiang.shotlink.project.dao.entity.ShortLinkGotoDO;
import com.hexiang.shotlink.project.dao.mapper.ShortLinkGotoMapper;
import com.hexiang.shotlink.project.dao.mapper.ShortLinkMapper;
import com.hexiang.shotlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.hexiang.shotlink.project.dto.req.ShortLinkPageReqDTO;
import com.hexiang.shotlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.hexiang.shotlink.project.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkGroupCountResqDTO;
import com.hexiang.shotlink.project.dto.resp.ShortLinkPageRespDTO;
import com.hexiang.shotlink.project.mq.producer.ShortLinkStatsSaveProducer;
import com.hexiang.shotlink.project.service.ShortLinkService;
import com.hexiang.shotlink.project.toolkit.HashUtil;
import com.hexiang.shotlink.project.toolkit.LinkUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.hexiang.shotlink.project.common.constant.RedisKeyConstant.*;


@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    @Autowired
    private RBloomFilter<String> shortlinkCreateRBloomFilter;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ShortLinkGotoMapper shortLinkGotoMapper;
    @Autowired
    private ShortLinkStatsSaveProducer shortLinkStatsSaveProducer;



    @Override
    public ShortLinkCreateRespDTO createShortLink(ShotLinkCreateReqDTO requestParm) {

        String shortLink = generateShortLinkHashString(requestParm);
        ShortLinkDO shortLinkDO = ShortLinkDO.builder().domain(requestParm.getDomain())
                .originUrl(requestParm.getOriginUrl())
                .gid(requestParm.getGid())
                .validDataType(requestParm.getValidDataType())
                .validData(requestParm.getValidData())
                .createType(requestParm.getCreateType())
                .describe(requestParm.getDescribe())
                .shortUrl(shortLink)
                .fullShortUrl(requestParm.getDomain() + '/' + shortLink)
                .enableStatus(0)
                .build();
        shortLinkDO.setValidData(requestParm.getValidData());
        shortLinkDO.setValidDataType(requestParm.getValidDataType());

        ShortLinkGotoDO shortLinkGotoDO = ShortLinkGotoDO.builder()
                .fullShortLink(shortLinkDO.getFullShortUrl())
                .gid(shortLinkDO.getGid())
                .build();
        try {
            baseMapper.insert(shortLinkDO);
            shortLinkGotoMapper.insert(shortLinkGotoDO);
        } catch (DuplicateKeyException e) {
            throw new ClientException("短链接重复，请重试");
        }
        stringRedisTemplate.opsForValue().set(
                String.format(GOTO_SHORT_LINK_KEY, shortLinkGotoDO.getFullShortLink()),
                shortLinkDO.getOriginUrl(),
                LinkUtil.getLinkCacheValidTime(requestParm.getValidData()),
                TimeUnit.MILLISECONDS
        );

        shortlinkCreateRBloomFilter.add(shortLinkDO.getFullShortUrl());
        return ShortLinkCreateRespDTO.builder()
                .gid(requestParm.getGid())
                .shortUrl(shortLinkDO.getShortUrl())
                .originUrl(shortLinkDO.getOriginUrl())
                .fullShortUrl(shortLinkDO.getFullShortUrl())
                .build();
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageSelect(ShortLinkPageReqDTO shortLinkPageReqDTO) {
        LambdaQueryWrapper<ShortLinkDO> shortLinkDOLambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, shortLinkPageReqDTO.getGid())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0)
                .and(wrapper -> wrapper.and(
                        item -> item
                                .eq(ShortLinkDO::getValidDataType, 1)
                                .gt(ShortLinkDO::getValidData, new Date()))
                        .or().eq(ShortLinkDO::getValidDataType, 0)
                        .or().isNull(ShortLinkDO::getValidDataType)
                );

        IPage<ShortLinkDO> results = baseMapper.selectPage(shortLinkPageReqDTO, shortLinkDOLambdaQueryWrapper);
        return results.convert(each -> BeanUtil.toBean(each, ShortLinkPageRespDTO.class));
    }

    @Override
    public List<ShortLinkGroupCountResqDTO> groupCount(List<String> requestParm) {
        QueryWrapper<ShortLinkDO> objectQueryWrapper = Wrappers.query(new ShortLinkDO()).select("gid as gid", "count(0) as shortLinkCount")
                .in("gid", requestParm)
                .eq("del_flag", 0)
                .eq("enable_status", 0)
                .groupBy("gid");
        List<Map<String, Object>> shortLinkDOList = baseMapper.selectMaps(objectQueryWrapper);
        return BeanUtil.copyToList(shortLinkDOList, ShortLinkGroupCountResqDTO.class);
    }

    @Override
    public Boolean updateShortLink(ShortLinkUpdateReqDTO shortLinkUpdateReqDTO) {
        LambdaQueryWrapper<ShortLinkDO> hasQuery = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, shortLinkUpdateReqDTO.getOriginalGid())
                .eq(ShortLinkDO::getFullShortUrl, shortLinkUpdateReqDTO.getFullShortUrl())
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getDelFlag, 0);
        ShortLinkDO hasShortLink = baseMapper.selectOne(hasQuery);
        if(hasShortLink == null){
            throw new ClientException("短链接不存在");
        }
        if(Objects.equals(hasShortLink.getGid(), shortLinkUpdateReqDTO.getGid())){
            LambdaUpdateWrapper<ShortLinkDO> set = Wrappers.lambdaUpdate(ShortLinkDO.class)
                    .eq(ShortLinkDO::getGid, shortLinkUpdateReqDTO.getGid())
                    .eq(ShortLinkDO::getFullShortUrl, shortLinkUpdateReqDTO.getFullShortUrl())
                    .eq(ShortLinkDO::getEnableStatus, 0)
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .set(Objects.equals(shortLinkUpdateReqDTO.getValidDataType(), VailDateTypeEnum.PERMANENT.getType()), ShortLinkDO::getValidData, null);
            ShortLinkDO updateShortLink = ShortLinkDO.builder()
                    .domain(hasShortLink.getDomain())
                    .shortUrl(hasShortLink.getShortUrl())
                    .favicon(hasShortLink.getFavicon())
                    .createType(hasShortLink.getCreateType())
                    .gid(shortLinkUpdateReqDTO.getGid())
                    .originUrl(shortLinkUpdateReqDTO.getOriginUrl())
                    .describe(shortLinkUpdateReqDTO.getDescribe())
                    .validDataType(shortLinkUpdateReqDTO.getValidDataType())
                    .validData(shortLinkUpdateReqDTO.getValidData())
                    .build();
            int result = baseMapper.update(updateShortLink, set);
            return  result > 0;
        } else {
            RReadWriteLock readWriteLock =  redissonClient.getReadWriteLock(String.format(LOCK_GID_UPDATE_KEY, hasShortLink.getGid()));
            RLock rLook = readWriteLock.writeLock();
            if(!rLook.tryLock()){
                throw new ServiceException("短链接正在被使用");
            }
            try {
                LambdaUpdateWrapper<ShortLinkDO> set = Wrappers.lambdaUpdate(ShortLinkDO.class)
                        .eq(ShortLinkDO::getGid, shortLinkUpdateReqDTO.getOriginalGid())
                        .eq(ShortLinkDO::getFullShortUrl, shortLinkUpdateReqDTO.getFullShortUrl())
                        .eq(ShortLinkDO::getEnableStatus, 0)
                        .eq(ShortLinkDO::getDelFlag, 0);
                // hasShortLink.setDelFlag(1);
                // hasShortLink.setDel
                ShortLinkDO delShortLink = ShortLinkDO.builder().build();
                delShortLink.setDelFlag(1);
                System.out.println("-------------:" + baseMapper.update(delShortLink, set));
                ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                        .domain(shortLinkUpdateReqDTO.getDomain())
                        .shortUrl(shortLinkUpdateReqDTO.getShortUrl())
                        .fullShortUrl(shortLinkUpdateReqDTO.getFullShortUrl())
                        .originUrl(shortLinkUpdateReqDTO.getOriginUrl())
                        .gid(shortLinkUpdateReqDTO.getGid())
                        .createType(hasShortLink.getCreateType())
                        .validDataType(shortLinkUpdateReqDTO.getValidDataType())
                        .validData(shortLinkUpdateReqDTO.getValidData())
                        .describe(shortLinkUpdateReqDTO.getDescribe())
                        .enableStatus(hasShortLink.getEnableStatus())
                        .build();
                int result = baseMapper.insert(shortLinkDO);
                return result > 0;
            }finally {
                rLook.unlock();
            }


        }
    }

    @Override
    public void restoreUrl(String shortlink, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String serverName = request.getServerName();
        String serverPort = Optional.of(request.getServerPort())
                .filter(each -> !Objects.equals(each, 80))
                .map(String::valueOf)
                .map(each -> ":" + each)
                .orElse("");
        String fullShortUrl = serverName + serverPort + "/" + shortlink;

        String originalUrl = stringRedisTemplate.opsForValue().get(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl));
        if(StrUtil.isNotBlank(originalUrl)){
            ShortLinkStatsRecordDTO shortLinkStatsRecordDTO = buildLinkStatsRecordAndSetUser(fullShortUrl, request, response);
            shortLinkStats(fullShortUrl, null, shortLinkStatsRecordDTO);
            response.sendRedirect(originalUrl);
            return;
        }

        boolean isContain = shortlinkCreateRBloomFilter.contains(fullShortUrl);
        if(!isContain){
            response.sendRedirect("/page/notfound");
            return;
        }

        String originUrl = stringRedisTemplate.opsForValue().get(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl));
        if(StrUtil.isNotBlank(originUrl)){
            response.sendRedirect("/page/notfound");
            return;
        }
        RLock rLock = redissonClient.getLock(String.format(LOCK_GOTO_SHORT_LINK_KEY, fullShortUrl));
        rLock.lock();
        try{
            // 双重锁 判定
            originalUrl = stringRedisTemplate.opsForValue().get(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl));
            if(StrUtil.isNotBlank(originalUrl)){
                ShortLinkStatsRecordDTO shortLinkStatsRecordDTO = buildLinkStatsRecordAndSetUser(fullShortUrl, request, response);
                shortLinkStats(fullShortUrl, null, shortLinkStatsRecordDTO);
                response.sendRedirect(originalUrl);
                return;
            }
            LambdaQueryWrapper<ShortLinkGotoDO> gotoDOLambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                    .eq(ShortLinkGotoDO::getFullShortLink, fullShortUrl);
            ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(gotoDOLambdaQueryWrapper);
            //防止缓存穿透
            if(shortLinkGotoDO == null){
                stringRedisTemplate.opsForValue().set(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl), "-", 30L, TimeUnit.MINUTES);
                response.sendRedirect("/page/notfound");
                return;
            }
            LambdaQueryWrapper<ShortLinkDO> shortLinkDOLambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getGid, shortLinkGotoDO.getGid())
                    .eq(ShortLinkDO::getFullShortUrl, shortLinkGotoDO.getFullShortLink())
                    .eq(ShortLinkDO::getEnableStatus, 0)
                    .eq(ShortLinkDO::getDelFlag, 0);
            ShortLinkDO shortLinkDO = baseMapper.selectOne(shortLinkDOLambdaQueryWrapper);
            // 防止缓存穿透
            if(shortLinkDO == null || (shortLinkDO.getValidData() != null && shortLinkDO.getValidData().before(new Date()))){
                stringRedisTemplate.opsForValue().set(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, fullShortUrl), "-", 30L, TimeUnit.MINUTES);
                response.sendRedirect("/page/notfound");
                return;
            }
            stringRedisTemplate.opsForValue().set(
                    String.format(GOTO_SHORT_LINK_KEY, fullShortUrl),
                    shortLinkDO.getOriginUrl(),
                    LinkUtil.getLinkCacheValidTime(shortLinkDO.getValidData()),
                    TimeUnit.MILLISECONDS
            );
            ShortLinkStatsRecordDTO shortLinkStatsRecordDTO = buildLinkStatsRecordAndSetUser(fullShortUrl, request, response);
            shortLinkStats(fullShortUrl, null, shortLinkStatsRecordDTO);
            response.sendRedirect(shortLinkDO.getOriginUrl());
        }finally {
            rLock.unlock();
        }
    }
    private ShortLinkStatsRecordDTO buildLinkStatsRecordAndSetUser(String fullShortUrl, ServletRequest request, ServletResponse response) {
        AtomicBoolean uvFirstFlag = new AtomicBoolean();
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        AtomicReference<String> uv = new AtomicReference<>();
        Runnable addResponseCookieTask = () -> {
            uv.set(UUID.fastUUID().toString());
            Cookie uvCookie = new Cookie("uv", uv.get());
            uvCookie.setMaxAge(60 * 60 * 24 * 30);
            uvCookie.setPath(StrUtil.sub(fullShortUrl, fullShortUrl.indexOf("/"), fullShortUrl.length()));
            ((HttpServletResponse) response).addCookie(uvCookie);
            uvFirstFlag.set(Boolean.TRUE);
            stringRedisTemplate.opsForSet().add(SHORT_LINK_STATS_UV_KEY + fullShortUrl, uv.get());
        };
        if (ArrayUtil.isNotEmpty(cookies)) {
            Arrays.stream(cookies)
                    .filter(each -> Objects.equals(each.getName(), "uv"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .ifPresentOrElse(each -> {
                        uv.set(each);
                        Long uvAdded = stringRedisTemplate.opsForSet().add(SHORT_LINK_STATS_UV_KEY + fullShortUrl, each);
                        uvFirstFlag.set(uvAdded != null && uvAdded > 0L);
                    }, addResponseCookieTask);
        } else {
            addResponseCookieTask.run();
        }
        String remoteAddr = LinkUtil.getActualIp(((HttpServletRequest) request));
        String os = LinkUtil.getOs(((HttpServletRequest) request));
        String browser = LinkUtil.getBrowser(((HttpServletRequest) request));
        String device = LinkUtil.getDevice(((HttpServletRequest) request));
        String network = LinkUtil.getNetwork(((HttpServletRequest) request));
        Long uipAdded = stringRedisTemplate.opsForSet().add(SHORT_LINK_STATS_UIP_KEY + fullShortUrl, remoteAddr);
        boolean uipFirstFlag = uipAdded != null && uipAdded > 0L;
        return ShortLinkStatsRecordDTO.builder()
                .fullShortUrl(fullShortUrl)
                .uv(uv.get())
                .uvFirstFlag(uvFirstFlag.get())
                .uipFirstFlag(uipFirstFlag)
                .remoteAddr(remoteAddr)
                .os(os)
                .browser(browser)
                .device(device)
                .network(network)
                .build();
    }

    @Override
    public void shortLinkStats(String fullShortUrl, String gid, ShortLinkStatsRecordDTO statsRecord) {
        Map<String, String> producerMap = new HashMap<>();
        producerMap.put("fullShortUrl", fullShortUrl);
        producerMap.put("gid", gid);
        producerMap.put("statsRecord", JSON.toJSONString(statsRecord));
        // TODO 增加消息队列
        shortLinkStatsSaveProducer.send(producerMap);
    }

    private String generateShortLinkHashString(ShotLinkCreateReqDTO requestParm){
        String shortLink = HashUtil.hashToBase62(requestParm.getOriginUrl());
        String fullLink = requestParm.getDomain() + "/" + shortLink;
        for(int i = 0; i < 10 && shortlinkCreateRBloomFilter.contains(fullLink); i ++){
            shortLink = HashUtil.hashToBase62(requestParm.getOriginUrl()+ UUID.randomUUID());
            fullLink = requestParm.getDomain() + "/" + shortLink;
        }
        if(shortlinkCreateRBloomFilter.contains(fullLink))
            throw new ClientException("短链接重复，请稍后再试");
        return shortLink;
    }
}
