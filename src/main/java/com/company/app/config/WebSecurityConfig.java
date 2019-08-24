package com.company.app.config;

import java.sql.SQLException;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableJpaRepositories
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource({ 
	  "classpath:application.properties"
	})
@EnableTransactionManagement
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
		
	@Value("${spring.datasource.url}")
	String url;
	@Value("${spring.datasource.username}")
	String username;
	@Value("${spring.datasource.password}")
	String password;
	@Value("${spring.datasource.driverClassName}")
	String driver;
	
	@Lazy
	@Autowired
	private DataSource dataSource;
	
	/*@Bean
    public EntityManagerFactory entityManagerFactory() throws SQLException {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);        
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.company.app");
        factory.setDataSource(dataSource());
        factory.setJpaProperties(additionalProperties());
        factory.afterPropertiesSet();
        
        return factory.getObject();
    }
	
	@Bean
	@Qualifier(value = "entityManager")
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }
*/
    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }
    
    Properties additionalProperties() {
        
    	Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.c3p0.acquire_increment", "1");
        properties.setProperty("hibernate.c3p0.idle_test_period", "60");
        properties.setProperty("hibernate.c3p0.min_size", "0");
        properties.setProperty("hibernate.c3p0.max_size", "2");
        properties.setProperty("hibernate.max_statements", "50");
        properties.setProperty("hibernate.c3p0.acquireRetryAttempts", "0");
        properties.setProperty("hibernate.c3p0.preferredTestQuery","SELECT 1");
        properties.setProperty("hibernate.c3p0.testConnectionOnCheckout","true");
        properties.setProperty("hibernate.c3p0.idleConnectionTestPeriod","1800");    
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
   
        return properties;
     }
			
	@Bean(name = "dataSource")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(driver);
        driverManagerDataSource.setUrl(url);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);
        return driverManagerDataSource;
    }
   /* @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource datasource() {
          return DataSourceBuilder.create().build();
    }
*/    
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        StringBuilder usersByUsernameQuery = new StringBuilder();
        StringBuilder authoritiesByUsernameQuery = new StringBuilder();
        
        usersByUsernameQuery.append("SELECT user_name AS login, user_pass AS PASSWORD, state AS Enabled ");
        usersByUsernameQuery.append("FROM \"m_user\" WHERE user_name = ?");							
        authoritiesByUsernameQuery.append("SELECT u.user_name AS username, UPPER(p.description) AS authority ");
        authoritiesByUsernameQuery.append("FROM m_user u INNER JOIN m_profile p ON u.id_profile = p.id_profile ");
        authoritiesByUsernameQuery.append("WHERE u.user_name = ?");
        
    	auth.jdbcAuthentication().dataSource(dataSource)
        .usersByUsernameQuery(usersByUsernameQuery.toString())
        .authoritiesByUsernameQuery(authoritiesByUsernameQuery.toString());
        //.passwordEncoder(passwordEncoder())
    }
	    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
        .antMatchers("/login").permitAll()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/dashboard").authenticated()
        
        .and().formLogin()
        .and().logout().logoutSuccessUrl("/login").permitAll()
        .and()
		        .formLogin()
		        .loginPage("/login")
		        .loginProcessingUrl("/j_spring_security_check")
		        .defaultSuccessUrl("/dashboard")
		        .failureUrl("/login?error=true")
		        .permitAll()		        
		    .and()
		        .logout().logoutUrl("/j_spring_security_logout")
		        .logoutSuccessUrl("/login?logout=true").deleteCookies("JSESSIONID")
		        .invalidateHttpSession(true)
		        .permitAll()
		    .and()
		        .csrf()
		        .disable();		
		http
        .sessionManagement()
        .maximumSessions(1)
        .expiredUrl("/expired")
        .maxSessionsPreventsLogin(true)
        .sessionRegistry(sessionRegistry())
        .and()
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        .invalidSessionUrl("/login?expiredSession=true")
		.sessionFixation().migrateSession();        
        //.logoutSuccessHandler(logoutSuccessHandler());
	}
    
	// Work around https://jira.spring.io/browse/SEC-2855
	@Bean
	public SessionRegistry sessionRegistry() {
	    SessionRegistry sessionRegistry = new SessionRegistryImpl();
	    return sessionRegistry;
	}

	// Register HttpSessionEventPublisher
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public static ServletListenerRegistrationBean httpSessionEventPublisher() {
	    return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
	}
		
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
    	
        return new BCryptPasswordEncoder();
    }
}
