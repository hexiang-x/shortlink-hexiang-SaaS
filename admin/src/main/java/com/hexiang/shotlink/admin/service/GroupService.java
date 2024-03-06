package com.hexiang.shotlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hexiang.shotlink.admin.dao.entity.GroupDO;
import com.hexiang.shotlink.admin.dto.req.GroupSortReqDTO;
import com.hexiang.shotlink.admin.dto.req.GroupUpdateReqDTO;
import com.hexiang.shotlink.admin.dto.resp.GroupRespDTO;

import java.util.List;

/**
 * 短链接分组业务层
 */
public interface GroupService extends IService<GroupDO> {
    /**
     * 按名新增短链接
     */
    public void saveShortLink(String name);

    /**
     * 查询所有分组
     * @return 分组列表
     */
    public List<GroupRespDTO> listGroup();

    /**
     * 更新一条分组
     * @param requestParm 分组信息
     * @return 更新是否成功
     */
    Boolean updateGroup(GroupUpdateReqDTO requestParm);

    /**
     * 删除一条分组
     * @param gid 分组gid
     * @return 删除是否成功
     */
    Boolean deleteGroup(String gid);

    void sortGroup(List<GroupSortReqDTO> requestParm);
}
