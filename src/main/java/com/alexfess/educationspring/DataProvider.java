package com.alexfess.educationspring;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

public class DataProvider implements Serializable {

    private static final long serialVersionUID = 171125L;
    private static HikariDataSource dataSourceInstance;

    public static DataSource dataSource() {
        if (dataSourceInstance == null) {
            synchronized (DataProvider.class) {
                if (dataSourceInstance == null) {
                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl("jdbc:oracle:thin:@//oracle-test.tmb.nf/hope.intra.net");
                    config.setUsername("moroz");
                    config.setPassword(ConfigLoader.getDbPassword());
                    config.setDriverClassName("oracle.jdbc.OracleDriver");

                    // Настройки пула
                    config.setMinimumIdle(2);
                    config.setMaximumPoolSize(10);
                    config.setConnectionTimeout(15000);
                    config.setIdleTimeout(300000);
                    config.setMaxLifetime(1800000);

                    // Проверка соединений
                    config.setConnectionTestQuery("SELECT 1 FROM DUAL");

                    config.setPoolName("OracleHikariPool");

                    dataSourceInstance = new HikariDataSource(config);
                }
            }
        }
        return dataSourceInstance;
    }

    public static Connection getConnection() throws NamingException, SQLException {
        Connection cn = dataSource().getConnection();
        cn.setAutoCommit(false);
        return cn;
    }
}
