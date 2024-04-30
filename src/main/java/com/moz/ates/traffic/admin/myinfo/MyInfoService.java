package com.moz.ates.traffic.admin.myinfo;

import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;

public interface MyInfoService {
	
    /**
     * @brief : 내 정보 조회
     * @details : 내 정보 조회
     * @author : KY.LEE
     * @date : 2023.04.23
     */
    public MozWebOprtr getMyInfoDetail();
    
	/**
	  * @Method Name : verfyPassword
      * @author : KY.LEE
      * @date : 2023.04.23
	  * @Method Brief : 비밀번호 체크
	  * @param MozWebOprtr
	  * @return
	  */
    public boolean verfyPassword(MozWebOprtr mozWebOprtr);

	/**
	  * @Method Name : modifyPassword
      * @author : KY.LEE
      * @date : 2023.04.23
	  * @Method Brief : 비밀번호 변경
	  * @param MozWebOprtr
	  * @return
	  */
	public void modifyPassword(MozWebOprtr mozWebOprtr);

	/**
	  * @Method Name : modifyMyProfile
      * @author : KY.LEE
      * @date : 2023.04.23
	  * @Method Brief : 내 정보 번경
	  * @param MozWebOprtr
	  * @return
	  */
	public void modifyMyProfile(MozWebOprtr mozWebOprtr); 
}
