package com.moz.ates.traffic.admin.trafficequipmentmng;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.admin.common.FeaturesLayerDTO;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpMaster;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEqpMntnHst;
import com.moz.ates.traffic.common.entity.equipment.MozTfcFacilityMaster;
import com.moz.ates.traffic.common.entity.equipment.MozTfcFacilityMntnHst;

public interface TrafficEqpService {
    /**
     * @brief : 단속장비 아이디 중복 조회
     * @details : 단속장비 아이디 중복 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : 
     * @return : 
     */
	int getEqpDupliCnt(MozTfcEnfEqpMaster tfcEnfEqpMaster);
	
    /**
     * @brief : 단속장비 등록 
     * @details : 단속장비 등록 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpMaster
     * @param : uploadFiles
     * @return : 
     */
    void registEqp(MozTfcEnfEqpMaster tfcEnfEqpMaster, MultipartFile[] uploadFiles);
    
    /**
     * @brief : 단속장비 상세 조회
     * @details : 단속장비 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpId
     * @return : 
     */
    MozTfcEnfEqpMaster getEqpDetail(String tfcEnfEqpId);
    
    /**
     * @brief : 단속장비 정보 수정
     * @details : 단속장비 정보 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpMaster
     * @param : uploadFiles
     * @return : 
     */
    void updateEqp(MozTfcEnfEqpMaster tfcEnfEqpMaster, MultipartFile[] uploadFiles) throws IOException;
    
    /**
     * @brief : 단속장비 정보 삭제
     * @details : 단속장비 정보 삭제
     * @author : KC.KIM
     * @date : 2024.03.04
     * @param : tfcEnfEqpId
     * @return : 
     */
	void deleteTfcEnfEqpMatser(String tfcEnfEqpId);
    
    void saveFile(String uploadDir, String fileName,MultipartFile multipartFile) throws IOException;
    
    /**
     * @brief : 단속장비 이미지 삭제
     * @details : 단속장비 이미지 삭제
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpMaster
     * @return : 
     */
    void updateEqpImage(MozTfcEnfEqpMaster tfcEnfEqpMaster);

    /**
     * @brief : 단속장비 리스트 조회
     * @details : 단속장비 리스트 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcEnfEqpMaster
     * @return : 
     */
	List<MozTfcEnfEqpMaster> getEqpList(MozTfcEnfEqpMaster tfcEnfEqpMaster);

    /**
     * @brief : 단속장비 리스트 개수 조회
     * @details : 단속장비 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcEnfEqpMaster
     * @return : 
     */
	int getEqpListCnt(MozTfcEnfEqpMaster tfcEnfEqpMaster);

	/**
     * @brief : 교통시설물 리스트 개수 조회
     * @details : 교통시설물 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.01.25
     * @param : tfcFacilityMaster
     * @return : 
     */
	int getFacilityListCnt(MozTfcFacilityMaster tfcFacilityMaster);

	/**
     * @brief : 교통시설물 리스트 조회
     * @details : 교통시설물 리스트 조회
     * @author : KC.KIM
     * @date : 2024.01.25
     * @param : tfcFacilityMaster
     * @return : 
     */
	List<MozTfcFacilityMaster> getFacilityList(MozTfcFacilityMaster tfcFacilityMaster);

	/**
     * @brief : 교통시설물 상세 조회
     * @details : 교통시설물 상세
     * @author : KC.KIM
     * @date : 2024.03.04
     * @param : tfcFacilityId
     * @return : 
     */
	MozTfcFacilityMaster getFacilityDetail(String tfcFacilityId);

	/**
     * @brief : 교통시설물 등록
     * @details : 교통시설물 등록
     * @author : KC.KIM
     * @date : 2024.03.04
     * @param : tfcFacilityMaster
     * @param : uploadFiles
     * @return : 
     */
	void registTfcFacility(MozTfcFacilityMaster tfcFacilityMaster, MultipartFile[] uploadFiles);

