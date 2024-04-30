package com.moz.ates.traffic.admin.sitemng.code;

import java.util.List;

import com.moz.ates.traffic.common.entity.common.MozCmCd;

public interface CodeService {

	/**
     * @brief : 코드 리스트 조회
     * @details : 코드 리스트 조회
     * @author : KC.KIM
     * @date : 2024.01.23
     * @param : mozCmCd
     * @return : 
     */
	public int getCodeListCnt(MozCmCd mozCmCd);

	/**
     * @brief : 코드 리스트 개수 조회
     * @details : 코드 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.01.23
     * @param : mozCmCd
     * @return : 
     */
	public List<MozCmCd> getCodeList(MozCmCd mozCmCd);

	/**
     * @brief : 코드 등록
     * @details : 코드 등록
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : mozCmCd
     * @return : 
     */
	public void cmCdSave(MozCmCd mozCmCd);

	/**
     * @brief : 코드 상세 정보 조회
     * @details : 코드 상세 정보 조회
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : cdId
     * @return : 
     */
	public MozCmCd getCodeDetail(String cdId);

	/**
     * @brief : 코드 정보 삭제 
     * @details : 코드 정보 삭제 
     * @author : KC.KIM
     * @date : 2024.02.01
     * @param : cdId
     * @return
     */ 
	public void cmCdDelete(String cdId);

	/**
     * @brief : 코드 상세 정보 수정 
     * @details : 코드 상세 정보 수정
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : mozCmCd
     * @return
     */ 
	public void cmCdModify(MozCmCd mozCmCd);

	/**
	  * @Method Name : getSubCodeDetail
	  * @작성일 : 2024. 2. 20.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 코드 상세정보 하위 코드 목록 조회
	  * @param cdId
	  * @return
	  */
	public List<MozCmCd> getSubCodeDetail(String cdId);

	/**
	 * @brief : 코드 정보 삭제 
	 * @details : 코드 정보 삭제 
	 * @author : KC.KIM
	 * @date : 2024.02.01
	 * @param : cdId
	 * @return
	 */
	public void subCmCdDelete(MozCmCd mozCmCd);

}
