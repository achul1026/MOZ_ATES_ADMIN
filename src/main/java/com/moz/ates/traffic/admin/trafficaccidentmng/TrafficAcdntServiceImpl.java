package com.moz.ates.traffic.admin.trafficaccidentmng;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.common.component.FileUploadComponent;
import com.moz.ates.traffic.common.component.accident.TrafficAccidentStatus;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntChgHst;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntFileInfo;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntMaster;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntTrgtInfo;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntTrgtPnrInfo;
import com.moz.ates.traffic.common.entity.common.UploadFileInfo;
import com.moz.ates.traffic.common.entity.police.MozPolInfo;
import com.moz.ates.traffic.common.enums.AccidentDamageCd;
import com.moz.ates.traffic.common.enums.AccidentTargetCd;
import com.moz.ates.traffic.common.enums.PassengerDamageCd;
import com.moz.ates.traffic.common.enums.PassengerDriverRelationCd;
import com.moz.ates.traffic.common.enums.RegisterType;
import com.moz.ates.traffic.common.enums.TrafficIdClassification;
import com.moz.ates.traffic.common.repository.accident.MozTfcAcdntChgHstRepository;
import com.moz.ates.traffic.common.repository.accident.MozTfcAcdntFileInfoRepository;
import com.moz.ates.traffic.common.repository.accident.MozTfcAcdntMasterRepository;
import com.moz.ates.traffic.common.repository.accident.MozTfcAcdntTrgtInfoRepository;
import com.moz.ates.traffic.common.repository.accident.MozTfcAcdntTrgtPnrInfoRepository;
import com.moz.ates.traffic.common.repository.police.MozPolInfoRepository;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class TrafficAcdntServiceImpl implements TrafficAcdntService{
	
    @Autowired
    MozTfcAcdntMasterRepository tfcAcdntMasterRepository;
    
    @Autowired
    MozTfcAcdntChgHstRepository tfcAcdntChgHstRepository;
    
    @Autowired
    MozTfcAcdntFileInfoRepository tfcAcdntFileInfoRepository;
    
    @Autowired
    MozTfcAcdntTrgtInfoRepository tfcAcdntTrgtInfoRepository;
    
    @Autowired
    MozTfcAcdntTrgtPnrInfoRepository tfcAcdntTrgtPnrInfoRepository;
    
    @Autowired
    MozPolInfoRepository polInfoRepository;
    
    @Autowired
    FileUploadComponent fileUploadComponent;
    
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
     * @param : uploadFiles 
     * @return : 
     */
    @Override
    public void mozTfcAcdntMasterSave(MozTfcAcdntMaster tfcAcdntMaster, MultipartFile[] uploadFiles) {
    	LocalDateTime now = LocalDateTime.now();
    	Date crDt = Timestamp.valueOf(now);
    	String code = TrafficAccidentStatus.REG;
    	String date = DateFormatUtils.format(tfcAcdntMaster.getAcdntDt(), "yyyyMMdd");
    	
    	//AcdntId조회
        MozPolInfo dbPolInfo =  polInfoRepository.findOneMozPolInfo(tfcAcdntMaster.getPolId());
        String acdntIdStr = TrafficIdClassification.ACCIDENT + "-" + date + "-" + dbPolInfo.getPolLcenId();
        Long tfcAcdntSeq = tfcAcdntMasterRepository.countPolSeqByTfcAcdntId(acdntIdStr);
        String creator = dbPolInfo.getPolLcenId() + "-" + String.valueOf(tfcAcdntSeq);
 
        String tfcAcdntId = MozatesCommonUtils.getAcdntId(creator, tfcAcdntMaster.getAcdntDt());
    	
    	String crtr = LoginOprtrUtils.getOprtrId();
    	
    	tfcAcdntMaster.setCrtr(crtr);
    	tfcAcdntMaster.setTfcAcdntId(tfcAcdntId);
    
    	// 사고 정보 저장
    	tfcAcdntMaster.setLastAcdntPrcCd(code);
    	tfcAcdntMasterRepository.saveMozTfcAcdntMaster(tfcAcdntMaster);
    	
    	// 사고 파일 정보 저장
    	for(MultipartFile uploadFile : uploadFiles) {
    		if(!uploadFile.isEmpty()){
				String[] extArr = {"jpg", "jpeg", "png", "PNG", "webm", "avi", "mp4"};
				UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(uploadFile, extArr); 
				MozTfcAcdntFileInfo atchFile = new MozTfcAcdntFileInfo();
				String acdnTFileNo = MozatesCommonUtils.getUuid();
				atchFile.setAcdntFileNo(acdnTFileNo);
				atchFile.setTfcAcdntId(tfcAcdntId);
				atchFile.setCrtr(crtr);
				atchFile.setFilePath(uploadFileInfo.getFilePath());
				atchFile.setFileOriginNm(uploadFileInfo.getOriginalFileNm());
				atchFile.setFileExt(uploadFileInfo.getFileExt());
				atchFile.setFileNm(uploadFileInfo.getFileNm());
				atchFile.setFileSize(uploadFileInfo.getFileSize());
    			tfcAcdntFileInfoRepository.insertMozTfcAcdntFileInfo(atchFile);
    		}
    	}
    	
    	// 타겟 정보 저장
    	List<MozTfcAcdntTrgtInfo> acdntTrgtInfoList = new ArrayList<MozTfcAcdntTrgtInfo>();
    	acdntTrgtInfoList = tfcAcdntMaster.getTfcAcdntTrgtInfo();
    	if(acdntTrgtInfoList.size() >= 1) {
    		for(MozTfcAcdntTrgtInfo trgtInfo : acdntTrgtInfoList) {
    			String tfcAcdntTrgtId = MozatesCommonUtils.getUuid();
    			trgtInfo.setAcdntTrgtId(tfcAcdntTrgtId);
    			trgtInfo.setTfcAcdntId(tfcAcdntId);
    			trgtInfo.setCrtr(crtr);
    			tfcAcdntTrgtInfoRepository.saveMozTfcAcdntTrgtInfo(trgtInfo);
    			//동승자 정보 저장
    			if(!MozatesCommonUtils.isNull(trgtInfo.getAcdntTrgtCd()) 
    					&& AccidentTargetCd.VEHICLE.getCode().equals(trgtInfo.getAcdntTrgtCd())) {
    				List<MozTfcAcdntTrgtPnrInfo> acdntTrgtPnrInfoList = new ArrayList<MozTfcAcdntTrgtPnrInfo>();
    				acdntTrgtPnrInfoList = trgtInfo.getTfcAcdntTrgtPnrInfo();
    				if(acdntTrgtPnrInfoList != null && acdntTrgtPnrInfoList.size() >= 1) {
    					for(MozTfcAcdntTrgtPnrInfo pnrInfo : acdntTrgtPnrInfoList) {
    						String tfcAcdntTrgtPnrId = MozatesCommonUtils.getUuid();
    						pnrInfo.setAcdntTrgtPnrId(tfcAcdntTrgtPnrId);
    						pnrInfo.setAcdntTrgtId(tfcAcdntTrgtId);
    						pnrInfo.setTfcAcdntId(tfcAcdntId);
    						pnrInfo.setCrtr(crtr);
    						tfcAcdntTrgtPnrInfoRepository.saveMozTfcAcdntTrgtPnrInfo(pnrInfo);
    					}
    				}
    			}
    		}
    	}
    	
    	// 로그 정보 저장
        MozTfcAcdntChgHst mozTfcAcdntChgHst = new MozTfcAcdntChgHst();
        String tfcAcdntChgHst = MozatesCommonUtils.getUuid();
        String crtrIpAddr = LoginOprtrUtils.getUserIpAddr();
        String registerType = getAcdntRegType(LoginOprtrUtils.getOprtrPermission());
        mozTfcAcdntChgHst.setHstId(tfcAcdntChgHst);
        mozTfcAcdntChgHst.setTfcAcdntId(tfcAcdntId);
        mozTfcAcdntChgHst.setAcdntPrcCd(code);
        mozTfcAcdntChgHst.setAcdntRegTy(registerType);
        mozTfcAcdntChgHst.setCrtr(crtr);
        mozTfcAcdntChgHst.setCrtrIpAddr(crtrIpAddr);
        tfcAcdntChgHstRepository.saveMozTfcAcdntChgHst(mozTfcAcdntChgHst);
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
    	tfcAcdntMaster = tfcAcdntMasterRepository.findOneMngDetail(tfcAcdntId);
    	
    	// 사고 파일 조회
    	List<MozTfcAcdntFileInfo> acdntFileList = new ArrayList<MozTfcAcdntFileInfo>();
    	acdntFileList = tfcAcdntFileInfoRepository.findAllTfcAcdntFileInfoByTfcAcdntId(tfcAcdntId);
    	tfcAcdntMaster.setTfcAcdntFileInfo(acdntFileList);
    	
    	// 타겟 및 탑승자 정보 조회
    	List<MozTfcAcdntTrgtInfo> trgtInfoList = new ArrayList<MozTfcAcdntTrgtInfo>();
    	trgtInfoList = tfcAcdntTrgtInfoRepository.findAllTfcAcdntTrgtByTfcAcdntId(tfcAcdntId);
    	
    	for(MozTfcAcdntTrgtInfo trgt : trgtInfoList) {
    		String acdntDmgCdNm = AccidentDamageCd.getAcdntDamageCdNameForCode(trgt.getAcdntDmgCd());
    		trgt.setAcdntDmgCdNm(acdntDmgCdNm);
    		
    		for(MozTfcAcdntTrgtPnrInfo pnr : trgt.getTfcAcdntTrgtPnrInfo()) {
    			String pnrDmgCdNm = PassengerDamageCd.getPassengerDamageCdNameForCode(pnr.getPnrDmgCd());
    			pnr.setPnrDmgCdNm(pnrDmgCdNm);
    			
    			String pnrDrvrRltnCdNm = PassengerDriverRelationCd.getPassengerDriverRelationCdNameForCode(pnr.getPnrDrvrRltnCd());
    			pnr.setPnrDrvrRltnCdNm(pnrDrvrRltnCdNm);
    		}
    	}
    	
    	tfcAcdntMaster.setTfcAcdntTrgtInfo(trgtInfoList);
    	return tfcAcdntMaster;
    }
    
    /**
     * @brief : 교통사고 정보 수정
     * @details : 교통사고 정보 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntMaster
     * @param : uploadFiles 
     * @return : 
     */
    @Override
    public void upateAcdnt(MozTfcAcdntMaster tfcAcdntMaster, MultipartFile[] uploadFiles) {
    	String tfcAcdntId = tfcAcdntMaster.getTfcAcdntId();
    	String crtr = LoginOprtrUtils.getOprtrId();
    	String code = TrafficAccidentStatus.UPD;
    	
    	// 사고 정보 수정
    	tfcAcdntMasterRepository.updateMozTfcAcdntMaster(tfcAcdntMaster);
    	// 사고 파일 정보 수정
		List<MozTfcAcdntFileInfo> deleteAtchFileList = new ArrayList<MozTfcAcdntFileInfo>();
		deleteAtchFileList = tfcAcdntMaster.getTfcAcdntFileInfo();
		
		if(deleteAtchFileList != null) {
			for(MozTfcAcdntFileInfo deleteFileInfo : deleteAtchFileList) {
				if(!MozatesCommonUtils.isNull(deleteFileInfo.getAcdntFileNo())) {
					MozTfcAcdntFileInfo fileInfo = new MozTfcAcdntFileInfo();
					fileInfo = tfcAcdntFileInfoRepository.findOneMozTfcAcdntFileInfoByAcdntFileNo(deleteFileInfo.getAcdntFileNo());
					fileUploadComponent.deleteUploadFile(fileInfo.getFilePath());
					tfcAcdntFileInfoRepository.deleteMozTfcAcdntFileInfoByAcdntFileNo(fileInfo.getAcdntFileNo());				
				}
			}
		}
		for(MultipartFile uploadFile : uploadFiles) {
			if(!uploadFile.isEmpty()){
				String[] extArr = {"jpg", "jpeg", "png", "PNG", "webm", "avi", "mp4"};
				UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(uploadFile, extArr); 
				MozTfcAcdntFileInfo atchFile = new MozTfcAcdntFileInfo();
				String acdnTFileNo = MozatesCommonUtils.getUuid();
				atchFile.setAcdntFileNo(acdnTFileNo);
				atchFile.setTfcAcdntId(tfcAcdntId);
				atchFile.setCrtr(crtr);
				atchFile.setFilePath(uploadFileInfo.getFilePath());
				atchFile.setFileOriginNm(uploadFileInfo.getOriginalFileNm());
				atchFile.setFileExt(uploadFileInfo.getFileExt());
				atchFile.setFileNm(uploadFileInfo.getFileNm());
				atchFile.setFileSize(uploadFileInfo.getFileSize());
				tfcAcdntFileInfoRepository.insertMozTfcAcdntFileInfo(atchFile);
			}
		}
		// 기존 정보 삭제
		List<MozTfcAcdntTrgtInfo> acdntTrgtInfoList = new ArrayList<MozTfcAcdntTrgtInfo>();
		List<MozTfcAcdntTrgtPnrInfo> acdntTrgtPnrInfoList = new ArrayList<MozTfcAcdntTrgtPnrInfo>();
		List<MozTfcAcdntTrgtInfo> dbTrgtInfoList = new ArrayList<MozTfcAcdntTrgtInfo>();
		List<MozTfcAcdntTrgtInfo> difrTrgtInfoList = new ArrayList<MozTfcAcdntTrgtInfo>();
		List<MozTfcAcdntTrgtPnrInfo> difrTrgtPnrInfoList = new ArrayList<MozTfcAcdntTrgtPnrInfo>();  
		acdntTrgtInfoList = tfcAcdntMaster.getTfcAcdntTrgtInfo();
		dbTrgtInfoList = tfcAcdntTrgtInfoRepository.findAllTfcAcdntTrgtByTfcAcdntId(tfcAcdntId);
		for(MozTfcAcdntTrgtInfo dbTrgtInfo : dbTrgtInfoList) {
			boolean trgtFound = false;
			if(acdntTrgtInfoList != null && acdntTrgtInfoList.size() >= 1) {
				for(MozTfcAcdntTrgtInfo trgtInfo : acdntTrgtInfoList) {
					if(dbTrgtInfo.getAcdntTrgtId().equals(trgtInfo.getAcdntTrgtId())) {
						trgtFound = true;
						for(MozTfcAcdntTrgtPnrInfo dbPnrInfo : dbTrgtInfo.getTfcAcdntTrgtPnrInfo()) {
							boolean pnrFound = false;
							for(MozTfcAcdntTrgtPnrInfo pnrInfo : trgtInfo.getTfcAcdntTrgtPnrInfo()) {
								if (dbPnrInfo.getAcdntTrgtPnrId().equals(pnrInfo.getAcdntTrgtPnrId())) {
									pnrFound = true;
								}
							}
							if(!pnrFound) {
								difrTrgtPnrInfoList.add(dbPnrInfo);
							}
						}
					}
				}
			}
			if(!trgtFound) {
				difrTrgtInfoList.add(dbTrgtInfo);
			}
		}
		
		if(!difrTrgtPnrInfoList.isEmpty()) {
			for(MozTfcAcdntTrgtPnrInfo dbPnrInfo : difrTrgtPnrInfoList) {
				tfcAcdntTrgtPnrInfoRepository.deleteMozTfcAcdntPnrInfoByAcdntTrgtPnrId(dbPnrInfo.getAcdntTrgtPnrId());
			}
		}
		
		if(!difrTrgtInfoList.isEmpty()) {
			for(MozTfcAcdntTrgtInfo dbTrgtInfo : difrTrgtInfoList) {
				tfcAcdntTrgtInfoRepository.deleteMozTfcAcdntTrgtInfoByAcdntTrgtId(dbTrgtInfo.getAcdntTrgtId());
			}
		}
		
		// 타겟 정보 수정
    	if(acdntTrgtInfoList != null && acdntTrgtInfoList.size() >= 1) {
    		for(MozTfcAcdntTrgtInfo trgtInfo : acdntTrgtInfoList) {
    			String tfcAcdntTrgtId = MozatesCommonUtils.getUuid();
    			if(!MozatesCommonUtils.isNull(trgtInfo.getAcdntTrgtId())){
    				tfcAcdntTrgtId = trgtInfo.getAcdntTrgtId();
    				tfcAcdntTrgtInfoRepository.updateMozTfcAcdntTrgtInfo(trgtInfo);    				    				
    			}else {
        			trgtInfo.setAcdntTrgtId(tfcAcdntTrgtId);
        			trgtInfo.setTfcAcdntId(tfcAcdntId);
        			trgtInfo.setCrtr(crtr);
    				tfcAcdntTrgtInfoRepository.saveMozTfcAcdntTrgtInfo(trgtInfo);
    			}
    			//동승자 정보 저장
    			if(!MozatesCommonUtils.isNull(trgtInfo.getAcdntTrgtCd()) 
    					&& AccidentTargetCd.VEHICLE.getCode().equals(trgtInfo.getAcdntTrgtCd())) {
    				acdntTrgtPnrInfoList = trgtInfo.getTfcAcdntTrgtPnrInfo();
    				if(acdntTrgtPnrInfoList != null && acdntTrgtPnrInfoList.size() >= 1) {
    					for(MozTfcAcdntTrgtPnrInfo pnrInfo : acdntTrgtPnrInfoList) {
    						if(!MozatesCommonUtils.isNull(pnrInfo.getAcdntTrgtPnrId())){
    							tfcAcdntTrgtPnrInfoRepository.updateMozTfcAcdntTrgtPnrInfo(pnrInfo);    							
    						}else {
    							String tfcAcdntTrgtPnrId = MozatesCommonUtils.getUuid();
    							pnrInfo.setAcdntTrgtPnrId(tfcAcdntTrgtPnrId);
    							pnrInfo.setAcdntTrgtId(tfcAcdntTrgtId);
    							pnrInfo.setTfcAcdntId(tfcAcdntId);
    							pnrInfo.setCrtr(crtr);
    							tfcAcdntTrgtPnrInfoRepository.saveMozTfcAcdntTrgtPnrInfo(pnrInfo);
    						}
    					}
    				}
    			}
    		}
    	}
    	
		// 로그 정보 저장
        MozTfcAcdntChgHst mozTfcAcdntChgHst = new MozTfcAcdntChgHst();
        String tfcAcdntChgHst = MozatesCommonUtils.getUuid();
        String crtrIpAddr = LoginOprtrUtils.getUserIpAddr();
        String registerType = getAcdntRegType(LoginOprtrUtils.getOprtrPermission());
        mozTfcAcdntChgHst.setHstId(tfcAcdntChgHst);
        mozTfcAcdntChgHst.setTfcAcdntId(tfcAcdntId);
        mozTfcAcdntChgHst.setAcdntPrcCd(code);
        mozTfcAcdntChgHst.setAcdntRegTy(registerType);
        mozTfcAcdntChgHst.setCrtr(crtr);
        mozTfcAcdntChgHst.setCrtrIpAddr(crtrIpAddr);
        tfcAcdntChgHstRepository.saveMozTfcAcdntChgHst(mozTfcAcdntChgHst);
    }
	
	public String getAcdntRegType(String oprtrPermissionCdStr) {
		String registerType = "";
		switch (oprtrPermissionCdStr) {
		case "OPC000":
			registerType = RegisterType.SUPER_ADMIN.getCode();
			break;
		case "OPC001":
			registerType = RegisterType.ADMIN_USER.getCode();
			break;
		case "OPC002":
			registerType = RegisterType.OFFICE_OPERATOR.getCode();
			break;
		case "OPC003":
			registerType = RegisterType.POLICE_OPERATOR.getCode();
			break;
		case "OPC004":
			registerType = RegisterType.POLICE_OFFICER.getCode();
			break;
		}
		
		return registerType;
	}

	/**
	 * @brief 사고타겟 이력 조회
	 * @details : 사고타겟 이력 조회
	 * @author KY.LEE
	 * @date 2024. 4. 11.
	 * @method getAcdntTrgtList
	 */
	@Override
	public List<MozTfcAcdntTrgtInfo> getAcdntTrgtList(String dvrLcenId) {
		return tfcAcdntTrgtInfoRepository.findAllTfcAcdntTrgtByDvrLcenId(dvrLcenId);
	}
}
