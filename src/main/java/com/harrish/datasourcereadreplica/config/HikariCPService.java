package com.harrish.datasourcereadreplica.config;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HikariCPService {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.hikari.minimum-idle}")
    private int minimumIdle;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;

    @Value("${spring.datasource.hikari.idle-timeout}")
    private long idleTimeout;

    @Value("${spring.datasource.hikari.pool-name}")
    private String poolName;

    @Value("${spring.datasource.hikari.max-lifetime}")
    private long maxLifetime;

    @Value("${spring.datasource.hikari.connection-test-query}")
    private String connectionTestQuery;

    private final Environment env;

    public HikariCPService(Environment env) {
        this.env = env;
    }

    public HikariDataSource getHikariDataSource(String url) {
        var dataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();

        dataSource.setMinimumIdle(minimumIdle);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        dataSource.setIdleTimeout(idleTimeout);
        dataSource.setPoolName(poolName);
        dataSource.setMaxLifetime(maxLifetime);
        dataSource.setConnectionTestQuery(connectionTestQuery);

        return dataSource;
    }

    public Map<String, Object> jpaProperties() {
        var props = new HashMap<String, Object>();
        props.put("hibernate.physical_naming_strategy", CamelCaseToUnderscoresNamingStrategy.class.getName());
        props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        props.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        props.put("hibernate.jdbc.batch_size", env.getProperty("spring.jpa.properties.hibernate.jdbc.batch_size"));
        props.put("hibernate.ddl-auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        props.put("hibernate.show-sql", env.getProperty("spring.jpa.hibernate.show-sql"));
        props.put("spring.jpa.open-in-view", env.getProperty("spring.jpa.open-in-view"));
        props.put("hibernate.proc.param_null_passing", env.getProperty("spring.jpa.properties.hibernate.proc.param_null_passing"));

        return props;
    }
}
