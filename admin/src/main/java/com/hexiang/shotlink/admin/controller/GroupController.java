package com.hexiang.shotlink.admin.controller;

import com.hexiang.shotlink.admin.common.convention.result.Result;
import com.hexiang.shotlink.admin.common.convention.result.Results;
import com.hexiang.shotlink.admin.dto.req.GroupSaveReqDTO;
import com.hexiang.shotlink.admin.dto.req.GroupSortReqDTO;
import com.hexiang.shotlink.admin.dto.req.GroupUpdateReqDTO;
import com.hexiang.shotlink.admin.dto.resp.GroupRespDTO;
import com.hexiang.shotlink.admin.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupController {
    @Autowired
    private GroupService groupService;
    @PostMapping("/api/short-link/admin/v1/group")
    public Result<Void> saveGroup(@RequestBody GroupSaveReqDTO groupSaveReqDTO){
        groupService.saveShortLink(groupSaveReqDTO.getName());
        return Results.success();
    }

    @GetMapping("/api/short-link/admin/v1/group")
    public Result<List<GroupRespDTO>> listGroup(){
        List<GroupRespDTO> groupRespDTOS = groupService.listGroup();
        return Results.success(groupRespDTOS);
    }

    @PutMapping("/api/short-link/admin/v1/group")
    public Result<Boolean> updateGroup(@RequestBody GroupUpdateReqDTO requestParm){
        Boolean result = groupService.updateGroup(requestParm);
        return Results.success(result);
    }

    @DeleteMapping("/api/short-link/admin/v1/group")
    public Result<Boolean> deleteGroup(String Gid){
        Boolean result = groupService.deleteGroup(Gid);
        return Results.success(result);
    }

    @PostMapping("/api/short-link/admin/v1/group/sort")
    public Result<Void> sortGroup(@RequestBody List<GroupSortReqDTO> requestParm){
        groupService.sortGroup(requestParm);
        return Results.success();
    }
}
