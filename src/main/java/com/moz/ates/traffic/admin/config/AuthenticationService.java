package com.moz.ates.traffic.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.common.enums.OprtrSttsCd;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtrDTO;
import com.moz.ates.traffic.common.repository.operator.MozWebOprtrRepository;

@Service
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private MozWebOprtrRepository mozWebOprtrRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		MozWebOprtrDTO result = mozWebOprtrRepository.findOneByOprtrAccountId(username);

		if (!result.getOprtrStts().equals(OprtrSttsCd.NORMAL.getCode())) {
			throw new UsernameNotFoundException(username);
		}

		AuthenticationEntity authenticationEntity = null;
		if (result != null && result.getOprtrAccountPw() != null) {
			authenticationEntity = new AuthenticationEntity(result);
			authenticationEntity.setOprtrAccountId(result.getOprtrAccountId());
			authenticationEntity.setPassword(result.getOprtrAccountPw());
			authenticationEntity.setOprtrPermission(result.getOprtrPermission());
		} else {
			throw new UsernameNotFoundException(username);
		}

		return authenticationEntity;
	}

}
