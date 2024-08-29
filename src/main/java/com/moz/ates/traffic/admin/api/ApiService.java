package com.moz.ates.traffic.admin.api;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfMaster;
import com.moz.ates.traffic.common.entity.payment.MozFinePymntInfo;
import com.moz.ates.traffic.common.entity.payment.MozPymntLog;
import com.moz.ates.traffic.common.enums.PaymentLogStatus;
import com.moz.ates.traffic.common.enums.PymntMethod;
import com.moz.ates.traffic.common.repository.payment.MozFinePymntInfoRepository;
import com.moz.ates.traffic.common.repository.payment.MozPymntLogRepository;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.support.exception.ErrorCode;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class ApiService {
	
	@Autowired
	MozFinePymntInfoRepository finePymntInfoRepository;
	
	@Autowired
	MozPymntLogRepository pymntLogRepository;
	
	/**
	  * @Method Name : checkRequestData
	  * @Date : 2024. 7. 1.
	  * @Author : IK.MOON
	  * @Method Brief : request 값 검증
	  * @param requestBody
	  * @param datePattern
	  * @return
	  */
	public MozFinePymntInfo checkRequestData(Map<String, Object> requestBody, String datePattern) {
		MozFinePymntInfo finePymntInfo = new MozFinePymntInfo();
		MozTfcEnfMaster tfcEnfMaster = new MozTfcEnfMaster();
		finePymntInfo.setTfcEnfMaster(tfcEnfMaster);
		
		// tfcEnfId
		finePymntInfo.getTfcEnfMaster().setTfcEnfId(String.valueOf(requestBody.get("nr_aviso")));
		
		// payerNm
	  String payerNm = (String) requestBody.get("nome_do_pagador");
	  if (payerNm == null) {
	  	throw new CommonException(ErrorCode.REQUIRED_FIELDS);
	  }
		finePymntInfo.setPayerNm(payerNm);
		
		// pymntPrice
		finePymntInfo.setPymntPrice(Float.parseFloat(String.valueOf(requestBody.get("valor_do_pagamento"))));
		
		// pymntDt
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
		LocalDateTime localDateTime = LocalDateTime.parse((String.valueOf(requestBody.get("data_de_pagamento"))), formatter);
		
		finePymntInfo.setPymntDt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
		
		return finePymntInfo;
	}
	
	/**
	  * @Method Name : updateFinePymnt
	  * @Date : 2024. 7. 1.
	  * @Author : IK.MOON
	  * @Method Brief : 결제정보 업데이트
	  * @param finePymntInfo
	  */
	@Transactional
	public void updateFinePymnt(MozFinePymntInfo finePymntInfo) {
		// 결제 고유번호 조회
		String pymntId =  finePymntInfoRepository.findOnePymntIdByTfcEnfId(finePymntInfo.getTfcEnfMaster());
		if (MozatesCommonUtils.isNull(pymntId)) {
			// Error: Payment information not found.
			throw new CommonException(ErrorCode.ENTITY_DATA_NULL, "Erro: Informações de pagamento não encontradas.");
		}
		finePymntInfo.setPymntId(pymntId);
		
		// 벌금 결제 정보 테이블 업데이트
		finePymntInfo.setPymntMethod(PymntMethod.INATRO_PORTAL.getCode());
		int updateResult = finePymntInfoRepository.updateFinePymntInfoByApiData(finePymntInfo);
		if (updateResult != 1) {
			// Error: Update failed
			throw new CommonException(ErrorCode.ENTITY_UPDATE_FAIL, "Erro: Falha na atualização");
		}
		
		// 결제 정보 로그 테이블 업데이트
		MozPymntLog mozPymntLog = new MozPymntLog();
		mozPymntLog.setPymntLogId(MozatesCommonUtils.getUuid());
		mozPymntLog.setPymntId(finePymntInfo.getPymntId());
		mozPymntLog.setLogStts(PaymentLogStatus.SUCCESS);
		mozPymntLog.setLogType(PymntMethod.INATRO_PORTAL.getCode());
		mozPymntLog.setPayer(finePymntInfo.getPayerNm());
		mozPymntLog.setLogDetail("INATRO portal payment");
		mozPymntLog.setReqPrice(finePymntInfo.getPymntPrice());
		pymntLogRepository.saveMozPymntLog(mozPymntLog);
	}
}
