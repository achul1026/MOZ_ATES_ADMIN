package com.moz.ates.traffic.admin.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.moz.ates.traffic.common.entity.operator.MozWebOprtrDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthenticationEntity extends MozWebOprtrDTO implements UserDetails{
	
	String oprtrAccountId;				//담당자 아이디
	String password;					//담당자 비밀번호
	String oprtrPermission;				//담당자 권한
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = Arrays.asList(oprtrPermission);
        List<GrantedAuthority> list = new ArrayList<>();
        roles.forEach(role -> list.add(new SimpleGrantedAuthority("ROLE_" + role)));
        return list;
    }

    public AuthenticationEntity(MozWebOprtrDTO mozWebOprtrDTO) {
        BeanUtils.copyProperties(mozWebOprtrDTO, this);
    }
    
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return oprtrAccountId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

