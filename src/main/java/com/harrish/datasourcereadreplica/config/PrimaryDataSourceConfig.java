package com.harrish.datasourcereadreplica.config;

import com.harrish.datasourcereadreplica.util.logging.InlineQueryLogEntryCreator;
import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.harrish",
        excludeFilters = @ComponentScan.Filter(ReadOnlyRepository.class),
        entityManagerFactoryRef = "primaryDataSourceManagerFactory"
)
public class PrimaryDataSourceConfig {

    @Value("${spring.datasource.url}")
    private String url;

    private final HikariCPService hikariCPService;

    public PrimaryDataSourceConfig(HikariCPService hikariCPService) {
        this.hikariCPService = hikariCPService;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        //return hikariCPService.getHikariDataSource(url);

        var loggingListener = new SLF4JQueryLoggingListener();
        loggingListener.setQueryLogEntryCreator(new InlineQueryLogEntryCreator());

        var dataSource = hikariCPService.getHikariDataSource(url);
        return ProxyDataSourceBuilder
                .create(dataSource)
                .name("primaryDataSource")
                .logQueryBySlf4j(SLF4JLogLevel.INFO)
                .listener(loggingListener)
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean primaryDataSourceManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSource") DataSource dataSource) {
        return builder.dataSource(dataSource)
                .packages("com.harrish.datasourcereadreplica")
                .persistenceUnit("main")
                .properties(hikariCPService.jpaProperties())
                .build();
    }
}
