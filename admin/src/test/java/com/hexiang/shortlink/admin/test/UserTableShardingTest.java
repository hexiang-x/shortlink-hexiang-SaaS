package com.hexiang.shortlink.admin.test;

public class UserTableShardingTest {
    private final static String Sql = "CREATE TABLE `t_link_%d`  (\n" +
            "  `id` bigint(20) NOT NULL COMMENT 'ID',\n" +
            "  `domain` varchar(128) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,\n" +
            "  `short_url` varchar(8) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,\n" +
            "  `full_short_url` varchar(128) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,\n" +
            "  `origin_url` varchar(1024) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,\n" +
            "  `click_num` int(11) UNSIGNED ZEROFILL NULL DEFAULT NULL,\n" +
            "  `gid` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,\n" +
            "  `favicon` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '网站标识图片地址',\n" +
            "  `enable_status` tinyint(1) NULL DEFAULT NULL,\n" +
            "  `create_type` tinyint(1) NULL DEFAULT NULL,\n" +
            "  `valid_data_type` tinyint(1) NULL DEFAULT NULL,\n" +
            "  `valid_data` datetime NULL DEFAULT NULL,\n" +
            "  `describe` varchar(1024) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,\n" +
            "  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',\n" +
            "  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',\n" +
            "  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',\n" +
            "  PRIMARY KEY (`id`) USING BTREE,\n" +
            "  UNIQUE INDEX `idx_unique_full_short`(`full_short_url`) USING BTREE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

    public static void main(String[] args) {
        for(int i = 0; i < 16; i ++){
            System.out.printf((Sql) + "%n", i);
        }
    }
}
