package com.moz.ates.traffic.admin.finentc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "finentc")
public class FineNtcController {
	
    /**
     * @brief : 고지 관리 리스트 화면
     * @details : 고지 관리 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : 
     * @return : 
     */
	@GetMapping("/mng/list.do")
	public String mngList(Model model) {
		return "views/finentc/fineNtcMngList";
	}
}
