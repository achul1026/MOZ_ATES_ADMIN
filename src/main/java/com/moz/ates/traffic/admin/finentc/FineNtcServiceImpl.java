package com.moz.ates.traffic.admin.finentc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.common.entity.driver.MozVioInfo;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfFileInfo;
import com.moz.ates.traffic.common.entity.finentc.MozFineNtcInfo;
import com.moz.ates.traffic.common.repository.driver.MozVioInfoRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfFileInfoRepository;
import com.moz.ates.traffic.common.repository.finentc.MozFineNtcInfoRepository;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class FineNtcServiceImpl implements FineNtcService{
	
	@Autowired
	MozFineNtcInfoRepository mozFineNtcInfoRepository;
	
	@Autowired
	MozTfcEnfFileInfoRepository mozTfcEnfFileInfoRepository;
	
	@Autowired
	MozVioInfoRepository mozVioInfoRepository;
	
	/**
     * @brief : 고지 관리 리스트 개수 조회
     * @details : 고지 관리 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.01.25
     * @param : 
     * @return : 
     */
	@Override
	public int getFineNtcListCnt(MozFineNtcInfo fineNtcInfo) {
		return mozFineNtcInfoRepository.countFineNtcList(fineNtcInfo);
	}

	/**
     * @brief : 고지 관리 리스트 조회
     * @details : 고지 관리 리스트 조회
     * @author : KC.KIM
     * @date : 2024.01.25
     * @param : 
     * @return : 
     */
	@Override
	public List<MozFineNtcInfo> findAllFineNtcList(MozFineNtcInfo fineNtcInfo) {
		return mozFineNtcInfoRepository.findAllFineNtcList(fineNtcInfo);
	}

	
	/**
     * @brief : 고지 관리 상세 조회
     * @details : 고지 관리 상세 조회
     * @author : KY.LEE
     * @date : 2024.02.19
     * @param : 
     * @return : 
     */
	@Override
	public MozFineNtcInfo findOneNtcDetailByFineNtcId(String fineNtcId) {
		MozFineNtcInfo mozFineNtcInfo = mozFineNtcInfoRepository.findOneNtcDetailByFineNtcId(fineNtcId);
		
		String tfcEnfId = mozFineNtcInfo.getTfcEnfId();
		
		if(!MozatesCommonUtils.isNull(tfcEnfId)) {
			List<MozTfcEnfFileInfo> fileList  = mozTfcEnfFileInfoRepository.findTfcEnfFileInfoByTfcEnfId(tfcEnfId);
			if(fileList != null && !fileList.isEmpty()) {
				mozFineNtcInfo.setFileList(fileList);
			}
		}
		
		return mozFineNtcInfo;
	}

	/**
     * @brief : 고지서 위반자 정보 수정
     * @details : 고지서 위반자 정보 수정
     * @author : KY.LEE
     * @date : 2024.02.19
     * @param : MozVioInfo mozVioInfo
     * @return : 
     */
	@Override
	public void updateMozVioInfo(MozVioInfo mozVioInfo) {
		mozVioInfoRepository.updateVioPnoAndVioEmailByVioId(mozVioInfo);
	}
}
