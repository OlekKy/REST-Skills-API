package com.ak.restskillsapi.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.*;
import ch.qos.logback.core.db.DriverManagerConnectionSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static String REALM="MY_TEST_REALM";
	@Autowired
	DataSource dataSource;
	
	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("bill").password("{noop}abc123").roles("ADMIN");
		auth.inMemoryAuthentication().withUser("tom").password("abc123").roles("USER");
		auth.jdbcAuthentication().dataSource(dataSource).authoritiesByUsernameQuery("SELECT name as username, 'normal' as role FROM users WHERE name = ?")
		.usersByUsernameQuery("SELECT name as username,password,true as enabled FROM users WHERE name = ?").passwordEncoder(NoOpPasswordEncoder.getInstance());
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
 
	
	  http.csrf().disable()
	  	.authorizeRequests().antMatchers(HttpMethod.POST, "/users").permitAll().antMatchers(HttpMethod.GET, "/users/{id}").permitAll()
	  	.antMatchers("/users/**","/skills/**").hasAuthority("normal")//hasRole("normal")
		.and().httpBasic().realmName(REALM).authenticationEntryPoint(getBasicAuthEntryPoint())
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
 	}
	
	@Bean
	public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
		return new CustomBasicAuthenticationEntryPoint();
	}
	
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.POST, "/users/");
    }
}
