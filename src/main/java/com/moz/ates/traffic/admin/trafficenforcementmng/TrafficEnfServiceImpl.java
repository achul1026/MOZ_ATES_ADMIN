package com.moz.ates.traffic.admin.trafficenforcementmng;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.common.component.FileUploadComponent;
import com.moz.ates.traffic.common.component.enforcement.TrafficEnforcementStatus;
import com.moz.ates.traffic.common.entity.common.MozMsgQueue;
import com.moz.ates.traffic.common.entity.common.UploadFileInfo;
import com.moz.ates.traffic.common.entity.driver.MozVioInfo;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfHst;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfMaster;
import com.moz.ates.traffic.common.entity.equipment.MozCameraEnfOrg;
import com.moz.ates.traffic.common.entity.equipment.MozCameraEnfOrgFile;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfFileInfo;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfFineInfo;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEqpEnfInfo;
import com.moz.ates.traffic.common.entity.finentc.MozFineNtcInfo;
import com.moz.ates.traffic.common.entity.law.MozTfcLwFineInfo;
import com.moz.ates.traffic.common.entity.law.MozTfcLwInfo;
import com.moz.ates.traffic.common.entity.payment.MozFinePymntInfo;
import com.moz.ates.traffic.common.entity.payment.MozPlPymntInfo;
import com.moz.ates.traffic.common.entity.police.MozPolInfo;
import com.moz.ates.traffic.common.enums.FileUploadType;
import com.moz.ates.traffic.common.enums.MsgQueueStatus;
import com.moz.ates.traffic.common.enums.MsgType;
import com.moz.ates.traffic.common.enums.NtcTypeCd;
import com.moz.ates.traffic.common.enums.RegisterType;
import com.moz.ates.traffic.common.enums.TrafficIdClassification;
import com.moz.ates.traffic.common.repository.accident.MozTfcAcdntMasterRepository;
import com.moz.ates.traffic.common.repository.common.MozMsgQueueRepository;
import com.moz.ates.traffic.common.repository.driver.MozVioInfoRepository;
import com.moz.ates.traffic.common.repository.enforcement.MozTfcEnfHstRepository;
import com.moz.ates.traffic.common.repository.enforcement.MozTfcEnfMasterRepository;
import com.moz.ates.traffic.common.repository.equipment.MozCameraEnfOrgFileRepository;
import com.moz.ates.traffic.common.repository.equipment.MozCameraEnfOrgRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfFileInfoRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfFineInfoRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEqpEnfInfoRepository;
import com.moz.ates.traffic.common.repository.finentc.MozFineNtcInfoRepository;
import com.moz.ates.traffic.common.repository.law.MozTfcLwAdtnRvsnRepository;
import com.moz.ates.traffic.common.repository.law.MozTfcLwFineInfoRepository;
import com.moz.ates.traffic.common.repository.law.MozTfcLwInfoRepository;
import com.moz.ates.traffic.common.repository.operator.MozWebOprtrRepository;
import com.moz.ates.traffic.common.repository.payment.MozFinePymntInfoRepository;
import com.moz.ates.traffic.common.repository.payment.MozPlPymntInfoRepository;
import com.moz.ates.traffic.common.repository.police.MozPolInfoRepository;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.support.exception.ErrorCode;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;
import com.moz.ates.traffic.common.util.SmsSendContentUtils;

@Service
public class TrafficEnfServiceImpl implements TrafficEnfService {
	
	@Value("${mail.sender.inatro}")
	private String sender;

	@Value("${mail.url.inatro}")
	private String url;
	
	@Autowired
	MozTfcEnfMasterRepository tfcEnfMasterRepository;

	@Autowired
	MozFinePymntInfoRepository finePymntInfoRepository;

	@Autowired
	MozTfcEnfHstRepository tfcEnfHstRepository;

	@Autowired
	MozTfcLwInfoRepository tfcLwInfoRepository;
	
	@Autowired
	MozTfcLwFineInfoRepository tfcLwFineInfoRepository;

	@Autowired
	MozTfcLwAdtnRvsnRepository tfcLwAdtnRvsnRepository;
	
	@Autowired
	MozFineNtcInfoRepository fineNtcInfoRepository;
	
	@Autowired
	MozVioInfoRepository vioInfoRepository;
	
	@Autowired
	MozTfcEnfFileInfoRepository tfcEnfFileInfoRepository;
	
	@Autowired
	MozTfcEnfFineInfoRepository tfcEnfFineInfoRepository;
	
	@Autowired
	MozPlPymntInfoRepository plPymntInfoRepository;
	
	@Autowired
	MozPolInfoRepository polInfoRepository;
	
	@Autowired
	MozTfcAcdntMasterRepository tfcAcdntMasterRepository;
	
	@Autowired
	MozWebOprtrRepository webOprtrRepository;
	
    @Autowired
    MozCameraEnfOrgRepository mozCameraEnfOrgRepository;
    
    @Autowired
    MozCameraEnfOrgFileRepository mozCameraEnfOrgFileRepository;
	
    @Autowired
    MozTfcEqpEnfInfoRepository mozTfcEqpEnfInfoRepository;
    
