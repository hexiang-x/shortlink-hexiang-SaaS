package com.hexiang.shotlink.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hexiang.shotlink.project.dao.entity.ShortLinkDO;
import lombok.Data;

import java.util.List;

@Data
public class ShortLinkRecycleBinPageReqDTO extends Page<ShortLinkDO> {
    List<String> gidList;
}
