package com.moz.ates.traffic.admin.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.moz.ates.traffic.common.entity.operator.MozWebOprtrDTO;
import com.moz.ates.traffic.common.support.exception.ErrorCode;
import com.moz.ates.traffic.common.support.exception.NoLoginException;

/**
 * @FileName : LoginSessionUtils.java
 * @Project : moz_ates_common
 * @Date : 2024. 1. 24.
 * @Author : IK.MOON
 * @Brief : 로그인 세션 관리
 */
public class LoginOprtrUtils {


	/**
	  * @Method Name : getSessionRequest
	  * @Date : 2024. 1. 25.
	  * @Author : IK.MOON
	  * @Method Brief : 세션 값 조회
	  * @return
	  */
	public static HttpServletRequest getSessionRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	
	/**
	 * @Method Name : getSession
	 * @Date : 2024. 1. 25.
	 * @Author : IK.MOON
	 * @Method Brief : 세션 값 조회
	 * @return
	 */
	public static HttpSession getSession() {
		return getSessionRequest().getSession();
	}
	
	/**
	  * @Method Name : getMozWebOprtr
	  * @Date : 2024. 1. 25.
	  * @Author : IK.MOON
	  * @Method Brief : 로그인 사용자 DTO객체 가져오기
	  * @return
	  */
	public static MozWebOprtrDTO getMozWebOprtr() {
		MozWebOprtrDTO mozWebOprtrDTO = null;
		// 현재 인증된 사용자의 Authentication 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			mozWebOprtrDTO = (MozWebOprtrDTO) authentication.getPrincipal();
		} else {
			 throw new NoLoginException(ErrorCode.SESSION_NOT_FOUND);
		}
		return mozWebOprtrDTO;
	}
	
	/**
	  * @Method Name : getOprtrId
	  * @Date : 2024. 2. 1.
	  * @Author : IK.MOON
	  * @Method Brief : 로그인 사용자 고유번호(PK) 가져오기
	  * @return
	  */
	public static String getOprtrId() {
		if(getMozWebOprtr() != null) {
			return getMozWebOprtr().getOprtrId();
		} else {
			throw new NoLoginException(ErrorCode.SESSION_NOT_FOUND);
		}
	}
	
	/**
	 * @Method Name : getOprtrPermission
	 * @Date : 2024. 2. 1.
	 * @Author : IK.MOON
	 * @Method Brief : 로그인 사용자 권한 조회
	 * @return
	 */
	public static String getOprtrPermission() {
		if(getMozWebOprtr() != null) {
			return getMozWebOprtr().getOprtrPermission();
		} else {
			throw new NoLoginException(ErrorCode.SESSION_NOT_FOUND);
		}
	}
	
	/**
	  * @Method Name : getAccountId
	  * @Date : 2024. 1. 25.
	  * @Author : IK.MOON
	  * @Method Brief : 로그인 사용자 계정 ID 가져오기
	  * @return
	  */
	public static String getAccountId() {
		if(getMozWebOprtr() != null) {
			return getMozWebOprtr().getOprtrAccountId();
		} else {
			throw new NoLoginException(ErrorCode.SESSION_NOT_FOUND);
		}
	}
	
	/**
	 * @Method Name : getOprtrNm
	 * @Date : 2024. 1. 25.
	 * @Author : IK.MOON
	 * @Method Brief : 로그인 사용자 이름 가져오기
	 * @return
	 */
	public static String getOprtrNm() {
		if(getMozWebOprtr() != null) {
			return getMozWebOprtr().getOprtrNm();
		} else {
			throw new NoLoginException(ErrorCode.SESSION_NOT_FOUND);
		}
	}
	
	/**
	  * @Method Name : getTmpPwIssuedYn
	  * @Date : 2024. 4. 12.
	  * @Author : IK.MOON
	  * @Method Brief : 임시 비밀번호 발급여부 조회
	  * @return
	  */
	public static String getTmpPwIssuedYn() {
		if(getMozWebOprtr() != null) {
			return getMozWebOprtr().getTmpPwIssuedYn();
		} else {
			throw new NoLoginException(ErrorCode.SESSION_NOT_FOUND);
		}
	}
 	
	/**
	 * @Method Name : getUserIpAddr
	 * @작성일 : 2023. 9. 25.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 현재 세션에 접속한 유저의 IP를 가져오는 메소드
	 * @return String
	 */
	public static String getUserIpAddr() {
		String ip = null;
		HttpServletRequest request = getSessionRequest();
		
		ip = request.getHeader("X-Forwarded-For");
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-Real-IP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-RealIP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }
        
		return ip;
	}
}
