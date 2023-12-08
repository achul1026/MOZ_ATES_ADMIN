package com.moz.ates.traffic.admin.penaltymng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.payment.MozFinePymntInfo;

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
     * @brief : 범칙금 리스트 화면
     * @details : 범칙금 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : finePymntInfo
     * @return : 
     */
    @GetMapping("/mng/list.do")
    public String mngList(Model model, @ModelAttribute MozFinePymntInfo finePymntInfo){
        model.addAttribute("finePymntInfo",finePymntInfo);
        model.addAttribute("penaltyList", penaltyService.getPenaltyList(finePymntInfo));
        model.addAttribute("totalCnt", penaltyService.getPenaltyListCnt(finePymntInfo));
        return "views/penaltymng/penaltyMngList";
    }

//    /**
//     * @brief : 범칙금 리스트 조회
//     * @details : 범칙금 리스트 조회
//     * @author : KC.KIM
//     * @date : 2023.08.04
//     * @param : finePymntInfo
//     * @return : DataTableVO
//     */
//    @PostMapping(value = "mngListAjax")
//    @ResponseBody
//    public DataTableVO mngListAjax(@ModelAttribute MozFinePymntInfo finePymntInfo){
//    	
//        return penaltyService.getPenaltyListDatatable(finePymntInfo);
//    }

    /**
     * @brief : 범칙금 등록 화면
     * @details : 범칙금 등록 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : 
     * @return : 
     */
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
     * @brief : 범칙금 상세 조회
     * @details : 범칙금 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : pymntId
     * @return : 
     */
    @GetMapping("/mng/detail.do")
    public String mngDetail(Model model, @RequestParam("pymntId")String pymntId){

    	MozFinePymntInfo finePymntInfo = penaltyService.getPenaltyDetail(pymntId);
        model.addAttribute("finePymntInfo",finePymntInfo);

        return "views/penaltymng/penaltyMngDetail";
    }
    
    /**
     * @brief : 범칙금 수정 화면
     * @details : 범칙금 수정 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : pymntId
     * @return : 
     */
    @GetMapping("/mng/update.do")
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
    @PostMapping("/mng/update.ajax")
    @ResponseBody
    public Map<String,Object> mngModifyAjax(MozFinePymntInfo finePymntInfo){
        Map<String, Object> result = new HashMap<>();
        try {
        	penaltyService.updatePenalty(finePymntInfo);
            
            penaltyService.updatePenaltyMaster(finePymntInfo);
            
            result.put("code", "1");
        }catch (Exception e){
            result.put("code", "0");
        }

        return result;
    }
    
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
    
    @PostMapping(value = "/penalty/sendemail.ajax")
    @ResponseBody
    public Map<String, Object> cmpSendEmail(@RequestBody Map<String,Object> penaltyParams){
    	log.info("email PenaltyParams={}", penaltyParams);	
    	return penaltyService.penaltySendEmail((String) penaltyParams.get("pymnyId")
    			, (String) penaltyParams.get("emailAddr")
    			, (String) penaltyParams.get("content")
    		);
    }
}
