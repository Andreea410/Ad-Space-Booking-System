package com.bookingsystem.config;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {
    @Autowired(required = false)
    private Flyway flyway;
    
    @Autowired
    private DataSource dataSource;
    
    @PostConstruct
    public void initializeFlyway() {
        if (flyway == null) {
            Flyway manualFlyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .load();
            
            manualFlyway.migrate();
        }
    }
}

