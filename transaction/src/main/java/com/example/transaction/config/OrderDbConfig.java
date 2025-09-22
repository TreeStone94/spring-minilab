package com.example.transaction.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
		basePackages = "com.example.transaction.order",
		entityManagerFactoryRef = "orderEntityManagerFactory"
)
public class OrderDbConfig {

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

		HashMap<String, Object> props = new HashMap<>();
		props.put("hibernate.transaction.jta.platform", "org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform");
		props.put("javax.persistence.transactionType", "JTA");
		props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		factory.setJpaPropertyMap(props);

		return factory;
	}
}
