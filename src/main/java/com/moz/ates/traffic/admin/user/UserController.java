package com.moz.ates.traffic.admin.user;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moz.ates.traffic.admin.common.enums.MethodType;
import com.moz.ates.traffic.admin.common.enums.OprtrPermissionCd;
import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.admin.config.Authority;
import com.moz.ates.traffic.admin.sitemng.auth.AuthService;
import com.moz.ates.traffic.admin.sitemng.code.CodeService;
import com.moz.ates.traffic.admin.sitemng.log.LogService;
import com.moz.ates.traffic.common.component.Pagination;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.operator.MozAuth;
import com.moz.ates.traffic.common.entity.operator.MozOprtrAudLog;
import com.moz.ates.traffic.common.entity.operator.MozPolAudLog;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;
import com.moz.ates.traffic.common.entity.police.MozPolInfo;

@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private CodeService codeService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * @brief : 관리자 등록 화면
     * @details : 관리자 등록 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : 
     * @return : 
     */
    @Authority(type = MethodType.READ)
    @GetMapping(value = "/admin/save.do")
    public String newUserRegist(Model model){
        return "views/user/userRegist";
    }

    /**
     * @brief : 관리자 등록
     * @details : 관리자 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : webOprtr
     * @return : 
     */
    @Authority(type = MethodType.CREATE)
    @PostMapping(value = "/admin/save.ajax")
    @ResponseBody
    public Map<String,Object> userRegistAjax(@ModelAttribute MozWebOprtr webOprtr, Principal principal){
        Map<String, Object> result = new HashMap<>();
        int dupliUserChk = userService.getDupliChk(webOprtr);
        
        if(dupliUserChk > 0){
            result.put("code", "-1");
            result.put("msg", "중복된 아이디가 있습니다.");
        }else{
            try {
                System.out.println(principal.getName());
                webOprtr.setApvr(principal.getName());
                if(webOprtr.getOprtrAccountPw() != null && !webOprtr.getOprtrAccountPw().isEmpty()){
                	webOprtr.setOprtrAccountPw(passwordEncoder.encode(webOprtr.getOprtrAccountPw()));
                }
                userService.registUser(webOprtr);
                result.put("code", "1");
            }catch (Exception e){
                result.put("code", "0");
                result.put("msg", e.getMessage());
            }
        }
        return result;
    }

    /**
     * @brief : 관리자 리스트 화면
     * @details : 관리자 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : webOprtr
     * @return : 
     */
    @Authority(type = MethodType.READ)
    @GetMapping("/admin/list.do")
    public String adminList(Model model,@ModelAttribute MozWebOprtr webOprtr, HttpServletRequest request){
    	String[] oprtrPermissionCdArr = {OprtrPermissionCd.SUPER_ADMIN.getCode(), OprtrPermissionCd.ADMIN_USER.getCode()};    	
    	webOprtr.setOprtrPermissionCdArr(oprtrPermissionCdArr);
    	
    	String userPermission = LoginOprtrUtils.getOprtrPermission();
    	if (userPermission.equals(OprtrPermissionCd.SUPER_ADMIN.getCode())) {
    		webOprtr.setLoginUserPermission(userPermission);
		}
    	
    	int page = webOprtr.getPage();
		int totalCnt = userService.getWebOprtrListCnt(webOprtr);
		Pagination pagination = new Pagination(totalCnt, page);
		
		webOprtr.setStart((page - 1) * pagination.getPageSize());
    		
        model.addAttribute("webOprtr", webOprtr);
        model.addAttribute("webOprtrList", userService.getWebOprtrList(webOprtr));
        model.addAttribute("pagination", pagination);
        
        return "views/user/adminList";
    }

    /**
     * @brief : 관리자 리스트 화면
     * @details : 관리자 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : webOprtr
     * @return : 
     */
    @Authority(type = MethodType.READ)
    @GetMapping("/operator/list.do")
    public String operatorList(Model model, @ModelAttribute MozWebOprtr webOprtr){
    	String[] oprtrPermissionCdArr = {OprtrPermissionCd.OFFICE_OPERATOR.getCode(), OprtrPermissionCd.POLICE_OPERATOR.getCode()};    	
    	webOprtr.setOprtrPermissionCdArr(oprtrPermissionCdArr);
    	
    	String userPermission = LoginOprtrUtils.getOprtrPermission();
    	if (userPermission.equals(OprtrPermissionCd.SUPER_ADMIN.getCode())) {
    		webOprtr.setLoginUserPermission(userPermission);
		}
    	
    	int page = webOprtr.getPage();
    	int totalCnt = userService.getWebOprtrListCnt(webOprtr);
    	Pagination pagination = new Pagination(totalCnt, page);
    	
    	webOprtr.setStart((page - 1) * pagination.getPageSize());
    	
    	model.addAttribute("webOprtr", webOprtr);
    	model.addAttribute("webOprtrList", userService.getWebOprtrList(webOprtr));
    	model.addAttribute("pagination", pagination);
    	
    	return "views/user/operatorList";
    }
    
    /**
      * @Method Name : operatorDetail
      * @작성일 : 2024. 2. 20.
      * @작성자 : SM.KIM
      * @Method 설명 : 운영자 상세 조회
      * @param model
      * @param oprtrId
      * @param oprtrAudLog
      * @return
      */
    @Authority(type = MethodType.READ)
    @GetMapping("/operator/detail.do")
    public String operatorDetail(Model model, @RequestParam("oprtrId")String oprtrId, @ModelAttribute MozOprtrAudLog oprtrAudLog){

        MozWebOprtr webOprtr = userService.getUserDetail(oprtrId);
        
        int page = oprtrAudLog.getPage();
    	int totalCnt = logService.getMozOprtrAudLogListCnt(oprtrAudLog);
    	Pagination pagination = new Pagination(totalCnt, page);
        
        model.addAttribute("webOprtr", webOprtr);
        model.addAttribute("oprtrAudLogList", logService.getOprtrAudLogByOprtrId(webOprtr.getOprtrId()));
        model.addAttribute("cmCdList", codeService.getSubCodeDetail(webOprtr.getOprtrPermissionGroupCode()));
        model.addAttribute("pagination", pagination);
        
        return "views/user/adminDetail";
    }

    /**
     * @brief : 관리자 상세 조회
     * @details : 관리자 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : oprtrId
     * @return : 
     */
    @Authority(type = MethodType.READ)
    @GetMapping("/admin/detail.do")
    public String adminDetail(Model model, @RequestParam("oprtrId")String oprtrId, @ModelAttribute MozOprtrAudLog oprtrAudLog, MozAuth mozAuth){

        MozWebOprtr webOprtr = userService.getUserDetail(oprtrId);
        
        int page = oprtrAudLog.getPage();
    	int totalCnt = logService.getMozOprtrAudLogListCnt(oprtrAudLog);
    	Pagination pagination = new Pagination(totalCnt, page);
        
    	List<MozCmCd> permissionList = codeService.getSubCodeDetail(webOprtr.getOprtrPermissionGroupCode());
		permissionList.removeIf(item -> item.getCdId().equals(OprtrPermissionCd.SUPER_ADMIN.getCode()));
		
        model.addAttribute("webOprtr", webOprtr);
        model.addAttribute("authList", authService.getAuthList(mozAuth));
        model.addAttribute("oprtrAudLogList", logService.getOprtrAudLogByOprtrId(webOprtr.getOprtrId()));
        model.addAttribute("permissionList", permissionList);
        model.addAttribute("pagination", pagination);
        
        return "views/user/adminDetail";
    }
    
    /**
     * @brief : 관리자 수정 화면
     * @details : 관리자 수정 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : oprtrId
     * @return : 
     */
    @Authority(type = MethodType.READ)
    @GetMapping("/admin/update.do")
    public String adminModify(Model model, @RequestParam("oprtrId")String oprtrId){
    	MozWebOprtr webOprtr = userService.getUserDetail(oprtrId);
        model.addAttribute("webOprtr", webOprtr);

        return "views/user/adminModify";
    }
    
    /**
     * @brief : 관리자 수정
     * @details : 관리자 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : webOprtr
     * @return : 
     */
    @Authority(type = MethodType.UPDATE)
    @PostMapping("/admin/update.ajax")
    @ResponseBody
    public Map<String,Object> adminModifyAjax(@ModelAttribute MozWebOprtr webOprtr){
        Map<String, Object> result = new HashMap<>();
        try {
            if(webOprtr.getOprtrAccountPw() != null && !webOprtr.getOprtrAccountPw().isEmpty()){
            	webOprtr.setOprtrAccountPw(passwordEncoder.encode(webOprtr.getOprtrAccountPw()));
            }
            userService.updateUser(webOprtr);
            result.put("code", "1");
        }catch (Exception e){
            result.put("code", "0");
            result.put("msg", e.getMessage());
        }

        return result;
    }

    
    /**
     * @brief : 경찰 리스트 화면
     * @details : 경찰 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.18
     * @param : polInfo
     * @return : 
     */
    @Authority(type = MethodType.READ)
	@GetMapping("/police/list.do")
	public String policeList(Model model, @ModelAttribute MozPolInfo polInfo) {
    	String userPermission = LoginOprtrUtils.getOprtrPermission();
    	if (userPermission.equals(OprtrPermissionCd.SUPER_ADMIN.getCode())) {
    		polInfo.setLoginUserPermission(userPermission);
		}
    	
    	int page = polInfo.getPage();
		int totalCnt = userService.getPolInfoListCnt(polInfo);
		Pagination pagination = new Pagination(totalCnt, page);
		
		polInfo.setStart((page - 1) * pagination.getPageSize());
		
		model.addAttribute("polInfo", polInfo);
		model.addAttribute("polList", userService.getPolInfoList(polInfo));
		model.addAttribute("pagination", pagination);
		
		return "views/user/polList";
	}

    /**
     * @brief : 사용자 상세 조회
     * @details : 사용자 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.18
     * @param : oprtrId
     * @return : 
     */
    @Authority(type = MethodType.READ)
    @GetMapping("/police/detail.do")
    public String polMngDetail(Model model, @RequestParam("polId")String polId, MozPolAudLog polAudLog){

        int page = polAudLog.getPage();
    	int totalCnt = logService.getMozPolAudLogListCnt(polAudLog);
    	Pagination pagination = new Pagination(totalCnt, page);
        
    	MozPolInfo polInfo = userService.getPolInfoDetail(polId);
    	
    	model.addAttribute("polInfo", polInfo);
        model.addAttribute("oprtrAudLogList", logService.getPolAudLogByPolId(polInfo.getPolId()));
        model.addAttribute("pagination", pagination);

        return "views/user/polDetail";
    }
    
    /**
      * @Method Name : adminDeleteAjax
      * @작성일 : 2024. 2. 21.
      * @작성자 : SM.KIM
      * @Method 설명 : 관리자 / 운영자 삭제
      * @param webOprtr
      * @return
      */
    @Authority(type = MethodType.DELETE)
    @PostMapping("/admin/delete.ajax")
    @ResponseBody
    public Map<String,Object> adminDeleteAjax(@ModelAttribute MozWebOprtr webOprtr){
        Map<String, Object> result = new HashMap<>();
        try {
            userService.deleteUser(webOprtr);
            result.put("code", "1");
        } catch (Exception e){
            result.put("code", "0");
            result.put("msg", e.getMessage());
        }
        return result;
    }
    
    /**
      * @Method Name : policeModifyAjax
      * @작성일 : 2024. 2. 22.
      * @작성자 : SM.KIM
      * @Method 설명 : 경찰 수정
      * @param polInfo
      * @return
      */
    @Authority(type = MethodType.UPDATE)
    @PostMapping("/police/update.ajax")
    @ResponseBody
    public Map<String,Object> policeModifyAjax(@ModelAttribute MozPolInfo polInfo){
        Map<String, Object> result = new HashMap<>();
        try {
            userService.updatePolice(polInfo);
            result.put("code", "1");
        } catch (Exception e){
            result.put("code", "0");
            result.put("msg", e.getMessage());
        }

        return result;
    }
    
    /**
      * @Method Name : policeDeleteAjax
      * @작성일 : 2024. 2. 22.
      * @작성자 : SM.KIM
      * @Method 설명 : 경찰 삭제
      * @param polInfo
      * @return
      */
    @Authority(type = MethodType.DELETE)
    @PostMapping("/police/delete.ajax")
    @ResponseBody
    public Map<String,Object> policeDeleteAjax(@ModelAttribute MozPolInfo polInfo){
        Map<String, Object> result = new HashMap<>();
        try {
            userService.deletePolice(polInfo);
            result.put("code", "1");
        }catch (Exception e){
            result.put("code", "0");
            result.put("msg", e.getMessage());
        }
        return result;
    }
    
}
