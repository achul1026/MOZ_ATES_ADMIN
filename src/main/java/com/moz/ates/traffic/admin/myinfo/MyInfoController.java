package com.moz.ates.traffic.admin.myinfo;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;
import com.moz.ates.traffic.common.support.exception.NoLoginException;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Controller
@RequestMapping(value = "/myinfo")
public class MyInfoController {
	
	
	@Autowired
	MyInfoService myInfoService;
	
	/**
	 * @brief 마이페이지
	 * @author KY.LEE
	 * @date 2024. 4. 19.
	 * @method viewMyPage
	 */
    @GetMapping("/myPage.do")
    public String viewMyPage(Model model) {
    	if(LoginOprtrUtils.getMozWebOprtr() == null) {
    		throw new NoLoginException();
    	}
    	model.addAttribute("myInfoDetail", myInfoService.getMyInfoDetail());
    	return "views/myinfo/myPage";
    }
    
	/**
	 * @brief 현재 비밀번호 인증 페이지
	 * @author KY.LEE
	 * @date 2024. 4. 19.
	 * @method viewPwConfirm
	 */
    @GetMapping("/{type}/confirmPw.do")
    public String viewPwConfirm(Model model ,@PathVariable String type) {
		HttpSession session = LoginOprtrUtils.getSession();
		session.removeAttribute("isPwChk");
    	
    	model.addAttribute("type", type);
    	return "views/myinfo/confirmPw";
    }
    
	/**
	 * @Method Name : verfyPassword
	 * @Date : 2024. 4. 23.
	 * @Author : KY.LEE
	 * @Method Brief : 현재 비밀번호 인증
	 * @param model
	 */
	@PostMapping("/verfy")
	public @ResponseBody CommonResponse<?> verfyPassword(Model model , MozWebOprtr mozWebOprtr) {
		if(MozatesCommonUtils.isNull(mozWebOprtr.getOprtrAccountPw())) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,"O parâmetro é nulo.");
		}
		
		if(!myInfoService.verfyPassword(mozWebOprtr)) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,"Incompatibilidade da palavra-passe.");
		}
		HttpSession session = LoginOprtrUtils.getSession();
		session.setAttribute("isPwChk", true);
		
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"Palavra-passe verificada.");
	}
	
	/**
	 * @brief 비밀번호 변경 페이지
	 * @author KY.LEE
	 * @date 2024. 4. 19.
	 * @method viewPwChange
	 */
    @GetMapping("/changePw.do")
    public String viewPwChange(Model model) {
		HttpSession session = LoginOprtrUtils.getSession();
		
	    boolean isPwChk = session.getAttribute("isPwChk") != null ? (boolean) session.getAttribute("isPwChk") : false;

	    model.addAttribute("isPwChk" , isPwChk);
    	return "views/myinfo/changePw";
    }

    
	/**
	 * @Method Name : modifyPassword
	 * @Date : 2024. 3. 21.
	 * @Author : IK.MOON
	 * @Method Brief : 비밀번호 수정
	 * @param model
	 * @return
	 */
	@PostMapping("/password/modify")
	public @ResponseBody CommonResponse<?> modifyPassword(Model model , MozWebOprtr mozWebOprtr) {
		if(MozatesCommonUtils.isNull(mozWebOprtr.getOprtrAccountPw()) || MozatesCommonUtils.isNull(mozWebOprtr.getOprtrAccountPwChk())) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,"O parâmetro é nulo.");
		}
		
		if(!mozWebOprtr.getOprtrAccountPw().equals(mozWebOprtr.getOprtrAccountPwChk())) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,"A palavra-passe não corresponde.");
		}
		
		myInfoService.modifyPassword(mozWebOprtr);
		
		HttpSession session = LoginOprtrUtils.getSession();
		
		session.removeAttribute("isPwChk");
		
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"Modificar o sucesso.");
	}
    
	/**
	 * @brief 내 정보 변경 페이지
	 * @author KY.LEE
	 * @date 2024. 4. 19.
	 * @method viewMyInfoChange
	 */
    @GetMapping("/changeMyInfo.do")
    public String viewMyInfoChange(Model model) {
		HttpSession session = LoginOprtrUtils.getSession();
		
	    boolean isPwChk = session.getAttribute("isPwChk") != null ? (boolean) session.getAttribute("isPwChk") : false;

	    model.addAttribute("isPwChk" , isPwChk);
    	model.addAttribute("myInfoDetail", myInfoService.getMyInfoDetail());
    	return "views/myinfo/changeMyInfo";
    }
    
	/**
	 * @Method Name : modifyPassword
	 * @Date : 2024. 3. 21.
	 * @Author : IK.MOON
	 * @Method Brief : 비밀번호 수정
	 * @param model
	 * @return
	 */
	@PostMapping("/modify")
	public @ResponseBody CommonResponse<?> myInfoModify(Model model , MozWebOprtr mozWebOprtr) {
		if(MozatesCommonUtils.isNull(mozWebOprtr.getOprtrNm()) || MozatesCommonUtils.isNull(mozWebOprtr.getOprtrPno())) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,"O parâmetro é nulo.");
		}
		
		myInfoService.modifyMyProfile(mozWebOprtr);
		
		HttpSession session = LoginOprtrUtils.getSession();
		
		session.removeAttribute("isPwChk");
		
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"Modificar o sucesso.");
	}
}
