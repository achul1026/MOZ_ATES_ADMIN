package com.moz.ates.traffic.admin.trafficequipmentmng;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpLog;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpMaster;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfEqpLogRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfEqpMasterRepository;

@Service
public class TrafficEqpServiceImpl implements TrafficEqpService {

    @Autowired
    MozTfcEnfEqpMasterRepository tfcEnfEqpMasterRepository;
    
    @Autowired
    MozTfcEnfEqpLogRepository tfcEnfEqpLogRepository;
    
    
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
    	return tfcEnfEqpMasterRepository.selectEqpDupliCnt(tfcEnfEqpMaster);
    }
    
    /**
     * @brief : 단속장비 등록 
     * @details : 단속장비 등록 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : 
     * @return : 
     */
    @Override
    public void registEqp(MozTfcEnfEqpMaster tfcEnfEqpMaster) {
    	tfcEnfEqpMasterRepository.insertEqp(tfcEnfEqpMaster);
    }
    
//    /**
//     * @brief : 단속장비 리스트 조회
//     * @details : 단속장비 리스트 조회
//     * @author : KC.KIM
//     * @date : 2023.08.04
//     * @param : finePymntInfo
//     * @return : DataTableVO
//     */
//    @Override
//    public DataTableVO getMngListDatatable(MozTfcEnfEqpMaster tfcEnfEqpMaster) {
//        return new DataTableVO(this.getMngList(tfcEnfEqpMaster),this.getMngListCnt(tfcEnfEqpMaster));
//    }
//    
//    @Override
//    public List getMngList(MozTfcEnfEqpMaster tfcEnfEqpMaster) {
//    	return tfcEnfEqpMasterRepository.selectMngList(tfcEnfEqpMaster);
//    }
//
//    @Override
//    public int getMngListCnt(MozTfcEnfEqpMaster tfcEnfEqpMaster) {
//    	return tfcEnfEqpMasterRepository.selectMngListCnt(tfcEnfEqpMaster);
//    }
    
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
    	return tfcEnfEqpMasterRepository.selectEqpDetail(tfcEnfEqpId);
    }
    
    /**
     * @brief : 단속장비 정보 수정
     * @details : 단속장비 정보 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcEnfEqpMaster
     * @return : 
     */
    @Override
    public void updateEqp(MozTfcEnfEqpMaster tfcEnfEqpMaster) {
    	tfcEnfEqpMasterRepository.updateEqp(tfcEnfEqpMaster);
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
     * @brief : 단속장비 로그 리스트 조회
     * @details : 단속장비 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcEnfEqpLog
     * @return : 
     */
	@Override
	public List<MozTfcEnfEqpLog> getEqpLogList(MozTfcEnfEqpLog tfcEnfEqpLog) {
		return tfcEnfEqpLogRepository.findAllMozTfcEnfEqpLog(tfcEnfEqpLog);
	}
	
    /**
     * @brief : 단속장비 리스트 개수 조회
     * @details : 단속장비 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcEnfEqpLog
     * @return : 
     */
	@Override
	public int getEqpLogListCnt(MozTfcEnfEqpLog tfcEnfEqpLog) {
		return tfcEnfEqpLogRepository.countMozTfcEnfEqpLog(tfcEnfEqpLog);
	}
}




