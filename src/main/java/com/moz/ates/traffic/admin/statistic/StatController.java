package com.moz.ates.traffic.admin.statistic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moz.ates.traffic.admin.common.CommonCdService;
import com.moz.ates.traffic.admin.common.enums.MethodType;
import com.moz.ates.traffic.admin.config.Authority;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.stat.ChartSearchDTO;

@Controller
@RequestMapping(value = "stat")
public class StatController {
	
	@Autowired
	CommonCdService commonCdService;
	
	@Autowired
	StatService statService;
	
    /**
     * @brief : 교통단속 통계 화면
     * @details : 교통단속 통계 화면
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : 
     * @return : 
     */
	@Authority(type = MethodType.READ)
    @GetMapping("/enforce/chart.do")
    public String enforceChartList(Model model){
		List<MozCmCd> lawCdList = commonCdService.getCdList("LAW_CHTR_CD");
		model.addAttribute("lawCdList", lawCdList);
        return "views/statistic/enforceChart";
    }
    
    /**
     * @brief : 범칙금 통계 화면
     * @details : 범칙금 통계 화면
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : 
     * @return : 
     */
	@Authority(type = MethodType.READ)
    @GetMapping("/penalty/chart.do")
    public String penaltyChartList(Model model){
		List<MozCmCd> ntcCdList = commonCdService.getCdList("NOTICE_TYPE");
		model.addAttribute("ntcCdList", ntcCdList);
		return "views/statistic/penaltyChart";
    }
    
    /**
     * @brief : 교통사고 통계 화면
     * @details : 교통사고 통계 화면
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : 
     * @return : 
     */
	@Authority(type = MethodType.READ)
    @GetMapping("/accident/chart.do")
    public String accidentChartList(Model model){
		List<MozCmCd> acdntCdList = commonCdService.getCdList("ACCIDENT_TYPE");
		model.addAttribute("acdntCdList", acdntCdList);
        return "views/statistic/accidentChart";
    }

    /**
     * @brief : 교통 시설물 통계 화면
     * @details : 교통 시설물 통계 화면
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : 
     * @return : 
     */
	@Authority(type = MethodType.READ)
    @GetMapping("facility/chart.do")
    public String facilityChartList(Model model){
		List<MozCmCd> eqpCdList = commonCdService.getCdList("EQUIPMENT_TYPE");
		model.addAttribute("eqpCdList", eqpCdList);
        return "views/statistic/facilityChart";
    }
	
	/**
     * @brief : 통계 데이터 조회
     * @details : 통계 데이터 조회
     * @author : KC.KIM
     * @date : 2024.04.24
     * @param : type
     * @param : chartDTO
     * @return : 
     */
	@Authority(type = MethodType.READ)
    @GetMapping("{type}/chart.ajax")
    public @ResponseBody CommonResponse<?> administMngUpdate(Model model, @PathVariable String type, ChartSearchDTO chartDTO){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		chartDTO.setType(type);
		
		resultMap =  statService.findAllChartData(chartDTO);
		
		return CommonResponse.successToData(resultMap, "");
    }
}
