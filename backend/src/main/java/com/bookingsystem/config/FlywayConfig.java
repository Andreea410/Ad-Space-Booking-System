package com.bookingsystem.config;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Manual Flyway Configuration
 * ARCHITECTURAL DECISION:
 * ----------------------
 * We configure Flyway manually instead of relying on Spring Boot's auto-configuration.
 * REASON:
 * Spring Boot 4.0.0 Flyway auto-configuration does not properly initialize in this project setup.
 * Despite having flyway-core and flyway-database-postgresql dependencies, the Flyway bean
 * is not created automatically.
 * SOLUTION:
 * - Check if Flyway bean exists (auto-configured)
 * - If null, create and configure Flyway manually
 * - Run migrations during application startup via @PostConstruct
 * BENEFITS:
 * - Ensures migrations always run on startup
 * - Provides explicit control over Flyway configuration
 * - Works consistently across different Spring Boot versions
 * - Logs migration status for debugging
 * TRADE-OFFS:
 * - Requires manual configuration (not leveraging Spring Boot magic)
 * - Need to maintain this configuration if upgrading Spring Boot
 */
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
