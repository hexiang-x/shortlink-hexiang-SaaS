package com.hexiang.shotlink.admin.controller;

import com.hexiang.shotlink.admin.common.convention.result.Result;
import com.hexiang.shotlink.admin.common.convention.result.Results;
import com.hexiang.shotlink.admin.dto.req.GroupSaveReqDTO;
import com.hexiang.shotlink.admin.dto.req.GroupSortReqDTO;
import com.hexiang.shotlink.admin.dto.req.GroupUpdateReqDTO;
import com.hexiang.shotlink.admin.dto.resp.GroupRespDTO;
import com.hexiang.shotlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.hexiang.shotlink.admin.remote.dto.resp.ShortLinkGroupCountResqDTO;
import com.hexiang.shotlink.admin.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分组接口
 */
@RestController
public class GroupController {
    @Autowired
    private GroupService groupService;

    /**
     * 新增分组
     * @param groupSaveReqDTO
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/group")
    public Result<Void> saveGroup(@RequestBody GroupSaveReqDTO groupSaveReqDTO){
        groupService.saveShortLink(groupSaveReqDTO.getName());
        return Results.success();
    }

    /**
     * 查询所有分组（当前用户）
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/group")
    public Result<List<ShortLinkGroupRespDTO>> listGroup(){
        List<ShortLinkGroupRespDTO> groupRespDTOS = groupService.listGroup();
        return Results.success(groupRespDTOS);
    }

    /**
     * 更新分组信息
     * @param requestParm
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/group")
    public Result<Boolean> updateGroup(@RequestBody GroupUpdateReqDTO requestParm){
        Boolean result = groupService.updateGroup(requestParm);
        return Results.success(result);
    }

    /**
     * 删除分组
     * @param Gid
     * @return
     */
    @DeleteMapping("/api/short-link/admin/v1/group")
    public Result<Boolean> deleteGroup(@RequestParam String gid){
        Boolean result = groupService.deleteGroup(gid);
        return Results.success(result);
    }

    /**
     * 更新分组排序
     * @param requestParm
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/group/sort")
    public Result<Void> sortGroup(@RequestBody List<GroupSortReqDTO> requestParm){
        groupService.sortGroup(requestParm);
        return Results.success();
    }
}
