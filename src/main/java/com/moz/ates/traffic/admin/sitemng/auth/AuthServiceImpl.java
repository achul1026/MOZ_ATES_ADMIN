package com.moz.ates.traffic.admin.sitemng.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.common.entity.menu.MozAuthMenu;
import com.moz.ates.traffic.common.entity.menu.MozMenu;
import com.moz.ates.traffic.common.entity.operator.MozAuth;
import com.moz.ates.traffic.common.entity.operator.MozAuthDTO;
import com.moz.ates.traffic.common.repository.menu.MozAuthMenuRepository;
import com.moz.ates.traffic.common.repository.operator.MozAuthRepository;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.support.exception.ErrorCode;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	MozAuthRepository mozAuthRepository;

	@Autowired
	MozAuthMenuRepository mozAuthMenuRepository;

	/**
	 * @brief : 권한 리스트 조회
	 * @details : 권한 리스트 조회
	 * @author : KC.KIM
	 * @date : 2024.01.23
	 * @param : mozAuth
	 * @return :
	 */
	@Override
	public List<MozAuth> getAuthList(MozAuth mozAuth) {
		return mozAuthRepository.findAllMozAuth(mozAuth);
	}

	/**
	 * @brief : 권한 리스트 개수 조회
	 * @details : 권한 리스트 개수 조회
	 * @author : KC.KIM
	 * @date : 2024.01.23
	 * @param : mozAuth
	 * @return :
	 */
	@Override
	public int getAuthListCnt(MozAuth mozAuth) {
		return mozAuthRepository.countMozAuth(mozAuth);
	}

	/**
	 * @Method Name : getOneAuth
	 * @Date : 2024. 1. 31.
	 * @Author : IK.MOON
	 * @Method Brief : 권한 단일 조회
	 * @param mozAuth
	 * @return
	 */
	@Override
	public MozAuth getOneAuth(MozAuth mozAuth) {
		return mozAuthRepository.findOneMozAuth(mozAuth);
	}

	/**
	 * @Method Name : registMozAuth
	 * @Date : 2024. 1. 26.
	 * @Author : IK.MOON
	 * @Method Brief : 권한 저장
	 * @param mozAuth
	 * @return
	 */
	@Override
	public void registMozAuth(MozAuth mozAuth) {
		String authId = MozatesCommonUtils.getUuid();
		String crtr = LoginOprtrUtils.getOprtrNm();
		boolean isDashboardVerfied = false;
		
		mozAuth.setAuthId(authId);
		mozAuth.setCrtr(crtr);

		mozAuthRepository.saveMozAuth(mozAuth);

		List<MozAuthMenu> authMenuList = mozAuth.getAuthMenuList();
		for (MozAuthMenu authMenu : authMenuList) {
			String menuUrlPattrn = authMenu.getMenuUrlPattrn();
			// Dashboard인 경우 무조건 Y로 처리 
			if(!MozatesCommonUtils.isNull(menuUrlPattrn) && menuUrlPattrn.equals("/dashboard")) {
				authMenu.setCreateYn("Y");
				authMenu.setReadYn("Y");
				authMenu.setUpdateYn("Y");
				authMenu.setDeleteYn("Y");
				isDashboardVerfied = true;
			}
			authMenu.setAuthMenuId(MozatesCommonUtils.getUuid());
			authMenu.setAuthId(authId);
			authMenu.setCrtr(crtr);
			mozAuthMenuRepository.saveMozAuthMenu(authMenu);
		}
		
		if(!isDashboardVerfied) {
			throw new CommonException(ErrorCode.REQUIRED_FIELDS);
		}
		
	}

	/**
	 * @Method Name : getAuthDetail
	 * @Date : 2024. 2. 2.
	 * @Author : IK.MOON
	 * @Method Brief : 권한 상세 조회
	 * @param mozAuth
	 * @return
	 */
	@Override
	public List<MozAuthDTO> getAuthDetail(MozAuth mozAuth) {
		List<MozAuthDTO> resultList = new ArrayList<>();
		List<MozMenu> menuList = mozAuthMenuRepository.findAllMenuAndAuth(mozAuth);
		if (!MozatesCommonUtils.isNull(menuList)) {
			resultList = menuList.stream().filter(x -> MozatesCommonUtils.isNull(x.getParentMenuId()))
					.map(mozMenu -> new MozAuthDTO(mozMenu)).collect(Collectors.toList());

			if (!MozatesCommonUtils.isNull(resultList)) {
				for (MozAuthDTO mozSideMenuDTO : resultList) {
					List<MozMenu> subMenuInfoList = menuList.stream()
							.filter(x -> mozSideMenuDTO.getMenuId().equals(x.getParentMenuId()))
							.collect(Collectors.toList());
					mozSideMenuDTO.setSubMenuInfoList(subMenuInfoList);
				}
			}
		}
		return resultList;
	}

	/**
	 * @Method Name : authUpdate
	 * @Date : 2024. 2. 2.
	 * @Author : IK.MOON
	 * @Method Brief : 권한, 메뉴권한 수정
	 * @param mozAuth
	 */
	@Override
	public void authUpdate(MozAuth mozAuth) {
		boolean isDashboardVerfied = false;
		
		mozAuthRepository.updateMozAuth(mozAuth);

		List<MozAuthMenu> authMenuList = mozAuth.getAuthMenuList();

		if (authMenuList.size() > 0) {
			for (MozAuthMenu authMenu : mozAuth.getAuthMenuList()) {
				String menuUrlPattrn = authMenu.getMenuUrlPattrn();
				// Dashboard인 경우 무조건 Y로 처리 
				if(!MozatesCommonUtils.isNull(menuUrlPattrn) && menuUrlPattrn.equals("/dashboard")) {
					authMenu.setCreateYn("Y");
					authMenu.setReadYn("Y");
					authMenu.setUpdateYn("Y");
					authMenu.setDeleteYn("Y");
					isDashboardVerfied = true;
				}
				mozAuthMenuRepository.updateMozAuthMenu(authMenu);
			}
		}
		
		if(!isDashboardVerfied) {
			throw new CommonException(ErrorCode.REQUIRED_FIELDS);
		}

	}

}
