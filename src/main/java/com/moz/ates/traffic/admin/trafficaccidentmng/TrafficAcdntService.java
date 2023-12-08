package com.moz.ates.traffic.admin.trafficaccidentmng;

import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntChgHst;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntMaster;

import java.util.List;

public interface TrafficAcdntService {
    /**
     * @brief : 교통사고 정보 등록
     * @details : 교통사고 정보 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntMaster
     * @return : 
     */
    void mozTfcAcdntMasterSave(MozTfcAcdntMaster tfcAcdntMaster);
    
    /**
     * @brief : 교통사고 중복 체크
     * @details : 교통사고 중복 체크
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntMaster
     * @return : 
     */
    int countTfcAcdntMaster(MozTfcAcdntMaster tfcAcdntMaster);

    /**
     * @brief : 교통사고 정보 상세 조회
     * @details : 교통사고 정보 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntId
     * @return : 
     */
    MozTfcAcdntMaster getMngDetail(String tfcAcdntId);
    
    /**
     * @brief : 교통사고 정보 수정
     * @details : 교통사고 정보 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntMaster
     * @return : 
     */
    void upateAcdnt(MozTfcAcdntMaster tfcAcdntMaster);

    /**
     * @brief : 교통사고 정보 조회
     * @details : 교통사고 정보 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcAcdntMaster
     * @return : 
     */
	List<MozTfcAcdntMaster> getAcdntList(MozTfcAcdntMaster tfcAcdntMaster);

    /**
     * @brief : 교통사고 리스트 개수 조회
     * @details : 교통사고 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcAcdntMaster
     * @return : 
     */
	int getAcdntListCnt(MozTfcAcdntMaster tfcAcdntMaster);

    /**
     * @brief : 교통사고 로그 리스트 조회
     * @details : 교통사고 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcAcdntChgHst
     * @return : 
     */
	List<MozTfcAcdntChgHst> getAcdntLogList(MozTfcAcdntChgHst tfcAcdntChgHst);

    /**
     * @brief : 교통사고 로그 리스트 개수 조회
     * @details : 교통사고 로그 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcAcdntChgHst
     * @return : 
     */
	int getAcdntLogListCnt(MozTfcAcdntChgHst tfcAcdntChgHst);
}
