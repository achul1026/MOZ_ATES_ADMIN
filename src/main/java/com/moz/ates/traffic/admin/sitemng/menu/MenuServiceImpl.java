package com.moz.ates.traffic.admin.sitemng.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.common.entity.menu.MozMenu;
import com.moz.ates.traffic.common.repository.menu.MozMenuRepository;

@Service
public class MenuServiceImpl implements MenuService{

	@Autowired
	MozMenuRepository menuRepository;
	
	@Override
	public List<Map<String, Object>> getMenuList() {
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		
		//대 메뉴 리스트 조회
		List<MozMenu> mainMenuList = menuRepository.selectParentMenuList();
		if(!mainMenuList.isEmpty()) {
			for(int i = 0; i < mainMenuList.size(); i++) {
				Map<String,Object> resultMap = new HashMap<String,Object>();
				MozMenu dbMenu = mainMenuList.get(i);
				String menuId = dbMenu.getMenuId();
				List<MozMenu> subMenuList = menuRepository.selectSubMenuListByMenuId(menuId);
				resultMap.put("mainMenu", mainMenuList.get(i));
				resultMap.put("subMenuList", subMenuList);
				result.add(resultMap);
			}
		}
		return result;
	}

}
