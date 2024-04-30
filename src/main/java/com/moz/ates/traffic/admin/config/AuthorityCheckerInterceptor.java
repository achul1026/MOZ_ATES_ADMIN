package com.moz.ates.traffic.admin.config;


import com.moz.ates.traffic.admin.common.enums.OprtrSttsCd;
import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.admin.user.UserService;
import com.moz.ates.traffic.common.entity.menu.MozAuthMenu;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtrDTO;
import com.moz.ates.traffic.common.repository.menu.MozAuthMenuRepository;
import com.moz.ates.traffic.common.support.exception.ErrorCode;
import com.moz.ates.traffic.common.support.exception.NoLoginException;
import com.moz.ates.traffic.common.support.exception.NotPermissionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Configuration
public class AuthorityCheckerInterceptor implements HandlerInterceptor {
	
	@Autowired
	MozAuthMenuRepository mozAuthMenuRepository;
	
	@Autowired
	UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response , Object handler ) throws Exception {
		
		MozWebOprtrDTO mozWebOprtrDTO = LoginOprtrUtils.getMozWebOprtr();
			
		//세션 정보 못가져올때
		if(mozWebOprtrDTO == null) {
			throw new NoLoginException();
		}
		
		MozWebOprtr dbMozWebOprtr = userService.getUserDetail(mozWebOprtrDTO.getOprtrId());
		//탈퇴한 계정일 경우
		if (dbMozWebOprtr.getOprtrStts().equals(OprtrSttsCd.WITHDRAW.getCode())) {
			// 임시 예외처리
			throw new NotPermissionException();
		}
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Authority auth = handlerMethod.getMethodAnnotation(Authority.class);

			if(auth != null) {
				MozAuthMenu mozAuthMenu = null;
				String url = request.getRequestURI().toString();

				//메뉴 사용 여부 조회
				switch(auth.type()) {
				case CREATE:
					mozAuthMenu = new MozAuthMenu(mozWebOprtrDTO.getAuthId(), "Y", null, null, null);
					break;
				case READ:
					mozAuthMenu = new MozAuthMenu(mozWebOprtrDTO.getAuthId(), null, "Y", null, null);
					break;
				case UPDATE:
					mozAuthMenu = new MozAuthMenu(mozWebOprtrDTO.getAuthId(), null, null, "Y", null);
					break;
				case DELETE:
					mozAuthMenu = new MozAuthMenu(mozWebOprtrDTO.getAuthId(), null, null, null, "Y");
					break;
				default:
					break;
				}

				List<MozAuthMenu> authMenuList = mozAuthMenuRepository.findMozAuthMenuAndUrlPatternBAnyYn(mozAuthMenu);

				AntPathMatcher pathMatcher = new AntPathMatcher();

				Optional<MozAuthMenu> currentMenuAuthInfo = authMenuList.stream().filter(x -> pathMatcher.match(x.getMenuUrlPattrn(), url)).findFirst();
				if(!currentMenuAuthInfo.isPresent()) {
					throw new NotPermissionException(ErrorCode.PERMISSION_DENIED);
				}

				request.setAttribute("currentMenuAuthInfo", currentMenuAuthInfo.get());
			} else {
				throw new NotPermissionException(ErrorCode.PERMISSION_DENIED);
			}
		}
	    
		return true;
	}
}
