package com.hexiang.shortlink.admin.test;

public class UserTableShardingTest {
    private final static String Sql = "CREATE TABLE `t_link_goto_%d`  (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
            "  `full_short_link` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '短链接',\n" +
            "  `gid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分组ID',\n" +
            "  PRIMARY KEY (`id`) USING BTREE\n" +
            ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;";

    public static void main(String[] args) {
        for(int i = 0; i < 16; i ++){
            System.out.printf((Sql) + "%n", i);
        }
    }
}
