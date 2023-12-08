package com.moz.ates.traffic.admin.govportal;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moz.ates.traffic.admin.common.CommonCdService;
import com.moz.ates.traffic.common.component.validate.ValidateBuilder;
import com.moz.ates.traffic.common.component.validate.ValidateChecker;
import com.moz.ates.traffic.common.component.validate.ValidateResult;
import com.moz.ates.traffic.common.entity.board.MozBrd;
import com.moz.ates.traffic.common.entity.board.MozComplaintsReg;
import com.moz.ates.traffic.common.entity.board.MozFaq;
import com.moz.ates.traffic.common.entity.board.MozObjReg;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.payment.MozPlPymntInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "portal")
public class PortalController {

    @Autowired
    private CommonCdService commonCdService;

    @Autowired
    private PortalService portalService;


    /**
     * @brief : 공지사항 리스트 화면
     * @details : 공지사항 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    @GetMapping("/notice/list.do")
    public String noticeList(Model model, @ModelAttribute MozBrd brd){
    	List<MozCmCd> cdList = commonCdService.getCdList("ntc");
    	model.addAttribute("cdList", cdList);
        model.addAttribute("brd", brd);
        
        model.addAttribute("noticeList", portalService.getNoticeList(brd));
        model.addAttribute("totalCnt", portalService.getNoticeListCnt(brd));
        
        return "views/govpotal/noticeList";
    }

    /**
     * @brief : 공지사항 등록 화면
     * @details : 공지사항 등록 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : 
     * @return : 
     */
    @GetMapping("/notice/save.do")
    public String noticeRegist(Model model){

        List<MozCmCd> cdList = commonCdService.getCdList("ntc");
        model.addAttribute("cdList", cdList);

        return "views/govpotal/noticeRegist";
    }

