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
                .antMatchers("/joinUs","/joinUsAjax","/password/**" ,"/id/**" ,"/css/**","/js/**", "/images/**", "/modal/**", "/common/**").permitAll()
//                .antMatchers("/**").hasAnyRole("sup","pol","opr")
                .antMatchers("/**").hasAnyRole("OPC000","OPC001","OPC002", "OPC003")
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginAjax")
                .usernameParameter("oprtrAccountId")
                .passwordParameter("oprtrAccountPw")
                .defaultSuccessUrl("/dashboard",true)
                .successHandler(successHandler())
                .failureHandler(failureHandler())
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
