package com.moz.ates.traffic.admin.sitemng.auth;

import java.util.List;

import com.moz.ates.traffic.common.entity.operator.MozAuth;

public interface AuthService {

	public List<MozAuth> getAuthList(MozAuth mozAuth);
	
	public String registMozAuth(MozAuth mozAuth);
	
}
