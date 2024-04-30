package com.moz.ates.traffic.admin.scheduler.job.fine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.common.entity.administrative.MozAdministDip;
import com.moz.ates.traffic.common.entity.common.MozMsgQueue;
import com.moz.ates.traffic.common.entity.driver.MozVioInfo;
import com.moz.ates.traffic.common.enums.MsgQueueStatus;
import com.moz.ates.traffic.common.enums.MsgType;
import com.moz.ates.traffic.common.enums.NtcTypeCd;
import com.moz.ates.traffic.common.repository.administrative.MozAdministDipRepository;
import com.moz.ates.traffic.common.repository.common.MozMsgQueueRepository;
import com.moz.ates.traffic.common.repository.driver.MozVioInfoRepository;
import com.moz.ates.traffic.common.repository.finentc.MozFineNtcInfoRepository;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;
import com.moz.ates.traffic.common.util.SmsSendContentUtils;

/**
 * @brief : 고지 스케쥴러
 * @details : 고지 스케쥴러
 * @author : NK.KIM
 * @date : 2024.02.23
 */
@Service
public class FineJobService {
	
	@Value("${mail.sender.inatro}")
	private String sender;

	@Value("${mail.url.inatro}")
	private String inatroUrl;
	
	@Value("${mail.url.portal}")
	private String portalUrl;
	
	
	@Autowired
	MozFineNtcInfoRepository mozFineNtcInfoRepository;
	
	@Autowired
	MozVioInfoRepository mozVioInfoRepository;
	
	@Autowired
	MozMsgQueueRepository mozMsgQueueRepository;
	
	@Autowired
	MozAdministDipRepository mozAdministDipRepository;
	
	
	
	/**
     * @brief : 1차 고지 만료 체크 후 2차 고지 데이터 업데이트 
     * @details : 1차 고지 만료 체크 후 2차 고지 데이터 업데이트
     * @author : NK.KIM
     * @date : 2024.02.23
     */
	public void updateFirstNoticeBatch() {
		//고지서 상태값 Update
		//0대기 1진행 2성공 9실패
		mozFineNtcInfoRepository.updateFirstNoticeBatch();
		
		//2차고지로 변경된 고지서의 Violator조회
		List<MozVioInfo> mozVioInfoList = mozVioInfoRepository.findAllSecondFineNtcVioInfo();
		
		if(mozVioInfoList != null && !mozVioInfoList.isEmpty()) {
			//SMS발송
			for(MozVioInfo mozVioInfo : mozVioInfoList) {
				MozMsgQueue mozMsgQueue = new MozMsgQueue();
				mozMsgQueue.setSender(sender);
				mozMsgQueue.setMsgType(MsgType.SMS);
				mozMsgQueue.setStatus(MsgQueueStatus.WAITING);
				mozMsgQueue.setRetry(0);
				//TODO::URL추가 이나트로 결제
				mozMsgQueue.setContent(SmsSendContentUtils.fineNoticeSmsContent(mozVioInfo.getVioNm(), portalUrl, NtcTypeCd.SECOND_NOTICE ,sender,mozVioInfo.getTfcEnfId()));
				mozMsgQueue.setReceiver(mozVioInfo.getVioPno());
				mozMsgQueue.setTfcEnfId(mozVioInfo.getTfcEnfId());
				mozMsgQueueRepository.saveMozMsgQueue(mozMsgQueue);
			}	
		}
	}
	
	/**
     * @brief : 2차 고지 만료 체크 후 행정처리
     * @details : 2차 고지 만료 체크 후 행정처리
     * @author : KY.LEE
     * @date : 2024.04.03
     */
	public void updateSecondNoticeBatch() {
		//고지서 상태값 UPDATE
		mozFineNtcInfoRepository.updateSecondNoticeBatch();
		
		List<MozVioInfo> mozVioInfoList = mozVioInfoRepository.findAllAdministrativeActionVioInfo();
		
		if(mozVioInfoList != null && !mozVioInfoList.isEmpty()) {
			//SMS발송
			for(MozVioInfo mozVioInfo : mozVioInfoList) {
				MozMsgQueue mozMsgQueue = new MozMsgQueue();
				mozMsgQueue.setSender(sender);
				mozMsgQueue.setMsgType(MsgType.SMS);
				mozMsgQueue.setStatus("0");
				mozMsgQueue.setRetry(0);
				//TODO::URL추가 이나트로 결제페이지
				mozMsgQueue.setContent(SmsSendContentUtils.fineNoticeSmsContent(mozVioInfo.getVioNm(), inatroUrl, NtcTypeCd.ADMINISTRATIVE_ACTION ,sender,mozVioInfo.getTfcEnfId()));
				mozMsgQueue.setReceiver(mozVioInfo.getVioPno());
				mozMsgQueue.setTfcEnfId(mozVioInfo.getTfcEnfId());
				mozMsgQueueRepository.saveMozMsgQueue(mozMsgQueue);
				
				MozAdministDip mozAdministDip = new MozAdministDip();
				mozAdministDip.setAdministDipId(MozatesCommonUtils.getUuid());
				mozAdministDip.setTfcEnfId(mozVioInfo.getTfcEnfId());
				mozAdministDip.setTfcLawId(mozVioInfo.getTfcLawId());
				mozAdministDip.setProHoldYn("N");
				mozAdministDip.setCrtr("batch");
				//범칙금 미납부
				mozAdministDip.setCaseTy("ADC000");
				mozAdministDip.setVioId(mozVioInfo.getVioId());
				mozAdministDip.setProcessYn("N");
				mozAdministDip.setDipDesc("Não pagamento de multas"); //범칙금 미납부
				mozAdministDipRepository.saveMozAdministDip(mozAdministDip);
			}
		}
	}
}
