package com.harrish.datasourcereadreplica.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.harrish",
        includeFilters = @ComponentScan.Filter(ReadOnlyRepository.class),
        entityManagerFactoryRef = "readOnlyDataSourceManagerFactory"
)
public class ReadOnlyDataSourceConfig {

    @Value("${spring.datasource.read.replica.url}")
    private String url;

    private final HikariCPService hikariCPService;

    public ReadOnlyDataSourceConfig(HikariCPService hikariCPService) {
        this.hikariCPService = hikariCPService;
    }

    @Bean
    public DataSource readDataSource() {
        return hikariCPService.getHikariDataSource(url);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean readOnlyDataSourceManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("readDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource)
                .packages("com.harrish.datasourcereadreplica")
                .persistenceUnit("read")
                .properties(hikariCPService.jpaProperties())
                .build();
    }
}
