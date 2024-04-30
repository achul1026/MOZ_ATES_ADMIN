package com.moz.ates.traffic.admin.main;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.common.enums.OprtrSttsCd;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntMaster;
import com.moz.ates.traffic.common.entity.common.ChartDTO;
import com.moz.ates.traffic.common.entity.common.ChartDTO.AccidentChartGraph;
import com.moz.ates.traffic.common.entity.common.ChartDTO.AccidentCircularGraph;
import com.moz.ates.traffic.common.entity.common.ChartDTO.EnforcementChartGraph;
import com.moz.ates.traffic.common.entity.common.ChartDTO.EnforcementCircularGraph;
import com.moz.ates.traffic.common.entity.common.ChartDTO.PaymentChartGraph;
import com.moz.ates.traffic.common.entity.common.ChartDTO.PaymentCircularGraph;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.common.MozMsgQueue;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfMaster;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;
import com.moz.ates.traffic.common.enums.MsgQueueStatus;
import com.moz.ates.traffic.common.repository.accident.MozTfcAcdntMasterRepository;
import com.moz.ates.traffic.common.repository.common.MozCmCdRepository;
import com.moz.ates.traffic.common.repository.common.MozMsgQueueRepository;
import com.moz.ates.traffic.common.repository.enforcement.MozTfcEnfMasterRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfEqpMasterRepository;
import com.moz.ates.traffic.common.repository.operator.MozWebOprtrRepository;
import com.moz.ates.traffic.common.repository.payment.MozFinePymntInfoRepository;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.support.exception.ErrorCode;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;
import com.moz.ates.traffic.common.util.SmsSendContentUtils;


@Service
public class MainServiceImpl implements MainService {

    @Autowired
    MozWebOprtrRepository webOprtrRepository;
    
    @Autowired
    MozCmCdRepository mozCmCdRepository;
    
    @Autowired
    MozMsgQueueRepository msgQueueRepository;    
    
    @Autowired
    MozFinePymntInfoRepository mozFinePymntInfoRepository;    
    
    @Autowired
    MozTfcEnfMasterRepository mozTfcEnfMasterRepository;
    
    @Autowired
    MozTfcAcdntMasterRepository mozTfcAcdntMasterRepository;
    
    @Autowired
    MozTfcEnfEqpMasterRepository mozTfcEnfEqpMasterRepository;
    
    
    @Value("${mail.sender.inatro}")
    String sender;
    
    @Override
    public MozWebOprtr getUserById(MozWebOprtr webOprtr) {
    	return webOprtrRepository.findOneUserById(webOprtr);

    }
    
	/**
	 * @brief 어드민 관리자 회원가입
	 * @author KY.LEE
	 * @Date 2023.08.11
	 * @param webOprtr
	 */
	@Override
	public void registWebOprtr(MozWebOprtr webOprtr) {
		BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
		String encodePw = encode.encode(webOprtr.getOprtrAccountPw());
		
		if(this.dupChkAccountId(webOprtr)) {
			webOprtr.setOprtrId(MozatesCommonUtils.getUuid());
			webOprtr.setOprtrAccountPw(encodePw);
			webOprtr.setOprtrStts(OprtrSttsCd.WAITTING.getCode());
			webOprtrRepository.insertUser(webOprtr);
		} else {
			throw new CommonException(ErrorCode.DUPLICATE_ACCOUNTS);
		}
	}

	
	/**
	 * @brief 어드민 관리자아이디 중복체크
	 * @author KY.LEE
	 * @Date 2023.08.11
	 * @param webOprtr
	 */
	@Override
	public boolean dupChkAccountId(MozWebOprtr webOprtr) {
		boolean result = false;
		int isUserChk = webOprtrRepository.countDupliChk(webOprtr);
		if(0 == isUserChk) {
			result = true;
		}
		return result;
	}

	/**
	  * @Method Name : findId
	  * @Date : 2024. 2. 15.
	  * @Author : IK.MOON
	  * @Method Brief : 계정 ID 찾기
	  * @param webOprtr
	  * @return
	  */
	@Override
	public MozWebOprtr findId(MozWebOprtr webOprtr) {
		MozWebOprtr webOprtrFound = webOprtrRepository.findOneWebOprtrByOprtrNmAndOprtrPno(webOprtr);
		
		if(MozatesCommonUtils.isNull(webOprtrFound)) {
			throw new CommonException(ErrorCode.ENTITY_DATA_NULL);
		}
		
		return webOprtrFound;
	}
	
	/**
	  * @Method Name : findPw
	  * @Date : 2024. 4. 11.
	  * @Author : IK.MOON
	  * @Method Brief : 비밀번호 찾기
	  * @param webOprtr
	  * @return
	  */
	@Override
	public void findPw(MozWebOprtr webOprtr) {
		// 일치 조회
		MozWebOprtr webOprtrDb = webOprtrRepository.findOneWebOprtrByOprtrAccountIdAndOprtrNm(webOprtr);
		
		if (MozatesCommonUtils.isNull(webOprtrDb)) {
			throw new CommonException(ErrorCode.ENTITY_DATA_NULL);
		}
		
		// 랜덤 비밀번호 생성
    String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    int MIN_LENGTH = 8;
    int MAX_LENGTH = 15;
    
    SecureRandom random = new SecureRandom();
    int length = MIN_LENGTH + random.nextInt(MAX_LENGTH - MIN_LENGTH + 1);
    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
        int randomIndex = random.nextInt(CHARACTERS.length());
        char randomChar = CHARACTERS.charAt(randomIndex);
        sb.append(randomChar);
    }
    String tmpPw =  sb.toString();
		
