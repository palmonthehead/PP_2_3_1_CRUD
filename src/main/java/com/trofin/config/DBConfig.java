package com.trofin.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("com.trofin")
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@ComponentScan("com.trofin")
public class DBConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource getDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(env.getProperty("db.url"));
        basicDataSource.setDriverClassName(env.getProperty("db.driver"));
        basicDataSource.setUsername(env.getProperty("db.username"));
        basicDataSource.setPassword(env.getProperty("db.password"));

        basicDataSource.setInitialSize(Integer.valueOf(env.getProperty("db.initialSize")));
        basicDataSource.setMinIdle(Integer.valueOf(env.getProperty("db.minIdle")));
        basicDataSource.setMaxIdle(Integer.valueOf(env.getProperty("db.maxIdle")));
        basicDataSource.setTestOnBorrow(Boolean.valueOf(env.getProperty("db.testOnBorrow")));
        basicDataSource.setValidationQuery(env.getProperty("db.validationQuery"));

        return basicDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(getDataSource());
        em.setPackagesToScan(env.getProperty("db.entity.package"));
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(getHibernateProperties());

        return em;
    }

    public Properties getHibernateProperties() {
        Properties properties = new Properties();
        try(InputStream is = getClass().getClassLoader()
                .getResourceAsStream("hibernate.properties")
        ) {
            properties.load(is);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public PlatformTransactionManager getTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(getEntityManagerFactory().getObject());
        return transactionManager;
    }
}
