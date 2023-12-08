package com.moz.ates.traffic.admin.govportal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.common.entity.board.MozBrd;
import com.moz.ates.traffic.common.entity.board.MozComplaintsReg;
import com.moz.ates.traffic.common.entity.board.MozFaq;
import com.moz.ates.traffic.common.entity.board.MozObjReg;
import com.moz.ates.traffic.common.entity.payment.MozPlPymntInfo;
import com.moz.ates.traffic.common.repository.board.MozBrdRepository;
import com.moz.ates.traffic.common.repository.board.MozComplaintsRegRepository;
import com.moz.ates.traffic.common.repository.board.MozFaqRepository;
import com.moz.ates.traffic.common.repository.board.MozObjRegRepository;
import com.moz.ates.traffic.common.repository.payment.MozPlPymntInfoRepository;

/**
 * className : PortalServiceImpl
 * author : Mike Lim
 * description : 포털 관련 impl
 */
@Service
@Component
public class PortalServiceImpl implements PortalService {
    
    @Autowired
    private MozBrdRepository brdRepository;
    
    @Autowired
    private MozFaqRepository faqRepository;
    
    @Autowired
    private MozObjRegRepository objRegRepository;
    
    @Autowired
    private MozComplaintsRegRepository complaintsRegRepository;
    
    @Autowired
    private MozPlPymntInfoRepository plPymntInfoRepository;
    
    @Autowired
    private JavaMailSender sender;
    
    @Value("${spring.mail.username}")
    private String Ansname;
    
    /**
     * @brief : 공지사항 등록
     * @details : 공지사항 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    @Override
    public void registNotice(MozBrd brd) {
    	//TODO
    	brd.setOprtrId("KIM");
    	brdRepository.saveMozBrd(brd);
    }

    /**
     * @brief : 공지사항 리스트 조회
     * @details : 공지사항 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    @Override
    public List<MozBrd> getNoticeList(MozBrd brd) {
    	return brdRepository.findAllMozBrd(brd);
    }
    
    /**
     * @brief : 공지사항 리스트 개수 조회
     * @details : 공지사항 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    @Override
    public int getNoticeListCnt(MozBrd brd) {
    	return brdRepository.countMozBrd(brd);
    }
    
    /**
     * @brief : 공지사항 상세 화면
     * @details : 공지사항 상세 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : boardIdx
     * @return : 
     */
    @Override
    public MozBrd getNoticeDetail(String boardIdx) {
    	return brdRepository.selectNoticeDetail(boardIdx);
    }
    
    /**
     * @brief : 공지사항 수정 
     * @details : 공지사항 수정 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    @Override
    public void updateNotice(MozBrd brd) {
    	brdRepository.updateNotice(brd);
    }
    
//    /**
//     * @brief : 공지사항 리스트 조회
//     * @details : 공지사항 리스트 조회
//     * @author : KC.KIM
//     * @date : 2023.08.04
//     * @param : brd
//     * @return : DataTableVO
//     */
//    @Override
//    public DataTableVO getNoticeListDatatable(MozBrd brd) {
//        return new DataTableVO(this.getNoticeList(brd), this.getNoticeListCnt(brd));
//    }
    
    /**
     * @brief : 공지사항 삭제
     * @details : 공지사항 삭제 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : boardIdx
     * @return : 
     */
    @Override
    public void deleteNotice(String boardIdx) {
    	brdRepository.deleteNotice(boardIdx);
    }
    
    /**
     * @brief : FAQ 리스트 조회
     * @details : FAQ 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    @Override
    public List<MozFaq> getFaqList(MozFaq faq) {
    	return faqRepository.findAllMozFaq(faq);
    }
    
    /**
     * @brief : FAQ 리스트 개수 조회
     * @details : FAQ 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    @Override
    public int getFaqListCnt(MozFaq faq) {
    	return faqRepository.countMozFaq(faq);
    }
    
    /**
     * @brief : FAQ 등록 
     * @details : FAQ 등록 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    @Override
    public void registFaq(MozFaq faq) {
    	// TODO
    	faq.setWrtrId("lim");
    	faqRepository.saveMozFaq(faq);
    }
    
    /**
     * @brief : FAQ 상세 조회
     * @details : FAQ 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faqIdx
     * @return : 
     */
    @Override
    public MozFaq getFaqDetail(String faqIdx) {
    	return faqRepository.findOneMozFaq(faqIdx);
    }
    
    /**
     * @brief : FAQ 수정
     * @details : FAQ 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    @Override
    public void updateFaq(MozFaq faq) {
    	faqRepository.updateMozFaq(faq);
    }
    
    /**
     * @brief : FAQ 삭제
     * @details : FAQ 삭제
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faqIdx
     * @return : 
     */
    @Override
    public void deleteFaq(String faqIdx) {
    	faqRepository.deleteMozFaq(faqIdx);
    }

    /**
     * @brief : 이의제기 리스트 조회
     * @details : 이의제기 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : objIdx
     * @return : 
     */
    @Override
    public List<MozObjReg> getObjectionList(MozObjReg objReg) {
    	return objRegRepository.findAllMozObjReg(objReg);
    }

    /**
     * @brief : 이의제기 리스트 개수 조회
     * @details : 이의제기 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : objIdx
     * @return : 
     */
    @Override
    public int getObjectionListCnt(MozObjReg objReg) {
    	return objRegRepository.countMozObjReg(objReg);
    }
    
