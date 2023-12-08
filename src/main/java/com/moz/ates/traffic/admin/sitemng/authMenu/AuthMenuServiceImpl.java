package com.moz.ates.traffic.admin.sitemng.authMenu;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.common.entity.menu.MozAuthMenu;
import com.moz.ates.traffic.common.repository.menu.MozAuthMenuRepository;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class AuthMenuServiceImpl implements AuthMenuService {
	
	@Autowired
	MozAuthMenuRepository mozAuthMenuRepository;
	
	@Override
	public void registAuthMenu(List<Map<String, Object>> authMenuList, String authId) {
		for(int i = 0; i < authMenuList.size(); i++) {
			//신규 객체 생성
			MozAuthMenu parMozAuthMenu = new MozAuthMenu();
			
			String parentMenuId = (String) authMenuList.get(i).get("parentMenuId");
			String parCreateYn = (String) authMenuList.get(i).get("parCreateYn");
			String parReadYn = (String) authMenuList.get(i).get("parReadYn");
			String parUpdateYn = (String) authMenuList.get(i).get("parUpdateYn");
			String parDeleteYn = (String) authMenuList.get(i).get("parDeleteYn");
			List<Map<String,Object>> childMenuList = (List<Map<String, Object>>) authMenuList.get(i).get("childMenuArr");
			
			parMozAuthMenu.setAuthId(authId);
			parMozAuthMenu.setMenuId(parentMenuId);
			parMozAuthMenu.setAuthMenuId(MozatesCommonUtils.getUuid());
			parMozAuthMenu.setCreateYn(parCreateYn);
			parMozAuthMenu.setReadYn(parReadYn);
			parMozAuthMenu.setUpdateYn(parUpdateYn);
			parMozAuthMenu.setDeleteYn(parDeleteYn);
			mozAuthMenuRepository.saveMozAuthMenu(parMozAuthMenu);
			
			if(!childMenuList.isEmpty()) {
				for(int j = 0; j < childMenuList.size(); j++) {
					MozAuthMenu childMozAuthMenu = new MozAuthMenu();
					
					String childMenuId = (String) childMenuList.get(j).get("childMenuId");
					String childCreateYn = (String) childMenuList.get(j).get("childCreateYn");
					String childReadYn = (String) childMenuList.get(j).get("childReadYn");
					String childUpdateYn = (String) childMenuList.get(j).get("childUpdateYn");
					String childDeleteYn = (String) childMenuList.get(j).get("childDeleteYn");
					
					childMozAuthMenu.setAuthId(authId);
					childMozAuthMenu.setMenuId(childMenuId);
					childMozAuthMenu.setAuthMenuId(MozatesCommonUtils.getUuid());
					childMozAuthMenu.setCreateYn(childCreateYn);
					childMozAuthMenu.setReadYn(childReadYn);
					childMozAuthMenu.setUpdateYn(childUpdateYn);
					childMozAuthMenu.setDeleteYn(childDeleteYn);
					mozAuthMenuRepository.saveMozAuthMenu(childMozAuthMenu);
				}
			}
		}
	}
}
