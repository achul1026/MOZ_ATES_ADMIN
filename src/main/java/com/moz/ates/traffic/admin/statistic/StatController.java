package com.moz.ates.traffic.admin.statistic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.lang.Delegate;

@Controller
@RequestMapping(value = "stat")
public class StatController {
	
    /**
     * @brief : 교통단속 통계 화면
     * @details : 교통단속 통계 화면
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : 
     * @return : 
     */
    @GetMapping("/enforce/chart.do")
    public String enforceChartList(Model model){

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
    @GetMapping("/penalty/chart.do")
    public String penaltyChartList(Model model){

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
    @GetMapping("/accident/chart.do")
    public String accidentChartList(Model model){

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
    @GetMapping("facility/chart.do")
    public String facilityChartList(Model model){

        return "views/statistic/facilityChart";
    }

}
