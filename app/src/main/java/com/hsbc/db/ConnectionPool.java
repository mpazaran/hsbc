package com.hsbc.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static HikariDataSource dataSource;

    // Initialize the HikariCP connection pool
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + System.getenv("MYSQL_DB_HOST") + ":" + System.getenv("MYSQL_DB_PORT") + "/" + System.getenv("MYSQL_DB_NAME"));
        config.setUsername(System.getenv("MYSQL_DB_USER"));
        config.setPassword(System.getenv("MYSQL_DB_PASSWORD"));
        config.setMaximumPoolSize(10);  // Set the maximum pool size
        config.setConnectionTimeout(30000);  // Timeout if connection is not available
        config.setIdleTimeout(600000);  // Connection idle timeout
        config.setMaxLifetime(1800000);  // Maximum lifetime of a connection in the pool

        dataSource = new HikariDataSource(config);
    }

    // Method to get a connection from the pool
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Method to close the data source and release resources
    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
