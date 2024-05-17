package com.im.common.util.hikari;

import com.zaxxer.hikari.HikariDataSource;

/**
 * 自定义一些Hikari参数
 *
 * @author Barry
 * @date 2019-10-14
 */
public class HikariCustomDataSource extends HikariDataSource {
    public HikariCustomDataSource() {
        // 是否自定义配置，为true时下面两个参数才生效
        addDataSourceProperty("cachePrepStmts", "true");
        // 连接池大小默认25，官方推荐250-500
        addDataSourceProperty("prepStmtCacheSize", "250");
        // 单条语句最大长度默认256，官方推荐2048
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        // 新版本MySQL支持服务器端准备，开启能够得到显著性能提升
        // useServerPrepStmts可能会报如下错误，把maxAllowedPacket调整大即可
        // When 'useServerPrepStmts=true', 'maxAllowedPacket' must be higher than 8,203. Check also 'max_allowed_packet' in MySQL configuration files.
        addDataSourceProperty("useServerPrepStmts", "true");
        addDataSourceProperty("useLocalSessionState", "true");
        addDataSourceProperty("useLocalTransactionState", "false");

        // 该参数可大幅度提升写入的性能
        addDataSourceProperty("rewriteBatchedStatements", "true");

        addDataSourceProperty("cacheResultSetMetadata", "true");
        addDataSourceProperty("cacheServerConfiguration", "true");
        addDataSourceProperty("elideSetAutoCommits", "true");
        addDataSourceProperty("maintainTimeStats", "false");
    }

    @Override
    public void close() {
        super.close();
    }
}
