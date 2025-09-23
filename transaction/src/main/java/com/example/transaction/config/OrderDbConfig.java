package com.example.transaction.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
		basePackages = "com.example.transaction.order",
		entityManagerFactoryRef = "orderEntityManagerFactory"
)
@RequiredArgsConstructor
public class OrderDbConfig {
	private final JpaProperties jpaProperties;
	private final HibernateProperties hibernateProperties;

	@Bean
	@ConfigurationProperties("spring.jta.atomikos.datasource.order-db")
	public DataSource orderDataSource() {
		return new AtomikosDataSourceBean();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean orderEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJtaDataSource(orderDataSource());
		factory.setPackagesToScan("com.example.transaction.order");
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		Map<String, Object> props = hibernateProperties.determineHibernateProperties(
				               jpaProperties.getProperties(), new HibernateSettings());
		props.put("hibernate.transaction.jta.platform", "org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform");
		props.put("jakarta.persistence.transactionType", "JTA");
		props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		factory.setJpaPropertyMap(props);

		return factory;
	}
}
