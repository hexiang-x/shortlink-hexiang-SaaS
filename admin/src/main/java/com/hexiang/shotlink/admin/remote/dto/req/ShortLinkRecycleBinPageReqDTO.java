package com.hexiang.shotlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

@Data
public class ShortLinkRecycleBinPageReqDTO extends Page {
    List<String> gidList;
}
