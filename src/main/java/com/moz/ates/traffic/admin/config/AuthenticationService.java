package com.moz.ates.traffic.admin.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.main.MainService;
import com.moz.ates.traffic.admin.main.SecurityAccount;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;

@Service
public class AuthenticationService implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";

    @Autowired
    private MainService mainService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MozWebOprtr webOprtr = new MozWebOprtr(username);
        MozWebOprtr result = mainService.getUserById(webOprtr);
        AuthenticationEntity authenticationEntity = new AuthenticationEntity();

        if(result != null && result.getOprtrAccountPw() != null){
            authenticationEntity.setOprtrAccountId(result.getOprtrAccountId());
            authenticationEntity.setOprtrAccountPw(result.getOprtrAccountPw());
            authenticationEntity.setAuthorities(makeGrantedAuthority(Arrays.asList(new String[] {result.getOprtrPermission()})));
        }else{
            throw new UsernameNotFoundException(username);
        }

        return new SecurityAccount(authenticationEntity);
    }

    private Collection<? extends GrantedAuthority> makeGrantedAuthority(List<String> roles) {
        List<GrantedAuthority> list = new ArrayList<>();
        roles.forEach(role -> list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role)));
        return list;
    }
}
