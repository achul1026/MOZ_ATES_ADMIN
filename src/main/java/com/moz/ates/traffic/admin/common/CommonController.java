package com.moz.ates.traffic.admin.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moz.ates.traffic.admin.common.enums.MethodType;
import com.moz.ates.traffic.admin.config.Authority;
import com.moz.ates.traffic.common.component.Pagination;
import com.moz.ates.traffic.common.entity.common.ApiDriverInfoDTO;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;
import com.moz.ates.traffic.common.entity.police.MozPolInfo;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.support.exception.ErrorCode;

@Controller
@RequestMapping(value = "/common")
public class CommonController {
	
	@Autowired
	private CommonCdService commonCdService;
	
	@Authority(type = MethodType.READ)
    @GetMapping("modal/search.do")
    public String modalSearch() {
    
    	return "views/common/modalSearch";
    }
	
	/**
	 * @brief : 담당자 정보 조회
	 * @details : 담당자 정보 조회
	 * @author : KC.KIM
	 * @date : 2024.02.29
	 * @param : webOprtr
	 * @return :
	 */
	@GetMapping("/modal/oprtr/list.ajax")
	public String getModalDeptList(Model model, @ModelAttribute MozWebOprtr webOprtr) {
		int page = webOprtr.getPage();
		int totalCnt = commonCdService.getOprtrListCnt(webOprtr);
		Pagination pagination = new Pagination(totalCnt, page);
	
		webOprtr.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("webOprtr", webOprtr);
		model.addAttribute("oprtrList", commonCdService.getOprtrList(webOprtr));
		model.addAttribute("pagination", pagination);
   	return "views/common/oprtrListModel";
   }
	
	/**
	 * @brief : 담당자 정보 조회
	 * @details : 담당자 정보 조회
	 * @author : KC.KIM
	 * @date : 2024.01.29
	 * @param :
	 * @return :
	 */
	@GetMapping(value ="/modal/oprtr/search/list.ajax")
	@ResponseBody
    public CommonResponse<?> groupListAjax(@ModelAttribute MozWebOprtr webOprtr){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		int totalCnt = commonCdService.getOprtrListCnt(webOprtr);
		int page = webOprtr.getPage();
		Pagination pagination = new Pagination(totalCnt, page);
		
		webOprtr.setStart((page - 1) * pagination.getPageSize());
		List<MozWebOprtr> webOprtrList = commonCdService.getOprtrList(webOprtr);
		
    	resultMap.put("list", webOprtrList);
    	resultMap.put("pagination", pagination);
    	resultMap.put("webOprtr", webOprtr);
		return CommonResponse.successToData(resultMap,"");
    }
	
	/**
	 * @brief : 경찰관 정보 조회
	 * @details : 경찰관 정보 조회
	 * @author : KC.KIM
	 * @date : 2024.03.05
	 * @param : polInfo
	 * @return :
	 */
	@GetMapping("/modal/pol/list.ajax")
	public String getModalPolList(Model model, @ModelAttribute MozPolInfo polInfo) {
		int page = polInfo.getPage();
		int totalCnt = commonCdService.getPolListCnt(polInfo);
		Pagination pagination = new Pagination(totalCnt, page);
	
		polInfo.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("polInfo", polInfo);
		model.addAttribute("polList", commonCdService.getPolList(polInfo));
		model.addAttribute("pagination", pagination);
   	return "views/common/polListModel";
   }
	
	/**
	 * @brief : 현장 결제 모달
	 * @details : 현장 결제 모달
	 * @author : KY.LEE
	 * @date : 2024.03.20
	 * @param : 
	 * @return :
	 */
	@GetMapping("/modal/payment/onsite.ajax")
	public String getOnsitePayment(Model model , @RequestParam(name="totalPrice",required = true) Float totalPrice) {
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("placePaymentList", commonCdService.getPlacePaymentList());
		return "views/common/modalOnsitePayment";
   }
	
