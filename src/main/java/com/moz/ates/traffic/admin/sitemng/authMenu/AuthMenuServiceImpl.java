package com.moz.ates.traffic.admin.sitemng.authMenu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.common.entity.menu.MozAuthMenu;
import com.moz.ates.traffic.common.repository.menu.MozAuthMenuRepository;

@Service
public class AuthMenuServiceImpl implements AuthMenuService {
	
	@Autowired
	MozAuthMenuRepository mozAuthMenuRepository;
	
	@Override
	public void registAuthMenu(List<MozAuthMenu> authMenuList) {
		
	}

}
