package com.hexiang.shortlink.admin.test;

public class UserTableShardingTest {
    private final static String Sql = "ALTER TABLE t_link_%d\n" +
            "MODIFY COLUMN `describe` VARCHAR(255) CHARACTER SET utf8mb4;";

    public static void main(String[] args) {
        for(int i = 0; i < 16; i ++){
            System.out.printf((Sql) + "%n", i);
        }
    }
}
