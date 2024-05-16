package com.moz.ates.traffic.admin.trafficaccidentmng;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntMaster;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntTrgtInfo;

public interface TrafficAcdntService {
    /**
     * @brief : 교통사고 정보 등록
     * @details : 교통사고 정보 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntMaster
     * @param : uploadFiles 
     * @return : 
     */
    void mozTfcAcdntMasterSave(MozTfcAcdntMaster tfcAcdntMaster, MultipartFile[] uploadFiles);
    
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
     * @param uploadFiles 
     * @return : 
     */
    void upateAcdnt(MozTfcAcdntMaster tfcAcdntMaster, MultipartFile[] uploadFiles);

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
	 * @brief 사고타겟 이력 조회
	 * @details : 사고타겟 이력 조회
	 * @author KY.LEE
	 * @date 2024. 4. 11.
	 * @method getAcdntTrgtList
	 */
	List<MozTfcAcdntTrgtInfo> getAcdntTrgtList(String dvrLcenId);

	/**
	 * @brief 사고타겟 이력 조회
	 * @details : 사고타겟 이력 조회
	 * @author KY.LEE
	 * @date 2024. 4. 11.
	 * @method getAcdntTrgtListByDocNid
	 */
	List<MozTfcAcdntTrgtInfo> getAcdntTrgtListByDocNid(String docNid);
}
