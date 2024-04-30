package com.moz.ates.traffic.admin.config;


import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import com.moz.ates.traffic.admin.common.enums.UrlType;
import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.admin.sitemng.menu.MenuService;
import com.moz.ates.traffic.admin.user.UserService;
import com.moz.ates.traffic.common.entity.menu.MozMenuRouteDTO;
import com.moz.ates.traffic.common.entity.menu.MozSideMenuDTO;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtrDTO;
import com.moz.ates.traffic.common.repository.menu.MozMenuRepository;
import com.moz.ates.traffic.common.repository.operator.MozWebOprtrRepository;
import com.moz.ates.traffic.common.support.exception.NoLoginException;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MenuInterceptor implements HandlerInterceptor {

	@Autowired
	MozWebOprtrRepository mozWebOprtrRepository;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	MozMenuRepository menuRepository;
	
	@Autowired
	UserService userService;
	
    /**
     * @brief 사이드메뉴 설정
     * @return  boolean
     * @throws Exception
     */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response , Object handler ) throws Exception {
		log.info("==============Menu Interceptor preHandle Start ===============");
		log.info("Request Url : {}", request.getRequestURL());
		
		MozWebOprtrDTO mozWebOprtrDTO = LoginOprtrUtils.getMozWebOprtr();
		//세션 정보 못가져올때
		if(mozWebOprtrDTO == null) {
			throw new NoLoginException();
		}
		
		MozWebOprtr dbMozWebOprtr = userService.getUserDetail(mozWebOprtrDTO.getOprtrId());
		mozWebOprtrDTO.setAuthId(dbMozWebOprtr.getAuthId());
		
		List<MozSideMenuDTO> data = menuService.getSidebarAuthMenuList(mozWebOprtrDTO); 
		
		//쿠키값 비교 다국어 처리
		Cookie[] cookies = request.getCookies();
		String lang = "eng";
		for(Cookie cookie : cookies) {
			if("lang".equals(cookie.getName())) {
				lang = cookie.getValue();
			}
		}
		
		//메뉴 경로 세팅
		//페이지 이동 하는 화면에서만 체크(GET방식)
		if("GET".equals(request.getMethod())) {
			MozMenuRouteDTO menuRouteInfo = new MozMenuRouteDTO();
			menuRouteInfo.setLang(lang);
			String currentUrl = request.getRequestURI();
			
			menuRouteInfo.setMenuUrlPattrn(MozatesCommonUtils.getUrlPattrn(request.getRequestURI(), '/'));
			//URL PATH DB데이터 조회
			menuRouteInfo = menuRepository.selectMenuRouteInfo(menuRouteInfo);

			//데이터가 존재 하는 경우에만
			if(menuRouteInfo != null && !MozatesCommonUtils.isNull(menuRouteInfo.getMenuId())){
				//3 DEPTH MENU 일 경우
				if(!menuRouteInfo.getSubMenuUrl().equals(currentUrl)){
					menuRouteInfo.setCurrentMenuUrl(currentUrl);
					
					String currentMenuNm = UrlType.getCodeByName(lang, MozatesCommonUtils.getUrlType(currentUrl, '/'));
					menuRouteInfo.setMenuActiveLocation("THREE");
					menuRouteInfo.setCurrentMenuNm(currentMenuNm);
				}
				request.setAttribute("menuRouteInfo", menuRouteInfo);
			}
		}
		
		request.setAttribute("lang", lang);
		request.setAttribute("sideMenuList", data);
		request.setAttribute("webOprtrInfo", mozWebOprtrDTO);
		
		return true;
	}
}
