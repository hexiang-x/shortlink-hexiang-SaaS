package com.hexiang.shotlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexiang.shotlink.admin.common.biz.user.UserContext;
import com.hexiang.shotlink.admin.common.biz.user.UserInfoDTO;
import com.hexiang.shotlink.admin.common.convention.exception.ClientException;
import com.hexiang.shotlink.admin.common.convention.exception.ServiceException;
import com.hexiang.shotlink.admin.common.convention.result.Result;
import com.hexiang.shotlink.admin.controller.ShortLinkController;
import com.hexiang.shotlink.admin.dao.entity.GroupDO;
import com.hexiang.shotlink.admin.dao.mapper.GroupMapper;
import com.hexiang.shotlink.admin.dto.req.GroupSortReqDTO;
import com.hexiang.shotlink.admin.dto.req.GroupUpdateReqDTO;
import com.hexiang.shotlink.admin.dto.resp.GroupRespDTO;
import com.hexiang.shotlink.admin.remote.ShortLinkRemoteService;
import com.hexiang.shotlink.admin.remote.dto.req.ShotLinkCreateReqDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkGroupCountResqDTO;
import com.hexiang.shotlink.admin.service.GroupService;
import com.hexiang.shotlink.admin.service.UserService;
import com.hexiang.shotlink.admin.toolkit.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};
    @Override
    public void saveShortLink(String name) {
        saveShortLink(UserContext.getUsername(), name);
    }

    public void saveShortLink(String username, String name) {
        String gid = null;
        do{
            gid = RandomGenerator.generateRandom();
        }while (hasGid(username, gid));
        GroupDO groupDo = GroupDO.builder()
                .gid(gid)
                .sortOrder(0)
                .username(username)
                .name(name)
                .build();
        baseMapper.insert(groupDo);
    }

    @Override
    public List<GroupRespDTO> listGroup() {
        LambdaQueryWrapper<GroupDO> hexiang = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUsername);
        List<GroupDO> groupDOS = baseMapper.selectList(hexiang);
        List<GroupRespDTO> result = BeanUtil.copyToList(groupDOS, GroupRespDTO.class);
        List<ShortLinkGroupCountResqDTO> data = shortLinkRemoteService.groupCount(groupDOS.stream().map(GroupDO::getGid).toList()).getData();
        Map<String, Integer> counts = data.stream().collect(Collectors.toMap(ShortLinkGroupCountResqDTO::getGid, ShortLinkGroupCountResqDTO::getCount));

        return result.stream().peek(each -> each.setCount(counts.get(each.getGid()))).toList();
    }

    @Override
    public Boolean updateGroup(GroupUpdateReqDTO requestParm) {
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getGid, requestParm.getGid());
        GroupDO groupDO = new GroupDO();
        groupDO.setName(requestParm.getName());
        int result = baseMapper.update(groupDO, updateWrapper);
        return result > 0;
    }

    @Override
    public Boolean deleteGroup(String gid) {
        if(!hasGid(UserContext.getUsername(), gid)){
            throw new ClientException("分组不存在");
        }
        LambdaQueryWrapper<GroupDO> eq = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid);
        int result = baseMapper.delete(eq);
        return result > 0;
    }

    @Override
    public void sortGroup(List<GroupSortReqDTO> requestParm) {
        requestParm.forEach(groupSortReqDTO -> {
            GroupDO groupDO = GroupDO.builder()
                    .gid(groupSortReqDTO.getGid())
                    .sortOrder(groupSortReqDTO.getSort())
                    .build();
            LambdaUpdateWrapper<GroupDO> eq = Wrappers.lambdaUpdate(GroupDO.class)
                    .eq(GroupDO::getGid, groupDO.getGid());
            baseMapper.update(groupDO, eq);
        });
    }

    private boolean hasGid(String username, String gid){
        LambdaQueryWrapper<GroupDO> eq = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUsername, Optional.ofNullable(username).orElse(UserContext.getUsername()));
        GroupDO groupDO = baseMapper.selectOne(eq);
        return groupDO  != null;
    }
}
