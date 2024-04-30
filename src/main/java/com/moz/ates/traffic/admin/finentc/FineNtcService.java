package com.moz.ates.traffic.admin.finentc;

import java.util.List;

import com.moz.ates.traffic.common.entity.driver.MozVioInfo;
import com.moz.ates.traffic.common.entity.finentc.MozFineNtcInfo;

public interface FineNtcService {
	
	 /**
     * @brief : 고지 관리 리스트 개수 조회
     * @details : 고지 관리 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.01.25
     * @param : 
     * @return : 
     */
	int getFineNtcListCnt(MozFineNtcInfo fineNtcInfo);

	/**
     * @brief : 고지 관리 리스트 조회
     * @details : 고지 관리 리스트 조회
     * @author : KC.KIM
     * @date : 2024.01.25
     * @param : 
     * @return : 
     */
	List<MozFineNtcInfo> findAllFineNtcList(MozFineNtcInfo fineNtcInfo);
	
	/**
     * @brief : 고지 관리 상세 조회
     * @details : 고지 관리 상세 조회
     * @author : KY.LEE
     * @date : 2024.02.19
     * @param : 
     * @return : 
     */
	MozFineNtcInfo findOneNtcDetailByFineNtcId(String fineNtcId);

	/**
     * @brief : 고지서 위반자 정보 수정
     * @details : 고지서 위반자 정보 수정
     * @author : KY.LEE
     * @date : 2024.02.19
     * @param : 
     * @return : 
     */
	void updateMozVioInfo(MozVioInfo mozVioInfo);
	
}
