package com.luv2code.springdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource securityDataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication().dataSource(securityDataSource);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
//                .anyRequest()
//                    .authenticated()
                .antMatchers("/customer/delete*").hasAnyRole("ADMIN")
                .antMatchers("/customer/showForm*").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/customer/save*").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/customer/**").hasAnyRole("EMPLOYEE")
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/**").permitAll()
                .and()
                .formLogin()
                    .loginProcessingUrl("/authenticateTheUser")
                    .permitAll()
                .and()
                .logout()
                    .permitAll()
                .and()
                .exceptionHandling()
                    .accessDeniedPage("/access-denied");
/*
* http://localhost:8080/customer/showFormForUpdate?customerId=1
	http://localhost:8080/customer/delete?customerId=3
	http://localhost:8080/customer/showFormForAdd*/
    }

//    @Bean
//    private BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    private DaoAuthenticationProvider authenticationProvider() {
//
//        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
//        auth.setUserDetailsService();
//
//    }
}
