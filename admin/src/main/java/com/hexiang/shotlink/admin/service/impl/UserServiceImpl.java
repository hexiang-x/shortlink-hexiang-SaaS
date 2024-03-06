package com.hexiang.shotlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hexiang.shotlink.admin.common.constant.RedisCacheConstant;
import com.hexiang.shotlink.admin.common.convention.exception.ClientException;
import com.hexiang.shotlink.admin.common.convention.exception.ServiceException;
import com.hexiang.shotlink.admin.common.enmus.UserErrorCodeEnum;
import com.hexiang.shotlink.admin.dao.entity.UserDO;
import com.hexiang.shotlink.admin.dao.mapper.UserMapper;
import com.hexiang.shotlink.admin.dto.req.UserLoginReqDTO;
import com.hexiang.shotlink.admin.dto.req.UserRegisterReqDTO;
import com.hexiang.shotlink.admin.dto.req.UserUpdateReqDTO;
import com.hexiang.shotlink.admin.dto.resp.UserLoginTokenRespDTO;
import com.hexiang.shotlink.admin.dto.resp.UserRespDTO;
import com.hexiang.shotlink.admin.service.UserService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.hexiang.shotlink.admin.common.constant.RedisCacheConstant.USER_LOGIN_KEY;
import static com.hexiang.shotlink.admin.common.enmus.UserErrorCodeEnum.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    @Autowired
    private RBloomFilter<String> cachePenetrationBloomFilter;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public UserRespDTO getUserByUserName(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if(userDO == null) {
            throw new ServiceException(UserErrorCodeEnum.USER_NULL);
        }
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean hasUsername(String username) {
//        LambdaQueryWrapper<UserDO> eq = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username);
//        UserDO userDO = baseMapper.selectOne(eq);
//        return userDO != null;
        return cachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterReqDTO requestParm) {
        if(hasUsername(requestParm.getUsername())){
            throw new ClientException(USER_NAME_EXIST);
        }
        RLock lock = redissonClient.getLock(RedisCacheConstant.LOCK_USER_REGISTER_KEY+requestParm.getUsername());;
        try {
            if(lock.tryLock()){
                try{
                    int inserted = baseMapper.insert(BeanUtil.toBean(requestParm, UserDO.class));
                    if(inserted < 1){
                        throw new ServiceException(USER_SAVE_ERROR);
                    }
                }catch (DuplicateKeyException ex){
                    throw new ClientException(USER_EXIST);
                }
                cachePenetrationBloomFilter.add(requestParm.getUsername());
                return;
            }
            throw new ClientException(USER_NAME_EXIST);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public Boolean updateUser(UserUpdateReqDTO requestParm) {
        // TODO 验证当前用户是否登录；
        LambdaUpdateWrapper<UserDO> eq = Wrappers.lambdaUpdate(UserDO.class).eq(UserDO::getUsername, requestParm.getUsername())
                .eq(UserDO::getPassword, requestParm.getPassword());
        int updateStatus = baseMapper.update(BeanUtil.toBean(requestParm, UserDO.class), eq);
        return updateStatus > 0;
    }

    @Override
    public UserLoginTokenRespDTO loginUser(UserLoginReqDTO requestParm) {
        LambdaQueryWrapper<UserDO> eq = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, requestParm.getUsername())
                .eq(UserDO::getPassword, requestParm.getPassword());
        UserDO userDO = baseMapper.selectOne(eq);
        if(userDO == null)
            throw new ClientException(USER_NULL);
        Map<Object, Object> haslogin = stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY+requestParm.getUsername());
        if(CollUtil.isNotEmpty(haslogin)){
            stringRedisTemplate.expire(USER_LOGIN_KEY+requestParm.getUsername(), 30L, TimeUnit.MINUTES);
            String toker = haslogin.keySet().stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElseThrow(() -> new ClientException("用户登录错误"));
            return new UserLoginTokenRespDTO(toker);
        }

        /**
         * Hash
         * key: USER_LOGIN_KEY+username
         * Value:
         *      key: toker用户标识（uuid）
         *      value: 用户信息（JSONString）
         */
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put(USER_LOGIN_KEY+requestParm.getUsername(), uuid, JSON.toJSONString(userDO));
        stringRedisTemplate.expire(USER_LOGIN_KEY+requestParm.getUsername(), 30L, TimeUnit.MINUTES);

        return new UserLoginTokenRespDTO(uuid);
    }

    @Override
    public Boolean checkLoginUser(String username, String token) {
        return stringRedisTemplate.opsForHash().get(USER_LOGIN_KEY+username, token) != null;
    }

    @Override
    public void logoutUser(String username, String token) {
        if(checkLoginUser(username,token)){
            stringRedisTemplate.delete(USER_LOGIN_KEY+username);
            return;
        }
        throw new ClientException("用户Token不存在或未登录");
    }
}
