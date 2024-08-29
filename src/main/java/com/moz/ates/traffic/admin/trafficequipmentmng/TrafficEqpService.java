package com.moz.ates.traffic.admin.trafficequipmentmng;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.moz.ates.traffic.common.repository.accident.MozTfcAcdntTrgtInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.admin.common.FeaturesLayerDTO;
import com.moz.ates.traffic.admin.common.GeoJsonDTO;
import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.common.component.FileUploadComponent;
import com.moz.ates.traffic.common.entity.common.UploadFileInfo;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpFileInfo;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpMaster;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEqpMntnHst;
import com.moz.ates.traffic.common.entity.equipment.MozTfcFacilityFileInfo;
import com.moz.ates.traffic.common.entity.equipment.MozTfcFacilityMaster;
import com.moz.ates.traffic.common.entity.equipment.MozTfcFacilityMntnHst;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfEqpFileInfoRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfEqpMasterRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEqpMntnHstRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcFacilityFileInfoRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcFacilityMasterRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcFacilityMntnHstRepository;
import com.moz.ates.traffic.common.repository.monitoring.MonitoringMapRepository;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class TrafficEqpService {
	
	@Autowired
    MozTfcEnfEqpMasterRepository tfcEnfEqpMasterRepository;
    
    @Autowired
    MozTfcFacilityMasterRepository mozTfcFacilityMasterRepository;
    
    @Autowired
    MozTfcFacilityFileInfoRepository tfcFacilityFileInfoRepository;
    
    @Autowired
    MozTfcEnfEqpFileInfoRepository tfcEnfEqpFileInfoRepository;

	@Autowired
	MonitoringMapRepository monitoringMapRepository;
	
	@Autowired
	MozTfcEqpMntnHstRepository mozTfcEqpMntnHstRepository;
    
	@Autowired
	MozTfcFacilityMntnHstRepository mozTfcFacilityMntnHstRepository;
	
    @Autowired
    FileUploadComponent fileUploadComponent;
    @Autowired
    private MozTfcFacilityFileInfoRepository mozTfcFacilityFileInfoRepository;
	private MozTfcAcdntTrgtInfoRepository mozTfcAcdntTrgtInfoRepository;

	/**
     * @brief : 단속장비 아이디 중복 조회
     * @details : 단속장비 아이디 중복 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : 
     * @return : 
     */
    public int getEqpDupliCnt(String tfcEnfEqpId) {
    	return tfcEnfEqpMasterRepository.countEqpDuplicateCnt(tfcEnfEqpId);
    }
    
    /**
     * @brief : 단속장비 등록 
     * @details : 단속장비 등록 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpMaster
     * @param : uploadFiles
     * @return : 
     */
    @Transactional
    public void registEqp(MozTfcEnfEqpMaster tfcEnfEqpMaster, MultipartFile[] uploadFiles){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	if(!MozatesCommonUtils.isNull(tfcEnfEqpMaster.getInstlDate())) {
			tfcEnfEqpMaster.setInstlDateString(formatter.format(tfcEnfEqpMaster.getInstlDate()));  		
    	}
    	
    	// 장비 정보 저장
    	tfcEnfEqpMasterRepository.insertTfcEnfEqpMaster(tfcEnfEqpMaster);
    	// 장비 이미지 저장
    	if(uploadFiles != null && uploadFiles.length >= 1) {
			for(MultipartFile file : uploadFiles) {
				if(!MozatesCommonUtils.isNull(file.getOriginalFilename())) {
					String[] extArr = {"jpg", "jpeg", "png", "PNG"};
					UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(file, extArr);
					MozTfcEnfEqpFileInfo fileInfo = new MozTfcEnfEqpFileInfo();
					String eqpFileNo = MozatesCommonUtils.getUuid();
					fileInfo.setEqpFileNo(eqpFileNo);
					fileInfo.setTfcEnfEqpId(tfcEnfEqpMaster.getTfcEnfEqpId());
					fileInfo.setFilePath(uploadFileInfo.getFilePath());
					fileInfo.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
					fileInfo.setFileExt(uploadFileInfo.getFileExt());
					fileInfo.setFileNm(uploadFileInfo.getFileNm());
					fileInfo.setFileSize(uploadFileInfo.getFileSize());
					fileInfo.setCrtr(LoginOprtrUtils.getOprtrId());
					tfcEnfEqpFileInfoRepository.insertMozTfcEnfEqpFileInfo(fileInfo);
				}
			}
		}
    }

    /**
     * @brief : 단속장비 상세 조회
     * @details : 단속장비 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpId
     * @return : 
     */
    public MozTfcEnfEqpMaster getEqpDetail(String tfcEnfEqpId) {
    	return tfcEnfEqpMasterRepository.findOneEqpDetail(tfcEnfEqpId);
    }
    
    /**
     * @brief : 단속장비 정보 수정
     * @details : 단속장비 정보 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpMaster
     * @param : uploadFiles
     * @return : 
     */
    @Transactional
    public void updateEqp(MozTfcEnfEqpMaster tfcEnfEqpMaster, MultipartFile[] uploadFiles) throws IOException {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	
    	if(tfcEnfEqpMaster.getTfcEnfEqpFileInfoList() != null) {
    		for(MozTfcEnfEqpFileInfo fileItem : tfcEnfEqpMaster.getTfcEnfEqpFileInfoList()) {
    			MozTfcEnfEqpFileInfo orgFileInfo = tfcEnfEqpFileInfoRepository.findOneMozMozTfcEnfEqpFileInfoByEqpFileNo(fileItem.getEqpFileNo());
    			fileUploadComponent.deleteUploadFile(orgFileInfo.getFilePath());
    			tfcEnfEqpFileInfoRepository.deleteMozTfcEnfEqpFileInfooByEqpFileNo(fileItem.getEqpFileNo());
    		}
    	}

		// 장비 ID 변경된 경우
		if (!tfcEnfEqpMaster.getTfcEnfEqpId().equals(tfcEnfEqpMaster.getNewTfcEnfEqpId())) {
			// 파일 테이블 장비 ID 업데이트
			List<MozTfcEnfEqpFileInfo> fileList = tfcEnfEqpFileInfoRepository.findAllByTfcEnfEqpId(tfcEnfEqpMaster.getTfcEnfEqpId());
			if (!fileList.isEmpty()) {
				tfcEnfEqpFileInfoRepository.updateAllByTfcEnfEqpIdAndNewTfcEnfEqpId(tfcEnfEqpMaster.getTfcEnfEqpId(), tfcEnfEqpMaster.getNewTfcEnfEqpId());
			}

			// 유지보수 이력 테이블 장비 ID 업데이트
			List<MozTfcEqpMntnHst> mntnHstList = mozTfcEqpMntnHstRepository.findAllEqpMntnHstList(tfcEnfEqpMaster.getTfcEnfEqpId());
			if (!mntnHstList.isEmpty()) {
				mozTfcEqpMntnHstRepository.updateByTfcEnfEqpIdAndNewTfcEnfEqpId(tfcEnfEqpMaster.getTfcEnfEqpId(), tfcEnfEqpMaster.getNewTfcEnfEqpId());
			}
		}

		if(uploadFiles != null && uploadFiles.length >= 1) {
			for(MultipartFile file : uploadFiles) {
				if(!MozatesCommonUtils.isNull(file.getOriginalFilename())) {
					String[] extArr = {"jpg", "jpeg", "png", "PNG"};
					UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(file, extArr); 
					MozTfcEnfEqpFileInfo fileInfo = new MozTfcEnfEqpFileInfo();
					String eqpFileNo = MozatesCommonUtils.getUuid();
					fileInfo.setEqpFileNo(eqpFileNo);
					fileInfo.setTfcEnfEqpId(tfcEnfEqpMaster.getNewTfcEnfEqpId());
					fileInfo.setFilePath(uploadFileInfo.getFilePath());
					fileInfo.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
					fileInfo.setFileExt(uploadFileInfo.getFileExt());
					fileInfo.setFileNm(uploadFileInfo.getFileNm());
					fileInfo.setFileSize(uploadFileInfo.getFileSize());
					fileInfo.setCrtr(LoginOprtrUtils.getOprtrId());
					tfcEnfEqpFileInfoRepository.insertMozTfcEnfEqpFileInfo(fileInfo);					
				}
			}
		}
    	
		if(!MozatesCommonUtils.isNull(tfcEnfEqpMaster.getInstlDate())) {
			tfcEnfEqpMaster.setInstlDateString(formatter.format(tfcEnfEqpMaster.getInstlDate()));  		
    	}
		
    	tfcEnfEqpMasterRepository.updateEqp(tfcEnfEqpMaster);
    }
    
    /**
     * @brief : 단속장비 정보 삭제
     * @details : 단속장비 정보 삭제
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpMaster
     * @param : imageFiles
     * @return : 
     */
	@Transactional
	public void deleteTfcEnfEqpMaster(String tfcEnfEqpId) {

		List<MozTfcEnfEqpFileInfo> eqpFileList = tfcEnfEqpFileInfoRepository.findAllByTfcEnfEqpId(tfcEnfEqpId);
		if (!eqpFileList.isEmpty()) {
			for(MozTfcEnfEqpFileInfo fileItem : eqpFileList) {
				fileUploadComponent.deleteUploadFile(fileItem.getFilePath());
				tfcEnfEqpFileInfoRepository.deleteMozTfcEnfEqpFileInfooByEqpFileNo(fileItem.getEqpFileNo());
			}
		}

		tfcEnfEqpFileInfoRepository.deleteMozTfcEnfEqpFileInfooByTfcEnfEqpId(tfcEnfEqpId);
		tfcEnfEqpMasterRepository.deleteTfcEnfEqpMasterByTfcEnfEqpId(tfcEnfEqpId);
		mozTfcEqpMntnHstRepository.deleteMozTfcEqpMntnHstByTfcEnfEqpId(tfcEnfEqpId);
	}

    /**
     * @brief : 파일 등록
     * @details : 파일 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : saveFile
     */
	public void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
         
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {        
            throw new IOException("Could not save image file: " + fileName, ioe);
        }      
    }

    /**
     * @brief : 단속장비 리스트 조회
     * @details : 단속장비 리스트 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcEnfEqpMaster
     * @return : 
     */
	public List<MozTfcEnfEqpMaster> getEqpList(MozTfcEnfEqpMaster tfcEnfEqpMaster) {
		return tfcEnfEqpMasterRepository.findAllMozTfcEnfEqpMaster(tfcEnfEqpMaster);
	}

    /**
     * @brief : 단속장비 리스트 개수 조회
     * @details : 단속장비 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcEnfEqpMaster
     * @return : 
     */
	public int getEqpListCnt(MozTfcEnfEqpMaster tfcEnfEqpMaster) {
		return tfcEnfEqpMasterRepository.countMozTfcEnfEqpMaster(tfcEnfEqpMaster);
	}

	/**
     * @brief : 교통시설물 리스트 개수 조회
     * @details : 교통시설물 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.01.25
     * @param : tfcFacilityMaster
     * @return : 
     */
	public int getFacilityListCnt(MozTfcFacilityMaster tfcFacilityMaster) {
		return mozTfcFacilityMasterRepository.countMozTfcFacility(tfcFacilityMaster);
	}

	/**
     * @brief : 교통시설물 리스트 조회
     * @details : 교통시설물 리스트 조회
     * @author : KC.KIM
     * @date : 2024.01.25
     * @param : tfcFacilityMaster
     * @return : 
     */
	public List<MozTfcFacilityMaster> getFacilityList(MozTfcFacilityMaster tfcFacilityMaster) {
		return mozTfcFacilityMasterRepository.findAllMozFacility(tfcFacilityMaster);
	}

	/**
     * @brief : 교통시설물 상세 조회
     * @details : 교통시설물 상세
     * @author : KC.KIM
     * @date : 2024.03.04
     * @param : tfcFacilityId
     * @return : 
     */
	public MozTfcFacilityMaster getFacilityDetail(String tfcFacilityId) {
		return mozTfcFacilityMasterRepository.findOneMozTfcFacilityBytfcFacilityId(tfcFacilityId);
	}

	/**
     * @brief : 교통시설물 등록
     * @details : 교통시설물 등록 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcFacilityMaster
     * @param : uploadFiles
     * @return : 
     */
	@Transactional
	public void registTfcFacility(MozTfcFacilityMaster tfcFacilityMaster, MultipartFile[] uploadFiles){
		String tfcFacilityId = MozatesCommonUtils.getUuid();

		tfcFacilityMaster.setTfcFacilityId(tfcFacilityId);
		tfcFacilityMaster.setCrtr(LoginOprtrUtils.getOprtrId());
		mozTfcFacilityMasterRepository.insertTfcFacilityMaster(tfcFacilityMaster);

		if(uploadFiles != null && uploadFiles.length >= 1) {
			for(MultipartFile file : uploadFiles) {
				if(!MozatesCommonUtils.isNull(file.getOriginalFilename())) {
					String[] extArr = {"jpg", "jpeg", "png", "PNG"};
					UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(file, extArr); 
					MozTfcFacilityFileInfo tfcFacilityFileInfo = new MozTfcFacilityFileInfo();
					String tfcFacilityFileNo = MozatesCommonUtils.getUuid();
					tfcFacilityFileInfo.setTfcFacilityFileNo(tfcFacilityFileNo);
					tfcFacilityFileInfo.setTfcFacilityId(tfcFacilityId);
					tfcFacilityFileInfo.setFilePath(uploadFileInfo.getFilePath());
					tfcFacilityFileInfo.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
					tfcFacilityFileInfo.setFileExt(uploadFileInfo.getFileExt());
					tfcFacilityFileInfo.setFileNm(uploadFileInfo.getFileNm());
					tfcFacilityFileInfo.setFileSize(uploadFileInfo.getFileSize());
					tfcFacilityFileInfo.setCrtr(LoginOprtrUtils.getOprtrId());
					tfcFacilityFileInfoRepository.insertMozTfcFacilityFileInfo(tfcFacilityFileInfo);
				}
			}
		}
	}

	/**
     * @brief : 교통시설물 삭제
     * @details : 교통시설물 삭제
     * @author : KC.KIM
     * @date : 2024.03.04
     * @param : tfcFacilityId
     * @return : 
     */
	@Transactional
	public void deleteTfcFacilityMaster(String tfcFacilityId) {

		List<MozTfcFacilityFileInfo> facilityFileInfoList = mozTfcFacilityFileInfoRepository.findAllByTfcFacilityId(tfcFacilityId);
		if (!facilityFileInfoList.isEmpty()) {
			for(MozTfcFacilityFileInfo fileItem : facilityFileInfoList) {
				fileUploadComponent.deleteUploadFile(fileItem.getFilePath());
				mozTfcFacilityFileInfoRepository.deleteMozTfcFacilityFileInfoByTfcFacilityFileNo(fileItem.getTfcFacilityFileNo());
			}
		}

		mozTfcFacilityMntnHstRepository.deleteMozTfcFacilityMntnHstByTfcFacilityId(tfcFacilityId);
		tfcFacilityFileInfoRepository.deleteMozTfcFacilityFileInfoByTfcFacilityId(tfcFacilityId);
		mozTfcFacilityMasterRepository.deleteTfcFacilityMasterByTfcFacilityId(tfcFacilityId);
	}

	/**
     * @brief : 교통시설물 수정
     * @details : 교통시설물 수정
     * @author : KC.KIM
     * @date : 2024.03.04
     * @param : tfcFacilityMaster
     * @param : uploadFiles
     * @return : 
     */
	@Transactional
	public void updateMoztfcFacility(MozTfcFacilityMaster tfcFacilityMaster, MultipartFile[] uploadFiles){
		if(tfcFacilityMaster.getTfcFacilityFileInfoList() != null) {
			for(MozTfcFacilityFileInfo fileItem : tfcFacilityMaster.getTfcFacilityFileInfoList()) {
				MozTfcFacilityFileInfo orgFileInfo = tfcFacilityFileInfoRepository.findOneMozTfcFacilityFileInfoByTfcFacilityFileNo(fileItem.getTfcFacilityFileNo());
				fileUploadComponent.deleteUploadFile(orgFileInfo.getFilePath());
				tfcFacilityFileInfoRepository.deleteMozTfcFacilityFileInfoByTfcFacilityFileNo(fileItem.getTfcFacilityFileNo());
			}
		}
		if(uploadFiles != null && uploadFiles.length >= 1) {
			for(MultipartFile file : uploadFiles) {
				if(!MozatesCommonUtils.isNull(file.getOriginalFilename())) {
					String[] extArr = {"jpg", "jpeg", "png", "PNG"};
					UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(file, extArr); 
					MozTfcFacilityFileInfo tfcFacilityFileInfo = new MozTfcFacilityFileInfo();
					String tfcFacilityFileNo = MozatesCommonUtils.getUuid();
					tfcFacilityFileInfo.setTfcFacilityFileNo(tfcFacilityFileNo);
					tfcFacilityFileInfo.setTfcFacilityId(tfcFacilityMaster.getTfcFacilityId());
					tfcFacilityFileInfo.setFilePath(uploadFileInfo.getFilePath());
					tfcFacilityFileInfo.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
					tfcFacilityFileInfo.setFileExt(uploadFileInfo.getFileExt());
					tfcFacilityFileInfo.setFileNm(uploadFileInfo.getFileNm());
					tfcFacilityFileInfo.setFileSize(uploadFileInfo.getFileSize());
					tfcFacilityFileInfo.setCrtr(LoginOprtrUtils.getOprtrId());
					tfcFacilityFileInfoRepository.insertMozTfcFacilityFileInfo(tfcFacilityFileInfo);					
				}
			}
		}
		mozTfcFacilityMasterRepository.updateMozTfcFacilityMaster(tfcFacilityMaster);
	}

    /**
     * @brief : 교통시설물 GeoJSON조회
     * @details : 교통시설물 GeoJSON조회
     * @author : KY.LEE
     * @date : 2023.05.31
     */
	public FeaturesLayerDTO getFacilityGeoJson(Map<String, String> param) {
		FeaturesLayerDTO featuresLayerDTO = new FeaturesLayerDTO();
		List<GeoJsonDTO.FeaturesDTO> featuresList = new ArrayList<>();
		List<Map<String, Object>> facilityList = monitoringMapRepository.findAllFacilityByFacilityTy(param);
		return getFeaturesLayerDTO(featuresLayerDTO, featuresList, facilityList);
	}

    /**
     * @brief : 교통단속장비 GeoJSON조회
     * @details : 교통단속장비 GeoJSON조회
     * @author : KY.LEE
     * @date : 2023.05.31
     */
	public FeaturesLayerDTO getEquipmentGeoJson(Map<String, String> param) {
		FeaturesLayerDTO featuresLayerDTO = new FeaturesLayerDTO();
		List<GeoJsonDTO.FeaturesDTO> featuresList = new ArrayList<>();
		List<Map<String, Object>> equipmentList = monitoringMapRepository.findAllEquipmentByEquipmentTy(param);
		return getFeaturesLayerDTO(featuresLayerDTO, featuresList, equipmentList);
	}
	
    /**
     * @brief : 교통단속 GeoJSON조회
     * @details : 교통단속 GeoJSON조회
     * @author : KY.LEE
     * @date : 2023.05.31
     */
	public FeaturesLayerDTO getEnforcementGeoJson(Map<String, String> param) {
		FeaturesLayerDTO featuresLayerDTO = new FeaturesLayerDTO();
		List<GeoJsonDTO.FeaturesDTO> featuresList = new ArrayList<>();
		List<Map<String, Object>> enforcementList = monitoringMapRepository.findAllEnforcementByDate(param);
		return getFeaturesLayerDTO(featuresLayerDTO, featuresList, enforcementList);
	}
	
    /**
     * @brief : 교통사고 GeoJSON조회
     * @details : 교통사고 GeoJSON조회
     * @author : KY.LEE
     * @date : 2023.05.31
     */
	public FeaturesLayerDTO getAccidentGeoJson(Map<String, String> param) {
		FeaturesLayerDTO featuresLayerDTO = new FeaturesLayerDTO();
		List<GeoJsonDTO.FeaturesDTO> featuresList = new ArrayList<>();
		List<Map<String, Object>> accidentList = monitoringMapRepository.findAllAccidentByDateForAdminDashboard(param);
		return getFeaturesLayerDTO(featuresLayerDTO, featuresList, accidentList);
	}

    /**
     * @brief : 레이어 조회
     * @details : 레이어 조회
     * @author : KY.LEE
     * @date : 2023.05.31
     */
	private FeaturesLayerDTO getFeaturesLayerDTO(FeaturesLayerDTO featuresLayerDTO, List<GeoJsonDTO.FeaturesDTO> featuresList, List<Map<String, Object>> facilityList) {
		facilityList.forEach(map -> {
			GeoJsonDTO.FeaturesDTO featuresDomain = new GeoJsonDTO.FeaturesDTO();
			GeoJsonDTO.GeometryDTO geometryDomain = new GeoJsonDTO.GeometryDTO();
			geometryDomain.setType("Point");
			geometryDomain.setLng(Double.parseDouble((String) map.get("LNG")));
			geometryDomain.setLat(Double.parseDouble((String) map.get("LAT")));

			featuresDomain.setGeometry(geometryDomain);
			featuresDomain.setProperties(map);
			featuresList.add(featuresDomain);
		});
		featuresLayerDTO.setFeatures(featuresList);
		return featuresLayerDTO;
	}

	
	/**
     * @brief : 교통시설물 유지보수 이력 조회
     * @details : 교통시설물 유지보수 이력 조회
     * @author : KY.LEE
     * @date : 2024.04.24
     * @param : getEqpMntnHstList
     */
	public List<MozTfcEqpMntnHst> getEqpMntnHstList(String tfcEnfEqpId) {
		return mozTfcEqpMntnHstRepository.findAllEqpMntnHstList(tfcEnfEqpId);
	}

	
	/**
     * @brief : 교통시설물 유지보수 이력 삭제
     * @details : 교통시설물 유지보수 이력 삭제
     * @author : KY.LEE
     * @date : 2024.04.24
     * @param : deleteEqpHist
     */
	public void deleteEqpHist(String mntnHstId) {
		mozTfcEqpMntnHstRepository.deleteMozTfcEqpMntnHstByMntnHstId(mntnHstId);
	}

	/**
     * @brief : 교통시설물 유지보수 이력 조회
     * @details : getFacilityMntnHstList
     * @author : KY.LEE
     * @date : 2024.04.24
     * @param : tfcFacilityId
     */
	public List<MozTfcFacilityMntnHst> getFacilityMntnHstList(String tfcFacilityId) {
		return mozTfcFacilityMntnHstRepository.findAllByTfcFacilityId(tfcFacilityId);
	}
	
	/**
	 * @brief : 교통시설물 유지보수 이력 등록
	 * @details : 교통시설물 유지보수 이력 등록
	 * @author : KY.LEE
	 * @date : 2024.04.24
	 * @param : saveMaintenance
	 */
	public void saveEquipmentMaintenance(MozTfcEqpMntnHst mozTfcEqpMntnHst) {
		mozTfcEqpMntnHst.setMntnHstId(MozatesCommonUtils.getUuid());
		mozTfcEqpMntnHst.setOprtrId(LoginOprtrUtils.getOprtrId());
		mozTfcEqpMntnHst.setCrtr(LoginOprtrUtils.getOprtrId());
		mozTfcEqpMntnHstRepository.saveTfcEqpMntnHst(mozTfcEqpMntnHst);
	}

	/**
     * @brief : 교통시설물 유지보수 이력 등록
     * @details : 교통시설물 유지보수 이력 등록
     * @author : KY.LEE
     * @date : 2024.04.24
     * @param : saveFacilityMaintenance
     */
	public void saveFacilityMaintenance(MozTfcFacilityMntnHst mozTfcFacilityMntnHst) {
		mozTfcFacilityMntnHst.setTfcFacilityLogId(MozatesCommonUtils.getUuid());
		mozTfcFacilityMntnHst.setOprtrId(LoginOprtrUtils.getOprtrId());
		mozTfcFacilityMntnHst.setCrtr(LoginOprtrUtils.getOprtrId());
		mozTfcFacilityMntnHstRepository.saveMozTfcFacilityMntnHst(mozTfcFacilityMntnHst);
	}
	 
	/**
    * @brief : 교통시설물 유지보수 삭제
    * @details : 교통시설물 유지보수 삭제
    * @author : KY.LEE
    * @date : 2024.04.24
    * @param : deleteFacilityHist
    */
	public void deleteFacilityHist(String tfcFacilityLogId) {
		mozTfcFacilityMntnHstRepository.deleteMozTfcFacilityMntnHstByTfcFacilityLogId(tfcFacilityLogId);
	}

	/**
	 * @breif : 유관기관 카운트 조회
	 * @date : 2024.07.15
	 * @param tfcFacilityMaster
	 * @return
	 */
	public int getOrganizationCount(MozTfcFacilityMaster tfcFacilityMaster) {
		return mozTfcFacilityMasterRepository.countAllOrganizations(tfcFacilityMaster);
	}

	/**
	 * @breif : 유관기관 리스트 조회
	 * @date : 2024.07.15
	 * @param tfcFacilityMaster
	 * @return
	 */
	public List<MozTfcFacilityMaster> getOrganizationList(MozTfcFacilityMaster tfcFacilityMaster) {
		return mozTfcFacilityMasterRepository.findAllOrganizations(tfcFacilityMaster);
	}

	/**
	 * @breif : 유관기관 Map 조회
	 * @date : 2024.07.17
	 * @param param
	 * @return
	 */
	public FeaturesLayerDTO getOrganizationGeoJson(Map<String, String> param) {
		FeaturesLayerDTO featuresLayerDTO = new FeaturesLayerDTO();
		List<GeoJsonDTO.FeaturesDTO> featuresList = new ArrayList<>();
		List<Map<String, Object>> facilityList = monitoringMapRepository.findAllFacilityByFacilityTy(param);
		return getFeaturesLayerDTO(featuresLayerDTO, featuresList, facilityList);
	}

	/**
	 * @brief : 유관기관 저장
	 * @date : 2024.07.17
	 * @param tfcFacilityMaster
	 * @param uploadFiles
	 */
	@Transactional
	public void registOrganization(MozTfcFacilityMaster tfcFacilityMaster, MultipartFile[] uploadFiles){
		String tfcFacilityId = MozatesCommonUtils.getUuid();

		tfcFacilityMaster.setTfcFacilityId(tfcFacilityId);
		tfcFacilityMaster.setOprtrId(LoginOprtrUtils.getOprtrId());
		tfcFacilityMaster.setCrtr(LoginOprtrUtils.getOprtrId());
		tfcFacilityMaster.setFacilityTy("TFT900");
		mozTfcFacilityMasterRepository.insertTfcFacilityMaster(tfcFacilityMaster);

		if(uploadFiles != null && uploadFiles.length >= 1) {
			for(MultipartFile file : uploadFiles) {
				if(!MozatesCommonUtils.isNull(file.getOriginalFilename())) {
					String[] extArr = {"jpg", "jpeg", "png", "PNG"};
					UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(file, extArr);
					MozTfcFacilityFileInfo tfcFacilityFileInfo = new MozTfcFacilityFileInfo();
					String tfcFacilityFileNo = MozatesCommonUtils.getUuid();
					tfcFacilityFileInfo.setTfcFacilityFileNo(tfcFacilityFileNo);
					tfcFacilityFileInfo.setTfcFacilityId(tfcFacilityId);
					tfcFacilityFileInfo.setFilePath(uploadFileInfo.getFilePath());
					tfcFacilityFileInfo.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
					tfcFacilityFileInfo.setFileExt(uploadFileInfo.getFileExt());
					tfcFacilityFileInfo.setFileNm(uploadFileInfo.getFileNm());
					tfcFacilityFileInfo.setFileSize(uploadFileInfo.getFileSize());
					tfcFacilityFileInfo.setCrtr(LoginOprtrUtils.getOprtrId());
					tfcFacilityFileInfoRepository.insertMozTfcFacilityFileInfo(tfcFacilityFileInfo);
				}
			}
		}
	}

	/**
	 * @brief : 유관기관 수정
	 * @date : 2024.07.18
	 * @param tfcFacilityMaster
	 * @param uploadFiles
	 */
	@Transactional
	public void updateOrganization(MozTfcFacilityMaster tfcFacilityMaster, MultipartFile[] uploadFiles){
		if(tfcFacilityMaster.getTfcFacilityFileInfoList() != null) {
			for(MozTfcFacilityFileInfo fileItem : tfcFacilityMaster.getTfcFacilityFileInfoList()) {
				MozTfcFacilityFileInfo orgFileInfo = tfcFacilityFileInfoRepository.findOneMozTfcFacilityFileInfoByTfcFacilityFileNo(fileItem.getTfcFacilityFileNo());
				fileUploadComponent.deleteUploadFile(orgFileInfo.getFilePath());
				tfcFacilityFileInfoRepository.deleteMozTfcFacilityFileInfoByTfcFacilityFileNo(fileItem.getTfcFacilityFileNo());
			}
		}
		if(uploadFiles != null && uploadFiles.length >= 1) {
			for(MultipartFile file : uploadFiles) {
				if(!MozatesCommonUtils.isNull(file.getOriginalFilename())) {
					String[] extArr = {"jpg", "jpeg", "png", "PNG"};
					UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(file, extArr);
					MozTfcFacilityFileInfo tfcFacilityFileInfo = new MozTfcFacilityFileInfo();
					String tfcFacilityFileNo = MozatesCommonUtils.getUuid();
					tfcFacilityFileInfo.setTfcFacilityFileNo(tfcFacilityFileNo);
					tfcFacilityFileInfo.setTfcFacilityId(tfcFacilityMaster.getTfcFacilityId());
					tfcFacilityFileInfo.setFilePath(uploadFileInfo.getFilePath());
					tfcFacilityFileInfo.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
					tfcFacilityFileInfo.setFileExt(uploadFileInfo.getFileExt());
					tfcFacilityFileInfo.setFileNm(uploadFileInfo.getFileNm());
					tfcFacilityFileInfo.setFileSize(uploadFileInfo.getFileSize());
					tfcFacilityFileInfo.setCrtr(LoginOprtrUtils.getOprtrId());
					tfcFacilityFileInfoRepository.insertMozTfcFacilityFileInfo(tfcFacilityFileInfo);
				}
			}
		}
		mozTfcFacilityMasterRepository.updateOrganization(tfcFacilityMaster);
	}
}
