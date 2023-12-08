package com.moz.ates.traffic.admin.administmng;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "administ")
public class AdministController {
    /**
     * @brief : 법원이송 리스트 화면
     * @details : 법원이송 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : 
     * @return : 
     */
    @GetMapping(value = "/court/mng/list.do")
    public String noticeList(Model model){
        return "views/administmng/administMngList";
    }
}
