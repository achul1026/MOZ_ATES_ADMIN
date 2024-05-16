package com.moz.ates.traffic.admin.sitemng;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.admin.common.CommonCdService;
import com.moz.ates.traffic.admin.common.enums.MethodType;
import com.moz.ates.traffic.admin.config.Authority;
import com.moz.ates.traffic.admin.sitemng.auth.AuthService;
import com.moz.ates.traffic.admin.sitemng.authMenu.AuthMenuService;
import com.moz.ates.traffic.admin.sitemng.code.CodeService;
import com.moz.ates.traffic.admin.sitemng.log.LogService;
import com.moz.ates.traffic.admin.sitemng.menu.MenuService;
import com.moz.ates.traffic.admin.sitemng.policeNotice.PoliceNoticeService;
import com.moz.ates.traffic.common.component.Pagination;
import com.moz.ates.traffic.common.component.validate.ValidateBuilder;
import com.moz.ates.traffic.common.component.validate.ValidateChecker;
import com.moz.ates.traffic.common.component.validate.ValidateResult;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntChgHst;
import com.moz.ates.traffic.common.entity.board.MozBrd;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfHst;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpLog;
import com.moz.ates.traffic.common.entity.log.MozTfcClctnFlrLog;
import com.moz.ates.traffic.common.entity.log.MozTfcSystmErrLog;
import com.moz.ates.traffic.common.entity.log.MozTfcUserLog;
import com.moz.ates.traffic.common.entity.menu.MozMenu;
import com.moz.ates.traffic.common.entity.operator.MozAuth;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

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
	
	@Autowired
	CodeService codeService;
	
	@Autowired
	PoliceNoticeService policeNoticeService;
	
	@Autowired
	LogService logService;
	
	@Autowired
	CommonCdService commonCdService;
	
    /**
     * @brief : 메뉴관리 리스트 화면
     * @details : 메뉴관리 리스트 화면
     * @author : KY.LEE
     * @date : 2023.08.18
     * @param : model
     * @return String
     */
		@Authority(type = MethodType.READ)
    @GetMapping("/menu/list.do")
    public String menuList(Model model, @ModelAttribute MozMenu mozMenu){
    	
        model.addAttribute("mozMenuList",menuService.getMainMenuList());

        return "views/sitemng/menuMngList";
    }
    
    /**
      * @Method Name : menuInfo
      * @Date : 2024. 1. 16.
      * @Author : IK.MOON
      * @Method Brief : 메뉴 상세 정보 화면
      * @param model
      * @param menuId
      * @return
      */
		@Authority(type = MethodType.READ)
    @GetMapping("menu/detail.do")
    public String menuInfo(Model model, @RequestParam("menuId") String menuId) {
    	
    	model.addAttribute("parentMenu", menuService.getMenu(menuId));
    	model.addAttribute("subMenuList", menuService.getSubMenuList(menuId));
    	
    	return "views/sitemng/menuMngDetail";
    }
		
    /**
      * @Method Name : addMainMenu
      * @Date : 2024. 2. 5.
      * @Author : IK.MOON
      * @Method Brief : 메뉴 추가 화면
      * @return
      */
		@Authority(type = MethodType.READ)
    @GetMapping("menu/save.do")
    public String addMainMenu() {
    	return "views/sitemng/menuMngSave";
    }
    
    /**
      * @Method Name : saveMenuAjax
      * @Date : 2024. 1. 22.
      * @Author : IK.MOON
      * @Method Brief : 메뉴 추가 비동기 호출
      * @param mozMenu
      * @return
      */
		@Authority(type = MethodType.CREATE)
    @PostMapping("menu/save.ajax")
    public @ResponseBody CommonResponse<?> saveMenuAjax(MozMenu mozMenu) {
    	// TODO :: validation
    	
    	try {
				menuService.saveMenu(mozMenu);
				// 로그 등록
	    		logService.insertUserLog("SLT001", "Regist Menu", "Y");
			} catch (SQLException e) {
				// 로그 등록
				logService.insertUserLog("SLT001", "Regist Menu", "N");
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
			}
    	
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "O registo do menu foi bem sucedido.");
    }
    
    /**
      * @Method Name : updateMainMenuAjax
      * @Date : 2024. 1. 22.
      * @Author : IK.MOON
      * @Method Brief : 메뉴 업데이트 비동기 처리
      * @return
      */
		@Authority(type = MethodType.UPDATE)
    @PostMapping("menu/update.ajax")
    public @ResponseBody CommonResponse<?> updateMainMenuAjax(@RequestBody MozMenu mozMenu) {
    	// TODO :: validation
    	
    	try {
    		menuService.updateMenu(mozMenu);
    		
    		//로그 등록
    		logService.insertUserLog("SLT001", "Update Menu", "Y");
    	} catch (SQLException e) {
    		//로그 등록
    		logService.insertUserLog("SLT001", "Update Menu", "N");
    		return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
			}
    	
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "A modificação do menu foi bem sucedida.");
    }
    
    /**
      * @Method Name : deleteMenuAjax
      * @Date : 2024. 1. 22.
      * @Author : IK.MOON
      * @Method Brief : 메뉴 삭제
      * @param mozMenu
      * @return
      */
		@Authority(type = MethodType.DELETE)
    @PostMapping("menu/{menuId}/delete.ajax")
    public @ResponseBody CommonResponse<?> deleteMenuAjax(@PathVariable(name = "menuId", required = true) String menuId) {
    	MozMenu mozMenu = new MozMenu();
    	mozMenu.setMenuId(menuId);
    	
    	try {
				menuService.deleteMenu(mozMenu);
				
				//로그 등록
				logService.insertUserLog("SLT001", "Delete Menu", "Y");
			} catch (CommonException e) {
				//로그 등록
				logService.insertUserLog("SLT001", "Delete Menu", "N");
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
			}
    	
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Eliminou com êxito o menu.");
    }
    
    
    /**
     * @brief : 권한관리 리스트 화면
     * @details : 권한관리 리스트 화면
     * @author : KY.LEE
     * @date : 2023.08.18
     * @param : model
     * @return String
     */
		@Authority(type = MethodType.READ)
    @GetMapping("/auth/list.do")
    public String authList(Model model, @ModelAttribute MozAuth mozAuth){
    	int page = mozAuth.getPage();
    	int totalCnt = authService.getAuthListCnt(mozAuth);
    	Pagination pagination = new Pagination(totalCnt, page);
    	
    	mozAuth.setStart((page - 1) * pagination.getPageSize());
    	
        model.addAttribute("mozAuth", mozAuth);
        model.addAttribute("mozAuthList", authService.getAuthList(mozAuth));
        model.addAttribute("pagination", pagination);
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
		@Authority(type = MethodType.READ)
    @GetMapping("/auth/detail.do")
    public String authDetail(Model model, MozAuth mozAuth){
    		
        model.addAttribute("mozAuth",authService.getOneAuth(mozAuth));
        model.addAttribute("authDetail", authService.getAuthDetail(mozAuth));
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
		@Authority(type = MethodType.READ)
    @GetMapping("/auth/save.do")
    public String authRegist(Model model, MozAuth mozAuth){
    	model.addAttribute("menuAllList",authService.getAuthDetail(mozAuth));
    	
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
		@Authority(type = MethodType.CREATE)
    @PostMapping(value = "/auth/save.ajax", produces = "application/json")
    @ResponseBody
    public CommonResponse<?> authRegistAjax(Model model, @RequestBody MozAuth mozAuth){
    	try {
    		authService.registMozAuth(mozAuth);
    		//로그 등록
    		logService.insertUserLog("SLT002", "Regist Authority", "Y");
        } catch (CommonException e) {
        	//로그 등록
    		logService.insertUserLog("SLT002", "Regist Authority", "N");
        	return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "O registo da permissão foi bem sucedido.");
    }
    
    /**
      * @Method Name : authUpdateAjax
      * @Date : 2024. 2. 2.
      * @Author : IK.MOON
      * @Method Brief : 권한, 메뉴권한 수정
      * @param model
      * @param mozAuth
      * @return
      */
		@Authority(type = MethodType.UPDATE)
    @PostMapping(value = "/auth/update.ajax", produces = "application/json")
    @ResponseBody
    public CommonResponse<?> authUpdateAjax(Model model, @RequestBody MozAuth mozAuth) {
    	
    	try {
				authService.authUpdate(mozAuth);
				//로그 등록
				logService.insertUserLog("SLT002", "Update Authority", "Y");
			} catch (CommonException e) {
				//로그 등록
				logService.insertUserLog("SLT002", "Update Authority", "N");
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
			}
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"A modificação da permissão foi bem sucedida.");
    }
    
    /**
     * @brief : 코드 관리 리스트
     * @details : 코드 관리 리스트
     * @author : KC.KIM
     * @date : 2024.01.23
     * @param : model
     * @param : mozCmCd
     * @return String
     */
		@Authority(type = MethodType.READ)
    @GetMapping("/code/list.do")
    public String codeList(Model model, @ModelAttribute MozCmCd mozCmCd){
    	int page = mozCmCd.getPage();
    	int totalCnt = codeService.getCodeListCnt(mozCmCd);
    	Pagination pagination = new Pagination(totalCnt, page);
    	
    	mozCmCd.setStart((page - 1) * pagination.getPageSize());
    	
        model.addAttribute("mozCmCd", mozCmCd);
        model.addAttribute("mozCodeList", codeService.getCodeList(mozCmCd));
        model.addAttribute("pagination", pagination);
    	return "views/sitemng/codeList";
    }
    
    /**
     * @brief : 코드 등록 페이지 이동
     * @details : 코드 등록 페이지 이동
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : model
     * @return String
     */   
		@Authority(type = MethodType.READ)
    @GetMapping("/code/save.do")
    public String codeRegist(Model model){
    	return "views/sitemng/codeRegist";
    }
    
    /**
     * @brief : 코드 등록 
     * @details : 코드 등록 
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : model
     * @return String
     */ 
	@Authority(type = MethodType.CREATE)
    @PostMapping("/code/save.ajax")
    @ResponseBody
		public CommonResponse<?> codeSaveAjax(@ModelAttribute MozCmCd mozCmCd) {
	
			ValidateBuilder dtoBuilder = new ValidateBuilder(mozCmCd);
	
			ValidateResult dtoValidatorResult =
					dtoBuilder.addRule("cdId", new ValidateChecker().setRequired())
							.addRule("cdNm", new ValidateChecker().setRequired()).isValid();
	
			if (!dtoValidatorResult.isSuccess()) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
						dtoValidatorResult.getMessage());
			}
	
			try {
				codeService.cmCdSave(mozCmCd);
				// 로그 등록
				logService.insertUserLog("SLT003", "Regist Common Code", "Y");
			} catch (CommonException e) {
				// 로그 등록
				logService.insertUserLog("SLT003", "Regist Common Code", "N");
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "O resgate do código foi bem sucedido.");
		}
	
    /**
     * @brief : 서브 코드 등록 
     * @details : 서브 코드 등록 
     * @author : KY.LEE
     * @date : 2024.02.21
     * @param : mozCmCd
     * @return
     */ 
		@Authority(type = MethodType.CREATE)
    @PostMapping("/code/subSave.ajax")
    @ResponseBody
		public CommonResponse<?> subCodeSaveAjax(@ModelAttribute MozCmCd mozCmCd) {

			ValidateBuilder dtoBuilder = new ValidateBuilder(mozCmCd);

			ValidateResult dtoValidatorResult =
					dtoBuilder.addRule("cdId", new ValidateChecker().setRequired())
							.addRule("cdNm", new ValidateChecker().setRequired())
							.addRule("cdGroupId", new ValidateChecker().setRequired()).isValid();

			if (!dtoValidatorResult.isSuccess()) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
						dtoValidatorResult.getMessage());
			}
			try {
				codeService.cmCdSave(mozCmCd);
				// 로그 등록
				logService.insertUserLog("SLT003", "Regist Common Code", "Y");
			} catch (CommonException e) {
				// 로그 등록
				logService.insertUserLog("SLT003", "Regist Common Code", "N");

				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "O resgate do código foi bem sucedido.");
		}
    
    /**
     * @brief : 코드 상세 정보 조회 
     * @details : 코드 상세 정보 조회 
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : model
     * @param : cdId
     * @return String
     */ 
		@Authority(type = MethodType.READ)
    @GetMapping("/code/detail.do")
    public String codeDetail(Model model, @RequestParam("cdId") String cdId){
    	MozCmCd mozCmCd = codeService.getCodeDetail(cdId);
    	List<MozCmCd> subMozCmCdList = codeService.getSubCodeDetail(mozCmCd.getCdId());
		model.addAttribute("mozCmCd", mozCmCd);
		model.addAttribute("subMozCmCdList", subMozCmCdList);
        return "views/sitemng/codeDetail";
    }
    
    /**
     * @brief : 코드 상세 정보 수정 페이지 이동
     * @details : 코드 상세 정보 수정 페이지 이동
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : model
     * @param : cdId
     * @return String
     */ 
	@Authority(type = MethodType.READ)
    @GetMapping("/code/update.do")
    public String codeModify(Model model, @RequestParam("cdId") String cdId) {
    	MozCmCd mozCmCd = codeService.getCodeDetail(cdId);
    	List<MozCmCd> subMozCmCdList = codeService.getSubCodeDetail(mozCmCd.getCdId());
		model.addAttribute("mozCmCd", mozCmCd);
		model.addAttribute("subMozCmCdList", subMozCmCdList);
    	return "views/sitemng/codeModify";
    }
    
    /**
     * @brief : 코드 상세 정보 수정 
     * @details : 코드 상세 정보 수정
     * @author : KC.KIM
     * @date : 2024.01.31
     * @param : mozCmCd
     * @return
     */ 
		@Authority(type = MethodType.UPDATE)
    @PostMapping("/code/update.ajax")
    @ResponseBody
    public CommonResponse<?> codeModifyAjax(Model model, @ModelAttribute MozCmCd mozCmCd) {
    	ValidateBuilder dtoBuilder = new ValidateBuilder(mozCmCd);
    	ValidateResult dtoValidatorResult = dtoBuilder
    			.addRule("cdId", new ValidateChecker().setRequired())
    			.addRule("cdNm", new ValidateChecker().setRequired())
    			.addRule("cdIdOg", new ValidateChecker().setRequired())
    			.isValid();
    	
    	if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
					dtoValidatorResult.getMessage());
		}
    	try {
			codeService.cmCdModify(mozCmCd);
			//로그 등록
			logService.insertUserLog("SLT003", "Update Common Code", "Y");
		} catch (CommonException e) {
			//로그 등록
			logService.insertUserLog("SLT003", "Update Common Code", "N");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "A modificação do código foi bem sucedida.");
    }
    
    /**
     * @brief : 코드 정보 삭제 
     * @details : 코드 정보 삭제 
     * @author : KC.KIM
     * @date : 2024.02.01
     * @param : cdId
     * @return
     */ 
		@Authority(type = MethodType.DELETE)
    @PostMapping("/code/delete.ajax")
    @ResponseBody
    public CommonResponse<?> codeDeleteAjax(@RequestParam("cdId") String cdId){
    	MozCmCd mozCmCd = new MozCmCd();
    	mozCmCd.setCdId(cdId);
    	
    	ValidateBuilder dtoBuilder = new ValidateBuilder(mozCmCd);
    	ValidateResult dtoValidatorResult = dtoBuilder.addRule("cdId", new ValidateChecker().setRequired()).isValid();
    	
    	if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
					dtoValidatorResult.getMessage());
		}
    	
    	try {
			codeService.cmCdDelete(cdId);
			//로그 등록
			logService.insertUserLog("SLT003", "Delete Common Code", "Y");
		} catch (CommonException e) {
			//로그 등록
			logService.insertUserLog("SLT003", "Delete Common Code", "N");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "A eliminação do código foi bem sucedida.");
    }
		
		
	    
    /**
     * @brief : 서브 코드 삭제
     * @details : 서브 코드 삭제
     * @author : KY.LEE
     * @date : 2024.02.21
     * @param : cdId , cdGroupId
     * @return
     */ 
		@Authority(type = MethodType.DELETE)
    @PostMapping("/code/subDelete.ajax")
    @ResponseBody
    public CommonResponse<?> subCodeDeleteAjax(@RequestParam(name = "cdId", required = true) String cdId , @RequestParam(name = "cdGroupId", required = true) String cdGroupId){
    	MozCmCd mozCmCd = new MozCmCd();
    	mozCmCd.setCdId(cdId);
    	mozCmCd.setCdGroupId(cdGroupId);
    	
    	ValidateBuilder dtoBuilder = new ValidateBuilder(mozCmCd);
    	ValidateResult dtoValidatorResult = dtoBuilder.addRule("cdId", new ValidateChecker().setRequired())
    												  .addRule("cdGroupId", new ValidateChecker().setRequired()).isValid();
    	
    	if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,dtoValidatorResult.getMessage());
		}
    	
    	try {
			codeService.subCmCdDelete(mozCmCd);
		} catch (CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Subcódigo eliminado com êxito.");
    }
    
    /**
     * @brief : 로그 관리 리스트
     * @details : 로그 관리 리스트
     * @author : KC.KIM
     * @date : 2024.01.23
     * @param : model
     * @return String
     */
		@Authority(type = MethodType.READ)
    @GetMapping("/log/list.do")
    public String logList(Model model, 
    		@RequestParam(name="logType", required = false) String logType
    		, @RequestParam(name="searchType", required = false) String searchType
    		, @RequestParam(name="sDate", required = false) String sDate
    		, @RequestParam(name="eDate", required = false) String eDate
    		, @RequestParam(name="searchTxt", required = false) String searchTxt
    		, @RequestParam(name="page", required = false, defaultValue = "1") String page){
    	switch (!MozatesCommonUtils.isNull(logType) ? logType : "enf") {
    	case "enf":
    		logType = "enf";
    		break;
    	case "acdnt":
    		logType = "acdnt";
    		break;
    	case "eqp":
    		logType = "eqp";
    		break;
    	case "user":
    		logType = "user";
    		break;
		case "sysFail":	// 시스템 장애
			logType = "sysFail";
			break;
		case "colFail":	// 수집 실패
			logType = "colFail";
			break;
		default:	// 사용자
			logType = "user";
			break;
		}
	    
    	model.addAttribute("logType", logType);
    	model.addAttribute("searchType", searchType);
    	model.addAttribute("sDate", sDate);
    	model.addAttribute("eDate", eDate);
    	model.addAttribute("searchTxt", searchTxt);
    	model.addAttribute("page", page);
    	
        return "views/sitemng/logList";
    }

	/**
	 * @Method Name : enfLogListAjax
	 * @Date : 2024. 2. 6.
	 * @Author : IK.MOON
	 * @Method Brief : 교통 단속 로그 목록 비동기호출
	 * @return
	 */
	@Authority(type = MethodType.READ)
	@PostMapping("/log/enf/list.ajax")
	public String enfLogListAjax(Model model, @ModelAttribute MozTfcEnfHst tfcEnfHst) {
		int page = tfcEnfHst.getPage();
		int totalCnt = logService.getTfcLogListCnt(tfcEnfHst);
		Pagination pagination = new Pagination(totalCnt, page);

		tfcEnfHst.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("tfcEnfHst", tfcEnfHst);
		model.addAttribute("logLists", logService.getTfcLogList(tfcEnfHst));
		model.addAttribute("pagination", pagination);
		return "views/sitemng/logEnfAjax";
	}
	
	/**
	 * @Method Name : acdntLogListAjax
	 * @Date : 2024. 2. 6.
	 * @Author : IK.MOON
	 * @Method Brief : 교통 사고 로그 목록 비동기호출
	 * @return
	 */
	@Authority(type = MethodType.READ)
	@PostMapping("/log/acdnt/list.ajax")
	public String acdntLogListAjax(Model model, @ModelAttribute MozTfcAcdntChgHst tfcAcdntChgHst) {
		int page = tfcAcdntChgHst.getPage();
		int totalCnt = logService.getAcdntLogListCnt(tfcAcdntChgHst);
		Pagination pagination = new Pagination(totalCnt, page);
		
		tfcAcdntChgHst.setStart((page - 1) * pagination.getPageSize());
		
		model.addAttribute("tfcAcdntChgHst", tfcAcdntChgHst);
		model.addAttribute("acdntLogList", logService.getAcdntLogList(tfcAcdntChgHst));
		model.addAttribute("pagination", pagination);
		return "views/sitemng/logAcdntAjax";
	}
	
	/**
	 * @Method Name : enfLogListAjax
	 * @Date : 2024. 2. 6.
	 * @Author : IK.MOON
	 * @Method Brief : 교통 단속 목록 비동기호출
	 * @return
	 */
	@Authority(type = MethodType.READ)
	@PostMapping("/log/eqp/list.ajax")
	public String eqpLogListAjax(Model model, @ModelAttribute MozTfcEnfEqpLog tfcEnfEqpLog) {
		int page = tfcEnfEqpLog.getPage();
		int totalCnt = logService.getEqpLogListCnt(tfcEnfEqpLog);
		Pagination pagination = new Pagination(totalCnt, page);
		
		tfcEnfEqpLog.setStart((page - 1) * pagination.getPageSize());
		
		model.addAttribute("tfcEnfEqpLog", tfcEnfEqpLog);
		model.addAttribute("eqpLogList", logService.getEqpLogList(tfcEnfEqpLog));
		model.addAttribute("pagination", pagination);
		return "views/sitemng/logEqpAjax";
	}
		
    /**
      * @Method Name : userLogListAjax
      * @Date : 2024. 2. 6.
      * @Author : IK.MOON
      * @Method Brief : 유저 로그 리스트 비동기 호출
      * @return
      */
	@Authority(type = MethodType.READ)
    @PostMapping("/log/user/list.ajax")
    public String userLogListAjax(Model model, @ModelAttribute MozTfcUserLog mozTfcUserLog) {
    	int page = mozTfcUserLog.getPage();
    	int totalCnt = logService.getUserLogListCnt(mozTfcUserLog);
    	Pagination pagination = new Pagination(totalCnt, page);
    	
    	mozTfcUserLog.setStart((page - 1) * pagination.getPageSize());
    	
    	model.addAttribute("mozTfcUserLog", mozTfcUserLog);
    	model.addAttribute("userLogList", logService.getUserLogList(mozTfcUserLog));
    	model.addAttribute("pagination", pagination);
    	return "views/sitemng/logListUserAjax";
    }
    
    /**
      * @Method Name : sysFailLogListAjax
      * @Date : 2024. 2. 6.
      * @Author : IK.MOON
      * @Method Brief : 시스템 장애 로그 비동기 호출
      * @return
      */
		@Authority(type = MethodType.READ)
    @PostMapping("/log/sysFail/list.ajax")
    public String sysFailLogListAjax(Model model, @ModelAttribute MozTfcSystmErrLog mozTfcSystmErrLog) {
    	int page = mozTfcSystmErrLog.getPage();
    	int totalCnt = logService.getSystmErrLogListCnt(mozTfcSystmErrLog);
    	Pagination pagination = new Pagination(totalCnt, page);
    	
    	mozTfcSystmErrLog.setStart((page - 1) * pagination.getPageSize());
    	
        model.addAttribute("mozTfcSystmErrLog", mozTfcSystmErrLog);
        model.addAttribute("systmErrLogList", logService.getSystmErrLogList(mozTfcSystmErrLog));
        model.addAttribute("pagination", pagination);
    	return "views/sitemng/logListSysFailAjax";
    }
    
    /**
     * @brief : 수집실패 로그 비동기 호출
     * @details : 수집실패 로그 비동기 호출
     * @author : KC.KIM
     * @date : 2024.02.16
     * @param : model
     * @return String
     */
		@Authority(type = MethodType.READ)
    @PostMapping("/log/colFail/list.ajax")
    public String colFailLogListAjax(Model model, @ModelAttribute MozTfcClctnFlrLog mozTfcClctnFlrLog) {
    	int page = mozTfcClctnFlrLog.getPage();
    	int totalCnt = logService.getClctnFlrLogListCnt(mozTfcClctnFlrLog);
    	Pagination pagination = new Pagination(totalCnt, page);
    	
    	mozTfcClctnFlrLog.setStart((page - 1) * pagination.getPageSize());
    	
        model.addAttribute("mozTfcClctnFlrLog", mozTfcClctnFlrLog);
        model.addAttribute("clctnFlrLogList", logService.getClctnFlrLogList(mozTfcClctnFlrLog));
        model.addAttribute("pagination", pagination);
    	return "views/sitemng/logListColFailAjax";
    }
    
    /**
      * @Method Name : logDetail
      * @Date : 2024. 2. 6.
      * @Author : IK.MOON
      * @Method Brief : 로그 상세 화면
      * @param model
      * @param mozCmCd
      * @return
      */
		@Authority(type = MethodType.READ)
    @GetMapping("/log/detail.do")
    public String logDetail(Model model
    		, @RequestParam(name="logType", required = true) String logType
    		, @RequestParam(name="logId", required = true) String logId){
			
    	switch (!MozatesCommonUtils.isNull(logType) ? logType : "enf") {
    	case "enf":
    		model.addAttribute("logType", "enf");
    		model.addAttribute("tfcEnfHst", logService.getTfcLogDetail(logId));
    		return "views/sitemng/logEnfDetail";
    	case "acdnt":
    		model.addAttribute("logType", "acdnt");
    		model.addAttribute("tfcAcdntChgHst", logService.getAcdntLogDetail(logId));
    		return "views/sitemng/logAcdntDetail";
    	case "eqp":
    		model.addAttribute("logType", "eqp");
    		model.addAttribute("logInfo", logService.getEqpLogDetail(logId));
    		return "views/sitemng/logEqpDetail";
    	case "user":
    		logType = "user";
    		model.addAttribute("logDetail", logService.getUserLogDetail(logId));
    		break;
		case "sysFail":	// 시스템 장애
			logType = "sysFail";
			model.addAttribute("logDetail", logService.getSystmErrLogDetail(logId));
			break;
		case "colFail":	// 수집 실패
			logType = "colFail";
			model.addAttribute("logDetail", logService.getClctnFlrLog(logId));
			break;
		default:	// 사용자
			logType = "enf";
			model.addAttribute("logDetail", logService.getTfcLogDetail(logId));
			break;
		}
    	
    	model.addAttribute("logType", logType);
        return "views/sitemng/logDetail";
    }
	
	/**
	 * @brief : 경찰 공지사항 리스트 화면
	 * @details : 경찰 공지사항 리스트 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : brd
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/polNtc/list.do")
	public String noticeList(Model model, @ModelAttribute MozBrd brd) {
		int page = brd.getPage();
		int totalCnt = policeNoticeService.getNoticeListCnt(brd);
		Pagination pagination = new Pagination(totalCnt, page);

		brd.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("brd", brd);
		model.addAttribute("noticeList", policeNoticeService.getNoticeList(brd));
		model.addAttribute("pagination", pagination);
		return "views/sitemng/polNtcList";
	}

		/**
		 * @brief : 경찰 공지사항 등록 화면
		 * @details : 경찰 공지사항 등록 화면
		 * @author : KC.KIM
		 * @date : 2023.08.04
		 * @param :
		 * @return :
		 */
		@Authority(type = MethodType.READ)
		@GetMapping("/polNtc/save.do")
		public String noticeRegist(Model model) {

			List<MozCmCd> cdList = commonCdService.getCdList("ntc");
			model.addAttribute("cdList", cdList);

			return "views/sitemng/polNtcRegist";
		}

		/**
		 * @brief : 경찰 공지사항 등록
		 * @details : 경찰 공지사항 등록
		 * @author : KC.KIM
		 * @date : 2023.08.04
		 * @param : brd
		 * @return :
		 */
		@Authority(type = MethodType.CREATE)
		@PostMapping("/polNtc/save.ajax")
		public @ResponseBody CommonResponse<?> noticeRegistAjax(MozBrd brd
				, @RequestPart(required = false) MultipartFile[] uploadFiles) {

			ValidateBuilder dtoValidator = new ValidateBuilder(brd);

			ValidateResult dtoValidatorResult = dtoValidator
					.addRule("boardTitle", new ValidateChecker().setRequired().setMaxLength(200, "O título não pode ter mais de 200 caracteres."))
					.addRule("imprtYn", new ValidateChecker().setRequired())
					.addRule("useYn", new ValidateChecker().setRequired())
					.addRule("popupYn", new ValidateChecker().setRequired())
					.addRule("boardContents", new ValidateChecker().setMaxLength(200, "O conteúdo não pode ter mais de 200 caracteres."))
					.isValid();
			
			if (!dtoValidatorResult.isSuccess()) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
						dtoValidatorResult.getMessage());
			}
			
			try {
				policeNoticeService.registNotice(brd, uploadFiles);
			} catch (Exception e) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
			}

			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "O presente aviso foi registado.");
		}

		/**
		 * @brief : 경찰 공지사항 삭제
		 * @details : 경찰 공지사항 삭제
		 * @author : KC.KIM
		 * @date : 2023.08.04
		 * @param : boardIdx
		 * @return :
		 */
		@Authority(type = MethodType.DELETE)
		@PostMapping("/polNtc/delete.ajax")
		@ResponseBody
		public CommonResponse<?> noticeDeleteAjax(@RequestParam("boardIdx") String boardIdx) {
			MozBrd mozFaq = new MozBrd();
			mozFaq.setBoardIdx(boardIdx);

			ValidateBuilder dtoValidator = new ValidateBuilder(mozFaq);

			ValidateResult dtoValidatorResult =
					dtoValidator.addRule("boardIdx", new ValidateChecker().setRequired()).isValid();

			if (!dtoValidatorResult.isSuccess()) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
						dtoValidatorResult.getMessage());
			}

			try {
				policeNoticeService.deleteNotice(boardIdx);
			} catch (Exception e) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
			}

			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Este aviso foi suprimido.");
		}

		/**
		 * @brief : 경찰 공지사항 상세 화면
		 * @details : 경찰 공지사항 상세 화면
		 * @author : KC.KIM
		 * @date : 2023.08.04
		 * @param : boardIdx
		 * @return :
		 */
		@Authority(type = MethodType.READ)
		@GetMapping("/polNtc/detail.do")
		public String noticeDetail(Model model, @RequestParam("boardIdx") String boardIdx) {

			MozBrd brd = policeNoticeService.getNoticeDetail(boardIdx);
			model.addAttribute("brd", brd);
			return "views/sitemng/polNtcDetail";
		}

		/**
		 * @brief : 경찰 공지사항 수정 화면
		 * @details : 경찰 공지사항 수정 화면
		 * @author : KC.KIM
		 * @date : 2023.08.04
		 * @param : boardIdx
		 * @return :
		 */
		@Authority(type = MethodType.READ)
		@GetMapping("/polNtc/update.do")
		public String noticeModify(Model model, @RequestParam("boardIdx") String boardIdx) {
			MozBrd brd = policeNoticeService.getNoticeDetail(boardIdx);
			model.addAttribute("brd", brd);
			
			return "views/sitemng/polNtcModify";
		}

		/**
		 * @brief : 경찰 공지사항 수정
		 * @details : 경찰 공지사항 수정
		 * @author : KC.KIM
		 * @date : 2023.08.04
		 * @param : brd
		 * @param : uploadFiles
		 * @return :
		 */
		@Authority(type = MethodType.UPDATE)
		@PostMapping("/polNtc/update.ajax")
		@ResponseBody
		public CommonResponse<?> noticeModifyAjax(@ModelAttribute MozBrd brd
				, @RequestPart(required = false) MultipartFile[] uploadFiles) {
			ValidateBuilder dtoValidator = new ValidateBuilder(brd);
			ValidateResult dtoValidatorResult = dtoValidator
					.addRule("boardTitle", new ValidateChecker().setRequired().setMaxLength(200, "O título não pode ter mais de 200 caracteres."))
					.addRule("imprtYn", new ValidateChecker().setRequired())
					.addRule("useYn", new ValidateChecker().setRequired())
					.addRule("popupYn", new ValidateChecker().setRequired())
					.addRule("boardContents", new ValidateChecker().setMaxLength(200, "O conteúdo não pode ter mais de 200 caracteres."))
					.isValid();

			if (!dtoValidatorResult.isSuccess()) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
			}

			try {
				policeNoticeService.updateNotice(brd, uploadFiles);
			} catch (Exception e) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
			}

			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "O presente aviso foi alterado.");
		}
}
