package com.moz.ates.traffic.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class LoginFailureHandler implements AuthenticationFailureHandler {

    MessageSource messageSource;

    @Autowired
    LocaleResolver localeResolver;

    public LoginFailureHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String error = "";
        Locale locale = localeResolver.resolveLocale(request);
        //아이디 입력 안했을때
        if(StringUtils.isEmpty((String)request.getParameter("oprtrAccountId"))){
            error = messageSource.getMessage("login.fail.id.alert",null,locale);

        }else{
            if(StringUtils.isEmpty((String)request.getParameter("oprtrAccountPw"))){
                error = messageSource.getMessage("login.fail.password.alert",null,locale);
            }else{
                error = messageSource.getMessage("login.fail.error.alert",null,locale);
            }
        }


        System.out.println(request.getContextPath());

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('"+error+"'); location.href='"+request.getContextPath()+"/login';</script>");
        out.flush();
    }
}
