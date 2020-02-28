package com.epam.lab.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@ComponentScan("com.epam.lab")
@EnableTransactionManagement
public class SpringRepoConfig {
    @Autowired
    private Environment env;

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource,
                                                                           JpaVendorAdapter vendorAdapter) {
        LocalContainerEntityManagerFactoryBean managerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        managerFactoryBean.setDataSource(dataSource);
        managerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        managerFactoryBean.setPackagesToScan("com.epam.lab");
//        managerFactoryBean.setJpaProperties(additionalProperties());
        return managerFactoryBean;
    }

    @Bean
    public PersistenceAnnotationBeanPostProcessor paPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }

    @Bean
    public BeanPostProcessor persistenceTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    @Profile("prod")
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig(Objects.requireNonNull(env.getProperty("database.properties")));
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    @Profile("dev")
    public DataSource dataSourceEmbedded() {
        return new EmbeddedDatabaseBuilder()
                .addScript("createDB.sql")
//                .addScript("createTestData.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    @Profile("prod")
    public JpaVendorAdapter jpaVendorAdapterPostgresQL() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.POSTGRESQL);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        adapter.setDatabasePlatform(PostgreSQL9Dialect.class.getName());
        return adapter;
    }

    @Bean
    @Profile("dev")
    public JpaVendorAdapter jpaVendorAdapterH2() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.H2);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        adapter.setDatabasePlatform(H2Dialect.class.getName());
        return adapter;
    }

//    private Properties additionalProperties() {
//        final Properties hibernateProperties = new Properties();
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
//        hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
//        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", env.getProperty("hibernate.cache.use_second_level_cache"));
//        hibernateProperties.setProperty("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));
//        return hibernateProperties;
//    }
}
