package com.moz.ates.traffic.admin.config;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class AuthenticationEntity {
    private String oprtrAccountId;				//담당자 아이디
    private String oprtrAccountPw;				//담당자 비밀번호
    private Collection<? extends GrantedAuthority> authorities;
}
