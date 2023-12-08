package com.moz.ates.traffic.admin.main;

import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;

public interface MainService {

	MozWebOprtr getUserById(MozWebOprtr webOprtr);
	
	/**
	 * @briedf 어드미 관리자 회원가입
	 * @author KY.LEE
	 * @Date 2023.08.11
	 * @param webOprtr
	 */
	void registWebOprtr(MozWebOprtr webOprtr);
	
	/**
	 * @briedf 어드민 관리자
	 * @author KY.LEE
	 * @Date 2023.08.11
	 * @param webOprtr
	 */
	boolean dupChkUserEmail(MozWebOprtr webOprtr);
}
