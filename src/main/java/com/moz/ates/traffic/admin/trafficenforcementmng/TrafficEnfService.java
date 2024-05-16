package com.moz.ates.traffic.admin.trafficenforcementmng;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.common.entity.driver.MozVioInfo;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfHst;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfMaster;
import com.moz.ates.traffic.common.entity.equipment.MozCameraEnfOrg;
import com.moz.ates.traffic.common.entity.equipment.MozCameraEnfOrgFile;
import com.moz.ates.traffic.common.entity.law.MozTfcLwFineInfo;
import com.moz.ates.traffic.common.entity.law.MozTfcLwInfo;
import com.moz.ates.traffic.common.entity.payment.MozFinePymntInfo;
import com.moz.ates.traffic.common.entity.payment.MozPlPymntInfo;

public interface TrafficEnfService {
    DriverVO getDriverDetail(EnfSearchVO enfSearchVO);

    CarVO getCarDetail(EnfSearchVO enfSearchVO);

    /**
     * @brief : 교통단속 정보 리스트 조회
     * @details : 교통단속 정보 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcEnfMaster
     * @return : 
     */
    List<MozTfcEnfMaster> getInfoList(MozTfcEnfMaster tfcEnfMaster);

    int getInfoListCnt(MozTfcEnfMaster tfcEnfMaster);

    /**
     * @brief : 교통단속 정보 상세 조회
     * @details : 교통단속 정보 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcEnfId
     * @return : 
     */
    MozTfcEnfMaster getTrafficEnfDetail(String tfcEnfId);
    
    /**
     * @brief : 교통단속 정보 등록
     * @details : 교통단속 정보 등록
     * @author : KC.KIM
     * @date : 2024.03.06
     * @param : tfcEnfMaster
     * @param : uploadFiles
     * @return : 
     */
    void insertMozTfcEnfMaster(MozTfcEnfMaster tfcEnfMaster, MultipartFile[] uploadFiles);
    
    /**
     * @brief : 교통단속 정보 수정
     * @details : 교통단속 정보 수정
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param uploadFiles 
     * @param : tfcEnfMaster
     * @return : 
     */
    void updateInfo(MozTfcEnfMaster tfcEnfMaster, MultipartFile[] uploadFiles);
    
    /**
     * @brief : 교통단속 정보 삭제
     * @details : 교통단속 정보 삭제
     * @author : KC.KIM
     * @date : 2024.03.11
     * @param : tfcEnfMaster
     * @return : 
     */
	void deleteTfcEnfMasterByTfcEnfId(MozTfcEnfMaster tfcEnfMaster);
    
    /**
     * @brief : 벌금 정보 수정
     * @details : 벌금 정보 수정
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : finePymntInfo
     * @return : 
     */
    void updateInfoPrice(MozFinePymntInfo finePymntInfo);
    
    /**
     * @brief : 교통단속 로그 리스트 조회
     * @details : 교통단속 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcEnfHst
     * @return : 
     */
    List getLogList(MozTfcEnfHst tfcEnfHst);

    int getLogListCnt(MozTfcEnfHst tfcEnfHst);
    
    /**
     * @brief : 위반 차량 사진 삭제
     * @details : 위반 차량 사진 삭제
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcEnfMaster
     * @return : 
     */
    void deleteEnfImage(MozTfcEnfMaster tfcEnfMaster);

    /**
     * @brief : 교통단속 로그 상세 조회
     * @details : 교통단속 로그 상세 조회
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : hstId
     * @return : MozTfcEnfHst
     */
	public MozTfcEnfHst getLogDetail(String hstId);

	List<MozTfcLwInfo> getTrafficLawsListByNotNullFineInfo();

	List<MozPlPymntInfo> getPlacePaymentList();

	List<MozTfcLwFineInfo> getLawFineInfoList(String tfcLawId);

	void deleteTfcEnfMaster(MozTfcEnfMaster tfcEnfMaster);

	/**
     * @brief : getViolationCount
     * @details : 단속카메라 단속대상 목록 카운트 
     * @author : KY.LEE
     * @date : 2024.04.06
     * @param : mozCameraEnfOrg
     */
	int getViolationCount(MozCameraEnfOrg mozCameraEnfOrg);

	/**
	 * @brief : getViolationList
	 * @details : 단속 카메라 단속대상 목록
	 * @author : KY.LEE
	 * @date : 2024.04.06
	 * @param : mozCameraEnfOrg
	 */
	List<MozCameraEnfOrg> getViolationList(MozCameraEnfOrg mozCameraEnfOrg);

	/**
	 * @brief : getViolationDetail
	 * @details : 단속 카메라 단속대상 상세
	 * @author : KY.LEE
	 * @date : 2024.04.06
	 * @param : mozCameraEnfOrg
	 */
	MozCameraEnfOrg getViolationDetail(Long idx);

	/**
	 * @brief : getViolationImageList
	 * @details : 단속 카메라 이미지 목록 조회
	 * @author : KY.LEE
	 * @date : 2024.04.06
	 * @param : MozCameraEnfOrgFile
	 */
	List<MozCameraEnfOrgFile> getViolationImageList(Long idx);

	/**
	 * @brief : getViolationImage
	 * @details : 단속 카메라 이미지 파일 경로 조회
	 * @author : KY.LEE
	 * @date : 2024.04.06
	 * @param : MozCameraEnfOrgFile
	 */
	String getViolationImage(Long idx);


	/**
	 * @brief : insertMozTfcEnfMasterForEquipment
	 * @details : 단속 카메라 단속 등록
	 * @author : KY.LEE
	 * @date : 2024.04.09
	 * @param : MozTfcEnfMaster
	 */
	public void insertMozTfcEnfMasterForEquipment(MozTfcEnfMaster tfcEnfMaster);

	/**
	 * @brief : 위반자 정보 조회
	 * @details : 위반자 정보 조회
	 * @author : KY.LEE
	 * @date : 2024.04.09
	 * @param : driverLicenseId
	 */
	List<MozVioInfo> getViolationInfoList(String dvrLcenId);

	/**
	 * @brief : 위반자 정보 조회
	 * @details : 위반자 정보 조회
	 * @author : KY.LEE
	 * @date : 2024.05.02
	 * @param : docNid
	 */
	List<MozVioInfo> getViolationInfoListByDocNid(String docNid);
}
