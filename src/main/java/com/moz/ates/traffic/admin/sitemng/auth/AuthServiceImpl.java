package com.moz.ates.traffic.admin.sitemng.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.common.entity.operator.MozAuth;
import com.moz.ates.traffic.common.repository.operator.MozAuthRepository;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class AuthServiceImpl implements AuthService{
	
	@Autowired
	MozAuthRepository mozAuthRepository;

	@Override
	public List<MozAuth> getAuthList(MozAuth mozAuth) {
		return mozAuthRepository.findAllMozAuth(mozAuth);
	}

	@Override
	public String registMozAuth(MozAuth mozAuth) {
		String authId = MozatesCommonUtils.getUuid();
		mozAuth.setAuthId(authId);
		mozAuthRepository.insert(mozAuth);
		return authId;
	}

}
