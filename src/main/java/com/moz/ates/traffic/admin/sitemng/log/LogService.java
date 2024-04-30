package com.moz.ates.traffic.admin.sitemng.log;

import java.util.List;
import java.util.Map;

import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntChgHst;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfHst;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpLog;
import com.moz.ates.traffic.common.entity.log.MozTfcClctnFlrLog;
import com.moz.ates.traffic.common.entity.log.MozTfcSystmErrLog;
import com.moz.ates.traffic.common.entity.log.MozTfcUserLog;
import com.moz.ates.traffic.common.entity.operator.MozOprtrAudLog;
import com.moz.ates.traffic.common.entity.operator.MozPolAudLog;

public interface LogService {
	
	/**
     * @brief : 사용자 로그 리스트 개수 조회
     * @details : 사용자 로그 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : mozTfcUserLog
     * @return : 
     */
	public int getUserLogListCnt(MozTfcUserLog mozTfcUserLog);

	/**
     * @brief : 사용자 로그 리스트 조회
     * @details : 사용자 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : mozTfcUserLog
     * @return : 
     */
	public List<MozTfcUserLog> getUserLogList(MozTfcUserLog mozTfcUserLog);

	/**
     * @brief : 사용자 로그 상세 조회
     * @details : 사용자 로그 상세 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : logId
     * @return : 
     */
	public Map<String, Object> getUserLogDetail(String logId);
	
	/**
     * @brief : 시스템 장애 로그 리스트 개수 조회
     * @details : 시스템 장애 로그 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : mozTfcSystmErrLog
     * @return : 
     */
	public int getSystmErrLogListCnt(MozTfcSystmErrLog mozTfcSystmErrLog);

	/**
     * @brief : 시스템 장애 로그 리스트 조회
     * @details : 시스템 장애 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : mozTfcSystmErrLog
     * @return : 
     */
	public List<MozTfcSystmErrLog> getSystmErrLogList(MozTfcSystmErrLog mozTfcSystmErrLog);

	/**
     * @brief : 시스템 장애 로그 상세 조회
     * @details : 시스템 장애 로그 상세 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : logId
     * @return : 
     */
	public Map<String, Object> getSystmErrLogDetail(String logId);
	
	/**
     * @brief : 수집실패 로그 리스트 개수 조회
     * @details : 수집실패 로그 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : mozTfcClctnFlrLog
     * @return : 
     */
	public int getClctnFlrLogListCnt(MozTfcClctnFlrLog mozTfcClctnFlrLog);

	/**
     * @brief : 수집실패 로그 리스트 조회
     * @details : 수집실패 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : mozTfcClctnFlrLog
     * @return : 
     */
	public List<MozTfcClctnFlrLog> getClctnFlrLogList(MozTfcClctnFlrLog mozTfcClctnFlrLog);

	/**
     * @brief : 수집실패 로그 상세 조회
     * @details : 수집실패 로그 상세 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : logId
     * @return : 
     */
	public Map<String, Object> getClctnFlrLog(String logId);
	
	/**
	  * @param oprtrId 
	  * @Method Name : getOprtrAudLogByOprtrId
	  * @작성일 : 2024. 2. 19.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 관리자 로그 목록 조회
	  * @return
	  */
	List<MozOprtrAudLog> getOprtrAudLogByOprtrId(String oprtrId);

	/**
	  * @Method Name : getAudLogListCnt
	  * @작성일 : 2024. 2. 19.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 관리자 로그 목록 갯수 조회
	  * @param oprtrAudLog
	  * @return
	  */
	public int getMozOprtrAudLogListCnt(MozOprtrAudLog oprtrAudLog);
	
	/**
     * @brief : 사용자 로그 등록
     * @details : 사용자 로그 등록
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : logCateCd
     * @param : logCn
     * @param : logRslt
     * @return : 
     */
	public void insertUserLog(String logCateCd, String logCntn, String logRslt);

	/**
	  * @Method Name : getMozPolAudLogListCnt
	  * @작성일 : 2024. 2. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 경찰 로그 목록 개수 조회
	  * @param polAudLog
	  * @return
	  */
	public int getMozPolAudLogListCnt(MozPolAudLog polAudLog);

	/**
	  * @Method Name : getPolAudLogByOprtrId
	  * @작성일 : 2024. 2. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 경찰 로그 목록 조회
	  * @param polId
	  * @return
	  */
	public List<MozPolAudLog> getPolAudLogByPolId(String polId);
	
	/**
	 * @brief : 교통단속 로그 리스트 조회
	 * @details : 교통단속 로그 리스트 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfHst
	 * @return :
	 */
	public List<MozTfcEnfHst> getTfcLogList(MozTfcEnfHst tfcEnfHst);
	
	/**
	 * @brief 교통단속로그
	 * @author KY.LEE
	 * @date 2024. 4. 25.
	 * @method getTfcLogListCnt
	 */
	public int getTfcLogListCnt(MozTfcEnfHst tfcEnfHst);
	
	/**
     * @brief : 교통단속 로그 상세 조회
     * @details : 교통단속 로그 상세 조회
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : hstId
     * @return : MozTfcEnfHst
     */
	public MozTfcEnfHst getTfcLogDetail(String hstId);
	
    /**
     * @brief : 교통사고 로그 리스트 조회
     * @details : 교통사고 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcAcdntChgHst
     * @return : 
     */
	public List<MozTfcAcdntChgHst> getAcdntLogList(MozTfcAcdntChgHst tfcAcdntChgHst);

    /**
     * @brief : 교통사고 로그 리스트 개수 조회
     * @details : 교통사고 로그 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcAcdntChgHst
     * @return : 
     */
	public int getAcdntLogListCnt(MozTfcAcdntChgHst tfcAcdntChgHst);
	
	/**
	 * @brief : 교통사고 로그 상세 화면
	 * @details : 교통사고 로그 상세 화면
	 * @author : KC.KIM
	 * @date : 2024.01.31
	 * @param : hstId
	 * @return :
	 */
	public MozTfcAcdntChgHst getAcdntLogDetail(String hstId);
	
    /**
     * @brief : 단속장비 로그 리스트 조회
     * @details : 단속장비 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcEnfEqpLog
     * @return : 
     */
	public List<MozTfcEnfEqpLog> getEqpLogList(MozTfcEnfEqpLog tfcEnfEqpLog);
	
    /**
     * @brief : 단속장비 리스트 개수 조회
     * @details : 단속장비 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcEnfEqpLog
     * @return : 
     */
	public int getEqpLogListCnt(MozTfcEnfEqpLog tfcEnfEqpLog);
	
	/**
     * @brief : 단속장비 로그 상세 조회
     * @details : 단속장비 로그 상세 조회 
     * @author : KC.KIM
     * @date : 2024.02.23
     * @param : logId
     * @return : 
     */
	public MozTfcEnfEqpLog getEqpLogDetail(String logId);
}
