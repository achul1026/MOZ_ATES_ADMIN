package com.moz.ates.traffic.admin.trafficaccidentmng;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntChgHst;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntMaster;
import com.moz.ates.traffic.common.repository.accident.MozTfcAcdntChgHstRepository;
import com.moz.ates.traffic.common.repository.accident.MozTfcAcdntMasterRepository;
import com.moz.ates.traffic.common.support.exception.ErrorCode;

@Service
public class TrafficAcdntServiceImpl implements TrafficAcdntService{
	
    @Autowired
    MozTfcAcdntMasterRepository tfcAcdntMasterRepository;
    
    @Autowired
    MozTfcAcdntChgHstRepository tfcAcdntChgHstRepository;
    
    
	@Override
	public List<MozTfcAcdntMaster> getAcdntList(MozTfcAcdntMaster tfcAcdntMaster) {
		return tfcAcdntMasterRepository.findAllMozTfcAcdntMaster(tfcAcdntMaster);
	}

	@Override
	public int getAcdntListCnt(MozTfcAcdntMaster tfcAcdntMaster) {
		return tfcAcdntMasterRepository.countMozTfcAcdntMaster(tfcAcdntMaster);
	}
    
    /**
     * @brief : 교통사고 중복 체크
     * @details : 교통사고 중복 체크
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntMaster
     * @return : 
     */
    @Override
    public int countTfcAcdntMaster(MozTfcAcdntMaster tfcAcdntMaster) {
    	return tfcAcdntMasterRepository.countTfcAcdntMaster(tfcAcdntMaster);
    }

    /**
     * @brief : 교통사고 정보 등록
     * @details : 교통사고 정보 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntMaster
     * @return : 
     */
    @Override
    public void mozTfcAcdntMasterSave(MozTfcAcdntMaster tfcAcdntMaster) {
    	// TODO
    	tfcAcdntMaster.setCrtr("lim");
    	tfcAcdntMaster.setCrDt(tfcAcdntMaster.getAcdntDt());
    	tfcAcdntMasterRepository.saveMozTfcAcdntMaster(tfcAcdntMaster);
    }
    
    /**
     * @brief : 교통사고 정보 상세 조회
     * @details : 교통사고 정보 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntId
     * @return : 
     */
    @Override
    public MozTfcAcdntMaster getMngDetail(String tfcAcdntId) {
    	MozTfcAcdntMaster tfcAcdntMaster = new MozTfcAcdntMaster();
    	tfcAcdntMaster = tfcAcdntMasterRepository.selectMngDetail(tfcAcdntId);
    	
    	return tfcAcdntMaster;
    }
    
    /**
     * @brief : 교통사고 정보 수정
     * @details : 교통사고 정보 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntMaster
     * @return : 
     */
    @Override
    public void upateAcdnt(MozTfcAcdntMaster tfcAcdntMaster) {
    	tfcAcdntMasterRepository.updateMozTfcAcdntMaster(tfcAcdntMaster);
    }
    
    /**
     * @brief : 교통사고 로그 리스트 조회
     * @details : 교통사고 로그 리스트 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcAcdntChgHst
     * @return : 
     */
	@Override
	public List<MozTfcAcdntChgHst> getAcdntLogList(MozTfcAcdntChgHst tfcAcdntChgHst) {
		return tfcAcdntChgHstRepository.findAllMozTfcAcdntChgHst(tfcAcdntChgHst);
	}

    /**
     * @brief : 교통사고 로그 리스트 개수 조회
     * @details : 교통사고 로그 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : tfcAcdntChgHst
     * @return : 
     */
	@Override
	public int getAcdntLogListCnt(MozTfcAcdntChgHst tfcAcdntChgHst) {
		return tfcAcdntChgHstRepository.countMozTfcAcdntChgHst(tfcAcdntChgHst);
	}
}
