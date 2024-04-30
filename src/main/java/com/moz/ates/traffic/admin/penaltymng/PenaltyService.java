package com.moz.ates.traffic.admin.penaltymng;

import java.util.List;
import java.util.Map;

import com.moz.ates.traffic.common.entity.payment.MozFinePymntInfo;
import com.moz.ates.traffic.common.enums.PymntMethod;

public interface PenaltyService {
	
    /**
     * @brief : 범칙금 등록
     * @details : 범칙금 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : finePymntInfo
     * @return : 
     */
    void registPenalty(MozFinePymntInfo finePymntInfo);
    
    /**
     * @brief : 범칙금 상세 조회
     * @details : 범칙금 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : pymntId
     * @return : 
     */
    MozFinePymntInfo getPenaltyDetail(String pymntId);
    
    /**
     * @brief : 범칙금 수정
     * @details : 범칙금 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : finePymntInfo
     * @return : 
     */
	void updatePenaltyMaster(MozFinePymntInfo finePymntInfo);
	
    /**
     * @brief : 범칙금 수정
     * @details : 범칙금 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : finePymntInfo
     * @return : 
     */
	void updatePenalty(MozFinePymntInfo finePymntInfo);

	void paymentClear(String pymntId);

	void paymentCencal(String pymntId);


//	Map<String, Object> penaltySendEmail(String pymntId, String emailAddr, String content);

    /**
     * @brief : 범칙금 리스트 조회
     * @details : 범칙금 리스트 조회 
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : finePymntInfo
     * @return : 
     */
	List<MozFinePymntInfo> getPenaltyList(MozFinePymntInfo finePymntInfo);

    /**
     * @brief : 범칙금 리스트 개수 조회
     * @details : 범칙금 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : finePymntInfo
     * @return : 
     */
	int getPenaltyListCnt(MozFinePymntInfo finePymntInfo);


	void updatePayStatus(MozFinePymntInfo finePymntInfo);

   /**
     * @brief : 현장 결제 로직
     * @details : 현장 결제 로직
     * @author : KY.LEE
     * @date : 2024.03.20
     * @param : onsitePayment
     * @return : 
     */
	public void onsitePayment(MozFinePymntInfo mozFinePymntInfo);

	/**
	 * @brief : 결제 취소
	 * @details : 결제 취소
	 * @author : KY.LEE
	 * @date : 2024.03.29
	 * @param : cancelPayment
	 * @return : 
	 */
	public void cancelPayment(String pymntId , PymntMethod pymntMethod);
}
