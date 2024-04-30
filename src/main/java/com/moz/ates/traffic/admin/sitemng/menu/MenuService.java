package com.moz.ates.traffic.admin.sitemng.menu;

import java.sql.SQLException;
import java.util.List;

import com.moz.ates.traffic.common.entity.menu.MozMenu;
import com.moz.ates.traffic.common.entity.menu.MozSideMenuDTO;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtrDTO;

public interface MenuService {
	
	/**
	  * @Method Name : getMainMenuList
	  * @Date : 2024. 1. 11.
	  * @Author : IK.MOON
	  * @Method Brief : 대메뉴 리스트 조회
	  * @return
	  */
	public List<MozMenu> getMainMenuList();
	
	/**
	  * @Method Name : getMenu
	  * @Date : 2024. 1. 24.
	  * @Author : IK.MOON
	  * @Method Brief : 메뉴 단일 조회
	  * @param menuId
	  * @return
	  */
	public MozMenu getMenu(String menuId);
	
	/**
	  * @Method Name : getSubMenuList
	  * @Date : 2024. 1. 24.
	  * @Author : IK.MOON
	  * @Method Brief : 서브메뉴 전체 조회
	  * @param menuId
	  * @return
	  */
	public List<MozMenu> getSubMenuList(String menuId);
	
	/**
	  * @Method Name : saveMenu
	  * @Date : 2024. 1. 22.
	  * @Author : IK.MOON
	  * @Method Brief : 메뉴 신규 저장
	  * @param mozMenu
	  */
	public void saveMenu(MozMenu mozMenu) throws SQLException;
	
	/**
	  * @Method Name : deleteMenu
	  * @Date : 2024. 1. 22.
	  * @Author : IK.MOON
	  * @Method Brief : 메뉴 삭제
	  * @param mozMenu
	  * @throws SQLException
	  */
	public void deleteMenu(MozMenu mozMenu);
	
	/**
	  * @Method Name : updateMenu
	  * @Date : 2024. 1. 24.
	  * @Author : IK.MOON
	  * @Method Brief : 메뉴 수정
	  * @param mozMenu
	  * @throws SQLException
	  */
	public void updateMenu(MozMenu mozMenu) throws SQLException;
	
	/**
	  * @Method Name : getSidebarAuthMenuList
	  * @Date : 2024. 1. 24.
	  * @Author : IK.MOON
	  * @Method Brief : 사이드 메뉴 목록 조회
	  * @param MozWebOprtr
	  */
	public List<MozSideMenuDTO> getSidebarAuthMenuList(MozWebOprtrDTO mozWebOprtrDTO);
}
