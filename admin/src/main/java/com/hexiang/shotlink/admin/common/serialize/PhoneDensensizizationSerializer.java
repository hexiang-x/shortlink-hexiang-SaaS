package com.hexiang.shotlink.admin.common.serialize;


import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hexiang.shotlink.admin.dto.resp.UserRespDTO;

import java.io.IOException;

/**
 * 电话号码脱敏
 */
public class PhoneDensensizizationSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String phoneDesensitization = DesensitizedUtil.mobilePhone(s);
        jsonGenerator.writeString(phoneDesensitization);
    }
}
