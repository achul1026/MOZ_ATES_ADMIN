package com.moz.ates.traffic.admin.penaltymng;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfMaster;
import com.moz.ates.traffic.common.entity.payment.MozFinePymntInfo;
import com.moz.ates.traffic.common.entity.payment.MozPymntLog;
import com.moz.ates.traffic.common.enums.PaymentLogStatus;
import com.moz.ates.traffic.common.enums.PymntMethod;
import com.moz.ates.traffic.common.repository.enforcement.MozTfcEnfMasterRepository;
import com.moz.ates.traffic.common.repository.payment.MozFinePymntInfoRepository;
import com.moz.ates.traffic.common.repository.payment.MozPymntLogRepository;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.support.exception.ErrorCode;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class PenaltyServiceImpl implements PenaltyService {
    
    @Autowired
    MozFinePymntInfoRepository finePymntInfoRepository;
    
    @Autowired
    MozTfcEnfMasterRepository tfcEnfMasterRepository;
    
    @Autowired
    MozPymntLogRepository pymntLogRepository;

    /**
     * @brief : 단속 정보 수정
     * @details : 단속 정보 수정 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : finePymntInfo
     * @return : 
     */
    @Override
	public void updatePenaltyMaster(MozFinePymntInfo finePymntInfo) {
    	MozTfcEnfMaster tfcEnfMaster = new MozTfcEnfMaster();
//    	tfcEnfMaster.setTfcEnfId(finePymntInfo.getTfcEnfId());
    	tfcEnfMaster.setRoadAddr(finePymntInfo.getTfcEnfMaster().getRoadAddr());
    	tfcEnfMaster.setVhRegNo(finePymntInfo.getTfcEnfMaster().getVhRegNo());
    	tfcEnfMasterRepository.updateMozTfcEnfMaster(tfcEnfMaster);
	}
    
    /**
     * @brief : 범칙금 등록
     * @details : 범칙금 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : finePymntInfo
     * @return : 
     */
    @Override
    public void registPenalty(MozFinePymntInfo finePymntInfo) {
    	finePymntInfo.setCrtr("lim");
//    	finePymntInfoRepository.insertFinePaymentInfo(finePymntInfo);
    	finePymntInfoRepository.saveMozFinePymntInfo(finePymntInfo);
    }

    /**
     * @brief : 범칙금 상세 조회
     * @details : 범칙금 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : pymntId
     * @return : 
     */
    @Override
    public MozFinePymntInfo getPenaltyDetail(String pymntId) {
    	return finePymntInfoRepository.findOneMozFinePymntInfoForAdmin(pymntId);
    }
    
    /**
     * @brief : 범칙금 수정
     * @details : 범칙금 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : finePymntInfo
     * @return : 
     */
    @Override
	public void updatePenalty(MozFinePymntInfo finePymntInfo) {
		finePymntInfoRepository.updateMozFinePymntInfo(finePymntInfo);
	}
    
    /**
     * @brief : 범칙금 납부 처리
     * @details : 범칙금 납부 처리
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : pymntId
     * @return : 
     */
	@Override
	public void paymentClear(String pymntId) {
		finePymntInfoRepository.updateMozFinePymntInfoForPymntY(pymntId);
	}

    /**
     * @brief : 범칙금 미납 처리
     * @details : 범칙금 미납 처리
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : pymntId
     * @return : 
     */
	@Override
	public void paymentCencal(String pymntId) {
		finePymntInfoRepository.updateMozFinePymntInfoForPymntN(pymntId);
	}

//	//email
//	@Override
//	public Map<String, Object> penaltySendEmail(String pymntId, String emailAddr, String content) {
//		Map<String,Object> penaltyResult = new HashMap<String,Object>();
//		MimeMessage message = sender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(message);
//		String penaltyTitle = "We will inform you of the fine";
//		try {
//			helper.setTo(emailAddr);
//			helper.setSubject(penaltyTitle);
//			helper.setText(content);
//			penaltyResult.put("resultCode", 200);
//			
//		} catch(MessagingException e) {
//			e.printStackTrace();
//			penaltyResult.put("resultCode", 500);
//		}
//		sender.send(message);
//		return penaltyResult;
//	}

    /**
     * @brief : 범칙금 리스트 조회
     * @details : 범칙금 리스트 조회 
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : finePymntInfo
     * @return : 
     */
	@Override
	public List<MozFinePymntInfo> getPenaltyList(MozFinePymntInfo finePymntInfo) {
		return finePymntInfoRepository.findAllMozFinePymntInfo(finePymntInfo);
	}
	
    /**
     * @brief : 범칙금 리스트 개수 조회
     * @details : 범칙금 리스트 조회 
     * @author : KC.KIM
     * @date : 2023.09.13
     * @param : finePymntInfo
     * @return : 
     */
	@Override
	public int getPenaltyListCnt(MozFinePymntInfo finePymntInfo) {
		return finePymntInfoRepository.countMozFinePymntInfo(finePymntInfo);
	}

	@Override
	public void updatePayStatus(MozFinePymntInfo finePymntInfo) {
		try {
			String pymntId = finePymntInfo.getPymntId();
			MozFinePymntInfo mozFinePymntInfo = finePymntInfoRepository.findOnePenaltyDetail(pymntId);
			if(mozFinePymntInfo == null) {
				//Exception
			}
			
			if(mozFinePymntInfo.getTotalPrice() != finePymntInfo.getTotalPrice()) {
				//Exception 금액이 다릅니다.
			}
			//DB 조회 값에서 미납(N)이면 납부로(Y) 납부면(Y) 미납으로(N)
			if("N".equals(mozFinePymntInfo.getPymntStts())) {
				finePymntInfoRepository.updateMozFinePymntInfoForPymntY(pymntId);
			}else {
				finePymntInfoRepository.updateMozFinePymntInfoForPymntN(pymntId);
			}
			
		}catch (Exception e) {
		}
	}

    /**
     * @brief : 현장 결제 로직
     * @details : 현장 결제 로직
     * @author : KY.LEE
     * @date : 2024.03.20
     * @param : onsitePayment
     * @return : 
     */
	@Override
	public void onsitePayment(MozFinePymntInfo mozFinePymntInfo) {
		MozFinePymntInfo dbMozFinePymntInfo = finePymntInfoRepository.findOneMozFinePymntInfoForAdmin(mozFinePymntInfo.getPymntId());
		
		Float paymentPrice = mozFinePymntInfo.getPymntPrice();
		String payerNm = mozFinePymntInfo.getPayerNm();
		String penaltydetail = mozFinePymntInfo.getPenaltydetail();
		String placePymntId = mozFinePymntInfo.getPlacePymntId();
		
		if(paymentPrice != null) {
			if(dbMozFinePymntInfo != null) {
				dbMozFinePymntInfo.setPymntStts("Y");
				dbMozFinePymntInfo.setPymntPrice(paymentPrice);
				dbMozFinePymntInfo.setPayerNm(payerNm);
				dbMozFinePymntInfo.setPymntDt(new Date());
				dbMozFinePymntInfo.setPymntMethod(PymntMethod.CASH.getCode());
				dbMozFinePymntInfo.setPymntOprtr(LoginOprtrUtils.getOprtrId());
				dbMozFinePymntInfo.setPenaltydetail(penaltydetail);
				dbMozFinePymntInfo.setPlacePymntId(placePymntId);
				finePymntInfoRepository.updatePymntStts(dbMozFinePymntInfo);
				
				MozPymntLog mozPymntLog = new MozPymntLog();
				mozPymntLog.setPymntLogId(MozatesCommonUtils.getUuid());
				mozPymntLog.setPymntId(mozFinePymntInfo.getPymntId());
				mozPymntLog.setLogStts(PaymentLogStatus.SUCCESS);
				mozPymntLog.setLogType(PymntMethod.CASH.getCode());
				mozPymntLog.setPayer(payerNm);
				mozPymntLog.setLogDetail("On-site payment");
				mozPymntLog.setReqPrice(paymentPrice);
				mozPymntLog.setPolId(dbMozFinePymntInfo.getPolId());
				mozPymntLog.setOprtrId(LoginOprtrUtils.getOprtrId());
				pymntLogRepository.saveMozPymntLog(mozPymntLog);
			} else {
				throw new CommonException(ErrorCode.ENTITY_DATA_NULL);
			}
		} else {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		
	}

	
	/**
	 * @brief : 결제 취소
	 * @details : 결제 취소
	 * @author : KY.LEE
	 * @date : 2024.03.29
	 * @param : cancelPayment
	 * @return : 
	 */
	@Override
	public void cancelPayment(String pymntId , PymntMethod pymntMethod) {
		MozFinePymntInfo dbMozFinePymntInfo = finePymntInfoRepository.findOneMozFinePymntInfoForDev(pymntId);
		
		if(dbMozFinePymntInfo != null) {
			String payerNm = dbMozFinePymntInfo.getPayerNm();
			Float paymentPrice = dbMozFinePymntInfo.getPymntPrice();
			String dbPymntMethod = dbMozFinePymntInfo.getPymntMethod();
			MozPymntLog mozPymntLog = new MozPymntLog();
			
			switch(pymntMethod) {
				case CASH :
					//현금결제
					dbMozFinePymntInfo.setPymntStts("N");
					dbMozFinePymntInfo.setPymntPrice(0f);
					dbMozFinePymntInfo.setPayerNm(null);
					dbMozFinePymntInfo.setPymntDt(null);
					dbMozFinePymntInfo.setPymntMethod(null);
					dbMozFinePymntInfo.setPymntOprtr(LoginOprtrUtils.getOprtrId());
					dbMozFinePymntInfo.setPenaltydetail("Payment Cancel");
					finePymntInfoRepository.updatePymntStts(dbMozFinePymntInfo);
					
					mozPymntLog.setPymntLogId(MozatesCommonUtils.getUuid());
					mozPymntLog.setLogStts(PaymentLogStatus.CANCEL);
					mozPymntLog.setPymntId(pymntId);
					mozPymntLog.setLogType(dbPymntMethod);
					mozPymntLog.setPayer(payerNm);
					mozPymntLog.setReqPrice(paymentPrice);
					mozPymntLog.setLogDetail("Payment Cancel");
					mozPymntLog.setPolId(dbMozFinePymntInfo.getPolId());
					mozPymntLog.setOprtrId(LoginOprtrUtils.getOprtrId());
					pymntLogRepository.saveMozPymntLog(mozPymntLog);
					
					break;
				case CHECK_CARD:
					//TODO::결제모듈 카드취소
					break;
				case CREDIT_CARD:
					//TODO::결제모듈 카드취소
					break;
				case ACCOUNT_TRANSFER:
					//계좌 이체
					dbMozFinePymntInfo.setPymntStts("N");
					dbMozFinePymntInfo.setPymntPrice(0f);
					dbMozFinePymntInfo.setPayerNm(null);
					dbMozFinePymntInfo.setPymntDt(null);
					dbMozFinePymntInfo.setPymntMethod(null);
					dbMozFinePymntInfo.setPymntOprtr(LoginOprtrUtils.getOprtrId());
					dbMozFinePymntInfo.setPenaltydetail("Payment Cancel");
					finePymntInfoRepository.updatePymntStts(dbMozFinePymntInfo);
					
					mozPymntLog.setPymntLogId(MozatesCommonUtils.getUuid());
					mozPymntLog.setLogStts(PaymentLogStatus.CANCEL);
					mozPymntLog.setPymntId(pymntId);
					mozPymntLog.setLogType(dbPymntMethod);
					mozPymntLog.setPayer(payerNm);
					mozPymntLog.setReqPrice(paymentPrice);
					mozPymntLog.setLogDetail("Payment Cancel");
					mozPymntLog.setPolId(dbMozFinePymntInfo.getPolId());
					mozPymntLog.setOprtrId(LoginOprtrUtils.getOprtrId());
					pymntLogRepository.saveMozPymntLog(mozPymntLog);

					break;
			}
		} else {
			throw new CommonException(ErrorCode.ENTITY_DATA_NULL);
		}
	}
}