		// 임시 비밀번호 DB 저장
    BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
    String encodeTmpPw = encode.encode(tmpPw);
		
    webOprtrDb.setOprtrAccountPw(encodeTmpPw);
    webOprtrDb.setTmpPwIssuedYn("Y");
    
    webOprtrRepository.updateOprtrAccountPw(webOprtrDb);
    
		// sms 발송
    MozMsgQueue msgQueue = new MozMsgQueue();
    
    msgQueue.setMsgType("sms");
    msgQueue.setSender(sender);
    msgQueue.setReceiver(webOprtrDb.getOprtrPno());
    msgQueue.setContent(SmsSendContentUtils.findPassword(webOprtrDb.getOprtrNm(), tmpPw));
    msgQueue.setStatus(MsgQueueStatus.WAITING);
    msgQueue.setRetry(0);
    
    msgQueueRepository.saveMozMsgQueue(msgQueue);
	}
	
	/**
	  * @Method Name : getAffiliationCd
	  * @Date : 2024. 3. 26.
	  * @Author : IK.MOON
	  * @Method Brief : 소속 기관 목록
	  * @param webOprtr
	  * @return List<MozCmCd>
	  */
	@Override
	public List<MozCmCd> getAffiliationCd(MozCmCd mozCmCd) {
		return mozCmCdRepository.findAllCdListByPagination(mozCmCd);
	}

	/**
	 * @Method Name : getTotalCountAffiliationCd
	 * @Date : 2024. 3. 26.
	 * @Author : IK.MOON
	 * @Method Brief : 소속 기관 카운트조회
	 * @param cdGroupId
	 * @return Long
	 */
	@Override
	public int getTotalCountAffiliationCd(MozCmCd mozCmCd) {
		return mozCmCdRepository.countByCdGroupId(mozCmCd);
	}
	
	/**
	 * @Method Name : getPaymentStatisticsInfoForDashboard
	 * @Date : 2024. 4. 25.
	 * @Author : NK.KIM
	 * @Method Brief : 대시보드 체납/미납 통계 정보
	 * @return chartDTO
	 */
	@Override
	public ChartDTO getStatisticsInfoForDashboard() {
		ChartDTO chartDTO = new ChartDTO();
		EnforcementChartGraph enforcementChartGraph = mozTfcEnfMasterRepository.findOneEnforcementStatisticsChartGraph();
		chartDTO.setEnforcementChartGraph(enforcementChartGraph);
		
		EnforcementCircularGraph enforcementCircularGraph = mozTfcEnfMasterRepository.findOneEnforcementStatisticsCircularGraph();
		chartDTO.setEnforcementCircularGraph(enforcementCircularGraph);
		
		AccidentCircularGraph accidentCircularGraph = mozTfcAcdntMasterRepository.findOneAccidentStatisticsCircularGraph();
		chartDTO.setAccidentCircularGraph(accidentCircularGraph);
		
		AccidentChartGraph accidentChartGraph = mozTfcAcdntMasterRepository.findOneAccidentStatisticsChartGraph();
		chartDTO.setAccidentChartGraph(accidentChartGraph);
		
		PaymentCircularGraph paymentCircularGraph = mozFinePymntInfoRepository.findOnePaymentStatisticsCircularGraph();
		chartDTO.setPaymentCircularGraph(paymentCircularGraph);
		
		PaymentChartGraph chartGraph = mozFinePymntInfoRepository.findOnePaymentStatisticsChartGraph();
		chartDTO.setPaymentChartGraph(chartGraph);
		return chartDTO;
	}

	/**
	 * @Method Name : getTodayEnforcementInfo
	 * @Date : 2024. 4. 25.
	 * @Author : KY.LEE
	 * @Method Brief : 대시보드 금일 단속 목록 조회 
	 * @return List<MozTfcEnfMaster>
	 */
	@Override
	public List<MozTfcEnfMaster> getTodayEnforcementInfo() {
		return mozTfcEnfMasterRepository.findMozTfcEnfMasterByToday();
	}

	/**
	 * @Method Name : getTodayAccidentInfo
	 * @Date : 2024. 4. 25.
	 * @Author : KY.LEE
	 * @Method Brief : 대시보드 금일 사고 목록
	 * @return List<MozTfcEnfMaster>
	 */
	@Override
	public List<MozTfcAcdntMaster> getTodayAccidentInfo() {
		return mozTfcAcdntMasterRepository.findMozTfcAcdntMatserByToday();
	}

	/**
	 * @Method Name : getEqpInfo
	 * @Date : 2024. 4. 25.
	 * @Author : KY.LEE
	 * @Method Brief : 단속장비 정보
	 * @return Map<String,Object>
	 */
	@Override
	public Map<String, Object> getEqpInfo() {
		return mozTfcEnfEqpMasterRepository.findOneEqpUseCnt();
	}

	/**
	 * @Method Name : getTodayAccidentCount
	 * @Date : 2024. 4. 25.
	 * @Author : KY.LEE
	 * @Method Brief : 대시보드 금일 사고 목록 카운트
	 */
	@Override
	public int getTodayEnforcementCount() {
		return mozTfcEnfMasterRepository.countMozTfcEnfMasterByToday();
	}

	
	/**
	 * @Method Name : getTodayAccidentCount
	 * @Date : 2024. 4. 25.
	 * @Author : KY.LEE
	 * @Method Brief : 대시보드 금일 사고 목록 카운트
	 */
	@Override
	public int getTodayAccidentCount() {
		return mozTfcAcdntMasterRepository.countMozTfcAcdntMatserByToday();
	}
	
}
