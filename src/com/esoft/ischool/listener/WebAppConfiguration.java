package  com.esoft.ischool.listener;

import java.util.List;
import java.util.Properties;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = "com.esoft.ischool.*")
public class WebAppConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// converters.add(createXmlHttpMessageConverter());
		converters.add(new MappingJackson2HttpMessageConverter());
		System.out.println("configureMessageConverters is callled");
		super.configureMessageConverters(converters);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		System.out.println("addResourceHandlers is called");
		// registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		registry.addResourceHandler("/**").addResourceLocations("/");
	}

	@Bean(name = "viewResolver")
	public InternalResourceViewResolver getViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");

		 /*
		 dataSource.setUrl("jdbc:mysql://127.9.190.2:3306/arelbou") ;
		 dataSource.setUsername("adminQXJBNXC");
		 dataSource.setPassword("kN1nuqbGpQ_r");
		*/
		
		dataSource.setUrl("jdbc:mysql://localhost:3306/arelbouprod");
		dataSource.setUsername("root");
		dataSource.setPassword("admin");
		 
		Properties connectionProperties = new Properties();
		connectionProperties.setProperty("defaultTransactionIsolation", "2");
		connectionProperties.setProperty("initialSize", "10");
		connectionProperties.setProperty("maxActive", "-1");
		connectionProperties.setProperty("defaultAutoCommit", "false");
		connectionProperties.setProperty("minIdle", "0");
		connectionProperties.setProperty("maxIdle", "-1");
		connectionProperties.setProperty("maxWait", "20000");
		connectionProperties.setProperty("removeAbandoned", "true");
		connectionProperties.setProperty("removeAbandonedTimeout", "300");
		connectionProperties.setProperty("logAbandoned", "true");
		connectionProperties.setProperty("validationQuery", "select 1 from dual");
		dataSource.setConnectionProperties(connectionProperties);

		return dataSource;
	}

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) {
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
		sessionBuilder.scanPackages("com.esoft.ischool.model");
		sessionBuilder.scanPackages("com.esoft.ischool.security.model");
		sessionBuilder.setProperty("hibernate.show_sql", "false");
		sessionBuilder.setProperty("checkWriteOperations", "false");
		sessionBuilder.setProperty("flushMode", "COMMIT");
		return sessionBuilder.buildSessionFactory();

	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver commonsMultipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("utf-8");
		commonsMultipartResolver.setMaxUploadSize(50000000);
		return commonsMultipartResolver;
	}

	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);

		return transactionManager;
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("4096KB");
		factory.setMaxRequestSize("4096KB");
		factory.setLocation("c:\\TEMP");
		return factory.createMultipartConfig();
	}

}