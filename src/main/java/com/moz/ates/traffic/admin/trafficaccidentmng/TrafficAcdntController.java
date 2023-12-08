package com.moz.ates.traffic.admin.trafficaccidentmng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moz.ates.traffic.admin.common.CommonCdService;
import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.admin.gis.service.GisService;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntChgHst;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntMaster;
import com.moz.ates.traffic.common.entity.common.AccidentDomain;
import com.moz.ates.traffic.common.entity.common.MozCmCd;

@Controller
@RequestMapping(value = "/acdnt")
public class TrafficAcdntController {

    @Autowired
    private CommonCdService commonCdService;

    @Autowired
    private TrafficAcdntService trafficAcdntService;
    
    @Autowired
    private GisService gService;
    
    /**
     * @brief : 교통사고 관리 리스트
     * @details : 교통사고 관리 리스트
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntMaster
     * @return : 
     */
    @GetMapping("/mng/list.do")
    public String acdntMngList(Model model, @ModelAttribute MozTfcAcdntMaster tfcAcdntMaster){
        model.addAttribute("tfcAcdntMaster", tfcAcdntMaster);
        model.addAttribute("acdntList", trafficAcdntService.getAcdntList(tfcAcdntMaster));
        model.addAttribute("totalCnt", trafficAcdntService.getAcdntListCnt(tfcAcdntMaster));
        return "views/accidentmng/acdntMngList";
    }

    /**
     * @brief : 교통사고 등록 화면
     * @details : 교통사고 등록 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : 
     * @return : 
     */
    @GetMapping("/mng/save.do")
    public String acdntSave(Model model){

        List<MozCmCd> cdList = commonCdService.getCdList("TAP");
        model.addAttribute("cdList", cdList);

        return "views/accidentmng/AcdntMngRegist";
    }
    
    /**
     * @brief : 교통사고 등록
     * @details : 교통사고 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntMaster
     * @return : 
     */
    @RequestMapping("/mng/save.ajax")
    @ResponseBody
    public Map<String,Object> saveAcdntInfo(@ModelAttribute MozTfcAcdntMaster tfcAcdntMaster){
        Map<String, Object> result = new HashMap<>();

        int dupliCnt = trafficAcdntService.countTfcAcdntMaster(tfcAcdntMaster);
        if(dupliCnt > 0){
            result.put("code", "-1");
            result.put("msg", "중복된 사고번호 입니다.");
        }else{
            try {
                trafficAcdntService.mozTfcAcdntMasterSave(tfcAcdntMaster);
                result.put("code", "1");
            }catch (Exception e){
                result.put("code", "0");
                result.put("msg", "error");
            }
        }
        return result;
    }
    
    /**
     * @brief : 교통사고 상세 조회
     * @details : 교통사고 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntId
     * @return : 
     */
    @GetMapping("/mng/detail.do")
    public String acdntMngDetail(Model model, @RequestParam("tfcAcdntId")String tfcAcdntId ){
    	MozTfcAcdntMaster tfcAcdntMaster = trafficAcdntService.getMngDetail(tfcAcdntId);
    	AccidentDomain aDomain = gService.getAcMapInfo(tfcAcdntId);
    	model.addAttribute("aDomain",aDomain);
    	model.addAttribute("tfcAcdntMaster",tfcAcdntMaster);

        return "views/accidentmng/acdntMngDetail";
    }

    /**
     * @brief : 교통사고 수정 화면
     * @details : 교통사고 수정 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntId
     * @return : 
     */
    @GetMapping("/mng/update.do")
    public String acdntMngModify(Model model, @RequestParam("tfcAcdntId")String tfcAcdntId){

        List<MozCmCd> cdList = commonCdService.getCdList("TAP");
        model.addAttribute("cdList", cdList);

        MozTfcAcdntMaster tfcAcdntMaster = trafficAcdntService.getMngDetail(tfcAcdntId);
        model.addAttribute("tfcAcdntMaster", tfcAcdntMaster);

        return "views/accidentmng/acdntMngModify";
    }

    /**
     * @brief : 교통사고 정보 수정
     * @details : 교통사고 정보 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntMaster
     * @return : 
     */
    @PostMapping("/mng/update.ajax")
    @ResponseBody
    public Map<String,Object> acdntMngUpdateAjax(@ModelAttribute MozTfcAcdntMaster tfcAcdntMaster){
        Map<String, Object> result = new HashMap<>();

            try {
                trafficAcdntService.upateAcdnt(tfcAcdntMaster);
                result.put("code", "1");
            }
            catch (Exception e){
                result.put("code", "0");
                result.put("msg", "error");
            }


        return result;
    }

    /**
     * @brief : 교통사고 로그 리스트 화면
     * @details : 교통사고 로그 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : tfcAcdntChgHst
     * @return : 
     */
    @GetMapping("/log/list.do")
    public String logList(Model model,@ModelAttribute MozTfcAcdntChgHst tfcAcdntChgHst){
        model.addAttribute("tfcAcdntChgHst", tfcAcdntChgHst);
        model.addAttribute("acdntLogList", trafficAcdntService.getAcdntLogList(tfcAcdntChgHst));
        model.addAttribute("totalCnt", trafficAcdntService.getAcdntLogListCnt(tfcAcdntChgHst));
        return "views/accidentmng/acdntLogList";
    }

}
