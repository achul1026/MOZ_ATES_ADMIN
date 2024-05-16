package com.moz.ates.traffic.admin.penaltymng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moz.ates.traffic.admin.common.CommonCdService;
import com.moz.ates.traffic.admin.common.enums.MethodType;
import com.moz.ates.traffic.admin.config.Authority;
import com.moz.ates.traffic.common.component.Pagination;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.payment.MozFinePymntInfo;
import com.moz.ates.traffic.common.enums.NtcTypeCd;
import com.moz.ates.traffic.common.enums.PymntMethod;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "penalty")
public class PenaltyController {

    @Autowired
    private CommonCdService commonCdService;

    @Autowired
    private PenaltyService penaltyService;

    /**
     * @brief : 1차 고지 목록 화면
     * @details : 1차 고지 목록 화면
     * @author : KY.LEE
     * @date : 2024.04.01
     * @param : finePymntInfo
     * @return : html
     */
    @Authority(type = MethodType.READ)
    @GetMapping("/first/list.do")
    public String mngFirstList(Model model, @ModelAttribute MozFinePymntInfo finePymntInfo){
    	finePymntInfo.setOtherType(NtcTypeCd.FIRST_NOTICE.getCode());
    	
    	int page = finePymntInfo.getPage();
    	int totalCnt = penaltyService.getPenaltyListCnt(finePymntInfo);
    	Pagination pagination = new Pagination(totalCnt, page);
    	
    	finePymntInfo.setStart((page - 1) * pagination.getPageSize());
    	
        model.addAttribute("finePymntInfo",finePymntInfo);
        model.addAttribute("penaltyList", penaltyService.getPenaltyList(finePymntInfo));
        model.addAttribute("pagination", pagination);
        return "views/penaltymng/penaltyFirstList";
    }
    
    /**
     * @brief : 2차 고지 목록 화면
     * @details : 2차 고지 목록 화면
     * @author : KY.LEE
     * @date : 2024.04.01
     * @param : finePymntInfo
     * @return : html
     */
    @Authority(type = MethodType.READ)
    @GetMapping("/second/list.do")
    public String mngSecondList(Model model, @ModelAttribute MozFinePymntInfo finePymntInfo){
    	finePymntInfo.setOtherType(NtcTypeCd.SECOND_NOTICE.getCode());
    	
    	int page = finePymntInfo.getPage();
    	int totalCnt = penaltyService.getPenaltyListCnt(finePymntInfo);
    	Pagination pagination = new Pagination(totalCnt, page);
    	
    	finePymntInfo.setStart((page - 1) * pagination.getPageSize());
    	
    	model.addAttribute("finePymntInfo",finePymntInfo);
    	model.addAttribute("penaltyList", penaltyService.getPenaltyList(finePymntInfo));
    	model.addAttribute("pagination", pagination);
    	return "views/penaltymng/penaltySecondList";
    }

    /**
     * @brief : 범칙금 등록 화면
     * @details : 범칙금 등록 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : 
     * @return : 
     */
    @Authority(type = MethodType.READ)
    @GetMapping("/mng/save.do")
    public String mngRegist(Model model){
        List<MozCmCd> cdList = commonCdService.getCdList("pay");
        model.addAttribute("cdList", cdList);

        return "views/penaltymng/penaltyMngRegist";
    }

    /**
     * @brief : 범칙금 등록
     * @details : 범칙금 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : finePymntInfo
     * @return : 
     */
    @Authority(type = MethodType.CREATE)
    @PostMapping("/mng/save.ajax")
    @ResponseBody
    public Map<String,Object> mngRegistAjax(MozFinePymntInfo finePymntInfo){
        Map<String, Object> result = new HashMap<>();
        
		penaltyService.registPenalty(finePymntInfo);
        
		penaltyService.updatePenaltyMaster(finePymntInfo);
		
		try {
            result.put("code", "1");
        }catch (Exception e){
            result.put("code", "0");
        }

        return result;
    }

