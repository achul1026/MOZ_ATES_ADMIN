package com.moz.ates.traffic.admin.administmng;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.moz.ates.traffic.common.entity.administrative.MozAdministDip;
import com.moz.ates.traffic.common.entity.administrative.MozCourtDcsn;
import com.moz.ates.traffic.common.entity.law.MozTfcLwAdtnRvsn;
import com.moz.ates.traffic.common.entity.law.MozTfcLwFineInfo;
import com.moz.ates.traffic.common.entity.law.MozTfcLwInfo;

public interface AdministService {
	
    /**
     * @brief : 법원 이송 관리 리스트 개수 조회
     * @details : 법원 이송 관리 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.01.22
     * @param : administDip
     * @return : 
     */
	int getAdministListCnt(MozAdministDip administDip);

	/**
     * @brief : 법원 이송 관리 리스트 조회
     * @details : 법원 이송 관리 리스트 조회
     * @author : KC.KIM
     * @date : 2024.01.22
     * @param : administDip
     * @return : 
     */
	List<MozAdministDip> getAdministList(MozAdministDip administDip);
	
	/**
     * @brief : 최종 판결 정보 등록
     * @details : 최종 판결 정보 등록
     * @author : NK.KIM
     * @date : 2024.02.20
     * @param : mozCourtDcsn
     * @return : 
     */
	void saveCourtDcsn(MozCourtDcsn mozCourtDcsn);
	
	/**
	 * @brief : 최종 판결 정보 수정
	 * @details : 최종 판결 정보 수정
	 * @author : NK.KIM
	 * @date : 2024.02.20
	 * @param : mozCourtDcsn
	 * @return : 
	 */
	void updateCourtDcsn(MozCourtDcsn mozCourtDcsn);
	
    /**
     * @brief : 교통단속 법률관리 리스트 조회
     * @details : 교통단속 법률관리 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcLwInfo
     * @return : 
     */
    List<MozTfcLwInfo> getLawList(MozTfcLwInfo tfcLwInfo);
    
    /**
     * @brief : 교통단속 법률관리 카운트
     * @details : 교통단속 법률관리 카운트
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcLwInfo
     * @return : 
     */
    int getLawListCnt(MozTfcLwInfo tfcLwInfo);
    
    /**
     * @brief : 교통단속 법률관리 등록
     * @details : 교통단속 법률관리 등록
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcLwInfo
     * @return : 
     */
    void lawSave(MozTfcLwInfo tfcLwInfo);
    
    /**
     * @brief : 교통단속 법률관리 상세 조회
     * @details : 교통단속 법률관리 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcLawId
     * @return : 
     */
    MozTfcLwInfo getLawDetail(String tfcLawId);

    /**
     * @brief : 교통단속 법률관리 정보 수정
     * @details : 교통단속 법률관리 정보 수정
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcLawId
     * @return : 
     */
    void deleteLaw(String tfcLawId);

    /**
     * @brief : 교통단속 법률관리 정보 수정
     * @details : 교통단속 법률관리 정보 수정
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcLwInfo
     * @return : 
     */
    void updateLaw(MozTfcLwInfo tfcLwInfo);
	
    /**
     * @brief : 법률 범칙금 목록
     * @details : 법률 범칙금 목록
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : tfcLawId
     * @return : List<MozTfcLwFineInfo>
     */
	List<MozTfcLwFineInfo> getLawFineList(String tfcLawId);

    /**
     * @brief : 법률 추가개정 목록
     * @details : 법률 추가개정 목록
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : tfcLawId
     * @return : List<MozTfcLwAdtnRvsn>
     */
	List<MozTfcLwAdtnRvsn> getLawAdtnRvsnList(String tfcLawId);

    /**
     * @brief : 법률 추가 개정 등록
     * @details : 법률 추가개정 등록
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : MozTfcLwInfo
     */
	void lawRevise(MozTfcLwInfo tfcLwInfo);

	
    /**
     * @brief : 범칙금 추가 개정 등록
     * @details : 범칙금 추가 개정 등록
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : MozTfcLwInfo
     */
	void lawAddFine(MozTfcLwInfo tfcLwInfo);


    /**
     * @brief : 법률 폐지 처리
     * @details : 법률 폐지 처리
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : MozTfcLwInfo
     */
	void lawAbolition(String tfcLawId);

    /**
     * @brief : 법률 복구 처리
     * @details : 법률 복구 처리
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : MozTfcLwInfo
     */
	void lawRecovery(String tfcLawId);

    /**
     * @brief : 범칙금 삭제
     * @details : 범칙금 삭제
     * @author : KY.LEE
     * @date : 2024.02.23
     * @param : MozTfcLwInfo
     */
	void fineDelete(String tfcLawFineId);

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
	void excelDownload(HttpServletResponse resp, MozAdministDip administDip) throws IOException;

}
