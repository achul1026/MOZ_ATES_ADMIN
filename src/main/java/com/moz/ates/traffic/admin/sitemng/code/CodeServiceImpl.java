package com.moz.ates.traffic.admin.sitemng.code;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.repository.common.MozCmCdRepository;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.support.exception.ErrorCode;

@Service
public class CodeServiceImpl implements CodeService{
	
	@Autowired
	MozCmCdRepository mozCmCdRepository;
	
	/**
     * @brief : 코드 리스트 개수 조회
     * @details : 코드 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.01.23
     * @param : mozCmCd
     * @return : 
     */
	@Override
	public int getCodeListCnt(MozCmCd mozCmCd) {
		return mozCmCdRepository.countMozCmCd(mozCmCd);
	}

	/**
     * @brief : 코드 리스트 조회
     * @details : 코드 리스트 조회
     * @author : KC.KIM
     * @date : 2024.01.23
     * @param : mozCmCd
     * @return : 
     */
	@Override
	public List<MozCmCd> getCodeList(MozCmCd mozCmCd) {
		return mozCmCdRepository.findAllMozCmCd(mozCmCd);
	}

	/**
     * @brief : 코드 등록
     * @details : 코드 등록
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : mozCmCd
     * @return : 
     */
	@Override
	public void cmCdSave(MozCmCd mozCmCd) {
		
		if (mozCmCdRepository.countMozCmCdByCdIdForSave(mozCmCd) > 0) {
			throw new CommonException(ErrorCode.DATA_DUPLICATE);
		}
		
		String crtr = LoginOprtrUtils.getOprtrId();

		mozCmCd.setCrtr(crtr);
		//TODO 화면 수정 필요
		mozCmCd.setUseYn("Y");
		mozCmCdRepository.saveMozCmCd(mozCmCd);
	}

	/**
     * @brief : 코드 상세 정보 조회
     * @details : 코드 상세 정보 조회
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : cdId
     * @return : 
     */
	@Override
	public MozCmCd getCodeDetail(String cdId) {
		return mozCmCdRepository.findOneMozCmCd(cdId); 
	}

	/**
     * @brief : 코드 정보 삭제 
     * @details : 코드 정보 삭제 
     * @author : KC.KIM
     * @date : 2024.02.01
     * @param : cdId
     * @return
     */ 
	@Override
	public void cmCdDelete(String cdId) {
		mozCmCdRepository.deleteMozCmCdByCdIdOrCdGroupId(cdId);
	}

	/**
	 * @brief : 코드 정보 삭제 
	 * @details : 코드 정보 삭제 
	 * @author : KC.KIM
	 * @date : 2024.02.01
	 * @param : cdId
	 * @return
	 */ 
	@Override
	public void subCmCdDelete(MozCmCd mozCmCd) {
		mozCmCdRepository.deleteMozCmCdByCdId(mozCmCd);
	}

	/**
     * @brief : 코드 상세 정보 수정 
     * @details : 코드 상세 정보 수정
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : mozCmCd
     * @return
     */ 
	@Override
	public void cmCdModify(MozCmCd mozCmCd) {
		
		if (mozCmCdRepository.countMozCmCdByCdIdForUpdate(mozCmCd) > 0) {
			throw new CommonException(ErrorCode.DATA_DUPLICATE);
		}
		
		mozCmCdRepository.updateMozCmCd(mozCmCd);
	}
	
	/**
	  * @Method Name : getSubCodeDetail
	  * @작성일 : 2024. 2. 20.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 코드 상세정보 하위 코드 리스트 조회
	  * @param cdId
	  * @return
	  */
	@Override
	public List<MozCmCd> getSubCodeDetail(String cdId) {
		return mozCmCdRepository.findAllSubCmcd(cdId);
	}

}
