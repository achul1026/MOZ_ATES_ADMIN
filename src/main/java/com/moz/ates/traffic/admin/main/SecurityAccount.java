package com.moz.ates.traffic.admin.main;


import com.moz.ates.traffic.admin.config.AuthenticationEntity;
import org.springframework.security.core.userdetails.User;


public class SecurityAccount extends User {


    public SecurityAccount(AuthenticationEntity authenticationEntity) {
        super(authenticationEntity.getOprtrAccountId(), authenticationEntity.getOprtrAccountPw(), authenticationEntity.getAuthorities());
    }
}