	@Autowired
	MozMsgQueueRepository mozMsgQueueRepository;
    
	@Autowired
	FileUploadComponent fileUploadComponent;

	@Override
	public DriverVO getDriverDetail(EnfSearchVO enfSearchVO) {

		// test
		DriverVO driverVO = new DriverVO();
//        driverVO.setName("홍길동");
//        driverVO.setPhoneNum("010-1111-2456");

		if ("hs".equals(enfSearchVO.getName()) && "010".equals(enfSearchVO.getPhone())) {
			driverVO.setName(enfSearchVO.getName());
			driverVO.setPhone(enfSearchVO.getPhone());
			driverVO.setDriverLicense("111-23-4555");
			driverVO.setNid("123123123123");
			driverVO.setNationality("모잠비크");
			driverVO.setLicenseRenewal("2회");
			driverVO.setAddress("moz africa city");
			driverVO.setBirth("920926");
			driverVO.setSex("m");
			driverVO.setEmail("mike.lim@bluedus.com");
			driverVO.setDrivetLicenseDate("2017-07-27");
			driverVO.setLicenseCheck("유효");
//            driverVO.set_LicenseCheck(true);
		}
//        return sqlSession.selectOne("TrafficEnf.selectSearchDriverDetail",tfcEnfId);

		return driverVO;
		// test end

//        return sqlSession.selectOne("TrafficEnf.selectDriverDetail",enfSearchVO);
	}

	@Override
	public CarVO getCarDetail(EnfSearchVO enfSearchVO) {

		// text
		CarVO carVO = new CarVO();

		String carNum = "9199";
		enfSearchVO.setCarNum("9199");

		if (carNum.equals(enfSearchVO.getCarNum())) {
			carVO.setCarNum(enfSearchVO.getCarNum());
			carVO.setCarDriverName("lim hs");
			carVO.setRegDt("2020-11-22");
			carVO.setCarType("Avante(2022 XD)");
			carVO.setCarClassification("소형");
			carVO.setCompetentAuthority("Maputo");
			carVO.setWtCarIsScrapped("해당 사항 없음");
			carVO.setNote("비고");
			carVO.setAutomotiveUse("자가용");
		}

		return carVO;
		// test end

//        return sqlSession.selectOne("TrafficEnf.selectCarDetail",enfSearchVO);
	}

	/**
	 * @brief : 교통단속 정보 리스트 조회
	 * @details : 교통단속 정보 리스트 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfMaster
	 * @return :
	 */
	@Override
	public List<MozTfcEnfMaster> getInfoList(MozTfcEnfMaster tfcEnfMaster) {
		return tfcEnfMasterRepository.findAllInfoList(tfcEnfMaster);
	}

	@Override
	public int getInfoListCnt(MozTfcEnfMaster tfcEnfMaster) {
		return tfcEnfMasterRepository.countInfoList(tfcEnfMaster);
	}

	/**
	 * @brief : 교통단속 정보 상세 조회
	 * @details : 교통단속 정보 상세 조회
	 * @author : KC.KIM	
	 * @date : 2023.08.08
	 * @param : tfcEnfId
	 * @return :
	 */
	@Override
	public MozTfcEnfMaster getTrafficEnfDetail(String tfcEnfId) {
		MozTfcEnfMaster tfcEnfMaster = tfcEnfMasterRepository.findOneMozTfcEnfMasterBytfcEnfId(tfcEnfId);
		
		List<MozTfcEnfFileInfo> fileList = new ArrayList<MozTfcEnfFileInfo>();
		fileList = tfcEnfFileInfoRepository.findTfcEnfFileInfoByTfcEnfId(tfcEnfId);
		tfcEnfMaster.setTfcEnfFileInfoList(fileList);
				
		return tfcEnfMaster;
	}

