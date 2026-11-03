
package com.campus.shuttle.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class DatabaseConfig {

    private static final Logger log =
            LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/shuttle_tracker?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true}")
    private String mysqlUrl;

    @Value("${spring.datasource.username:root}")
    private String mysqlUser;

    @Value("${spring.datasource.password:root}")
    private String mysqlPass;

    @Bean
    @Primary
    public DataSource dataSource() {

        log.info("Connecting to database: {}", mysqlUrl);

        try {
            DriverManager.setLoginTimeout(2);

            try (Connection conn =
                    DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPass)) {

                log.info("Successfully connected to MySQL database");

                DriverManagerDataSource ds =
                        new DriverManagerDataSource();

                ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
                ds.setUrl(mysqlUrl);
                ds.setUsername(mysqlUser);
                ds.setPassword(mysqlPass);

                return ds;
            }

        } catch (Exception ex) {

            log.warn("Unable to connect to MySQL: {}", ex.getMessage());

            log.info("Activating H2 database...");

            DriverManagerDataSource h2Ds =
                    new DriverManagerDataSource();

            h2Ds.setDriverClassName("org.h2.Driver");

            h2Ds.setUrl(
                "jdbc:h2:mem:shuttle_tracker;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1"
            );

            h2Ds.setUsername("sa");
            h2Ds.setPassword("");

            return h2Ds;
        }
    }
}
