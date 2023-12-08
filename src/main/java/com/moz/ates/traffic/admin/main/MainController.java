package com.moz.ates.traffic.admin.main;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;

@Controller
@RequestMapping("/")
public class MainController {

	@Autowired
	MainService mainService; 

    @RequestMapping("login")
    public String login(Model model){

        return "views/main/login";
    }

    /**
     * methodName : dashboard
     * author : Mike Lim
     * description : 대시보드 화면
     * @param model
     * @return string
     * @throws Exception 
     */
    @RequestMapping(value = "dashboard")
    public String dashboard(Model model) throws Exception{

        return "views/main/dashboardMain";
    }
    
    @RequestMapping("joinUs")
    public String joinUs(Model model) {
    	
    	return "views/main/joinUs";
    }
    
    /**
     * @brief 어드민 회원가입 신청
     * @param webOprtr
     * @return
     * @throws Exception
     */
    @PostMapping("joinUsAjax")
    @ResponseBody
    public Map<String,Object> joinUsAjax(@ModelAttribute MozWebOprtr webOprtr) throws Exception{
    	Map<String,Object> result = new HashMap<String,Object>();
    	String email = webOprtr.getOprtrAccountId();
    	String pw = webOprtr.getOprtrAccountPw();

    	//TODO::새로운 유효성 검사 방식으로 수정 필요
//    	ValidationUtils.stringRequired(email);
//		ValidationUtils.stringRequired(pw);
//		if(ValidationUtils.isRegexValidation(email, RegexType.EMAIL) && ValidationUtils.isRegexValidation(pw, RegexType.PASSWORD)) {
			mainService.registWebOprtr(webOprtr);
//		} else {
//			throw new CommonException(ErrorCode.VALIDATION_FAILED);
//		}
			
			
    	result.put("code", 200);
    	result.put("message", "회원가입 성공");
    	return result;
    }

}
