package com.moz.ates.traffic.admin.trafficequipmentmng;

import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpLog;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpMaster;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
     * @param : 
     * @return : 
     */
    void registEqp(MozTfcEnfEqpMaster tfcEnfEqpMaster);
    
//    /**
//     * @brief : 단속장비 리스트 조회
//     * @details : 단속장비 리스트 조회
//     * @author : KC.KIM
//     * @date : 2023.08.04
//     * @param : finePymntInfo
//     * @return : DataTableVO
//     */
//    DataTableVO getMngListDatatable(MozTfcEnfEqpMaster tfcEnfEqpMaster);
//
//    List getMngList(MozTfcEnfEqpMaster tfcEnfEqpMaster);
//
//    int getMngListCnt(MozTfcEnfEqpMaster tfcEnfEqpMaster);
    
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
     * @return : 
     */
    void updateEqp(MozTfcEnfEqpMaster tfcEnfEqpMaster);
    
    
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
     * @brief : 단속장비 로그 리스트 조회
     * @details : 단속장비 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcEnfEqpLog
     * @return : 
     */
	List<MozTfcEnfEqpLog> getEqpLogList(MozTfcEnfEqpLog tfcEnfEqpLog);

    /**
     * @brief : 단속장비 리스트 개수 조회
     * @details : 단속장비 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcEnfEqpLog
     * @return : 
     */
	int getEqpLogListCnt(MozTfcEnfEqpLog tfcEnfEqpLog);
}