    /**
     * @brief : 공지사항 등록
     * @details : 공지사항 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    @PostMapping("/notice/save.ajax")
    public @ResponseBody CommonResponse<?> noticeRegistAjax(MozBrd brd){
        
        ValidateBuilder dtoValidator = new ValidateBuilder(brd);
        
        ValidateResult dtoValidatorResult = dtoValidator
        		.addRule("boardTitle", new ValidateChecker().setRequired().setMaxLength(200, "Title cannot be more than 200 characters"))
				.addRule("boardContents", new ValidateChecker().setRequired().setMaxLength(200, "Contents cannot be more than 200 characters"))
				.isValid();
        
        if(!dtoValidatorResult.isSuccess()) {
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
        }

        try {
            portalService.registNotice(brd);
        }catch (Exception e){
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST , e.getMessage());
        }

        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK , "The Notice has been registered.");
    }
    	
    /**
     * @brief : 공지사항 삭제
     * @details : 공지사항 삭제 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : boardIdx
     * @return : 
     */
    @PostMapping("/notice/delete.ajax")
    @ResponseBody
    public CommonResponse<?> noticeDeleteAjax(@RequestParam("boardIdx")String boardIdx){
    	
    	MozBrd mozFaq = new MozBrd();
    	mozFaq.setBoardIdx(boardIdx);
    	
    	ValidateBuilder dtoValidator = new ValidateBuilder(mozFaq);
    	
    	ValidateResult dtoValidatorResult = dtoValidator
    			.addRule("boardIdx", new ValidateChecker().setRequired())
    			.isValid();
    	
        if(!dtoValidatorResult.isSuccess()) {
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
        }

        try {
            portalService.deleteNotice(boardIdx);
        }catch (Exception e){
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST , e.getMessage());
        }

        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK , "The Notice has been deleted.");
    }

    /**
     * @brief : 공지사항 상세 화면
     * @details : 공지사항 상세 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : boardIdx
     * @return : 
     */
    @RequestMapping(value = "/notice/detail.do")
    public String noticeDetail(Model model, @RequestParam("boardIdx")String boardIdx){

        MozBrd brd = portalService.getNoticeDetail(boardIdx);
        model.addAttribute("brd", brd);

        return "views/govpotal/noticeDetail";
    }

    /**
     * @brief : 공지사항 수정 화면
     * @details : 공지사항 수정 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : boardIdx
     * @return : 
     */
    @GetMapping("/notice/update.do")
    public String noticeModify(Model model, @RequestParam("boardIdx")String boardIdx){

        List<MozCmCd> cdList = commonCdService.getCdList("ntc");
        model.addAttribute("cdList", cdList);

        MozBrd brd = portalService.getNoticeDetail(boardIdx);
        model.addAttribute("brd", brd);

        return "views/govpotal/noticeModify";
    }

    /**
     * @brief : 공지사항 수정 
     * @details : 공지사항 수정 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    @PostMapping("/notice/update.ajax")
    @ResponseBody
    public CommonResponse<?> noticeModifyAjax(@ModelAttribute MozBrd brd){
    	
        ValidateBuilder dtoValidator = new ValidateBuilder(brd);
        
        ValidateResult dtoValidatorResult = dtoValidator
        		.addRule("boardTitle", new ValidateChecker().setRequired().setMaxLength(200, "Title cannot be more than 200 characters."))
				.addRule("boardContents", new ValidateChecker().setRequired().setMaxLength(200, "Contents cannot be more than 200 characters."))
				.isValid();
        
        if(!dtoValidatorResult.isSuccess()) {
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
        }
        
        try {
            portalService.updateNotice(brd);
        }catch (Exception e){
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The Notice has been modified.");
    }


    /**
     * @brief : FAQ 리스트 화면
     * @details : FAQ 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    @GetMapping("/faq/list.do")
    public String faqList(Model model, @ModelAttribute MozFaq faq){

        model.addAttribute("faq", faq);
        model.addAttribute("faqList", portalService.getFaqList(faq));
        model.addAttribute("totalCnt", portalService.getFaqListCnt(faq));
        return "views/govpotal/faqList";
    }

    /**
     * @brief : FAQ 등록 화면
     * @details : FAQ 등록 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : 
     * @return : 
     */
    @GetMapping("/faq/save.do")
    public String faqRegist(Model model){

        List<MozCmCd> cdList = commonCdService.getCdList("faq");
        model.addAttribute("cdList", cdList);

        return "views/govpotal/faqRegist";
    }
    
    /**
     * @brief : FAQ 등록 
     * @details : FAQ 등록 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    @PostMapping("/faq/save.ajax")
    @ResponseBody
    public CommonResponse<?> faqRegistAjax(MozFaq faq){
        
    	// TODO faqSeq 확인 필요!!
        ValidateBuilder dtoValidator = new ValidateBuilder(faq);
        
        ValidateResult dtoValidatorResult = dtoValidator
        		.addRule("postTitle", new ValidateChecker().setRequired().setMaxLength(200, "Title cannot be more than 200 characters."))
				.addRule("faqSeq", new ValidateChecker().setRequired())
				.addRule("postContent", new ValidateChecker().setRequired().setMaxLength(200, "Content cannot be more than 200 characters."))
				.isValid();
        
        if(!dtoValidatorResult.isSuccess()) {
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
        }
        
        try {
            portalService.registFaq(faq);
        }catch (Exception e){
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The FAQ has been registered.");
    }
    
    /**
     * @brief : FAQ 상세 조회
     * @details : FAQ 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faqIdx
     * @return : 
     */
    @GetMapping("/faq/detail.do")
    public String faqDetail(Model model, @RequestParam("faqIdx")String faqIdx){

        List<MozCmCd> cdList = commonCdService.getCdList("faq");
        model.addAttribute("cdList", cdList);

        MozFaq faq = portalService.getFaqDetail(faqIdx);
        model.addAttribute("faq", faq);

        return "views/govpotal/faqDetail";
    }

    /**
     * @brief : FAQ 수정 화면
     * @details : FAQ 수정 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faqIdx
     * @return : 
     */
    @GetMapping("/faq/update.do")
    public String faqModify(Model model, @RequestParam("faqIdx")String faqIdx){

        List<MozCmCd> cdList = commonCdService.getCdList("faq");
        model.addAttribute("cdList", cdList);

        MozFaq faq = portalService.getFaqDetail(faqIdx);
        model.addAttribute("faq", faq);

        return "views/govpotal/faqModify";
    }

    /**
     * @brief : FAQ 수정
     * @details : FAQ 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    @PostMapping("/faq/update.ajax")
    @ResponseBody
    public CommonResponse<?> faqModifyAjax(@ModelAttribute MozFaq faq){
    	
        ValidateBuilder dtoValidator = new ValidateBuilder(faq);
        
        // TODO faqSeq 확인 필요!!
        ValidateResult dtoValidatorResult = dtoValidator
        		.addRule("postTitle", new ValidateChecker().setRequired().setMaxLength(200, "Title cannot be more than 200 characters."))
				.addRule("faqSeq", new ValidateChecker().setRequired())
				.addRule("postContent", new ValidateChecker().setRequired().setMaxLength(200, "Content cannot be more than 200 characters."))
				.isValid();
        
        if(!dtoValidatorResult.isSuccess()) {
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
        }
        
        try {
            portalService.updateFaq(faq);
        }catch (Exception e){
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The FAQ has been modified.");
    }

    /**
     * @brief : FAQ 삭제
     * @details : FAQ 삭제
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faqIdx
     * @return : 
     */
    @PostMapping("/faq/delete.ajax")
    @ResponseBody
    public CommonResponse<?> faqDeleteAjax(@RequestParam("faqIdx")String faqIdx){
    	
    	MozFaq mozFaq = new MozFaq();
    	mozFaq.setFaqIdx(faqIdx);
    	
    	ValidateBuilder dtoValidator = new ValidateBuilder(mozFaq);
    	
    	ValidateResult dtoValidatorResult = dtoValidator
    			.addRule("faqIdx", new ValidateChecker().setRequired())
    			.isValid();
    	
        if(!dtoValidatorResult.isSuccess()) {
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
        }
    	
        try {
            portalService.deleteFaq(faqIdx);
        }catch (Exception e){
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The FAQ has been deleted");
    }

    /**
     * @brief : 이의제기 리스트 화면
     * @details : 이의제기 리스트 화면  
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : objReg
     * @return : 
     */
    @GetMapping("/objection/list.do")
    public String objectionList(Model model, @ModelAttribute MozObjReg objReg){

        model.addAttribute("objReg", objReg);
        model.addAttribute("objectionList", portalService.getObjectionList(objReg));
        model.addAttribute("totalCnt", portalService.getObjectionListCnt(objReg));
        return "views/govpotal/objectionList";
    }

    /**
     * @brief : 이의제기 상세 조회
     * @details : 이의제기 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : objIdx
     * @return : 
     */
    @GetMapping("/objection/detail.do")
    public String objectionDetail(Model model, @RequestParam("objIdx")String objIdx){
        MozObjReg objReg = portalService.getObjectionDetail(objIdx);
        model.addAttribute("objReg", objReg);

        return "views/govpotal/objectionDetail";
    }
    
    /**
     * @brief : 이의제기 답변 이메일 발송
     * @details : 이의제기 답변 이메일 발송
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : objparams
     * @return : 
     */
    @PostMapping("/object/sendemail.ajax")
    @ResponseBody
    public Map<String, Object> objSendEmail(@RequestBody Map<String,Object> objparams){
    	// TODO : Validation Check
    	log.info("email objparams={}", objparams);
    	return portalService.objSendEmail((String) objparams.get("objIdx")
    			, (String) objparams.get("userId")
    			, (String) objparams.get("content")
    		);
    }

//    @RequestMapping(value = "sendObjAnswer")
//    public Map<String, Object> sendObjAnswer(ObjectionVO objectionVO){
//        Map<String, Object> result = new HashMap<>();
//
//        Map<String, Object> emailValues = new HashMap<>();
//
//
//    }

    /**
     * @brief : 불만사항 리스트 화면
     * @details : 불만사항 리스트 화면  
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : complaintsReg
     * @return : 
     */
    @GetMapping("/complaint/list.do")
    public String complaintList(Model model, @ModelAttribute MozComplaintsReg complaintsReg){

        model.addAttribute("complaintsReg", complaintsReg);
        model.addAttribute("complaintList", portalService.getComplaintList(complaintsReg));
        model.addAttribute("totalCnt", portalService.getComplaintListCnt(complaintsReg));
        return "views/govpotal/complaintList";
    }

    /**
     * @brief : 불만사항 상세 조회
     * @details : 불만사항 상세
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : complaintsIdx
     * @return : 
     */
    @GetMapping("/complaint/detail.do")
    public String complaintDetail(Model model, @RequestParam("complaintsIdx")String complaintsIdx){

        MozComplaintsReg complaintsReg = portalService.getComplaintDetail(complaintsIdx);
        model.addAttribute("complaintsReg", complaintsReg);

        return "views/govpotal/complaintDetail";
    }
    
    /**
     * @brief : 불만사항 답변 이메일 발송
     * @details : 불만사항 답변 이메일 발송
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : cmpparams
     * @return : 
     */
    @PostMapping(value = "/complaint/sendemail.ajax")
    @ResponseBody
    public Map<String, Object> cmpSendEmail(@RequestBody Map<String,Object> cmpparams){
    	// TODO : Validation Check
    	log.info("email cmpparams={}", cmpparams);	
    	return portalService.cmpSendEmail((String) cmpparams.get("complaintsIdx")
    			, (String) cmpparams.get("userId")
    			, (String) cmpparams.get("content")
    		);
    }
    
    /**
     * @brief : 범칙금 납부처 리스트 화면
     * @details : 범칙금 납부처 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : plPymntInfo
     * @return : 
     */
    @GetMapping("/penaltyplace/list.do")
    public String penaltyPlaceList(Model model,@ModelAttribute MozPlPymntInfo plPymntInfo){
        model.addAttribute("plPymntInfo", plPymntInfo);
        model.addAttribute("plPymntList", portalService.getPenaltyPlaceList(plPymntInfo));
        model.addAttribute("totalCnt", portalService.getPenaltyPlaceListCnt(plPymntInfo));
        return "views/govpotal/penaltyPlaceList";
    }

    /**
     * @brief : 범칙금 납부처 등록 화면
     * @details : 범칙금 납부처 등록 화면
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : 
     * @return : 
     */
    @GetMapping("/penaltyplace/save.do")
    public String penaltyPlaceRegist(Model model){

        return "views/govpotal/penaltyPlaceRegist";
    }

    /**
     * @brief : 범칙금 납부처 등록 
     * @details : 범칙금 납부처 등록
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : 
     * @return : 
     */
    @PostMapping("/penaltyplace/save.ajax")
    @ResponseBody
    public CommonResponse<?> penaltyPlaceRegistAjax(MozPlPymntInfo plPymntInfo){
    	
    	ValidateBuilder dtoValidator = new ValidateBuilder(plPymntInfo);
    
    	ValidateResult dtoValidatorResult = dtoValidator
    			.addRule("placePymntNm", new ValidateChecker().setRequired().setMaxLength(200, "Name cannot be more than 200 characters."))
				.addRule("placePymntAddr", new ValidateChecker().setRequired().setMaxLength(200, "Address cannot be more than 200 characters."))
				.addRule("placePymntRprsvNm", new ValidateChecker().setRequired().setMaxLength(200, "Representative Name cannot be more than 200 characters."))
				.isValid();
   	
       if(!dtoValidatorResult.isSuccess()) {
       	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
       }

        try {
            portalService.registPenaltyPlace(plPymntInfo);
        }catch (Exception e){
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The New Payment Location has been saved");
    }

    /**
     * @brief : 범칙금 납부처 상세 
     * @details : 범칙금 납부처 상세
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : placePymntId
     * @return : 
     */
    @GetMapping("/penaltyplace/detail.do")
    public String penaltyPlaceDetail(Model model,@RequestParam("placePymntId")String placePymntId){

        MozPlPymntInfo plPymntInfo = portalService.getPenaltyPlaceDetail(placePymntId);
        model.addAttribute("plPymntInfo", plPymntInfo);

        return "views/govpotal/penaltyPlaceDetail";
    }

    /**
     * @brief : 범칙금 납부처 수정 화면
     * @details : 범칙금 납부처 수정 화면
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : placePymntId
     * @return : 
     */
    @GetMapping("/penaltyplace/update.do")
    public String penaltyPlaceModify(Model model,@RequestParam("placePymntId")String placePymntId){

    	MozPlPymntInfo plPymntInfo = portalService.getPenaltyPlaceDetail(placePymntId);
        model.addAttribute("plPymntInfo", plPymntInfo);

        return "views/govpotal/penaltyPlaceModify";
    }
    
    /**
     * @brief : 범칙금 납부처 수정
     * @details : 범칙금 납부처 수정
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : placePymntId
     * @return : 
     */
    @PostMapping("/penaltyplace/update.ajax")
    @ResponseBody
    public CommonResponse<?> penaltyPlaceModifyAjax(@ModelAttribute MozPlPymntInfo plPymntInfo){
    	
    	ValidateBuilder dtoValidator = new ValidateBuilder(plPymntInfo);
    	
    	ValidateResult validationResult = dtoValidator
    			.addRule("placePymntNm", new ValidateChecker().setRequired().setMaxLength(200, "Name cannot be more than 200 characters."))
				.addRule("placePymntAddr", new ValidateChecker().setRequired().setMaxLength(200, "Address cannot be more than 200 characters."))
				.addRule("placePymntRprsvNm", new ValidateChecker().setRequired().setMaxLength(200, "Representative Name cannot be more than 200 characters."))
				.isValid();
    	
    	if(!validationResult.isSuccess()) {
    		return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, validationResult.getMessage());
    	}
       
        try {
            portalService.updatePenaltyPlace(plPymntInfo);
        }catch (Exception e){
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The Payment Location has been modified.");
    }

    /**
     * @brief : 범칙금 납부처 삭제
     * @details : 범칙금 납부처 삭제 
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : placePymntId
     * @return : 
     */
    @PostMapping("/penaltyplace/delete.ajax")
    @ResponseBody
    public CommonResponse<?> penaltyPlaceDeleteAjax(@RequestParam("placePymntId")String placePymntId){
    	
    	MozPlPymntInfo plPymntInfo = new MozPlPymntInfo();
    	plPymntInfo.setPlacePymntId(placePymntId);
    	
    	ValidateBuilder dtoValidator = new ValidateBuilder(plPymntInfo);

    	ValidateResult dtoValidatorResult = dtoValidator
    			.addRule("placePymntId", new ValidateChecker().setRequired())
				.isValid();
   	
    	if(!dtoValidatorResult.isSuccess()) {
    		return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
    	}

        try {
            portalService.deletePenaltyPlace(placePymntId);
        }catch (Exception e){
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The Payment Location has been deleted");
    }
    
}
