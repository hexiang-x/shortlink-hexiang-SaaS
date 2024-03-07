package com.hexiang.shotlink.project.common.enmus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@RequiredArgsConstructor
public enum VailDateTypeEnum {
    /**
     * 永久有效期
     */
    PERMANENT(0),

    /**
     * 自定义有效期
     */
    CUSTOM(1);

    @Getter
    private final int type;
}
