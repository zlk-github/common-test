### common-db-test

数据库mybatis-plus集成测试，包含分页，自动注入通用字段等。

脚本：

    --ts_权限，tt_业务，tb_基础配置
    CREATE TABLE `ts_user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` int NOT NULL,
    `create_by` varchar(64) DEFAULT '' COMMENT '创建者id',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT '' COMMENT '更新者id',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIME
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
