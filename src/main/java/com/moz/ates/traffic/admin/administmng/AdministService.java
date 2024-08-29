package com.moz.ates.traffic.admin.administmng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.common.component.ExcelDownlodComponent;
import com.moz.ates.traffic.common.entity.administrative.MozAdministDip;
import com.moz.ates.traffic.common.entity.administrative.MozCourtDcsn;
import com.moz.ates.traffic.common.entity.law.MozTfcLwAdtnRvsn;
import com.moz.ates.traffic.common.entity.law.MozTfcLwFineInfo;
import com.moz.ates.traffic.common.entity.law.MozTfcLwInfo;
import com.moz.ates.traffic.common.repository.administrative.MozAdministDipRepository;
import com.moz.ates.traffic.common.repository.administrative.MozCourtDcsnRepository;
import com.moz.ates.traffic.common.repository.law.MozTfcLwAdtnRvsnRepository;
import com.moz.ates.traffic.common.repository.law.MozTfcLwFineInfoRepository;
import com.moz.ates.traffic.common.repository.law.MozTfcLwInfoRepository;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.support.exception.ErrorCode;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class AdministService {
	
	@Autowired
	MozAdministDipRepository mozAdministDipRepository;
	
	@Autowired
	MozCourtDcsnRepository mozCourtDcsnRepository;
	
	@Autowired
	MozTfcLwInfoRepository tfcLwInfoRepository;
	
	@Autowired
	MozTfcLwFineInfoRepository tfcLwFineInfoRepository;
	
	@Autowired
	MozTfcLwAdtnRvsnRepository tfcLwAdtnRvsnRepository;
	
	@Autowired
	ExcelDownlodComponent excelDownlodComponent;
	
	/**
     * @brief : 법원 이송 관리 리스트 개수 조회
     * @details : 법원 이송 관리 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.01.22
     * @param : administDip
     * @return : 
     */
	public int getAdministListCnt(MozAdministDip administDip) {
		return mozAdministDipRepository.countMozAdminstDip(administDip);
	}

    /**
     * @brief : 법원 이송 관리 리스트 조회
     * @details : 법원 이송 관리 리스트 조회
     * @author : KC.KIM
     * @date : 2024.01.22
     * @param : administDip
     * @return : 
     */
	public List<MozAdministDip> getAdministList(MozAdministDip administDip) {
		return mozAdministDipRepository.findAllMozAdministList(administDip);
	}
	
	/**
     * @brief : 최종 판결 정보 등록
     * @details : 최종 판결 정보 등록
     * @author : NK.KIM
     * @date : 2024.02.20
     * @param : mozCourtDcsn
     * @return : 
     */
	public void saveCourtDcsn(MozCourtDcsn mozCourtDcsn) {
		try {
			String courtDcsnId = MozatesCommonUtils.getUuid();
			mozCourtDcsn.setCourtDcsnId(courtDcsnId);
			mozCourtDcsnRepository.save(mozCourtDcsn);
		}catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAIL);
		}
	}
	
	/**
	 * @brief : 최종 판결 정보 수정
	 * @details : 최종 판결 정보 수정
	 * @author : NK.KIM
	 * @date : 2024.02.20
	 * @param : mozCourtDcsn
	 * @return : 
	 */
	public void updateCourtDcsn(MozCourtDcsn mozCourtDcsn) {
		MozCourtDcsn mozCourtDcsnInfo = mozCourtDcsnRepository.findOneByCourtDcsnId(mozCourtDcsn.getCourtDcsnId());
		if(mozCourtDcsnInfo == null) {
			throw new CommonException(ErrorCode.ENTITY_DATA_NULL);
		}
		
		try {
			
			if(!MozatesCommonUtils.isNull(mozCourtDcsn.getCourtMngr())) mozCourtDcsnInfo.setCourtMngr(mozCourtDcsn.getCourtMngr());
			if(!MozatesCommonUtils.isNull(mozCourtDcsn.getCourtNm())) mozCourtDcsnInfo.setCourtNm(mozCourtDcsn.getCourtNm());
			if(!MozatesCommonUtils.isNull(mozCourtDcsn.getDcsnStts())) mozCourtDcsnInfo.setDcsnStts(mozCourtDcsn.getDcsnStts());
			if(!MozatesCommonUtils.isNull(mozCourtDcsn.getFinePrice())) mozCourtDcsnInfo.setFinePrice(mozCourtDcsn.getFinePrice());
			if(!MozatesCommonUtils.isNull(mozCourtDcsn.getLawsuitType())) mozCourtDcsnInfo.setLawsuitType(mozCourtDcsn.getLawsuitType());
			if(!MozatesCommonUtils.isNull(mozCourtDcsn.getLawViolatedNm())) mozCourtDcsnInfo.setLawViolatedNm(mozCourtDcsn.getLawViolatedNm());
			if(!MozatesCommonUtils.isNull(mozCourtDcsn.getDscnContent())) mozCourtDcsnInfo.setDscnContent(mozCourtDcsn.getDscnContent());
			
			mozCourtDcsnRepository.update(mozCourtDcsnInfo);
			
		}catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_UPDATE_FAIL);
		}
	}
	
	/**
	 * @brief : 교통단속 법률관리 리스트 조회
	 * @details : 교통단속 법률관리 리스트 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLwInfo
	 * @return :
	 */
	public List<MozTfcLwInfo> getLawList(MozTfcLwInfo tfcLwInfo) {
		return tfcLwInfoRepository.findAllLawListsByTfcLwInfo(tfcLwInfo);
	}

    /**
     * @brief : 교통단속 법률관리 카운트
     * @details : 교통단속 법률관리 카운트
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcLwInfo
     * @return : 
     */
	public int getLawListCnt(MozTfcLwInfo tfcLwInfo) {
		return tfcLwInfoRepository.countLawListsByTfcLwInfo(tfcLwInfo);
	}

	/**
	  * @Method Name : checkDuplicateByArtclNo
	  * @Date : 2024. 6. 10.
	  * @Author : IK.MOON
	  * @Method Brief : 법률 lawArticleNo 중복 체크. lawArticleNo
	  * @param tfcLwInfo
	  * @return
	  */
	public boolean checkDuplicateByArtclNo(MozTfcLwInfo tfcLwInfo) {
		boolean isDuplicated = false;
		if (tfcLwInfoRepository.countAllLawBylawArticleNo(tfcLwInfo) > 0) {
			isDuplicated = true;
		}
		return isDuplicated;
	}
	
	/**
	  * @Method Name : checkDuplicateByArtclNoAndLawId
	  * @Date : 2024. 6. 10.
	  * @Author : IK.MOON
	  * @Method Brief : 법률 lawArticleNo 중복 체크. lawArticleNo & tfcLawId
	  * @param tfcLwInfo
	  * @return
	  */
	public boolean checkDuplicateByArtclNoAndLawId(MozTfcLwInfo tfcLwInfo) {
		boolean isDuplicated = false;
		if (tfcLwInfoRepository.countAllLawBylawArticleNoAndLawId(tfcLwInfo) > 0) {
			isDuplicated = true;
		}
		return isDuplicated;
	}
	
	/**
	  * @Method Name : checkDuplicateByTfcLwFineIdList
	  * @Date : 2024. 6. 20.
	  * @Author : IK.MOON
	  * @Method Brief : 범칙금 id 중복체크 list
	  * @param mozTfcLwFineInfoArr
	  * @return
	  */
	public boolean checkDuplicateByTfcLwFineIdList(List<MozTfcLwFineInfo> mozTfcLwFineInfoArr) {
		boolean isDuplicated = false;
		
		if (mozTfcLwFineInfoArr == null) {
			return isDuplicated;
		}
		
		long distinctCnt = mozTfcLwFineInfoArr.stream()
				.map(MozTfcLwFineInfo::getTfcLawFineId)
				.distinct()
				.count();
		
		if (distinctCnt != mozTfcLwFineInfoArr.size()) {
			isDuplicated = true;
		} else {
			for (MozTfcLwFineInfo mozTfcLwFineInfo : mozTfcLwFineInfoArr) {
				if (tfcLwFineInfoRepository.selectAllByTfcLawFineId(mozTfcLwFineInfo) > 0) {
					isDuplicated = true;
					break;
				}
			}
		}
		
		return isDuplicated;
	}
	
	/**
	  * @Method Name : checkDuplicateByTfcLwFineId
	  * @Date : 2024. 6. 20.
	  * @Author : IK.MOON
	  * @Method Brief : 범칙금 id 중복체크 단일
	  * @param mozTfcLwFineInfo
	  * @return
	  */
	public boolean checkDuplicateByTfcLwFineId(MozTfcLwFineInfo mozTfcLwFineInfo) {
		boolean isDuplicated = false;
		if (tfcLwFineInfoRepository.selectAllByTfcLawFineId(mozTfcLwFineInfo) > 0) {
			isDuplicated = true;
		}
		return isDuplicated;
	}
	
	/**
	 * @brief : 교통단속 법률관리 등록
	 * @details : 교통단속 법률관리 등록
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLwInfo
	 * @return :
	 */
	@Transactional
	public void lawSave(MozTfcLwInfo tfcLwInfo) {
		String lawId = MozatesCommonUtils.getUuid();
		String crtr = LoginOprtrUtils.getOprtrNm();
		
		tfcLwInfo.setTfcLawId(lawId);
		tfcLwInfo.setCrtr(crtr);
		tfcLwInfoRepository.insertMozTfcLwInfo(tfcLwInfo);

		if(tfcLwInfo.getMozTfcLwFineInfoArr() != null) {
			for(MozTfcLwFineInfo mozTfcLwFineInfo : tfcLwInfo.getMozTfcLwFineInfoArr()) {
				mozTfcLwFineInfo.setTfcLawId(lawId);
				mozTfcLwFineInfo.setCrtr(crtr);
				tfcLwFineInfoRepository.saveMozTfcLwFineInfo(mozTfcLwFineInfo);
			}
		}
	}

	/**
	 * @brief : 교통단속 법률관리 상세 조회
	 * @details : 교통단속 법률관리 상세 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLawId
	 * @return :
	 */
	public MozTfcLwInfo getLawDetail(String tfcLawId) {
		return tfcLwInfoRepository.findOneLawDetail(tfcLawId);
	}

	/**
	 * @brief : 법률 정보 수정
	 * @details : 법률 정보 수정
	 * @author : KY.LEE
	 * @date : 2024.02.23
	 * @param : MozTfcLwInfo
	 */
	@Transactional
	public void updateLaw(MozTfcLwInfo tfcLwInfo) {
		tfcLwInfoRepository.updateMozTfcLwInfoByTfcLawId(tfcLwInfo);

		if(tfcLwInfo.getMozTfcLwAdtnRvsnArr() != null) {
			for(MozTfcLwAdtnRvsn mozTfcLwAdtnRvsn : tfcLwInfo.getMozTfcLwAdtnRvsnArr()) {
				tfcLwAdtnRvsnRepository.updateMozTfcLwAdtnRvsn(mozTfcLwAdtnRvsn);
			}
		}
	}

	/**
	  * @Method Name : updateFineInfo
	  * @Date : 2024. 6. 20.
	  * @Author : IK.MOON
	  * @Method Brief : 범칙금 정보 수정
	  * @param mozTfcLwFineInfo
	  */
	public void updateFineInfo(MozTfcLwFineInfo mozTfcLwFineInfo) {
		tfcLwFineInfoRepository.updateMozTfcLwFineInfo(mozTfcLwFineInfo);
	}
	
    /**
     * @brief : 법률 범칙금 목록
     * @details : 법률 범칙금 목록
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : tfcLawId
     * @return : List<MozTfcLwFineInfo>
     */
	public List<MozTfcLwFineInfo> getLawFineList(String tfcLawId) {
		return tfcLwFineInfoRepository.findMozTfcLwFineInfoByTfcLawId(tfcLawId);
	}

	/**
	  * @Method Name : getLawFineListJoinCmCd
	  * @Date : 2024. 6. 10.
	  * @Author : IK.MOON
	  * @Method Brief : 법률 범칙금 목록 join Common Code
	  * @param tfcLawId
	  * @return
	  */
	public List<MozTfcLwFineInfo> getLawFineListJoinCmCd(String tfcLawId) {
		return tfcLwFineInfoRepository.findMozTfcLwFineInfoByTfcLawIdJoinMozCmCd(tfcLawId);
	}
	
    /**
     * @brief : 법률 추가개정 목록
     * @details : 법률 추가개정 목록
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : tfcLawId
     * @return : List<MozTfcLwAdtnRvsn>
     */
	public List<MozTfcLwAdtnRvsn> getLawAdtnRvsnList(String tfcLawId) {
		return tfcLwAdtnRvsnRepository.findMozTfcLwAdtnRvsnByTfcLawId(tfcLawId);
	}

	/**
	  * @Method Name : lawRevise
	  * @Date : 2024. 6. 11.
	  * @Author : IK.MOON
	  * @Method Brief : 법률 개정
	  * @param tfcLwInfo
	  */
	@Transactional
	public void lawRevise(MozTfcLwInfo tfcLwInfo) {
		if(MozatesCommonUtils.isNull(tfcLwInfo.getTfcLawId())) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}

		if(tfcLwInfo.getMozTfcLwAdtnRvsnArr() != null) {
			for(MozTfcLwAdtnRvsn mozTfcLwAdtnRvsn : tfcLwInfo.getMozTfcLwAdtnRvsnArr()) {
				String tfcLawAdtnRvsnId = MozatesCommonUtils.getUuid();
				mozTfcLwAdtnRvsn.setTfcLawId(tfcLwInfo.getTfcLawId());
				mozTfcLwAdtnRvsn.setTfcLawAdtnRvsnId(tfcLawAdtnRvsnId);
				tfcLwAdtnRvsnRepository.saveMozTfcLwAdtnRvsn(mozTfcLwAdtnRvsn);
			}
		}
		
		tfcLwInfoRepository.updateRvsnDe(tfcLwInfo);
	}

    /**
     * @brief : 법률 범칙금 추가
     * @details : 법률 범칙금 추가
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : MozTfcLwInfo
     */
	@Transactional
	public void lawAddFine(MozTfcLwInfo tfcLwInfo) {
		if(MozatesCommonUtils.isNull(tfcLwInfo.getTfcLawId())) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		
		String crtr = LoginOprtrUtils.getOprtrNm();
		
		if(tfcLwInfo.getMozTfcLwFineInfoArr() != null) {
			for(MozTfcLwFineInfo mozTfcLwFineInfo : tfcLwInfo.getMozTfcLwFineInfoArr()) {
				mozTfcLwFineInfo.setTfcLawId(tfcLwInfo.getTfcLawId());
				mozTfcLwFineInfo.setCrtr(crtr);
				tfcLwFineInfoRepository.saveMozTfcLwFineInfo(mozTfcLwFineInfo);
			}
		}
	}

    /**
     * @brief : 법률 폐지 처리
     * @details : 법률 폐지 처리
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : MozTfcLwInfo
     */
	public void lawAbolition(String tfcLawId) {
		
		if(MozatesCommonUtils.isNull(tfcLawId)) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		
		MozTfcLwInfo mozTfcLwInfo =  tfcLwInfoRepository.findOneLawDetail(tfcLawId);
		mozTfcLwInfo.setRepealYn("Y");
		mozTfcLwInfo.setRepealDe(new Date());
		tfcLwInfoRepository.updateRepealYnByTfcLawId(mozTfcLwInfo);
	}
	
    /**
     * @brief : 법률 복구 처리
     * @details : 법률 복구 처리
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : MozTfcLwInfo
     */
	public void lawRecovery(String tfcLawId) {
		
		if(MozatesCommonUtils.isNull(tfcLawId)) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		
		MozTfcLwInfo mozTfcLwInfo =  tfcLwInfoRepository.findOneLawDetail(tfcLawId);
		mozTfcLwInfo.setRepealYn("N");
		mozTfcLwInfo.setRepealDe(null);
		tfcLwInfoRepository.updateRepealYnByTfcLawId(mozTfcLwInfo);
	}

	/**
     * @brief : 법원이송 리스트 엑셀 다운로드
     * @details : 법원이송 리스트 엑셀 다운로드
     * @author : KC.KIM
     * @date : 2024.04.25
     * @param : resp
     * @param : administDip
     * @return : 
	 * @throws IOException 
     */
	public void excelDownload(HttpServletResponse resp, MozAdministDip administDip) throws IOException {
		List<MozAdministDip> administDipList = new ArrayList<MozAdministDip>();
		
		administDipList = mozAdministDipRepository.findAllMozAdministList(administDip);
		
		String[] headerArray = null;
		List<Object> bodyList = null;
		
		headerArray = new String[]{"No.", "Name", "Enforcement Number", "Case Type", "Administrative disposition transition date", "Retention", "Status"};
		
		if(administDipList != null && !administDipList.isEmpty()) {
			bodyList = new ArrayList<>();
			
			for(MozAdministDip item : administDipList) {
				bodyList.add(item.getRnum());
				bodyList.add(item.getVioInfo().getVioNm());
				bodyList.add(item.getTfcEnfId());
				bodyList.add(item.getCmCd().getCdNm());
				bodyList.add(item.getCrDt());
				bodyList.add(item.getProHoldYn());
				bodyList.add(item.getProcessYn());
			}
		}
		String fileName = MozatesCommonUtils.isNowStr("yyyyMMddhhmmss") + "_Court_Transfer";
		excelDownlodComponent.excelDownload(resp, headerArray, bodyList, fileName);
	}

}
