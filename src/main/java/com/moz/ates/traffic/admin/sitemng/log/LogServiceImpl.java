package com.moz.ates.traffic.admin.sitemng.log;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntChgHst;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfHst;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpLog;
import com.moz.ates.traffic.common.entity.log.MozTfcClctnFlrLog;
import com.moz.ates.traffic.common.entity.log.MozTfcSystmErrLog;
import com.moz.ates.traffic.common.entity.log.MozTfcUserLog;
import com.moz.ates.traffic.common.entity.operator.MozOprtrAudLog;
import com.moz.ates.traffic.common.entity.operator.MozPolAudLog;
import com.moz.ates.traffic.common.repository.accident.MozTfcAcdntChgHstRepository;
import com.moz.ates.traffic.common.repository.enforcement.MozTfcEnfHstRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfEqpLogRepository;
import com.moz.ates.traffic.common.repository.log.MozTfcClctnFlrLogRepository;
import com.moz.ates.traffic.common.repository.log.MozTfcSystmErrLogRepository;
import com.moz.ates.traffic.common.repository.log.MozTfcUserLogRepository;
import com.moz.ates.traffic.common.repository.operator.MozOprtrAudLogRepositorty;
import com.moz.ates.traffic.common.repository.operator.MozPolAudLogRepositorty;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class LogServiceImpl implements LogService{
	
	@Autowired
	MozTfcUserLogRepository mozTfcUserLogRepository;
	
	@Autowired
	MozTfcSystmErrLogRepository mozTfcSystmErrLogRepository;
	
	@Autowired
	MozTfcClctnFlrLogRepository mozTfcClctnFlrLogRepository;
	
	@Autowired
	MozOprtrAudLogRepositorty mozOprtrAudLogRepositorty;
	
	@Autowired
	MozPolAudLogRepositorty mozPolAudLogRepositorty;
	
	@Autowired
	MozTfcEnfHstRepository tfcEnfHstRepository;
	
	@Autowired 
	MozTfcAcdntChgHstRepository tfcAcdntChgHstRepository;
	
	@Autowired
	MozTfcEnfEqpLogRepository tfcEnfEqpLogRepository;
	
	/**
     * @brief : 사용자 로그 리스트 개수 조회
     * @details : 사용자 로그 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : mozTfcUserLog
     * @return : 
     */
	@Override
	public int getUserLogListCnt(MozTfcUserLog mozTfcUserLog) {
		return mozTfcUserLogRepository.countMozTfcUserLog(mozTfcUserLog);
	}

	/**
     * @brief : 사용자 로그 리스트 조회
     * @details : 사용자 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : mozTfcUserLog
     * @return : 
     */
	@Override
	public List<MozTfcUserLog> getUserLogList(MozTfcUserLog mozTfcUserLog) {
		return mozTfcUserLogRepository.findAllMozTfcUserLogList(mozTfcUserLog);
	}
	
	/**
     * @brief : 사용자 로그 상세 조회
     * @details : 사용자 로그 상세 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : logId
     * @return : 
     */
	@Override
	public Map<String, Object> getUserLogDetail(String logId) {
		return mozTfcUserLogRepository.findOneMozTfcUserLog(logId);
	}

	/**
     * @brief : 시스템 장애 로그 리스트 개수 조회
     * @details : 시스템 장애 로그 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : mozTfcSystmErrLog
     * @return : 
     */
	@Override
	public int getSystmErrLogListCnt(MozTfcSystmErrLog mozTfcSystmErrLog) {
		return mozTfcSystmErrLogRepository.countMozTfcSystmErrLog(mozTfcSystmErrLog);
	}

	/**
     * @brief : 시스템 장애 로그 리스트 조회
     * @details : 시스템 장애 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : mozTfcSystmErrLog
     * @return : 
     */
	@Override
	public List<MozTfcSystmErrLog> getSystmErrLogList(MozTfcSystmErrLog mozTfcSystmErrLog) {
		return mozTfcSystmErrLogRepository.findAllMozTfcSystmErrLogList(mozTfcSystmErrLog);
	}
	
	/**
     * @brief : 시스템 장애 로그 상세 조회
     * @details : 시스템 장애 로그 상세 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : logId
     * @return : 
     */
	@Override
	public Map<String, Object> getSystmErrLogDetail(String logId) {
		return mozTfcSystmErrLogRepository.findOneMozTfcSystmErrLog(logId);
	}

	/**
     * @brief : 수집실패 로그 리스트 개수 조회
     * @details : 수집실패 로그 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : mozTfcClctnFlrLog
     * @return : 
     */
	@Override
	public int getClctnFlrLogListCnt(MozTfcClctnFlrLog mozTfcClctnFlrLog) {
		return mozTfcClctnFlrLogRepository.countMozTfcClctnFlrLog(mozTfcClctnFlrLog);
	}

	/**
     * @brief : 수집실패 로그 리스트 조회
     * @details : 수집실패 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : mozTfcClctnFlrLog
     * @return : 
     */
	@Override
	public List<MozTfcClctnFlrLog> getClctnFlrLogList(MozTfcClctnFlrLog mozTfcClctnFlrLog) {
		return mozTfcClctnFlrLogRepository.findAllMozTfcClctnFlrLogList(mozTfcClctnFlrLog);
	}

	/**
     * @brief : 수집실패 로그 상세 조회
     * @details : 수집실패 로그 상세 조회
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : logId
     * @return : 
     */
	@Override
	public Map<String, Object> getClctnFlrLog(String logId) {
		return mozTfcClctnFlrLogRepository.findOneMozTfcClctnFlrLog(logId);
	}
	
	/**
	  * @Method Name : getOprtrAudLogByOprtrId
	  * @작성일 : 2024. 2. 19.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 사용자 로그 목록 조회
	  * @param oprtrId
	  * @return
	  */
	@Override
	public List<MozOprtrAudLog> getOprtrAudLogByOprtrId(String oprtrId) {
		return mozOprtrAudLogRepositorty.findAllMozOprtrAudLogList(oprtrId);
	}
	
	/**
	  * @Method Name : getMozOprtrAudLogListCnt
	  * @작성일 : 2024. 2. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 관리자 / 운영자 로그 목록 개수 조회
	  * @param oprtrAudLog
	  * @return
	  */
	@Override
	public int getMozOprtrAudLogListCnt(MozOprtrAudLog oprtrAudLog) {
		return mozOprtrAudLogRepositorty.countMozOprtrAudLog(oprtrAudLog);
	}

	/**
     * @brief : 사용자 로그 등록
     * @details : 사용자 로그 등록
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : logCateCd
     * @param : logCntn
     * @param : logRslt
     * @return : 
     */
	@Override
	public void insertUserLog(String logCateCd, String logCntn, String logRslt) {
		MozTfcUserLog userLog = new MozTfcUserLog();
		String crtr = LoginOprtrUtils.getOprtrId();
		String userLogId = MozatesCommonUtils.getUuid();
		String crtrIpAddr = LoginOprtrUtils.getUserIpAddr();
		
		userLog.setUserLogId(userLogId);
		userLog.setLogCateCd(logCateCd);
		userLog.setLogCntn(logCntn);
		userLog.setLogRslt(logRslt);
		userLog.setCrtr(crtr);
		userLog.setCrtrIpAddr(crtrIpAddr);

		mozTfcUserLogRepository.insertTfcUserLog(userLog);
	}
	
	/**
	  * @Method Name : getMozPolAudLogListCnt
	  * @작성일 : 2024. 2. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 경찰 로그 목록 개수 조회
	  * @param polAudLog
	  * @return
	  */
	@Override
	public int getMozPolAudLogListCnt(MozPolAudLog polAudLog) {
		return mozPolAudLogRepositorty.countMozPolAudLog(polAudLog);
	}
	
	/**
	  * @Method Name : getPolAudLogByOprtrId
	  * @작성일 : 2024. 2. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 경찰 로그 목록 조회
	  * @param polId
	  * @return
	  */
	@Override
	public List<MozPolAudLog> getPolAudLogByPolId(String polId){
		return mozPolAudLogRepositorty.findAllMozPolAudLogList(polId);
	}
	
	/**
	 * @brief : 교통단속 로그 리스트 조회
	 * @details : 교통단속 로그 리스트 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfHst
	 * @return :
	 */
	@Override
	public List<MozTfcEnfHst> getTfcLogList(MozTfcEnfHst tfcEnfHst) {
		return tfcEnfHstRepository.findAllLogListsByTfcEnfHst(tfcEnfHst);
		
	}
	
	/**
	 * @brief : 교통단속 로그 리스트 카운트 조회
	 * @details : 교통단속 로그 리스트 카운트 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfHst
	 * @return :
	 */
	@Override
	public int getTfcLogListCnt(MozTfcEnfHst tfcEnfHst) {
		return tfcEnfHstRepository.countLogListsByTfcEnfHst(tfcEnfHst);
	}
	
	/**
     * @brief : 교통단속 로그 상세 조회
     * @details : 교통단속 로그 상세 조회
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : hstId
     * @return : MozTfcEnfHst
     */
	@Override
	public MozTfcEnfHst getTfcLogDetail(String hstId) {
		return tfcEnfHstRepository.findOneTfcEnfHst(hstId);
	}
	
    /**
     * @brief : 교통사고 로그 리스트 조회
     * @details : 교통사고 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcAcdntChgHst
     * @return : 
     */
	@Override
	public List<MozTfcAcdntChgHst> getAcdntLogList(MozTfcAcdntChgHst tfcAcdntChgHst) {
		return tfcAcdntChgHstRepository.findAllMozTfcAcdntChgHst(tfcAcdntChgHst);
	}

    /**
     * @brief : 교통사고 로그 리스트 개수 조회
     * @details : 교통사고 로그 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcAcdntChgHst
     * @return : 
     */
	@Override
	public int getAcdntLogListCnt(MozTfcAcdntChgHst tfcAcdntChgHst) {
		return tfcAcdntChgHstRepository.countMozTfcAcdntChgHst(tfcAcdntChgHst);
	}

	/**
	 * @brief : 교통사고 로그 상세 화면
	 * @details : 교통사고 로그 상세 화면
	 * @author : KC.KIM
	 * @date : 2024.01.31
	 * @param : hstId
	 * @return :
	 */
	@Override
	public MozTfcAcdntChgHst getAcdntLogDetail(String hstId) {
		return tfcAcdntChgHstRepository.findOneTfcAcdntChgHst(hstId);
	}
	
    /**
     * @brief : 단속장비 로그 리스트 조회
     * @details : 단속장비 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcEnfEqpLog
     * @return : 
     */
	@Override
	public List<MozTfcEnfEqpLog> getEqpLogList(MozTfcEnfEqpLog tfcEnfEqpLog) {
		return tfcEnfEqpLogRepository.findAllMozTfcEnfEqpLog(tfcEnfEqpLog);
	}
	
    /**
     * @brief : 단속장비 리스트 개수 조회
     * @details : 단속장비 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcEnfEqpLog
     * @return : 
     */
	@Override
	public int getEqpLogListCnt(MozTfcEnfEqpLog tfcEnfEqpLog) {
		return tfcEnfEqpLogRepository.countMozTfcEnfEqpLog(tfcEnfEqpLog);
	}
	
	/**
     * @brief : 단속장비 로그 상세 조회
     * @details : 단속장비 로그 상세 조회 
     * @author : KC.KIM
     * @date : 2024.02.23
     * @param : logId
     * @return : 
     */
	@Override
	public MozTfcEnfEqpLog getEqpLogDetail(String logId) {
		return tfcEnfEqpLogRepository.findOneMozTfcEnfEqpLog(logId);
	}
}
