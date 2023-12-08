package com.moz.ates.traffic.admin.trafficenforcementmng;

import java.util.List;

import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfHst;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfMaster;
import com.moz.ates.traffic.common.entity.law.MozTfcLwInfo;
import com.moz.ates.traffic.common.entity.payment.MozFinePymntInfo;

public interface TrafficEnfService {
    DriverVO getDriverDetail(EnfSearchVO enfSearchVO);

    CarVO getCarDetail(EnfSearchVO enfSearchVO);

    
    /**
     * @brief : 교통단속 법률관리 리스트 조회
     * @details : 교통단속 법률관리 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcLwInfo
     * @return : 
     */
    List<MozTfcLwInfo> getLawList(MozTfcLwInfo tfcLwInfo);
    
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
     * @brief : 교통단속 정보 리스트 조회
     * @details : 교통단속 정보 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcEnfMaster
     * @return : 
     */
    List getInfoList(MozTfcEnfMaster tfcEnfMaster);

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
     * @brief : 교통단속 정보 수정
     * @details : 교통단속 정보 수정
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : tfcEnfMaster
     * @return : 
     */
    void updateInfo(MozTfcEnfMaster tfcEnfMaster);
    
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
}