	/**
     * @brief : 교통시설물 삭제
     * @details : 교통시설물 삭제
     * @author : KC.KIM
     * @date : 2024.03.04
     * @param : tfcFacilityId
     * @return : 
     */
	void deleteTfcFacilityMaster(String tfcFacilityId);

	/**
     * @brief : 교통시설물 수정
     * @details : 교통시설물 수정
     * @author : KC.KIM
     * @date : 2024.03.04
     * @param : tfcFacilityMaster
     * @param : uploadFiles
     * @return : 
     * @throws IOException
     */
	void updateMoztfcFacility(MozTfcFacilityMaster tfcFacilityMaster, MultipartFile[] uploadFiles);

	/**
     * @brief : 교통시설물 GeoJson 조회
     * @details : 교통시설물 GeoJson 조회
     * @author : CM.KIM
     * @date : 2024.04.24
     * @param : getFacilityGeoJson
     * @return : FeaturesLayerDTO
     */
	FeaturesLayerDTO getFacilityGeoJson(Map<String,String> param);

	/**
     * @brief : 교통시설물 GeoJson 조회
     * @details : 교통시설물 GeoJson 조회
     * @author : CM.KIM
     * @date : 2024.04.24
     * @param : getFacilityGeoJson
     * @return : FeaturesLayerDTO
     */
	FeaturesLayerDTO getEquipmentGeoJson(Map<String,String> param);
	
	/**
	 * @brief : 교통단속 GeoJson 조회
	 * @details : 교통단속 GeoJson 조회
	 * @author : NK.KIM
	 * @date : 2024.04.25
	 * @param : getFacilityGeoJson
	 * @return : FeaturesLayerDTO
	 */
	FeaturesLayerDTO getEnforcementGeoJson(Map<String,String> param);
	
	/**
	 * @brief : 교통사고 GeoJson 조회
	 * @details : 교통사고 GeoJson 조회
	 * @author : NK.KIM
	 * @date : 2024.04.25
	 * @param : getAccidentGeoJson
	 * @return : FeaturesLayerDTO
	 */
	FeaturesLayerDTO getAccidentGeoJson(Map<String,String> param);

	/**
     * @brief : 교통시설물 유지보수 이력 조회
     * @details : 교통시설물 유지보수 이력 조회
     * @author : KY.LEE
     * @date : 2024.04.24
     * @param : getEqpMntnHstList
     */
	List<MozTfcEqpMntnHst> getEqpMntnHstList(String tfcEnfEqpId);

	/**
     * @brief : 교통시설물 유지보수 이력 삭제
     * @details : 교통시설물 유지보수 이력 삭제
     * @author : KY.LEE
     * @date : 2024.04.24
     * @param : deleteEqpHist
     */
	void deleteEqpHist(String mntnHstId);

	/**
     * @brief : 교통시설물 유지보수 이력 조회
     * @details : 교통시설물 유지보수 이력 조회
     * @author : KY.LEE
     * @date : 2024.04.24
     * @param : getFacilityMntnHstList
     */
	List<MozTfcFacilityMntnHst> getFacilityMntnHstList(String tfcFacilityId);


	/**
     * @brief : 교통시설물 유지보수 이력 등록
     * @details : 교통시설물 유지보수 이력 등록
     * @author : KY.LEE
     * @date : 2024.04.24
     * @param : saveFacilityMaintenance
     */
	public void saveFacilityMaintenance(MozTfcFacilityMntnHst mozTfcFacilityMntnHst);
	
	/**
     * @brief : 감시카메라 유지보수 이력 등록
     * @details : 감시카메라 유지보수 이력 등록
     * @author : KY.LEE
     * @date : 2024.04.24
     * @param : saveEquipmentMaintenance
     */
	 public void saveEquipmentMaintenance(MozTfcEqpMntnHst mozTfcEqpMntnHst);
	 
	/**
     * @brief : 교통시설물 유지보수 삭제
     * @details : 교통시설물 유지보수 삭제
     * @author : KY.LEE
     * @date : 2024.04.24
     * @param : deleteFacilityHist
     */
	public void deleteFacilityHist(String tfcFacilityLogId);
}
