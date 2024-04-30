package com.moz.ates.traffic.admin.sitemng.menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.common.entity.menu.MozAuthMenu;
import com.moz.ates.traffic.common.entity.menu.MozMenu;
import com.moz.ates.traffic.common.entity.menu.MozSideMenuDTO;
import com.moz.ates.traffic.common.entity.operator.MozAuth;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtrDTO;
import com.moz.ates.traffic.common.repository.menu.MozAuthMenuRepository;
import com.moz.ates.traffic.common.repository.menu.MozMenuRepository;
import com.moz.ates.traffic.common.repository.operator.MozAuthRepository;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.support.exception.ErrorCode;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	MozMenuRepository menuRepository;

	@Autowired
	MozAuthRepository mozAuthRepository;

	@Autowired
	MozAuthMenuRepository mozAuthMenuRepository;
	
	/**
	 * @Method Name : getMainMenuList
	 * @Date : 2024. 1. 11.
	 * @Author : IK.MOON
	 * @Method Brief : 대메뉴 리스트 조회
	 * @return
	 */
	@Override
	public List<MozMenu> getMainMenuList() {
		return menuRepository.findAllParentMenu();
	}

	/**
	 * @Method Name : getMenu
	 * @Date : 2024. 1. 15.
	 * @Author : IK.MOON
	 * @Method Brief : 메뉴 조회
	 * @param menuId
	 * @return
	 */
	@Override
	public MozMenu getMenu(String menuId) {
		return menuRepository.findOneByMenuId(menuId);
	}

	/**
	 * @Method Name : getSubMenuList
	 * @Date : 2024. 1. 15.
	 * @Author : IK.MOON
	 * @Method Brief : 서브 메뉴 전체 조회
	 * @param menuId
	 * @return
	 */
	@Override
	public List<MozMenu> getSubMenuList(String menuId) {
		return menuRepository.findAllSubMenuByParentMenuId(menuId);
	}

	/**
	 * @Method Name : saveMenu
	 * @Date : 2024. 1. 22.
	 * @Author : IK.MOON
	 * @Method Brief : 메뉴 신규 저장
	 * @param mozMenu
	 */
	@Override
	public void saveMenu(MozMenu mozMenu) throws SQLException {
		
		String menuId = MozatesCommonUtils.getUuid();
		String crtr = LoginOprtrUtils.getOprtrId();
		
		// 메뉴 저장
		mozMenu.setCrtr(crtr);
		mozMenu.setMenuId(menuId);
		menuRepository.saveMenu(mozMenu);
		
		// 메뉴-권한 저장
		MozAuthMenu mozAuthMenu = new MozAuthMenu();
		mozAuthMenu.setMenuId(menuId);
		mozAuthMenu.setCrtr(crtr);
		
		MozAuth mozAuth = new MozAuth();
		List<MozAuth> dbMozAuthList = mozAuthRepository.findAllMozAuth(mozAuth);
		for(MozAuth dbMozAuth : dbMozAuthList) {
			mozAuthMenu.setAuthMenuId(MozatesCommonUtils.getUuid());
			mozAuthMenu.setAuthId(dbMozAuth.getAuthId());
			
			mozAuthMenuRepository.saveMozAuthMenu(mozAuthMenu);
		}

	}

	/**
	 * @Method Name : deleteMenu
	 * @Date : 2024. 1. 22.
	 * @Author : IK.MOON
	 * @Method Brief : 메뉴 삭제
	 * @param mozMenu
	 * @throws SQLException
	 */
	@Override
	public void deleteMenu(MozMenu mozMenu){
		String parentMenuId = menuRepository.findOneByMenuId(mozMenu.getMenuId()).getParentMenuId();

		// 대메뉴 삭제시 서브메뉴 삭제
		if (MozatesCommonUtils.isNull(parentMenuId)) {
			List<MozMenu> subMenuList = menuRepository.findAllSubMenuByParentMenuId(mozMenu.getMenuId());

			// 서브 메뉴 메뉴-권한 삭제
			MozAuthMenu authSubMenuToDelete = new MozAuthMenu();

			for (MozMenu subMenu : subMenuList) {
				authSubMenuToDelete.setMenuId(subMenu.getMenuId());
				List<MozAuthMenu> mozAuthSubMenuList =
						mozAuthMenuRepository.findAllByMenuId(authSubMenuToDelete);
				if (mozAuthSubMenuList.size() > 0) {
					for (MozAuthMenu mozAuthMenu : mozAuthSubMenuList) {
						mozAuthMenuRepository.deleteAuthMenu(mozAuthMenu);
					}
				}
			}

			// 서브 메뉴 삭제
			for (MozMenu subMenu : subMenuList) {
				menuRepository.deleteMenu(subMenu);
			}
		}

		// 메뉴 메뉴-권한 삭제
		MozAuthMenu authMenuToDelete = new MozAuthMenu();
		authMenuToDelete.setMenuId(mozMenu.getMenuId());
		List<MozAuthMenu> mozAuthMenuList = mozAuthMenuRepository.findAllByMenuId(authMenuToDelete);

		if (mozAuthMenuList.size() > 0) {
			for (MozAuthMenu mozAuthMenu : mozAuthMenuList) {
				mozAuthMenuRepository.deleteAuthMenu(mozAuthMenu);
			}
		}

		// 메뉴 삭제
		menuRepository.deleteMenu(mozMenu);

	}

	/**
	 * @Method Name : updateMenu
	 * @Date : 2024. 1. 24.
	 * @Author : IK.MOON
	 * @Method Brief : 메뉴 수정
	 * @param mozMenu
	 * @throws SQLException
	 */
	@Override
	public void updateMenu(MozMenu mozMenu) throws SQLException {
		
		if(MozatesCommonUtils.isNull(mozMenu)) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		
		String parentMenuId = menuRepository.findOneByMenuId(mozMenu.getMenuId()).getParentMenuId();
	
		// 메인메뉴 mainSortNo, menuAbvr 수정된 경우 확인
		if (MozatesCommonUtils.isNull(parentMenuId)) {
			MozMenu mainMenuDb = menuRepository.findOneByMenuId(mozMenu.getMenuId());
			boolean isSubMenUpdateRequired = false;
			
			int dbMainSortNo = mainMenuDb.getMainSortNo();
			int mainSortNo = mozMenu.getMainSortNo();
			
			String dbMenuAbv = mainMenuDb.getMenuAbv();
			String menuAbv = mainMenuDb.getMenuAbv();

			List<MozMenu> subMenuList =
					menuRepository.findAllSubMenuByParentMenuId(mozMenu.getMenuId());
			
			// mainSortNo 수정
			if (dbMainSortNo != mainSortNo) {
				isSubMenUpdateRequired = true;
				for (MozMenu subMenu : subMenuList) {
					subMenu.setMainSortNo(mainSortNo);
				}
			}
			
			// menuAbv 수정
			if (!menuAbv.equals(dbMenuAbv)) {
				isSubMenUpdateRequired = true;
				for (MozMenu subMenu : subMenuList) {
					subMenu.setMenuAbv(menuAbv);
				}
			}
			
			if (isSubMenUpdateRequired) {
				// 서브메뉴 업데이트
				for (MozMenu subMenu : subMenuList) {
					subMenu.setMainSortNo(mainSortNo);
					
					menuRepository.updateMenu(subMenu);
					
				}
			}
		}

		menuRepository.updateMenu(mozMenu);

	}
	
	/**
	  * @Method Name : getSidebarAuthMenuList
	  * @Date : 2024. 1. 24.
	  * @Author : IK.MOON
	  * @Method Brief : 사이드 메뉴 목록 조회
	  * @param MozWebOprtr
	  */
	@Override
	public List<MozSideMenuDTO> getSidebarAuthMenuList(MozWebOprtrDTO mozWebOprtrDTO) {
		List<MozSideMenuDTO> resultList = new ArrayList<>();
		List<MozMenu> sideMenuList = menuRepository.selectSidebarAuthMenuList(mozWebOprtrDTO);
		if(!MozatesCommonUtils.isNull(sideMenuList)) {
			resultList = sideMenuList.stream()
										.filter(x -> MozatesCommonUtils.isNull(x.getParentMenuId()))
										.map(mozMenu -> new MozSideMenuDTO(mozMenu)).collect(Collectors.toList());
			if(!MozatesCommonUtils.isNull(resultList)) {
				for(MozSideMenuDTO mozSideMenuDTO : resultList) {
					List<MozMenu> subMenuInfoList = sideMenuList.stream()
																.filter(x -> mozSideMenuDTO.getMenuId().equals(x.getParentMenuId()))
																.collect(Collectors.toList());
					mozSideMenuDTO.setSubMenuInfoList(subMenuInfoList);
					
				}
			}
		}
		return resultList;
	}

}
