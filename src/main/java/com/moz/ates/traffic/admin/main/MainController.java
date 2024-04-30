package com.moz.ates.traffic.admin.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moz.ates.traffic.admin.common.enums.MethodType;
import com.moz.ates.traffic.admin.common.enums.OprtrPermissionCd;
import com.moz.ates.traffic.admin.config.Authority;
import com.moz.ates.traffic.common.component.Pagination;
import com.moz.ates.traffic.common.entity.common.ChartDTO;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;

@Controller
@RequestMapping("/")
public class MainController {

	@Autowired
	MainService mainService; 

    /**
      * @Method Name : login
      * @Date : 2024. 2. 8.
      * @Author : IK.MOON
      * @Method Brief : 로그인 페이지 이동
      * @param model
      * @return
      */
    @GetMapping("login")
    public String login(Model model){
        return "views/main/login";
    }
    
	/**
	 * @brief 대시보드 화면
	 * @author KY.LEE
	 * @date 2024. 4. 24.
	 * @method dashboard
	 */
    @Authority(type = MethodType.READ)
    @GetMapping(value = "dashboard")
    public String dashboard(Model model) throws Exception{
    	ChartDTO dashboardChartInfo =  mainService.getStatisticsInfoForDashboard();
    	model.addAttribute("tfcEnfCnt", mainService.getTodayEnforcementCount());
    	model.addAttribute("tfcEnfList", mainService.getTodayEnforcementInfo());
    	model.addAttribute("tfcAcdntCnt", mainService.getTodayAccidentCount());
    	model.addAttribute("tfcAcdntList", mainService.getTodayAccidentInfo());
    	model.addAttribute("tfcEqpInfo", mainService.getEqpInfo());
    	model.addAttribute("dashboardChartInfo",dashboardChartInfo);
        return "views/main/dashboardMain";
    }
    
    /**
      * @Method Name : joinUs
      * @Date : 2024. 2. 8.
      * @Author : IK.MOON
      * @Method Brief : 관리자 등록신청 페이지 이동
      * @param model
      * @return
      */
    @GetMapping("joinUs")
    public String joinUs(Model model) {
    	return "views/main/joinUs";
    }
    
    /**
      * @Method Name : getModalDeptList
      * @Date : 2024. 2. 8.
      * @Author : IK.MOON
      * @Method Brief : 관리자 등록신청 > 소속기관 선택 모달 비동기 호출
      * @param model
      * @return
      */
    @GetMapping("modal/dept/list.do")
    public String getModalDeptList(Model model , MozCmCd mozCmCd) {
		int page = mozCmCd.getPage();
		mozCmCd.setCdGroupId("AFFILIATION_CD");
		int totalCnt = mainService.getTotalCountAffiliationCd(mozCmCd);
		Pagination pagination = new Pagination(totalCnt, page, 3, 5);

		mozCmCd.setLength(3);
		mozCmCd.setStart((page - 1) * pagination.getPageSize());
		
		model.addAttribute("mozCmCd", mozCmCd);
		model.addAttribute("pagination", pagination);
    	model.addAttribute("affiliationList", mainService.getAffiliationCd(mozCmCd));
    	return "views/main/modal/deptListModal";
    }

    /**
      * @Method Name : joinUsAjax
      * @Date : 2024. 2. 8.
      * @Author : IK.MOON
      * @Method Brief : 관리자 등록 신청 비동기 호출
      * @param webOprtr
      * @return
      * @throws Exception
      */
    @PostMapping("joinUsAjax")
    public @ResponseBody CommonResponse<?> joinUsAjax(@ModelAttribute MozWebOprtr webOprtr) throws Exception{
    	// TODO 벨리데이션
    	
    	// ValidateBuilder dtoValidator = new ValidateBuilder(webOprtr);
    	// dtoValidator.addRule("oprtrAccountId", new ValidateChecker().setEmail().setRequired().setMaxLength(40, "담당자 이메일은 40자를 넘을 수 없습니다."))
    	// 			.addRule("oprtrAccountPw", new ValidateChecker().setRequired().setPassword().setMaxLength(60, "담당자 비밀번호는 60자를 넘을 수 없습니다."))
    	// 			.addRule("oprtrNm", new ValidateChecker().setRequired().setMaxLength(50, "담당자 명은 50자를 넘을 수 없습니다."));
    	// 
    	// ValidateResult dtoValidatorResult = dtoValidator.isValid();
    	// 
    	// if(!dtoValidatorResult.isSuccess()) {
    	// 	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
    	// }
    	
    	// 권한 validation
    	String oprtrPermission = webOprtr.getOprtrPermission();

    	if(OprtrPermissionCd.getCodeByNameValidateSuperAdmin(oprtrPermission)) {
				webOprtr.setOprtrPermission(OprtrPermissionCd.getCodeByName(oprtrPermission));
			} else {
				// 권한을 확인해 주세요.
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Por favor, verifique suas permissões.");
			}
    	
    	try {
    		mainService.registWebOprtr(webOprtr);			
			} catch (Exception e) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST , e.getMessage());
			}
    	// 등록이 완료 되었습니다.
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK , "O registro foi concluído.");
    }
    
    /**
	 * @Method Name : passwordFind
	 * @작성일 : 2024. 01. 11.
	 * @작성자 : KC.KIM
	 * @Method 설명 : 비밀번호 찾기 페이지 이동
	 * @param model
	 * @return
	 */
    @GetMapping("password/find.do")
    public String passwordFind() {
    	return "views/main/passwordFind";
    }
    
    /**
      * @Method Name : passwordFindAjax
      * @Date : 2024. 4. 11.
      * @Author : IK.MOON
      * @Method Brief : 비밀번호 찾기 ajax
      * @param webOprtr
      * @return
      */
    @PostMapping("password/find.ajax")
    @ResponseBody
    public CommonResponse<?> passwordFindAjax(MozWebOprtr webOprtr) {
    	try {
				mainService.findPw(webOprtr);
			} catch (Exception e) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Fail");
			}
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Success");
    }
    
    /**
	 * @Method Name : passwordRe
	 * @작성일 : 2024. 01. 11.
	 * @작성자 : KC.KIM
	 * @Method 설명 : 비밀번호 재설정
	 * @param model
	 * @return
	 */
    @GetMapping("passwordRe")
    public String passwordRe(Model model) {
    	
    	return "views/main/passwordRe";
    }

    /**
     * @Method Name : idFind
     * @Date : 2024. 2. 14.
     * @Author : IK.MOON
     * @Method Brief : ID 찾기 페이지 이동
     * @param model
     * @return
     */
   @GetMapping("id/find.do")
   public String idFind(Model model) {
   	return "views/main/idFind";
   }
   
   /**
    * @Method Name : idFindAjax
    * @Date : 2024. 2. 15.
    * @Author : IK.MOON
    * @Method Brief : ID찾기 비동기 처리
    * @return
    */
  @PostMapping("id/find.ajax")
  @ResponseBody
  public CommonResponse<?> idFindAjax(MozWebOprtr mozWebOprtr) {
  	MozWebOprtr webOprtrFound = null;
  	
  	try {
  		webOprtrFound = mainService.findId(mozWebOprtr);
  	} catch (Exception e) {
  		// 일치하는 계정이 없습니다.
  		return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST , "Não há contas correspondentes.");
  	}
  	return CommonResponse.ResponseSuccess(HttpStatus.OK , "Success", null, webOprtrFound);
  }
  
}
