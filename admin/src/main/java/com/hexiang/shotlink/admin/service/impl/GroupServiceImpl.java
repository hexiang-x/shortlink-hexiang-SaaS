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
import com.hexiang.shotlink.admin.dao.entity.GroupDO;
import com.hexiang.shotlink.admin.dao.mapper.GroupMapper;
import com.hexiang.shotlink.admin.dto.req.GroupSortReqDTO;
import com.hexiang.shotlink.admin.dto.req.GroupUpdateReqDTO;
import com.hexiang.shotlink.admin.dto.resp.GroupRespDTO;
import com.hexiang.shotlink.admin.service.GroupService;
import com.hexiang.shotlink.admin.service.UserService;
import com.hexiang.shotlink.admin.toolkit.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    @Override
    public void saveShortLink(String name) {
        String gid = null;
        do{
            gid = RandomGenerator.generateRandom();
        }while (hasGid(gid));
        GroupDO groupDo = GroupDO.builder()
                .gid(gid)
                .sortOrder(0)
                .username(UserContext.getUsername())
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
        return BeanUtil.copyToList(groupDOS, GroupRespDTO.class);
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
        if(!hasGid(gid)){
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

    private boolean hasGid(String gid){
        LambdaQueryWrapper<GroupDO> eq = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid);
        //        .eq(GroupDO::getUsername, UserContext.getUsername());
        GroupDO groupDO = baseMapper.selectOne(eq);
        return groupDO  != null;
    }
}