    /**
     * @brief : 이의제기 상세 조회
     * @details : 이의제기 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : objIdx
     * @return : 
     */
    @Override
    public MozObjReg getObjectionDetail(String objIdx) {
    	return objRegRepository.findOneMozObjReg(objIdx);
    }

    /**
     * @brief : 불만사항 리스트 조회
     * @details : 불만사항 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : complaintsReg
     * @return : 
     */
    @Override
    public List<MozComplaintsReg> getComplaintList(MozComplaintsReg complaintsReg) {
    	return complaintsRegRepository.findAllMozComplaintReg(complaintsReg);
    }

    /**
     * @brief : 불만사항 리스트 개수 조회
     * @details : 불만사항 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : complaintsReg
     * @return : 
     */
    @Override
    public int getComplaintListCnt(MozComplaintsReg complaintsReg) {
    	return complaintsRegRepository.countMozComplaintReg(complaintsReg);
    }
    
    /**
     * @brief : 불만사항 상세 조회
     * @details : 불만사항 상세
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : complaintsIdx
     * @return : 
     */
    @Override
    public MozComplaintsReg getComplaintDetail(String complaintsIdx) {
    	return complaintsRegRepository.findOneComplaintDetail(complaintsIdx);
    }

    /**
     * @brief : 범칙금 납부처 등록 
     * @details : 범칙금 납부처 등록
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : 
     * @return : 
     */
    @Override
    public void registPenaltyPlace(MozPlPymntInfo plPymntInfo) {
    	//TODO
    	plPymntInfo.setCrtr("lim");
    	plPymntInfoRepository.saveMozPlPymntInfo(plPymntInfo);
    }

    /**
     * @brief : 범칙금 납부처 리스트 조회 
     * @details : 범칙금 납부처 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : plPymntInfo
     * @return : 
     */
    @Override
    public List<MozPlPymntInfo> getPenaltyPlaceList(MozPlPymntInfo plPymntInfo) {
    	return plPymntInfoRepository.findAllPenaltyPlaceList(plPymntInfo);
    }

    /**
     * @brief : 범칙금 납부처 리스트 개수 조회 
     * @details : 범칙금 납부처 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : plPymntInfo
     * @return : 
     */
    @Override
    public int getPenaltyPlaceListCnt(MozPlPymntInfo plPymntInfo) {
    	return plPymntInfoRepository.countPenaltyPlaceList(plPymntInfo);
    }

    /**
     * @brief : 범칙금 납부처 상세 
     * @details : 범칙금 납부처 상세
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : placePymntId
     * @return : 
     */
    @Override
    public MozPlPymntInfo getPenaltyPlaceDetail(String placePymntId) {
    	return plPymntInfoRepository.findOnePenaltyPlaceDetail(placePymntId);
    }

    /**
     * @brief : 범칙금 납부처 수정
     * @details : 범칙금 납부처 수정
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : placePymntId
     * @return : 
     */
    @Override
    public void updatePenaltyPlace(MozPlPymntInfo plPymntInfo) {
    	plPymntInfoRepository.updatePenaltyPlace(plPymntInfo);
    }
    
    /**
     * @brief : 범칙금 납부처 삭제
     * @details : 범칙금 납부처 삭제 
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : placePymntId
     * @return : 
     */
    @Override
    public void deletePenaltyPlace(String placePymntId) {
    	plPymntInfoRepository.deletePenaltyPlace(placePymntId);
    }
    
	@Override
	public void updateObjAns(MozObjReg objReg) {
		objRegRepository.updateObjAns(objReg);
	}
	
	@Override
	public void updateCmpAns(MozComplaintsReg complaintsReg) {
		complaintsRegRepository.updateCmpAns(complaintsReg);
	}

	//email
	@Override
	public Map<String, Object> objSendEmail(String objIdx,String userId,String content) {
		Map<String,Object> objResult = new HashMap<String,Object>();
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		String title = "Responses to objections";

		try {
			helper.setTo(userId);
			helper.setSubject(title);
			helper.setText(content);
			objResult.put("resultCode", 200);
			MozObjReg objReg = new MozObjReg();
			objReg.setObjIdx(objIdx);
			objReg.setWrtrEmail(userId);
			objReg.setAnsEmail(Ansname); //application.properties아이디값
			objReg.setAnsContents(content);
			this.updateObjAns(objReg);
		} catch(MessagingException e) {
			e.printStackTrace();
			objResult.put("resultCode", 500);
		}
		sender.send(message);
		return objResult;
	}
	
	//email
	@Override
	public Map<String, Object> cmpSendEmail(String complaintsIdx, String userId, String content) {
		Map<String,Object> cmpResult = new HashMap<String,Object>();
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		String cmtitle = "Answers to complaints";
		try {
			helper.setTo(userId);
			helper.setSubject(cmtitle);
			helper.setText(content);
			cmpResult.put("resultCode", 200);
			MozComplaintsReg cmpReg = new MozComplaintsReg();
			cmpReg.setComplaintsIdx(complaintsIdx);
			cmpReg.setWrtrEmail(userId);
			cmpReg.setAnsEmail(Ansname); //application.properties아이디값
			cmpReg.setAnsContents(content);
			this.updateCmpAns(cmpReg);
		} catch(MessagingException e) {
			e.printStackTrace();
			cmpResult.put("resultCode", 500);
		}
		sender.send(message);
		return cmpResult;
	}
}
