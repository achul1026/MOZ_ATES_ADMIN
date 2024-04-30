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
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfEqpFileInfoRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfEqpLogRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfEqpMasterRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEqpMntnHstRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcFacilityFileInfoRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcFacilityMasterRepository;
import com.moz.ates.traffic.common.repository.monitoring.MonitoringMapRepository;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class TrafficEqpServiceImpl implements TrafficEqpService {

    @Autowired
    MozTfcEnfEqpMasterRepository tfcEnfEqpMasterRepository;
    
    @Autowired
    MozTfcEnfEqpLogRepository tfcEnfEqpLogRepository;
    
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
    FileUploadComponent fileUploadComponent;
    
	/**
     * @brief : 단속장비 아이디 중복 조회
     * @details : 단속장비 아이디 중복 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : 
     * @return : 
     */
    @Override
    public int getEqpDupliCnt(MozTfcEnfEqpMaster tfcEnfEqpMaster) {
    	return tfcEnfEqpMasterRepository.countEqpDupliCnt(tfcEnfEqpMaster);
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
    @Override
    @Transactional
    public void registEqp(MozTfcEnfEqpMaster tfcEnfEqpMaster, MultipartFile[] uploadFiles){
    	String tfcEnfEqpId = MozatesCommonUtils.getUuid();
    	tfcEnfEqpMaster.setTfcEnfEqpId(tfcEnfEqpId);
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
					fileInfo.setTfcEnfEqpId(tfcEnfEqpId);
					fileInfo.setFilePath(uploadFileInfo.getFilePath());
					fileInfo.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
					fileInfo.setFileExt(uploadFileInfo.getFileExt());
					fileInfo.setFileNm(uploadFileInfo.getFileNm());
					fileInfo.setFileSize(uploadFileInfo.getFileSize());
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
    @Override
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
    @Override
    @Transactional
    public void updateEqp(MozTfcEnfEqpMaster tfcEnfEqpMaster, MultipartFile[] uploadFiles) throws IOException {
    	String crtr = LoginOprtrUtils.getOprtrId();
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	
    	if(tfcEnfEqpMaster.getTfcEnfEqpFileInfoList() != null) {
    		for(MozTfcEnfEqpFileInfo fileItem : tfcEnfEqpMaster.getTfcEnfEqpFileInfoList()) {
    			MozTfcEnfEqpFileInfo orgFileInfo = tfcEnfEqpFileInfoRepository.findOneMozMozTfcEnfEqpFileInfoByEqpFileNo(fileItem.getEqpFileNo());
    			fileUploadComponent.deleteUploadFile(orgFileInfo.getFilePath());
    			tfcEnfEqpFileInfoRepository.deleteMozTfcEnfEqpFileInfooByEqpFileNo(fileItem.getEqpFileNo());
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
					fileInfo.setTfcEnfEqpId(tfcEnfEqpMaster.getTfcEnfEqpId());
					fileInfo.setFilePath(uploadFileInfo.getFilePath());
					fileInfo.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
					fileInfo.setFileExt(uploadFileInfo.getFileExt());
					fileInfo.setFileNm(uploadFileInfo.getFileNm());
					fileInfo.setFileSize(uploadFileInfo.getFileSize());
					tfcEnfEqpFileInfoRepository.insertMozTfcEnfEqpFileInfo(fileInfo);					
				}
			}
		}
    	
		if(!MozatesCommonUtils.isNull(tfcEnfEqpMaster.getInstlDate())) {
			tfcEnfEqpMaster.setInstlDateString(formatter.format(tfcEnfEqpMaster.getInstlDate()));  		
    	}
		
    	tfcEnfEqpMasterRepository.updateEqp(tfcEnfEqpMaster);

    	// 장비 유지보수 이력 저장
    	List<MozTfcEqpMntnHst> tfcEqpMntnHstList = new ArrayList<MozTfcEqpMntnHst>();
    	tfcEqpMntnHstList = tfcEnfEqpMaster.getTfcEqpMntnHstList();
    	if(tfcEqpMntnHstList != null && !tfcEqpMntnHstList.isEmpty()) {
    		for(MozTfcEqpMntnHst item : tfcEqpMntnHstList) {
    			String mntnHstId = MozatesCommonUtils.getUuid();
    			item.setMntnHstId(mntnHstId);
    			item.setCrtr(crtr);
    			item.setTfcEnfEqpId(tfcEnfEqpMaster.getTfcEnfEqpId());
    			mozTfcEqpMntnHstRepository.saveTfcEqpMntnHst(item);
    		}
    	}
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
	@Override
	@Transactional
	public void deleteTfcEnfEqpMatser(String tfcEnfEqpId) {
		tfcEnfEqpFileInfoRepository.deleteMozTfcEnfEqpFileInfooByTfcEnfEqpId(tfcEnfEqpId);
		tfcEnfEqpMasterRepository.deleteTfcEnfEqpMasterByTfcEnfEqpId(tfcEnfEqpId);
		mozTfcEqpMntnHstRepository.deleteMozTfcEqpMntnHstByTfcEnfEqpId(tfcEnfEqpId);
	}

	@Override
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
     * @brief : 단속장비 이미지 삭제
     * @details : 단속장비 이미지 삭제
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpMaster
     * @return : 
     */
	@Override
	public void updateEqpImage(MozTfcEnfEqpMaster tfcEnfEqpMaster) {
		tfcEnfEqpMasterRepository.deleteEqpImage(tfcEnfEqpMaster);
	}

    /**
     * @brief : 단속장비 리스트 조회
     * @details : 단속장비 리스트 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcEnfEqpMaster
     * @return : 
     */
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	@Transactional
	public void registTfcFacility(MozTfcFacilityMaster tfcFacilityMaster, MultipartFile[] uploadFiles){
		String tfcFacilityId = MozatesCommonUtils.getUuid();
		tfcFacilityMaster.setTfcFacilityId(tfcFacilityId);
    	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	if(!MozatesCommonUtils.isNull(tfcFacilityMaster.getInstlDate())) {
    		tfcFacilityMaster.setInstlDateString(formatter.format(tfcFacilityMaster.getInstlDate()));  		
    	}
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
	@Override
	@Transactional
	public void deleteTfcFacilityMaster(String tfcFacilityId) {
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
	@Override
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
					tfcFacilityFileInfoRepository.insertMozTfcFacilityFileInfo(tfcFacilityFileInfo);					
				}
			}
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	if(!MozatesCommonUtils.isNull(tfcFacilityMaster.getInstlDate())) {
    		tfcFacilityMaster.setInstlDateString(formatter.format(tfcFacilityMaster.getInstlDate()));  		
    	}
    	
		mozTfcFacilityMasterRepository.updateMozTfcFacilityMaster(tfcFacilityMaster);
	}

	/**
     * @brief : 교통시설물 이미지 삭제
     * @details : 교통시설물 이미지 삭제
     * @author : KC.KIM
     * @date : 2024.03.04
     * @param : tfcFacilityMaster
     * @return : 
     */
	@Override
	public void updateFacilityImage(MozTfcFacilityMaster tfcFacilityMaster) {
		mozTfcFacilityMasterRepository.deleteFacilityImage(tfcFacilityMaster);
	}

	@Override
	public FeaturesLayerDTO getFacilityGeoJson(Map<String, String> param) {
		FeaturesLayerDTO featuresLayerDTO = new FeaturesLayerDTO();
		List<GeoJsonDTO.FeaturesDTO> featuresList = new ArrayList<>();
		List<Map<String, Object>> facilityList = monitoringMapRepository.findAllFacilityByFacilityTy(param);
		return getFeaturesLayerDTO(featuresLayerDTO, featuresList, facilityList);
	}

	@Override
	public FeaturesLayerDTO getEquipmentGeoJson(Map<String, String> param) {
		FeaturesLayerDTO featuresLayerDTO = new FeaturesLayerDTO();
		List<GeoJsonDTO.FeaturesDTO> featuresList = new ArrayList<>();
		List<Map<String, Object>> equipmentList = monitoringMapRepository.findAllEquipmentByEquipmentTy(param);
		return getFeaturesLayerDTO(featuresLayerDTO, featuresList, equipmentList);
	}
	
	@Override
	public FeaturesLayerDTO getEnforcementGeoJson(Map<String, String> param) {
		FeaturesLayerDTO featuresLayerDTO = new FeaturesLayerDTO();
		List<GeoJsonDTO.FeaturesDTO> featuresList = new ArrayList<>();
		List<Map<String, Object>> enforcementList = monitoringMapRepository.findAllEnforcementByDate(param);
		return getFeaturesLayerDTO(featuresLayerDTO, featuresList, enforcementList);
	}
	
	@Override
	public FeaturesLayerDTO getAccidentGeoJson(Map<String, String> param) {
		FeaturesLayerDTO featuresLayerDTO = new FeaturesLayerDTO();
		List<GeoJsonDTO.FeaturesDTO> featuresList = new ArrayList<>();
		List<Map<String, Object>> accidentList = monitoringMapRepository.findAllAccidentByDateForAdminDashboard(param);
		return getFeaturesLayerDTO(featuresLayerDTO, featuresList, accidentList);
	}

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
	@Override
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
	@Override
	public void deleteEqpHist(String mntnHstId) {
		mozTfcEqpMntnHstRepository.deleteMozTfcEqpMntnHstByMntnHstId(mntnHstId);
	}
}




