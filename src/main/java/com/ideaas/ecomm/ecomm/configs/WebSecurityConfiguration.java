package com.ideaas.ecomm.ecomm.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Order(1)
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.headers().frameOptions().disable();
        security.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
                .antMatchers("/h2-console","/login").permitAll();

                security.authorizeRequests(); 
        // security.authenticationProvider(authenticationProvider);

        // security.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
   
    }

}
