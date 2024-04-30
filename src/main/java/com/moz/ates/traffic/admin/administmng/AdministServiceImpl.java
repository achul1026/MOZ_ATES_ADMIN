package com.moz.ates.traffic.admin.administmng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class AdministServiceImpl implements AdministService{
	
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public int getLawListCnt(MozTfcLwInfo tfcLwInfo) {
		return tfcLwInfoRepository.countLawListsByTfcLwInfo(tfcLwInfo);
	}

	/**
	 * @brief : 교통단속 법률관리 등록
	 * @details : 교통단속 법률관리 등록
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLwInfo
	 * @return :
	 */
	@Override
	public void lawSave(MozTfcLwInfo tfcLwInfo) {
		String lawId = MozatesCommonUtils.getUuid();
		String crtr = LoginOprtrUtils.getOprtrNm();
		
		tfcLwInfo.setTfcLawId(lawId);
		tfcLwInfo.setCrtr(crtr);
		tfcLwInfoRepository.insertMozTfcLwInfo(tfcLwInfo);

		if(tfcLwInfo.getMozTfcLwFineInfoArr() != null) {
			for(MozTfcLwFineInfo mozTfcLwFineInfo : tfcLwInfo.getMozTfcLwFineInfoArr()) {
				String tfcLwFineId = MozatesCommonUtils.getUuid();
				mozTfcLwFineInfo.setTfcLawFineId(tfcLwFineId);
				mozTfcLwFineInfo.setTfcLawId(lawId);
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
	@Override
	public MozTfcLwInfo getLawDetail(String tfcLawId) {
		return tfcLwInfoRepository.findOneLawDetail(tfcLawId);
	}

	/**
	 * @brief : 교통단속 법률관리 정보 수정
	 * @details : 교통단속 법률관리 정보 수정
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLawId
	 * @return :
	 */
	@Override
	public void deleteLaw(String tfcLawId) {
		tfcLwInfoRepository.deleteMozTfcLwInfoByTfcLawId(tfcLawId);
	}

	/**
	 * @brief : 법률 정보 수정
	 * @details : 법률 정보 수정
	 * @author : KY.LEE
	 * @date : 2024.02.23
	 * @param : MozTfcLwInfo
	 */
	@Override
	public void updateLaw(MozTfcLwInfo tfcLwInfo) {
		tfcLwInfoRepository.updateMozTfcLwInfoByTfcLawId(tfcLwInfo);

		if(tfcLwInfo.getMozTfcLwFineInfoArr() != null) {
			for(MozTfcLwFineInfo mozTfcLwFineInfo : tfcLwInfo.getMozTfcLwFineInfoArr()) {
				tfcLwFineInfoRepository.updateMozTfcLwFineInfo(mozTfcLwFineInfo);
			}
		}

		if(tfcLwInfo.getMozTfcLwAdtnRvsnArr() != null) {
			for(MozTfcLwAdtnRvsn mozTfcLwAdtnRvsn : tfcLwInfo.getMozTfcLwAdtnRvsnArr()) {
				tfcLwAdtnRvsnRepository.updateMozTfcLwAdtnRvsn(mozTfcLwAdtnRvsn);
			}
		}
	}

    /**
     * @brief : 법률 범칙금 목록
     * @details : 법률 범칙금 목록
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : tfcLawId
     * @return : List<MozTfcLwFineInfo>
     */
	@Override
	public List<MozTfcLwFineInfo> getLawFineList(String tfcLawId) {
		return tfcLwFineInfoRepository.findMozTfcLwFineInfoByTfcLawId(tfcLawId);
	}

    /**
     * @brief : 법률 추가개정 목록
     * @details : 법률 추가개정 목록
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : tfcLawId
     * @return : List<MozTfcLwAdtnRvsn>
     */
	@Override
	public List<MozTfcLwAdtnRvsn> getLawAdtnRvsnList(String tfcLawId) {
		return tfcLwAdtnRvsnRepository.findMozTfcLwAdtnRvsnByTfcLawId(tfcLawId);
	}

	@Override
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
	}

    /**
     * @brief : 법률 범칙금 추가
     * @details : 법률 범칙금 추가
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : MozTfcLwInfo
     */
	@Override
	public void lawAddFine(MozTfcLwInfo tfcLwInfo) {
		if(MozatesCommonUtils.isNull(tfcLwInfo.getTfcLawId())) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		
		if(tfcLwInfo.getMozTfcLwFineInfoArr() != null) {
			for(MozTfcLwFineInfo mozTfcLwFineInfo : tfcLwInfo.getMozTfcLwFineInfoArr()) {
				String tfcLwFineId = MozatesCommonUtils.getUuid();
				mozTfcLwFineInfo.setTfcLawFineId(tfcLwFineId);
				mozTfcLwFineInfo.setTfcLawId(tfcLwInfo.getTfcLawId());
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
	@Override
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
	@Override
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
     * @brief : 범칙금 삭제
     * @details : 범칙금 삭제
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : MozTfcLwInfo
     */
	@Override
	public void fineDelete(String tfcLawFineId) {
		tfcLwFineInfoRepository.deleteMozTfcLwFineInfo(tfcLawFineId);
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
	@Override
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
