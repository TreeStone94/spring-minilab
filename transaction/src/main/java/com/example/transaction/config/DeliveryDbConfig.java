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
		basePackages = "com.example.transaction.delivery",
		entityManagerFactoryRef = "deliveryEntityManagerFactory"
)
public class DeliveryDbConfig {

	@Bean
	@ConfigurationProperties("spring.jta.atomikos.datasource.delivery-db")
	public DataSource deliveryDataSource() {
		return new AtomikosDataSourceBean();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean deliveryEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJtaDataSource(deliveryDataSource());
		factory.setPackagesToScan("com.example.transaction.delivery");
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		HashMap<String, Object> props = new HashMap<>();
		props.put("hibernate.transaction.jta.platform", "org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform");
		props.put("javax.persistence.transactionType", "JTA");
		props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		factory.setJpaPropertyMap(props);

		return factory;
	}
}
