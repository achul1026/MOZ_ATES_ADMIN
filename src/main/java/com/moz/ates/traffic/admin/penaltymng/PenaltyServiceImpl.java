package com.moz.ates.traffic.admin.penaltymng;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfMaster;
import com.moz.ates.traffic.common.entity.payment.MozFinePymntInfo;
import com.moz.ates.traffic.common.repository.enforcement.MozTfcEnfMasterRepository;
import com.moz.ates.traffic.common.repository.payment.MozFinePymntInfoRepository;

@Service
public class PenaltyServiceImpl implements PenaltyService {
    
    @Autowired
    MozFinePymntInfoRepository finePymntInfoRepository;
    
    @Autowired
    MozTfcEnfMasterRepository tfcEnfMasterRepository;

    @Autowired
    private JavaMailSender sender;
    
    @Value("${spring.mail.username}")
    private String Ansname;
    
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
    	tfcEnfMaster.setTfcEnfId(finePymntInfo.getTfcEnfId());
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
//    @Override
//    public List getPenaltyList(MozFinePymntInfo finePymntInfo) {
//    	return finePymntInfoRepository.selectPenaltyList(finePymntInfo);
//    }
//
//    @Override
//    public int getPenaltyListCnt(MozFinePymntInfo finePymntInfo) {
//    	return finePymntInfoRepository.selectPenaltyListCnt(finePymntInfo);
//    }
//    
//    /**
//     * @brief : 범칙금 리스트 조회
//     * @details : 범칙금 리스트 조회
//     * @author : KC.KIM
//     * @date : 2023.08.04
//     * @param : finePymntInfo
//     * @return : DataTableVO
//     */
//    @Override
//    public DataTableVO getPenaltyListDatatable(MozFinePymntInfo finePymntInfo) {
//        return new DataTableVO(this.getPenaltyList(finePymntInfo),this.getPenaltyListCnt(finePymntInfo));
//    }
    
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

	//email
	@Override
	public Map<String, Object> penaltySendEmail(String pymntId, String emailAddr, String content) {
		Map<String,Object> penaltyResult = new HashMap<String,Object>();
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		String penaltyTitle = "We will inform you of the fine";
		try {
			helper.setTo(emailAddr);
			helper.setSubject(penaltyTitle);
			helper.setText(content);
			penaltyResult.put("resultCode", 200);
			
		} catch(MessagingException e) {
			e.printStackTrace();
			penaltyResult.put("resultCode", 500);
		}
		sender.send(message);
		return penaltyResult;
	}

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
}

