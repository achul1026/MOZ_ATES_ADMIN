package com.moz.ates.traffic.admin.sitemng;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moz.ates.traffic.admin.sitemng.auth.AuthService;
import com.moz.ates.traffic.admin.sitemng.authMenu.AuthMenuService;
import com.moz.ates.traffic.admin.sitemng.menu.MenuService;
import com.moz.ates.traffic.common.entity.menu.MozMenu;
import com.moz.ates.traffic.common.entity.operator.MozAuth;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "siteMng")
@Slf4j
public class SiteMngController {

	@Autowired
	MenuService menuService;

	@Autowired
	AuthService authService;
	
	@Autowired
	AuthMenuService authMenuService;
	
    /**
     * @brief : 메뉴관리 리스트 화면
     * @details : 메뉴관리 리스트 화면
     * @author : KY.LEE
     * @date : 2023.08.18
     * @param : model
     * @return String
     */
    @GetMapping("/menu/list.do")
    public String menuList(Model model, @ModelAttribute MozMenu mozMenu){
        model.addAttribute("mozMenu",mozMenu);

        return "views/sitemng/menuMngList";
    }
    
    /**
     * @brief : 권한관리 리스트 화면
     * @details : 권한관리 리스트 화면
     * @author : KY.LEE
     * @date : 2023.08.18
     * @param : model
     * @return String
     */
    @GetMapping("/auth/list.do")
    public String authList(Model model, @ModelAttribute MozAuth mozAuth){
    	List<MozAuth> mozAuthList = authService.getAuthList(mozAuth);
    	
        model.addAttribute("mozAuth", mozAuth);
        model.addAttribute("mozAuthList", mozAuthList);

        return "views/sitemng/authMngList";
    }
    
    /**
     * @brief : 권한관리 상세 화면
     * @details : 권한관리 상세 화면
     * @author : KY.LEE
     * @date : 2023.08.18
     * @param : model
     * @return String
     */
    @GetMapping("/auth/detail.do")
    public String authDetail(Model model, @ModelAttribute MozAuth mozAuth){
    	
        model.addAttribute("mozAuth",mozAuth);
        return "views/sitemng/authDetail";
    }
    
    /**
     * @brief : 권한관리 등록 화면
     * @details : 권한관리 등록 화면
     * @author : KY.LEE
     * @date : 2023.08.18
     * @param : model
     * @return String
     */
    @GetMapping("/auth/save.do")
    public String authRegist(Model model, @ModelAttribute MozAuth mozAuth){
    	model.addAttribute("menuAllList",menuService.getMenuList());
    	model.addAttribute("mozAuth",mozAuth);

    	return "views/sitemng/authRegist";
    }
    
    /**
     * @brief : 권한관리 등록
     * @details : 권한관리 등록
     * @author : KY.LEE
     * @date : 2023.08.18
     * @param : model
     * @return String
     */
    @PostMapping(value = "/auth/save.ajax", produces = "application/json")
    @ResponseBody
    public Map<String,Object> authRegistAjax(Model model, @RequestBody Map<String,Object> paramMap){
        Map<String, Object> result = new HashMap<>();
        String code = "200";
        String message = "권한 등록에 성공했습니다.";
        
        try {
        	MozAuth mozAuth = new MozAuth();
        	String authCd = (String) paramMap.get("authCd");
        	String authNm = (String) paramMap.get("authNm");
        	mozAuth.setAuthCd(authCd);
        	mozAuth.setAuthNm(authNm);
        	String authId = authService.registMozAuth(mozAuth);
        	
        	if(paramMap.get("authMenuList") != null) {
        		List<Map<String,Object>> authMenuList = (List<Map<String, Object>>) paramMap.get("authMenuList");
        		authMenuService.registAuthMenu(authMenuList, authId);
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        	code = "400";
        	message = "권한 등록 중 오류가 발생하였습니다.";
        }
        
        
        result.put("code", code);
        result.put("message", message);
    	return result;
    }
    
    
    

}
