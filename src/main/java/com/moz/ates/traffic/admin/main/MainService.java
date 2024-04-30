package com.moz.ates.traffic.admin.main;

import java.util.List;
import java.util.Map;

import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntMaster;
import com.moz.ates.traffic.common.entity.common.ChartDTO;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfMaster;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;

public interface MainService {

	MozWebOprtr getUserById(MozWebOprtr webOprtr);
	
	/**
	 * @brief 어드민 관리자 회원가입
	 * @author KY.LEE
	 * @Date 2023.08.11
	 * @param webOprtr
	 */
	public void registWebOprtr(MozWebOprtr webOprtr);
	
	/**
	 * @brief 어드민 관리자아이디 중복체크
	 * @author KY.LEE
	 * @Date 2023.08.11
	 * @param dupChkAccountId
	 */
	public boolean dupChkAccountId(MozWebOprtr webOprtr);
	
	/**
	  * @Method Name : findId
	  * @Date : 2024. 2. 15.
	  * @Author : IK.MOON
	  * @Method Brief : 계정 ID 찾기
	  * @param webOprtr
	  * @return
	  */
	public MozWebOprtr findId(MozWebOprtr webOprtr);
	
	/**
	  * @Method Name : findPw
	  * @Date : 2024. 4. 11.
	  * @Author : IK.MOON
	  * @Method Brief : 비밀번호 찾기
	  * @param webOprtr
	  * @return
	  */
	public void findPw(MozWebOprtr webOprtr);
	
	/**
	  * @Method Name : getAffiliationCd
	  * @Date : 2024. 3. 26.
	  * @Author : IK.MOON
	  * @Method Brief : 소속 기관 목록
	  * @param webOprtr
	  * @return List<MozCmCd>
	  */
	public List<MozCmCd> getAffiliationCd(MozCmCd mozCmCd);
	
	/**
	 * @Method Name : getTotalCountAffiliationCd
	 * @Date : 2024. 3. 26.
	 * @Author : IK.MOON
	 * @Method Brief : 소속 기관 카운트조회
	 * @param mozCmCd
	 * @return Long
	 */
	public int getTotalCountAffiliationCd(MozCmCd mozCmCd);
	
	/**
	 * @Method Name : getPaymentStatisticsInfoForDashboard
	 * @Date : 2024. 4. 25.
	 * @Author : NK.KIM
	 * @Method Brief : 대시보드 체납/미납 통계 정보
	 * @return ChartDTO
	 */
	public ChartDTO getStatisticsInfoForDashboard();

	/**
	 * @Method Name : getTodayEnforcementInfo
	 * @Date : 2024. 4. 25.
	 * @Author : KY.LEE
	 * @Method Brief : 대시보드 금일 단속 목록 조회 
	 * @return List<MozTfcEnfMaster>
	 */
	public List<MozTfcEnfMaster> getTodayEnforcementInfo();
	
	/**
	 * @Method Name : getTodayEnforcementCount
	 * @Date : 2024. 4. 25.
	 * @Author : KY.LEE
	 * @Method Brief : 대시보드 금일 단속 목록 카운트
	 */
	public int getTodayEnforcementCount();

	/**
	 * @Method Name : getTodayAccidentInfo
	 * @Date : 2024. 4. 25.
	 * @Author : KY.LEE
	 * @Method Brief : 대시보드 금일 사고 목록
	 * @return List<MozTfcEnfMaster>
	 */
	public List<MozTfcAcdntMaster> getTodayAccidentInfo();

	/**
	 * @Method Name : getTodayAccidentCount
	 * @Date : 2024. 4. 25.
	 * @Author : KY.LEE
	 * @Method Brief : 대시보드 금일 사고 목록 카운트
	 */
	public int getTodayAccidentCount();

	/**
	 * @Method Name : getEqpInfo
	 * @Date : 2024. 4. 25.
	 * @Author : KY.LEE
	 * @Method Brief : 단속장비 정보
	 * @return Map<String,Object>
	 */
	Map<String,Object> getEqpInfo();
}
