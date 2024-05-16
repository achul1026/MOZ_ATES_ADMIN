package com.moz.ates.traffic.admin.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.common.repository.operator.MozWebOprtrRepository;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	MozWebOprtrRepository mozWebOprtrRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		// 성공시 실패한 세션 정리
		authenticationAttributes(request);

		// 로그인 성공시 보내는 주소
		String redirectUrl = "/dashboard";
		if (LoginOprtrUtils.getTmpPwIssuedYn().equals("Y")) {
			request.getSession().setAttribute("isPwChk", true);
			redirectUrl = "/myinfo/changePw.do";
		}
		getRedirectStrategy().sendRedirect(request, response, redirectUrl);

	}

	protected void authenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null)
			return;
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

}
