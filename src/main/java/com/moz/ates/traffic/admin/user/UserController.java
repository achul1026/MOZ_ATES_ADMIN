package com.moz.ates.traffic.admin.user;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

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

import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;
import com.moz.ates.traffic.common.entity.police.MozPolInfo;

@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    //TODO 추후 삭제
    @RequestMapping(value = "test")
    public String test(Model model){

        return "views/user/table_new";
    }
    
    //TODO 추후 삭제
    @RequestMapping(value = "test2")
    public String test2(Model model){

        return "views/user/userDetail";
    }
    
    //TODO 추후 삭제
    @RequestMapping(value = "test3")
    public String test3(Model model){

        return "views/user/userDetail2";
    }
    
    
    
    /**
     * @brief : 관리자 등록 화면
     * @details : 관리자 등록 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : 
     * @return : 
     */
    @GetMapping(value = "/user/save.do")
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
    @PostMapping(value = "/user/save.ajax")
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
                webOprtr.setCrtr(principal.getName());
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
    @GetMapping("/user/list.do")
    public String userList(Model model,@ModelAttribute MozWebOprtr webOprtr){

        model.addAttribute("webOprtr",webOprtr);
        model.addAttribute("webOprtrList", userService.getWebOprtrList(webOprtr));
        model.addAttribute("totalCnt", userService.getWebOprtrListCnt(webOprtr));
        return "views/user/userList";
    }

//    /**
//     * @brief : 관리자 리스트 조회
//     * @details : 관리자 리스트 조회
//     * @author : KC.KIM
//     * @date : 2023.08.04
//     * @param : webOprtr
//     * @return : DataTableVO
//     */
//    @PostMapping(value = "userListAjax")
//    @ResponseBody
//    public DataTableVO userListAjax(@ModelAttribute MozWebOprtr webOprtr){
//
//
//        return userService.getUserListDatatable(webOprtr);
//    }

    /**
     * @brief : 관리자 상세 조회
     * @details : 관리자 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : oprtrId
     * @return : 
     */
    @GetMapping("/user/detail.do")
    public String userDetail(Model model, @RequestParam("oprtrId")String oprtrId){

        MozWebOprtr webOprtr = userService.getUserDetail(oprtrId);
        model.addAttribute("webOprtr", webOprtr);

        return "views/user/userDetail_back";
    }
    
    /**
     * @brief : 관리자 수정 화면
     * @details : 관리자 수정 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : oprtrId
     * @return : 
     */
    @GetMapping("/user/update.do")
    public String mngModify(Model model, @RequestParam("oprtrId")String oprtrId){

    	MozWebOprtr webOprtr = userService.getUserDetail(oprtrId);
        model.addAttribute("webOprtr", webOprtr);

        return "views/user/userModify";
    }
    
    /**
     * @brief : 관리자 수정
     * @details : 관리자 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : webOprtr
     * @return : 
     */
    @PostMapping("/user/update.ajax")
    @ResponseBody
    public Map<String,Object> userModifyAjax(@ModelAttribute MozWebOprtr webOprtr){
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
     * @brief : 사용자 리스트 화면
     * @details : 사용자 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.18
     * @param : polInfo
     * @return : 
     */
	@GetMapping("/pol/list.do")
	public String policeList(Model model, @ModelAttribute MozPolInfo polInfo) {
		
		model.addAttribute("polInfo", polInfo);
		model.addAttribute("polList", userService.getPolInfoList(polInfo));
		model.addAttribute("totalCnt", userService.getPolInfoListCnt(polInfo));		
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
    @GetMapping("/pol/detail.do")
    public String polMngDetail(Model model, @RequestParam("polLcenId")String polLcenId){

        MozPolInfo polInfo = userService.getPolInfoDetail(polLcenId);
        model.addAttribute("polInfo", polInfo);

        return "views/user/polDetail";
    }
}
