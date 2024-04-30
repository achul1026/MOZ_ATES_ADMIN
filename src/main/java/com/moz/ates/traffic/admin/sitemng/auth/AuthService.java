package com.moz.ates.traffic.admin.sitemng.auth;

import java.util.List;

import com.moz.ates.traffic.common.entity.operator.MozAuth;
import com.moz.ates.traffic.common.entity.operator.MozAuthDTO;

public interface AuthService {
	
    /**
     * @brief : 권한 리스트 조회
     * @details : 권한 리스트 조회
     * @author : KC.KIM
     * @date : 2024.01.23
     * @param : mozAuth
     * @return : 
     */
	public List<MozAuth> getAuthList(MozAuth mozAuth);
	
    /**
     * @brief : 권한 리스트 개수 조회
     * @details : 권한 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.01.23
     * @param : mozAuth
     * @return : 
     */
	public int getAuthListCnt(MozAuth mozAuth);

	/**
	  * @Method Name : getOneAuth
	  * @Date : 2024. 1. 31.
	  * @Author : IK.MOON
	  * @Method Brief : 권한 단일 조회
	  * @param mozAuth
	  * @return
	  */
	public MozAuth getOneAuth(MozAuth mozAuth);
	
	/**
	  * @Method Name : registMozAuth
	  * @Date : 2024. 1. 26.
	  * @Author : IK.MOON
	  * @Method Brief : 권한 저장
	  * @param mozAuth
	  */
	public void registMozAuth(MozAuth mozAuth);

	/**
	  * @Method Name : getAuthDetail
	  * @Date : 2024. 1. 31.
	  * @Author : IK.MOON
	  * @Method Brief : 권한 상세 조회
	  * @param mozAuth
	  * @return
	  */
	public List<MozAuthDTO> getAuthDetail(MozAuth mozAuth);
	
	/**
	  * @Method Name : authUpdate
	  * @Date : 2024. 2. 2.
	  * @Author : IK.MOON
	  * @Method Brief : 권한, 메뉴권한 수정
	  * @param mozAuth
	  */
	public void authUpdate(MozAuth mozAuth);
	
}
