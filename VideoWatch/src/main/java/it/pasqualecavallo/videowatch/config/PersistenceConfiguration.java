package it.pasqualecavallo.videowatch.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages="it.pasqualecavallo.videowatch")
public class PersistenceConfiguration {

	@Value("${datasourceDriverClassName}")
	private String datasourceDriverClassName;

	@Value("${datasourceUserName}")
	private String datasourceUserName;

	@Value("${datasourcePassword}")
	private String datasourcePassword;

	@Value("${datasourceSchemaUrl}")
	private String datasourceSchemaUrl;

	@Value("${packageContainsJPARepos}")
	private String packageContainsJPARepos;

	@Value("${hibernateDialect}")
	private String hibernateDialect;

	@Value("${hibernateExecuteDdlAuto}")
	private String ddlAction;
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(datasourceDriverClassName);
		dataSource.setUsername(datasourceUserName);
		dataSource.setPassword(datasourcePassword);
		dataSource.setUrl(datasourceSchemaUrl);
		return dataSource;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(packageContainsJPARepos);
		factory.setDataSource(dataSource());
		factory.setJpaProperties(additionalProperties());
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}

	Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", hibernateDialect);
		properties.setProperty("hibernate.hbm2ddl.auto", ddlAction);
		return properties;
	}

}