	/**
	 * @brief : 담당자 정보 조회
	 * @details : 담당자 정보 조회
	 * @author : KC.KIM
	 * @date : 2024.01.29
	 * @param :
	 * @return :
	 */
	@GetMapping(value ="/modal/pol/search/list.ajax")
	@ResponseBody
    public CommonResponse<?> modalPolListAjax(@ModelAttribute MozPolInfo polInfo){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		int totalCnt = commonCdService.getPolListCnt(polInfo);
		int page = polInfo.getPage();
		Pagination pagination = new Pagination(totalCnt, page);
		
		polInfo.setStart((page - 1) * pagination.getPageSize());
		List<MozPolInfo> polList = commonCdService.getPolList(polInfo);
		
    	resultMap.put("list", polList);
    	resultMap.put("pagination", pagination);
    	resultMap.put("polInfo", polInfo);
		return CommonResponse.successToData(resultMap,"");
    }
	
	/**
	 * @brief : API MODAL
	 * @details : API Violator 정보 조회
	 * @author : KY.LEE
	 * @date : 2024.01.29
	 */
	@GetMapping("/modal/{type}/apiSearch.ajax")
	public String getModalApiSearch(Model model,
			@PathVariable(value="type") String type,
			@RequestParam(name="searchContent",required=false) String searchContent ) {
		model.addAttribute("searchContent", searchContent);
		model.addAttribute("type", type);
		return "views/common/apiSearch";
   }
	
	
	//TEST
	@PostMapping("/api/searchDriver")
	@ResponseBody
    public ResponseEntity<List<Map<String,Object>>> getInformation(
    		@RequestBody ApiDriverInfoDTO apiDriverInfoDTO
    		) {
		
		if(apiDriverInfoDTO == null) {			
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		
		String searchType = (String) apiDriverInfoDTO.getSearchType();
		String searchValue = (String) apiDriverInfoDTO.getSearchValue();
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> result1 = new HashMap<String,Object>();
        result1.put("driverLicenseId", "LCE01245678");
        result1.put("driverLicenseType", "1종 보통");
        result1.put("driverNm", "홍길동");
        result1.put("driverBrth", "1980-01-01");
        result1.put("driverAddr", "서울시 강남구");
        result1.put("driverPno", "010-1234-5678");
        result1.put("driverEmail", "hong@example.com");
        result1.put("expirationDate", "2024-08-07");
        result1.put("vehicleNo", "31바 1111");
        result1.put("vehicleType", "Sonata");
        
        Map<String,Object> result2 = new HashMap<String,Object>();
        result2.put("driverLicenseId", "123");
        result2.put("driverLicenseType", "C");
        result2.put("driverNm", "링퀴링");
        result2.put("driverBrth", "1994-09-15");
        result2.put("driverAddr", "서울시 마포구");
        result2.put("driverPno", "010-9787-5678");
        result2.put("driverEmail", "ring@example.com");
        result2.put("expirationDate", "2024-10-07");
        result2.put("vehicleNo", "51바 1234");
        result2.put("vehicleType", "porche 911-spider");
        
        Map<String,Object> result3 = new HashMap<String,Object>();
        result3.put("driverLicenseId", "LCE003827374");
        result3.put("driverLicenseType", "A");
        result3.put("driverNm", "리끼리");
        result3.put("driverBrth", "1999-07-27");
        result3.put("driverAddr", "모잠비크 마푸토");
        result3.put("driverPno", "010-1114-4444");
        result3.put("driverEmail", "lee@example.com");
        result3.put("expirationDate", "2024-05-07");
        result3.put("vehicleNo", "99바 9999");
        result3.put("vehicleType", "Tico");
        
		switch (searchType) {
        case "dvrLcenId":
        	result1.put("driverLicenseId", searchValue);
        	result2.put("driverLicenseId", searchValue);
        	result3.put("driverLicenseId", searchValue);
        	resultList.add(result3);
            break;
        case "vehicleNo":
        	result1.put("vehicleNo", searchValue);
        	result2.put("vehicleNo", searchValue);
        	result3.put("vehicleNo", searchValue);
        	resultList.add(result1);
        	resultList.add(result2);
        	resultList.add(result3);
            break;
        case "driverNm":
        	result1.put("driverNm", searchValue);
        	result2.put("driverNm", searchValue);
        	result3.put("driverNm", searchValue);
        	resultList.add(result1);
        	resultList.add(result2);
        	resultList.add(result3);
            break;
        default:
        	throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
        

        return ResponseEntity.ok(resultList);
    }
}
