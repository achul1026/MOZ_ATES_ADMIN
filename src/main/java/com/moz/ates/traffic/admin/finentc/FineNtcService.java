package com.moz.ates.traffic.admin.finentc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.common.entity.driver.MozVioInfo;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfFileInfo;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfFineInfo;
import com.moz.ates.traffic.common.entity.finentc.MozFineNtcInfo;
import com.moz.ates.traffic.common.repository.driver.MozVioInfoRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfFileInfoRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfFineInfoRepository;
import com.moz.ates.traffic.common.repository.finentc.MozFineNtcInfoRepository;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.support.exception.ErrorCode;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class FineNtcService {
	
	@Autowired
	MozFineNtcInfoRepository mozFineNtcInfoRepository;
	
	@Autowired
	MozTfcEnfFileInfoRepository mozTfcEnfFileInfoRepository;
	
	@Autowired
	MozVioInfoRepository mozVioInfoRepository;
	
	@Autowired
	MozTfcEnfFineInfoRepository mozTfcEnfFineInfoRepository;
 	
	/**
     * @brief : 고지 관리 리스트 개수 조회
     * @details : 고지 관리 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.01.25
     * @param : 
     * @return : 
     */
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
	  * @Method Name : getAllTfcEnfFineInfo
	  * @Date : 2024. 6. 21.
	  * @Author : IK.MOON
	  * @Method Brief : 범칙금 정보 조회
	  * @param tfcEnfId
	  * @return
	  */
	public List<MozTfcEnfFineInfo> getAllTfcEnfFineInfo(String tfcEnfId) {
		if (MozatesCommonUtils.isNull(tfcEnfId)) {
			throw new CommonException(ErrorCode.REQUIRED_FIELDS);
		}
		
		List<MozTfcEnfFineInfo> lawfineList = mozTfcEnfFineInfoRepository.findAllTfcEnfFineInfoJoinTfcLwFineInfoAndTfcLwInfoByTfcEnfId(tfcEnfId);
		for (MozTfcEnfFineInfo lawFine : lawfineList) {
			String lawType = lawFine.getTfcLwInfo().getLawType();
			String lawArticleNo = lawFine.getTfcLwInfo().getLawArticleNo();
			String artclNo = lawFine.getTfcLwFineInfo().getArtclNo();
			String par = lawFine.getTfcLwFineInfo().getPar();
			
			StringBuilder titleBuilder = new StringBuilder();
			
			if (MozatesCommonUtils.isNull(artclNo) && MozatesCommonUtils.isNull(par)) {
				titleBuilder.append("[").append(lawType).append("] ")
					.append(lawArticleNo);
			} else {
				if (MozatesCommonUtils.isNull(artclNo)) {
					titleBuilder.append("[").append(lawType).append("] ")
						.append(lawArticleNo).append("--").append(par);
				} else if (MozatesCommonUtils.isNull(par)) {
					titleBuilder.append("[").append(lawType).append("] ")
					.append(lawArticleNo).append("-").append(artclNo);
				} else {
					titleBuilder.append("[").append(lawType).append("] ")
					.append(lawArticleNo).append("-").append(artclNo).append("-").append(par);
				}
				
			}
			
			lawFine.getTfcLwInfo().setLawNm(titleBuilder.toString());
		}
		
		return lawfineList;
	}
	
	/**
     * @brief : 고지서 위반자 정보 수정
     * @details : 고지서 위반자 정보 수정
     * @author : KY.LEE
     * @date : 2024.02.19
     * @param : MozVioInfo mozVioInfo
     * @return : 
     */
	public void updateMozVioInfo(MozVioInfo mozVioInfo) {
		mozVioInfoRepository.updateVioPnoAndVioEmailByVioId(mozVioInfo);
	}
	
}