	/**
	 * @brief : 교통단속 정보 등록
	 * @details : 교통단속 정보 등록
	 * @author : KC.KIM
	 * @date : 2024.03.06
	 * @param : tfcEnfMaster
	 * @param : uploadFiles
	 * @return :
	 */
	@Override
	@Transactional
	public void insertMozTfcEnfMaster(MozTfcEnfMaster tfcEnfMaster, MultipartFile[] uploadFiles) {
		String crtr = LoginOprtrUtils.getOprtrId();
		String code = TrafficEnforcementStatus.REG;
        String date = DateFormatUtils.format(tfcEnfMaster.getTfcEnfDt(), "yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        Date crDt = Timestamp.valueOf(now);
        
        MozPolInfo dbPolInfo =  polInfoRepository.findOneMozPolInfo(tfcEnfMaster.getPolId());
        String enfIdStr = TrafficIdClassification.ENFORCEMENT + "-" + date + "-" + dbPolInfo.getPolLcenId();
        Long tfcEnfSeq = tfcEnfMasterRepository.countPolSeqByTfcEnfId(enfIdStr);
        String creator = dbPolInfo.getPolLcenId() + "-" + String.valueOf(tfcEnfSeq);
        
        String tfcEnfId = MozatesCommonUtils.getTfcEnfId(creator, tfcEnfMaster.getTfcEnfDt());
		tfcEnfMaster.setTfcEnfId(tfcEnfId);
		tfcEnfMaster.setCrtr(crtr);

		//위반자 정보 저장
		String vioId = MozatesCommonUtils.getUuid();
		MozVioInfo vioInfo = tfcEnfMaster.getVioInfo();
		vioInfo.setVioId(vioId);
		vioInfo.setCrtr(crtr);
		
		vioInfoRepository.insertVioInfo(vioInfo);
		
		// 교통단속 정보 저장
		tfcEnfMaster.setVioId(vioId);
		tfcEnfMaster.setLastTfcEnfProcCd(code);
		tfcEnfMasterRepository.insertTfcEnfInfo(tfcEnfMaster);
		
		//단속 파일 저장
		if(uploadFiles != null && uploadFiles.length >= 1) {
			for(MultipartFile uploadFile : uploadFiles) {
				String[] extArr = {"jpg", "jpeg", "png", "PNG", "webm", "avi", "mp4"};
				UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(uploadFile, extArr);
				MozTfcEnfFileInfo atchFile = new MozTfcEnfFileInfo();
				atchFile.setTfcEnfId(tfcEnfId);
				atchFile.setFileNm(uploadFileInfo.getFileNm());
				atchFile.setFilePath(uploadFileInfo.getFilePath());
				atchFile.setFileSize(String.valueOf(uploadFileInfo.getFileSize()));
				atchFile.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
				tfcEnfFileInfoRepository.insertTfcEnfFileInfo(atchFile);

			}
		}
		
		//범칙금 정보 저장
		List<MozTfcEnfFineInfo> fineInfoList = tfcEnfMaster.getTfcFineNtcInfoList();
		for(MozTfcEnfFineInfo fineInfo : fineInfoList) {
			fineInfo.setTfcEnfId(tfcEnfId);
			fineInfo.setTfcEnfDt(tfcEnfMaster.getTfcEnfDt());
			tfcEnfFineInfoRepository.insertTfcEnfFineInfo(fineInfo);
		}
		
		//고지관리 정보 저장
		MozFineNtcInfo fineNtcInfo = new MozFineNtcInfo();
		String fineNtcId = MozatesCommonUtils.getUuid();
		fineNtcInfo.setFineNtcId(fineNtcId);
		fineNtcInfo.setTfcEnfId(tfcEnfId);
		fineNtcInfo.setFirstFineNtcDt(tfcEnfMaster.getTfcEnfDt());
		fineNtcInfo.setFirstFineNtcPrice(tfcEnfMaster.getTotalPrice());
		fineNtcInfo.setNtcTy(NtcTypeCd.FIRST_NOTICE.getCode());
		fineNtcInfo.setFirstFineNtcDdln(MozatesCommonUtils.calculateAfterDays(tfcEnfMaster.getTfcEnfDt(), 15, Calendar.DAY_OF_MONTH));
		fineNtcInfo.setCrtr(crtr);
		fineNtcInfo.setCrDt(crDt);
		fineNtcInfoRepository.insertFineNtcInfo(fineNtcInfo);
		
		//벌금 결제 정보 저장
        MozFinePymntInfo finePymntInfo = new MozFinePymntInfo();
        finePymntInfo = tfcEnfMaster.getFinePymntInfo();
        String pymntId = MozatesCommonUtils.getUuid();
        finePymntInfo.setPymntId(pymntId);
        finePymntInfo.setFineNtcId(fineNtcId);
        finePymntInfo.setPayerNm(vioInfo.getVioNm());
        finePymntInfo.setTotalPrice(tfcEnfMaster.getTotalPrice());
        finePymntInfo.setPymntPrice(0F);
        finePymntInfo.setPymntStts("N");
        finePymntInfo.setCrtr(crtr);
        finePymntInfoRepository.insertFinePaymentInfo(finePymntInfo);
		
		//단속 로그 저장
		MozTfcEnfHst tfcEnfHst = new MozTfcEnfHst();
        String enfHstId = MozatesCommonUtils.getUuid();
        String crtrIpAddr = LoginOprtrUtils.getUserIpAddr();
        String registerType = getEnfRegType(LoginOprtrUtils.getOprtrPermission());
        tfcEnfHst.setHstId(enfHstId);
        tfcEnfHst.setTfcEnfRegTy(registerType);
        tfcEnfHst.setTfcEnfId(tfcEnfId);
        tfcEnfHst.setTfcEnfPrcCd(code);
        tfcEnfHst.setCrtr(crtr);
        tfcEnfHst.setCrtrIpAddr(crtrIpAddr);
        tfcEnfHstRepository.insertTfcEnfHstInfo(tfcEnfHst);
        
        // SMS 발송
        MozMsgQueue mozMsgQueue = new MozMsgQueue();
		mozMsgQueue.setSender(sender);
		mozMsgQueue.setMsgType(MsgType.SMS);
		mozMsgQueue.setStatus(MsgQueueStatus.WAITING);
		mozMsgQueue.setRetry(0);
		//TODO::URL추가 이나트로 결제
		mozMsgQueue.setContent(SmsSendContentUtils.fineNoticeSmsContent(vioInfo.getVioNm(), url, NtcTypeCd.FIRST_NOTICE ,sender,tfcEnfId));
		mozMsgQueue.setReceiver(vioInfo.getVioPno());
		mozMsgQueue.setTfcEnfId(tfcEnfId);
		mozMsgQueueRepository.saveMozMsgQueue(mozMsgQueue);
	}
	
	/**
	 * @brief : 교통단속 정보 수정
	 * @details : 교통단속 정보 수정
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfMaster
	 * @return :
	 */
	@Override
	@Transactional
	public void updateInfo(MozTfcEnfMaster tfcEnfMaster, MultipartFile[] uploadFiles) {
		String crtr = LoginOprtrUtils.getOprtrId();
		LocalDateTime now = LocalDateTime.now();
        Date crDt = Timestamp.valueOf(now);
        String code = TrafficEnforcementStatus.UPD;
		//위반자 정보 수정
		vioInfoRepository.updateMozVioINfo(tfcEnfMaster.getVioInfo());
		
		//교통단속 정보 수정
		tfcEnfMasterRepository.updateMozTfcEnfMaster(tfcEnfMaster);
		
		//단속 파일 수정
		List<MozTfcEnfFileInfo> deleteAtchFileList = new ArrayList<MozTfcEnfFileInfo>();
		deleteAtchFileList = tfcEnfMaster.getTfcEnfFileInfoList();
		
		if(deleteAtchFileList != null) {
			for(MozTfcEnfFileInfo deleteFileInfo : deleteAtchFileList) {
				if(!MozatesCommonUtils.isNull(deleteFileInfo.getVioFileNo())) {
					MozTfcEnfFileInfo fileInfo = new MozTfcEnfFileInfo();
					fileInfo = tfcEnfFileInfoRepository.findOneByMozTfcEnfFileInfoByVioFileId(String.valueOf(deleteFileInfo.getVioFileNo()));
					fileUploadComponent.deleteUploadFile(fileInfo.getFilePath());
					tfcEnfFileInfoRepository.deleteMozTfcEnfFileInfoByVioFileNo(String.valueOf(fileInfo.getVioFileNo()));				
				}
			}
		}
		if(uploadFiles != null && uploadFiles.length >= 1) {
			for(MultipartFile uploadFile : uploadFiles) {
				if(!MozatesCommonUtils.isNull(uploadFile.getOriginalFilename())) {
					String[] extArr = {"jpg", "jpeg", "png", "PNG", "webm", "avi", "mp4"};
					UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(uploadFile, extArr);
					MozTfcEnfFileInfo atchFile = new MozTfcEnfFileInfo();
					atchFile.setTfcEnfId(tfcEnfMaster.getTfcEnfId());
					atchFile.setFileNm(uploadFileInfo.getFileNm());
					atchFile.setFilePath(uploadFileInfo.getFilePath());
					atchFile.setFileSize(String.valueOf(uploadFileInfo.getFileSize()));
					atchFile.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
					tfcEnfFileInfoRepository.insertTfcEnfFileInfo(atchFile);					
				}
			}
		}
		MozFineNtcInfo dbFineNtcInfo = fineNtcInfoRepository.findOneFineNtcInfoByTfcEnfId(tfcEnfMaster.getTfcEnfId());
		finePymntInfoRepository.deleteFinePymntInfoByTfcEnfId(dbFineNtcInfo.getFineNtcId());
		fineNtcInfoRepository.deleteFineNtcInfoByTfcEnfId(tfcEnfMaster.getTfcEnfId());
		tfcEnfFineInfoRepository.deleteFineNtcInfoByTfcEnfId(tfcEnfMaster.getTfcEnfId());
		
		//범칙금 정보 저장
		List<MozTfcEnfFineInfo> fineInfoList = tfcEnfMaster.getTfcFineNtcInfoList();
		for(MozTfcEnfFineInfo fineInfo : fineInfoList) {
			fineInfo.setTfcEnfId(tfcEnfMaster.getTfcEnfId());
			fineInfo.setTfcEnfDt(tfcEnfMaster.getTfcEnfDt());
			tfcEnfFineInfoRepository.insertTfcEnfFineInfo(fineInfo);
		}
		
		//고지관리 정보 저장
		MozFineNtcInfo fineNtcInfo = new MozFineNtcInfo();
		String fineNtcId = MozatesCommonUtils.getUuid();
		fineNtcInfo.setFineNtcId(fineNtcId);
		fineNtcInfo.setTfcEnfId(tfcEnfMaster.getTfcEnfId());
		fineNtcInfo.setFirstFineNtcDt(tfcEnfMaster.getTfcEnfDt());
		fineNtcInfo.setFirstFineNtcPrice(tfcEnfMaster.getTotalPrice());
		fineNtcInfo.setNtcTy(NtcTypeCd.FIRST_NOTICE.getCode());
		fineNtcInfo.setFirstFineNtcDdln(MozatesCommonUtils.calculateAfterDays(tfcEnfMaster.getTfcEnfDt(), 15, Calendar.DAY_OF_MONTH));
		fineNtcInfo.setCrtr(crtr);
		fineNtcInfo.setCrDt(crDt);
		fineNtcInfoRepository.insertFineNtcInfo(fineNtcInfo);
		
		//벌금 결제 정보 저장
        MozFinePymntInfo finePymntInfo = new MozFinePymntInfo();
        finePymntInfo = tfcEnfMaster.getFinePymntInfo();
        String pymntId = MozatesCommonUtils.getUuid();
        finePymntInfo.setPymntId(pymntId);
        finePymntInfo.setFineNtcId(fineNtcId);
        finePymntInfo.setPayerNm(tfcEnfMaster.getVioInfo().getVioNm());
        finePymntInfo.setTotalPrice(tfcEnfMaster.getTotalPrice());
        finePymntInfo.setPymntPrice(0F);
        finePymntInfo.setPymntStts("N");
        finePymntInfo.setCrtr(crtr);
        finePymntInfoRepository.insertFinePaymentInfo(finePymntInfo);
        
       //단속 로그 저장
       MozTfcEnfHst tfcEnfHst = new MozTfcEnfHst();
       String enfHstId = MozatesCommonUtils.getUuid();
       String crtrIpAddr = LoginOprtrUtils.getUserIpAddr();
       String registerType = getEnfRegType(LoginOprtrUtils.getOprtrPermission());
       tfcEnfHst.setHstId(enfHstId);
       tfcEnfHst.setTfcEnfRegTy(registerType);
       tfcEnfHst.setTfcEnfId(tfcEnfMaster.getTfcEnfId());
       tfcEnfHst.setTfcEnfPrcCd(code);
       tfcEnfHst.setCrtr(crtr);
       tfcEnfHst.setCrtrIpAddr(crtrIpAddr);
       tfcEnfHstRepository.insertTfcEnfHstInfo(tfcEnfHst);
	}
	
	/**
     * @brief : 교통단속 정보 삭제
     * @details : 교통단속 정보 삭제
     * @author : KC.KIM
     * @date : 2024.03.11
     * @param : tfcEnfMaster
     * @return : 
     */
	@Override
	@Transactional
	public void deleteTfcEnfMasterByTfcEnfId(MozTfcEnfMaster tfcEnfMaster) {
		String code = TrafficEnforcementStatus.DTL;
		String crtr = LoginOprtrUtils.getOprtrId();
		if(MozatesCommonUtils.isNull(tfcEnfMaster.getTfcEnfId())) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		
		// 위반 파일 정보 삭제
		List<MozTfcEnfFileInfo> tfcEnfFileInfoList = new ArrayList<MozTfcEnfFileInfo>();
		tfcEnfFileInfoList = tfcEnfFileInfoRepository.findTfcEnfFileInfoByTfcEnfId(tfcEnfMaster.getTfcEnfId());
		if(tfcEnfFileInfoList != null) {
			for(MozTfcEnfFileInfo fileItem : tfcEnfFileInfoList) {
				fileUploadComponent.deleteUploadFile(fileItem.getFilePath());
			}			
		}
		tfcEnfFileInfoRepository.deleteTfcEnfFileInfoByTfcEnfId(tfcEnfMaster.getTfcEnfId());

		MozTfcEnfMaster dbEnfMaster = tfcEnfMasterRepository.findOneMozTfcEnfMasterBytfcEnfId(tfcEnfMaster.getTfcEnfId());
		MozFineNtcInfo fineNtcInfo = fineNtcInfoRepository.findOneFineNtcInfoByTfcEnfId(tfcEnfMaster.getTfcEnfId());
		finePymntInfoRepository.deleteFinePymntInfoByTfcEnfId(fineNtcInfo.getFineNtcId());
		fineNtcInfoRepository.deleteFineNtcInfoByTfcEnfId(tfcEnfMaster.getTfcEnfId());
		tfcEnfFineInfoRepository.deleteFineNtcInfoByTfcEnfId(tfcEnfMaster.getTfcEnfId());
		tfcEnfMasterRepository.deleteTfcEnfMasterByTfcEnfId(dbEnfMaster.getTfcEnfId());
		vioInfoRepository.deleteVioInfoByVioId(dbEnfMaster.getVioId());
		
		//단속 로그 저장
	    MozTfcEnfHst tfcEnfHst = new MozTfcEnfHst();
	    String enfHstId = MozatesCommonUtils.getUuid();
	    String crtrIpAddr = LoginOprtrUtils.getUserIpAddr();
	    String registerType = getEnfRegType(LoginOprtrUtils.getOprtrPermission());
	    tfcEnfHst.setHstId(enfHstId);
	    tfcEnfHst.setTfcEnfRegTy(registerType);
	    tfcEnfHst.setTfcEnfId(tfcEnfMaster.getTfcEnfId());
	    tfcEnfHst.setTfcEnfPrcCd(code);
	    tfcEnfHst.setCrtr(crtr);
	    tfcEnfHst.setCrtrIpAddr(crtrIpAddr);
	    tfcEnfHstRepository.insertTfcEnfHstInfo(tfcEnfHst);
	}
	
	/**
     * @brief : 교통단속 정보 삭제(soft Delete)
     * @details : 교통단속 정보 삭제(soft Delete)
     * @author : KC.KIM
     * @date : 2024.03.11
     * @param : tfcEnfMaster
     * @return : 
     */
	@Override
	@Transactional
	public void deleteTfcEnfMaster(MozTfcEnfMaster tfcEnfMaster) {
		String code = TrafficEnforcementStatus.DTL;
		String crtr = LoginOprtrUtils.getOprtrId();
		tfcEnfMaster.setDelYn("Y");
		
		MozTfcEnfMaster dbTfcEnfMaster = tfcEnfMasterRepository.findOneMozTfcEnfMaster(tfcEnfMaster.getTfcEnfId());
		
		if(!MozatesCommonUtils.isNull(dbTfcEnfMaster.getTfcEnfEqpId())) {
			//단속장비 단속정보 삭제
			mozTfcEqpEnfInfoRepository.deleteTfcEqpEnfInfoByTfcEnfId(dbTfcEnfMaster.getTfcEnfId());
		}
		
		
		tfcEnfMasterRepository.deleteTfcEnfMaster(tfcEnfMaster);
		
		//고지정보 수정
		MozFineNtcInfo fineNtcInfo = fineNtcInfoRepository.findOneFineNtcInfoByTfcEnfId(tfcEnfMaster.getTfcEnfId());
		if(fineNtcInfo != null) {
			fineNtcInfo.setNtcTy(NtcTypeCd.DELETE_NOTICE.getCode());
			fineNtcInfoRepository.updateNtcTyForDeleteEnfInfo(fineNtcInfo);
		}
		
		//단속 로그 저장
	    MozTfcEnfHst tfcEnfHst = new MozTfcEnfHst();
	    String enfHstId = MozatesCommonUtils.getUuid();
	    String crtrIpAddr = LoginOprtrUtils.getUserIpAddr();
	    String registerType = getEnfRegType(LoginOprtrUtils.getOprtrPermission());
	    tfcEnfHst.setHstId(enfHstId);
	    tfcEnfHst.setTfcEnfRegTy(registerType);
	    tfcEnfHst.setTfcEnfId(tfcEnfMaster.getTfcEnfId());
	    tfcEnfHst.setTfcEnfPrcCd(code);
	    tfcEnfHst.setCrtr(crtr);
	    tfcEnfHst.setCrtrIpAddr(crtrIpAddr);
	    tfcEnfHstRepository.insertTfcEnfHstInfo(tfcEnfHst);
	}

	/**
	 * @brief : 교통단속 로그 리스트 조회
	 * @details : 교통단속 로그 리스트 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfHst
	 * @return :
	 */
	@Override
	public List<MozTfcEnfHst> getLogList(MozTfcEnfHst tfcEnfHst) {
		return tfcEnfHstRepository.findAllLogListsByTfcEnfHst(tfcEnfHst);
		
	}

	@Override
	public int getLogListCnt(MozTfcEnfHst tfcEnfHst) {
		return tfcEnfHstRepository.countLogListsByTfcEnfHst(tfcEnfHst);
	}

	/**
	 * @brief : 벌금 정보 수정
	 * @details : 벌금 정보 수정
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : finePymntInfo
	 * @return :
	 */
	@Override
	public void updateInfoPrice(MozFinePymntInfo finePymntInfo) {
		finePymntInfoRepository.updateFineTotalPrice(finePymntInfo);
	}

	/**
	 * @brief : 위반 차량 사진 삭제
	 * @details : 위반 차량 사진 삭제
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfMaster
	 * @return :
	 */
	@Override
	public void deleteEnfImage(MozTfcEnfMaster tfcEnfMaster) {
		tfcEnfMasterRepository.deleteEnfImage(tfcEnfMaster);
	}

	/**
     * @brief : 교통단속 로그 상세 조회
     * @details : 교통단속 로그 상세 조회
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : hstId
     * @return : MozTfcEnfHst
     */
	@Override
	public MozTfcEnfHst getLogDetail(String hstId) {
		return tfcEnfHstRepository.findOneTfcEnfHst(hstId);
	}

	@Override
	public List<MozTfcLwInfo> getTrafficLawsListByNotNullFineInfo() {
		return tfcLwInfoRepository.findAllLawListsIsNotNullFineInfo();
	}

	@Override
	public List<MozPlPymntInfo> getPlacePaymentList() {
		MozPlPymntInfo plPymntInfo = new MozPlPymntInfo();
		return plPymntInfoRepository.findAllPlacePaymentList(plPymntInfo);
	}

	@Override
	public List<MozTfcLwFineInfo> getLawFineInfoList(String tfcLawId) {
		MozTfcLwFineInfo tfcLwFineInfo = new MozTfcLwFineInfo();
		tfcLwFineInfo.setTfcLawId(tfcLawId);
		return tfcLwFineInfoRepository.findMozTfcLwFineInfoByTfcLawId(tfcLawId);
	}
	
	public String getEnfRegType(String oprtrPermissionCdStr) {
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
     * @brief : getViolationCount
     * @details : 단속카메라 단속대상 목록 카운트 
     * @author : KY.LEE
     * @date : 2024.04.06
     * @param : mozCameraEnfOrg
     */
	@Override
	public int getViolationCount(MozCameraEnfOrg mozCameraEnfOrg) {
		return mozCameraEnfOrgRepository.countBySearchOption(mozCameraEnfOrg);
	}

	/**
	 * @brief : getViolationList
	 * @details : 단속 카메라 단속대상 목록
	 * @author : KY.LEE
	 * @date : 2024.04.06
	 * @param : mozCameraEnfOrg
	 */
	@Override
	public List<MozCameraEnfOrg> getViolationList(MozCameraEnfOrg mozCameraEnfOrg) {
		return mozCameraEnfOrgRepository.findAllBySearchOption(mozCameraEnfOrg);
	}

	/**
	 * @brief : getViolationDetail
	 * @details : 단속 카메라 단속대상 상세
	 * @author : KY.LEE
	 * @date : 2024.04.06
	 * @param : mozCameraEnfOrg
	 */
	@Override
	public MozCameraEnfOrg getViolationDetail(Long idx) {
		return mozCameraEnfOrgRepository.fineOneByIdx(idx);
	}
	
	/**
	 * @brief : getViolationImageList
	 * @details : 단속 카메라 이미지 목록 조회
	 * @author : KY.LEE
	 * @date : 2024.04.06
	 * @param : MozCameraEnfOrgFile
	 */
	@Override
	public List<MozCameraEnfOrgFile> getViolationImageList(Long orgIdx) {
		return mozCameraEnfOrgFileRepository.findAllByOrgIdx(orgIdx);
	}

	
	/**
	 * @brief : getViolationImage
	 * @details : 단속 카메라 이미지 파일 경로 조회
	 * @author : KY.LEE
	 * @date : 2024.04.06
	 * @param : MozCameraEnfOrgFile
	 */
	@Override
	public String getViolationImage(Long idx) {
		return mozCameraEnfOrgFileRepository.findOneByIdx(idx);
	}

	/**
	 * @brief : insertMozTfcEnfMasterForEquipment
	 * @details : 단속 카메라 단속 등록
	 * @author : KY.LEE
	 * @date : 2024.04.09
	 * @param : MozTfcEnfMaster
	 */
	@Override
	@Transactional
	public void insertMozTfcEnfMasterForEquipment(MozTfcEnfMaster tfcEnfMaster) {
		List<String> uploadFileList = new ArrayList<String>(); 

		//기본값 세팅
		String crtr = LoginOprtrUtils.getOprtrId();
		String code = TrafficEnforcementStatus.REG;
        String date = DateFormatUtils.format(tfcEnfMaster.getTfcEnfDt(), "yyyyMMdd");
        Date crDt = new Date();
        Long idx = tfcEnfMaster.getIdx();
        
        //DB조회 시작
        MozPolInfo dbPolInfo =  polInfoRepository.findOneMozPolInfo(tfcEnfMaster.getPolId());
        String enfIdStr = TrafficIdClassification.ENFORCEMENT + "-" + date + "-" + dbPolInfo.getPolLcenId();
        Long tfcEnfSeq = tfcEnfMasterRepository.countPolSeqByTfcEnfId(enfIdStr);
        String creator = dbPolInfo.getPolLcenId() + "-" + String.valueOf(tfcEnfSeq);
        
        String tfcEnfId = MozatesCommonUtils.getTfcEnfId(creator, tfcEnfMaster.getTfcEnfDt());
        String tfcEnfEqpId = tfcEnfMaster.getTfcEnfEqpId();
		tfcEnfMaster.setTfcEnfId(tfcEnfId);
		tfcEnfMaster.setCrtr(crtr);

		//위반자 정보 저장
		String vioId = MozatesCommonUtils.getUuid();
		MozVioInfo vioInfo = tfcEnfMaster.getVioInfo();
		vioInfo.setVioId(vioId);
		vioInfo.setCrtr(crtr);
		vioInfoRepository.insertVioInfo(vioInfo);
		
		// 교통단속 정보 저장
		tfcEnfMaster.setVioId(vioId);
		tfcEnfMaster.setLastTfcEnfProcCd(code);
		tfcEnfMasterRepository.insertTfcEnfInfo(tfcEnfMaster);

		MozTfcEqpEnfInfo mozTfcEqpEnfInfo = new MozTfcEqpEnfInfo();
		mozTfcEqpEnfInfo.setIdx(idx);
		mozTfcEqpEnfInfo.setTfcEnfId(tfcEnfId);
		mozTfcEqpEnfInfo.setTfcEnfEqpId(tfcEnfEqpId);
		mozTfcEqpEnfInfo.setCrDt(crDt);
		mozTfcEqpEnfInfo.setCrtr(creator);
		mozTfcEqpEnfInfoRepository.saveMozTfcEqpEnfInfo(mozTfcEqpEnfInfo);
		
		List<MozCameraEnfOrgFile> tfcEnfImgList = mozCameraEnfOrgFileRepository.findAllByOrgIdx(idx);

		try {
			if(!tfcEnfImgList.isEmpty()) {
					for(MozCameraEnfOrgFile mozCameraEnfOrgFile : tfcEnfImgList) {
						//단속 케메라 파일 저장
						UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToCopyFilePath(mozCameraEnfOrgFile.getFilePath() , FileUploadType.ENFORCEMENT);
						
						MozTfcEnfFileInfo atchFile = new MozTfcEnfFileInfo();

						uploadFileList.add(uploadFileInfo.getFilePath());
						atchFile.setTfcEnfId(tfcEnfId);
						atchFile.setFileNm(uploadFileInfo.getFileNm());
						atchFile.setFilePath(uploadFileInfo.getFilePath());
						atchFile.setFileSize(String.valueOf(uploadFileInfo.getFileSize()));
						atchFile.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
						tfcEnfFileInfoRepository.insertTfcEnfFileInfo(atchFile);
					}
			}
			//범칙금 정보 저장
			List<MozTfcEnfFineInfo> fineInfoList = tfcEnfMaster.getTfcFineNtcInfoList();
			for(MozTfcEnfFineInfo fineInfo : fineInfoList) {
				fineInfo.setTfcEnfId(tfcEnfId);
				fineInfo.setTfcEnfDt(tfcEnfMaster.getTfcEnfDt());
				tfcEnfFineInfoRepository.insertTfcEnfFineInfo(fineInfo);
			}
			
			//고지관리 정보 저장
			MozFineNtcInfo fineNtcInfo = new MozFineNtcInfo();
			String fineNtcId = MozatesCommonUtils.getUuid();
			fineNtcInfo.setFineNtcId(fineNtcId);
			fineNtcInfo.setTfcEnfId(tfcEnfId);
			fineNtcInfo.setFirstFineNtcDt(tfcEnfMaster.getTfcEnfDt());
			fineNtcInfo.setFirstFineNtcPrice(tfcEnfMaster.getTotalPrice());
			fineNtcInfo.setNtcTy(NtcTypeCd.FIRST_NOTICE.getCode());
			fineNtcInfo.setFirstFineNtcDdln(MozatesCommonUtils.calculateAfterDays(tfcEnfMaster.getTfcEnfDt(), 15, Calendar.DAY_OF_MONTH));
			fineNtcInfo.setCrtr(crtr);
			fineNtcInfo.setCrDt(crDt);
			fineNtcInfoRepository.insertFineNtcInfo(fineNtcInfo);
			
			//벌금 결제 정보 저장
	        MozFinePymntInfo finePymntInfo = new MozFinePymntInfo();
	        finePymntInfo = tfcEnfMaster.getFinePymntInfo();
	        String pymntId = MozatesCommonUtils.getUuid();
	        finePymntInfo.setPymntId(pymntId);
	        finePymntInfo.setFineNtcId(fineNtcId);
	        finePymntInfo.setPayerNm(vioInfo.getVioNm());
	        finePymntInfo.setTotalPrice(tfcEnfMaster.getTotalPrice());
	        finePymntInfo.setPymntPrice(0F);
	        finePymntInfo.setPymntStts("N");
	        finePymntInfo.setCrtr(crtr);
	        finePymntInfoRepository.insertFinePaymentInfo(finePymntInfo);
			
			//단속 로그 저장
			MozTfcEnfHst tfcEnfHst = new MozTfcEnfHst();
	        String enfHstId = MozatesCommonUtils.getUuid();
	        String crtrIpAddr = LoginOprtrUtils.getUserIpAddr();
	        String registerType = getEnfRegType(LoginOprtrUtils.getOprtrPermission());
	        tfcEnfHst.setHstId(enfHstId);
	        tfcEnfHst.setTfcEnfRegTy(registerType);
	        tfcEnfHst.setTfcEnfId(tfcEnfId);
	        tfcEnfHst.setTfcEnfPrcCd(code);
	        tfcEnfHst.setCrtr(crtr);
	        tfcEnfHst.setCrtrIpAddr(crtrIpAddr);
	        tfcEnfHstRepository.insertTfcEnfHstInfo(tfcEnfHst);
		} catch (CommonException e) {
			//업로드 했던 파일이 존재하면 삭제
			if(!uploadFileList.isEmpty()) {
				for(String uploadFilePath : uploadFileList) {
					fileUploadComponent.deleteUploadFile(uploadFilePath);
				}
			}
		}
	}

	/**
	 * @brief : 위반자 정보 조회
	 * @details : 위반자 정보 조회
	 * @author : KY.LEE
	 * @date : 2024.04.09
	 * @param : driverLicenseId
	 */
	@Override
	public List<MozVioInfo> getViolationInfoList(String dvrLcenId) {
		return vioInfoRepository.findAllViolationInfoByDvrLcenId(dvrLcenId);
	}

	/**
	 * @brief : 위반자 정보 조회
	 * @details : 위반자 정보 조회
	 * @author : KY.LEE
	 * @date : 2024.05.02
	 * @param : docNid
	 */
	@Override
	public List<MozVioInfo> getViolationInfoListByDocNid(String docNid) {
		return vioInfoRepository.findAllViolationInfoListByDocNid(docNid);
	}
}