    /**
     * @brief : 1차고지 현장 결제
     * @details : 1차고지 현장 결제
     * @author : KY.LEE
     * @date : 2024.03.29
     * @param : onsitePayment
     * @return : 
     */
    @Authority(type = MethodType.UPDATE)
    @PostMapping("/first/onsite/payment.ajax")
    @ResponseBody
    public CommonResponse<?> firstOnsitePayment(
    		MozFinePymntInfo mozFinePymntInfo
		){
    	try {    		
    		penaltyService.onsitePayment(mozFinePymntInfo);
    	}catch (CommonException e){
    		return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,"Erro, Pagamento no local.");
    	}
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"Sucesso do pagamento no local.");
    }
    
    /**
     * @brief : 현장 결제0.
     * @details : 현장 결제
     * @author : KY.LEE
     * @date : 2024.03.29
     * @param : onsitePayment
     * @return : 
     */
    @Authority(type = MethodType.UPDATE)
    @PostMapping("/second/onsite/payment.ajax")
    @ResponseBody
    public CommonResponse<?> secondOnsitePayment(
    		MozFinePymntInfo mozFinePymntInfo
    		){
    	try {    		
    		penaltyService.onsitePayment(mozFinePymntInfo);
    	}catch (CommonException e){
    		return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,"Erro, Pagamento no local.");
    	}
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"Sucesso do pagamento no local.");
    }
    
    /**
     * @brief : 결제 취소
     * @details : 결제 취소
     * @author : KY.LEE
     * @date : 2024.03.29
     * @param : onsitePaymentCancel
     * @return : 
     */
    @Authority(type = MethodType.UPDATE)
    @PostMapping("/first/cancel/onsite/payment.ajax")
    @ResponseBody
    public CommonResponse<?> firstOnsitePaymentCancel(
    		@RequestParam(name = "pymntId", required = true) String pymntId
    		){
    	try {
    		if(MozatesCommonUtils.isNull(pymntId)) {
    			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,"Obrigatório O parâmetro é nulo.");
    		}
    		penaltyService.cancelPayment(pymntId , PymntMethod.CASH);
    	}catch (CommonException e){
    		return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,"Cancelamento de pagamento falhou.");
    	}
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"Cancelar pagamento com êxito.");
    }

    /**
     * @brief : 결제 취소
     * @details : 결제 취소
     * @author : KY.LEE
     * @date : 2024.03.29
     * @param : onsitePaymentCancel
     * @return : 
     */
    @Authority(type = MethodType.UPDATE)
    @PostMapping("/second/cancel/onsite/payment.ajax")
    @ResponseBody
    public CommonResponse<?> secondOnsitePaymentCancel(
    		@RequestParam(name = "pymntId", required = true) String pymntId
    		){
    	try {
    		if(MozatesCommonUtils.isNull(pymntId)) {
    			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,"Obrigatório O parâmetro é nulo.");
    		}
    		penaltyService.cancelPayment(pymntId , PymntMethod.CASH);
    	}catch (CommonException e){
    		return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,"Cancelamento de pagamento falhou.");
    	}
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"Cancelar pagamento com êxito.");
    }
 
    /**
     * @brief : 범칙금 상세 조회
     * @details : 범칙금 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : pymntId
     * @return : 
     */
    @Authority(type = MethodType.READ)
    @GetMapping("/first/detail.do")
    public String mngFirstDetail(Model model, @RequestParam("pymntId")String pymntId){
    	MozFinePymntInfo finePymntInfo = penaltyService.getPenaltyDetail(pymntId);
        model.addAttribute("finePymntInfo",finePymntInfo);
        return "views/penaltymng/penaltyFirstDetail";
    }
    
    /**
     * @brief : 범칙금 상세 조회
     * @details : 범칙금 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : pymntId
     * @return : 
     */
    @Authority(type = MethodType.READ)
    @GetMapping("/second/detail.do")
    public String mngSecondDetail(Model model, @RequestParam("pymntId")String pymntId){
    	MozFinePymntInfo finePymntInfo = penaltyService.getPenaltyDetail(pymntId);
    	model.addAttribute("finePymntInfo",finePymntInfo);
    	return "views/penaltymng/penaltySecondDetail";
    }
    
    /**
     * @brief : 범칙금 수정 화면
     * @details : 범칙금 수정 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : pymntId
     * @return : 
     */
    @Authority(type = MethodType.READ)
    @GetMapping("/update.do")
    public String mngModify(Model model,@RequestParam("pymntId")String pymntId){

        List<MozCmCd> cdList = commonCdService.getCdList("pay");
        model.addAttribute("cdList", cdList);

        MozFinePymntInfo finePymntInfo = penaltyService.getPenaltyDetail(pymntId);
        model.addAttribute("finePymntInfo",finePymntInfo);

        return "views/penaltymng/penaltyMngModify";
    }

    /**
     * @brief : 범칙금 수정
     * @details : 범칙금 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : finePymntInfo
     * @return : 
     */
    @Authority(type = MethodType.UPDATE)
    @PostMapping("/update.ajax")
    public @ResponseBody CommonResponse<?> mngModifyAjax(MozFinePymntInfo finePymntInfo){
    	penaltyService.updatePayStatus(finePymntInfo);
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"As informações sobre o estado do pagamento foram corrigidas.");
    }
    
    
    @Authority(type = MethodType.UPDATE)
    @PostMapping("/pymnt/cmpl.ajax")
    @ResponseBody
    public Map<String,Object> paymentAjax(@RequestParam("pymntId")String pymntId){
    	
    	Map<String, Object> result = new HashMap<>();
    	try {
            penaltyService.paymentClear(pymntId);
        	
            result.put("code", "1");
        }catch (Exception e){
            result.put("code", "0");
        }
        return result;
    }
    
    @Authority(type = MethodType.UPDATE)
    @PostMapping(value = "pymnt/cncl.ajax")
    @ResponseBody
    public Map<String, Object> paymentCAjax(@RequestParam("pymntId")String pymntId){
    	Map<String, Object> result = new HashMap<>();
    	
    	try {
			penaltyService.paymentCencal(pymntId);
			result.put("code", "1");
		} catch (Exception e) {
			result.put("code", "0");
		}
    	
    	
    	return result;
    }
    
//    @Authority(type = MethodType.READ)
//    @PostMapping(value = "/penalty/sendemail.ajax")
//    @ResponseBody
//    public Map<String, Object> cmpSendEmail(@RequestBody Map<String,Object> penaltyParams){
//    	log.info("email PenaltyParams={}", penaltyParams);	
//    	return penaltyService.penaltySendEmail((String) penaltyParams.get("pymnyId")
//    			, (String) penaltyParams.get("emailAddr")
//    			, (String) penaltyParams.get("content")
//    		);
//    }
}
