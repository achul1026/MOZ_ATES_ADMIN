package com.moz.ates.traffic.admin.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.mail.Message;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    MessageSource messageSource;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        
        http.authorizeRequests()
                .antMatchers("/joinUs","/joinUsAjax","/bootstrap/css/**","/bootstrap/img/**","/bootstrap/js/**","/bootstrap/scss/**","/bootstrap/vendor/**").permitAll()
                .antMatchers("/**").hasAnyRole("sup")
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginAjax")
                .successHandler(successHandler())
                .failureHandler(failureHandler())
                .usernameParameter("oprtrAccountId")
                .passwordParameter("oprtrAccountPw")
                .defaultSuccessUrl("/dashboard",true)
                .permitAll();


        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder());
    }


    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler failureHandler(){
        return new LoginFailureHandler(messageSource);
    }
}
