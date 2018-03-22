package com.starin.conf;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class Datasource {
	
	@Autowired 
	EnvConfiguration configuration;
	
	private static final Logger logger = LoggerFactory.getLogger(Datasource.class);
	
	private DataSource datasource;
	
	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	/**
	 * Creating DataSouce database configuration bean
	 * This is used by spring boot
	 * @return DataSource Bean
	 */
	@Bean
	public DataSource dataSource(){
		PoolProperties poolProperties = new PoolProperties();   

		poolProperties.setUrl("jdbc:mysql://"+configuration.getDBIp()+":"+configuration.getDBPort()+"/"+configuration.getDBName()+"?autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone="+configuration.getServerTimeZone()+"&useLegacyDatetimeCode=false"); 
		
		poolProperties.setDriverClassName(configuration.getDBDriver());   
		poolProperties.setUsername(configuration.getDBUser()); 
		poolProperties.setPassword(configuration.getDBPass().trim()); 
		poolProperties.setTestWhileIdle(true);      
		poolProperties.setTestOnBorrow(true);     
		poolProperties.setValidationQuery("SELECT 1");       
		poolProperties.setTestOnReturn(false);       
		poolProperties.setValidationInterval(30000);   
		poolProperties.setTimeBetweenEvictionRunsMillis(30000);   
		poolProperties.setMaxActive(configuration.getMaxActiveDatabaseConnection());  
		poolProperties.setInitialSize(configuration.getInitialDataBaseConnection());  
		poolProperties.setMaxWait(30000);  
		poolProperties.setRemoveAbandonedTimeout(30);   
		poolProperties.setMinEvictableIdleTimeMillis(30000);  
		poolProperties.setMinIdle(configuration.getMinimumIdelDataBaseConnection());  
		poolProperties.setLogAbandoned(true);      
		poolProperties.setRemoveAbandoned(true); 
		poolProperties.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
		"org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;"+"org.apache.tomcat.jdbc.pool.interceptor.ResetAbandonedTimer"); 
		logger.debug("DataSource Bean creation");
		DataSource dataSource = null;
		try{
			dataSource = new DataSource();
			dataSource.setPoolProperties(poolProperties);
		}catch(Exception e){
			logger.debug("Exception occure while creating datasource : "+e);
			System.exit(-1);    	
			logger.error(":before system exit");
		}
		logger.debug("DataSource created successfully  : "+dataSource);
		this.setDatasource(dataSource);
		return dataSource;
	}
	

	/**
	 * Bean for getting the handle of SessionFactory for
	 * Manually Creating Hibernate Session if needed.
	 * @return
	 */
	@Bean
	public HibernateJpaSessionFactoryBean sessionFactory() {
		logger.debug("Creating hibernate Session factoy");

		HibernateJpaSessionFactoryBean hibernateJpaSessionFactoryBean = null;
		try{
			hibernateJpaSessionFactoryBean = new HibernateJpaSessionFactoryBean();
		}catch(Exception e){
			logger.debug("Exception occure while create HibernateJpaSessionFactoryBean : "+e);
		}
		logger.debug("HibernateJpaSessionFactoryBean created succesfully.");
		return hibernateJpaSessionFactoryBean;
	}
	
}
