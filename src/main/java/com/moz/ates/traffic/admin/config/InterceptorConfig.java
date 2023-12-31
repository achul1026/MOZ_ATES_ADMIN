package com.moz.ates.traffic.admin.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	@Autowired
	MenuInterceptor menuInterceptor;

    @Bean
    public LocaleResolver localeResolver() {

        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setCookieName("lang");
        resolver.setDefaultLocale(new Locale("eng"));
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Bean
    public MessageSource messageSource(
        @Value("${spring.messages.basename}") String basename,
        @Value("${spring.messages.encoding}") String encoding
    ) {
    	ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(basename.split(","));
        messageSource.setDefaultEncoding(encoding);
        return messageSource;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        
        //사이드 메뉴관련 추가
		registry.addInterceptor(menuInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns("/login")
		.excludePathPatterns("/loginAjax")
		.excludePathPatterns("/joinUs")
		.excludePathPatterns("/joinUsAjax")
		.excludePathPatterns("/bootstrap/css/**")
		.excludePathPatterns("/bootstrap/img/**")
		.excludePathPatterns("/bootstrap/js/**")
		.excludePathPatterns("/bootstrap/scss/**")
		.excludePathPatterns("/bootstrap/vendor/**")
		.excludePathPatterns("/favicon.ico")
		.excludePathPatterns("/error")
		.excludePathPatterns("/bootstrap/**");
    }
}