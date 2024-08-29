package com.moz.ates.traffic.admin.administmng;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.moz.ates.traffic.admin.common.CommonCdService;
import com.moz.ates.traffic.admin.common.enums.MethodType;
import com.moz.ates.traffic.admin.config.Authority;
import com.moz.ates.traffic.common.component.Pagination;
import com.moz.ates.traffic.common.entity.administrative.MozAdministDip;
import com.moz.ates.traffic.common.entity.administrative.MozCourtDcsn;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.law.MozTfcLwFineInfo;
import com.moz.ates.traffic.common.entity.law.MozTfcLwInfo;
import com.moz.ates.traffic.common.repository.administrative.MozAdministDipRepository;
import com.moz.ates.traffic.common.repository.administrative.MozCourtDcsnRepository;
import com.moz.ates.traffic.common.support.exception.CommonException;

@Controller
@RequestMapping(value = "administCourt")
public class AdministController {
	
	@Autowired
	CommonCdService commonCdService;
	
	@Autowired
	AdministService administService;
	
	@Autowired
	MozAdministDipRepository mozAdministDipRepository;
	
	@Autowired
	MozCourtDcsnRepository mozCourtDcsnRepository;
	
	
    /**
     * @brief : 법원이송 리스트 화면
     * @details : 법원이송 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : administDip, model
     * @return : 
     */
	@Authority(type = MethodType.READ)
    @GetMapping(value = "/mng/list.do")
    public String noticeList(Model model, @ModelAttribute MozAdministDip administDip){
    	int page = administDip.getPage();
    	int totalCnt = administService.getAdministListCnt(administDip);
    	Pagination pagination = new Pagination(totalCnt, page);
    	
    	administDip.setStart((page - 1) * pagination.getPageSize());
    	
    	model.addAttribute("administDip", administDip);
    	model.addAttribute("administList", administService.getAdministList(administDip));
    	model.addAttribute("pagination", pagination);
        return "views/administmng/administMngList";
    }
	
	/**
     * @brief : 법원이송 리스트 엑셀 다운로드
     * @details : 법원이송 리스트 엑셀 다운로드
     * @author : KC.KIM
     * @date : 2024.04.25
     * @param : resp
     * @param : administDip
     * @return : 
	 * @throws IOException 
     */
	@Authority(type = MethodType.READ)
	@GetMapping("/mng/download.do")
	public void excelDownload(HttpServletResponse resp, MozAdministDip administDip) throws IOException {
		administService.excelDownload(resp, administDip);
	}
    
    /**
      * @Method Name : administMngDetail
      * @Date : 2024. 1. 17.
      * @Author : IK.MOON
      * @Method Brief : 법원이송 상세 화면
      * @param model
      * @return
      */
	@Authority(type = MethodType.READ)
    @GetMapping(value = "/mng/detail.do")
    public String administMngDetail(Model model, @RequestParam("administDipId")String administDipId){
		
		MozAdministDip administDipInfo = mozAdministDipRepository.findOneByAdministDipId(administDipId);
		MozCourtDcsn courtDcsnInfo = mozCourtDcsnRepository.findOneByAdministDipId(administDipId);
		
		model.addAttribute("administDipInfo", administDipInfo);
		model.addAttribute("courtDcsnInfo", courtDcsnInfo);
		
    	return "views/administmng/administMngDetail";
    }
	
	/**
	 * @Method Name : administMngSavePage
	 * @Date : 2024. 1. 17.
	 * @Author : IK.MOON
	 * @Method Brief : 법원이송 > 판결 등록 화면
	 * @param model
	 * @return
	 */
	@Authority(type = MethodType.CREATE)
	@GetMapping(value = "/mng/save.do")
	public String administMngSavePage(Model model, @RequestParam("administDipId")String administDipId){
		
		MozAdministDip administDipInfo = mozAdministDipRepository.findOneByAdministDipId(administDipId);
		
		model.addAttribute("administDipInfo", administDipInfo);
		
		return "views/administmng/administMngRegist";
	}
	
	/**
	 * @Method Name : administMngSave
	 * @Date : 2024. 1. 17.
	 * @Author : IK.MOON
	 * @Method Brief : 판결 등록
	 * @param model
	 * @return
	 */
	@Authority(type = MethodType.CREATE)
    @PostMapping("/mng/save.ajax")
    public @ResponseBody CommonResponse<?> administMngSave(MozCourtDcsn mozCourtDcsn){
		administService.saveCourtDcsn(mozCourtDcsn);
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"As informações foram guardadas na sequência de uma decisão judicial.");
    }
	/**
	 * @Method Name : administMngUpdatePage
	 * @Date : 2024. 1. 17.
	 * @Author : IK.MOON
	 * @Method Brief : 법원이송 상세 화면
	 * @param model
	 * @return
	 */
	@Authority(type = MethodType.UPDATE)
	@GetMapping(value = "/mng/update.do")
	public String administMngUpdatePage(Model model, @RequestParam("administDipId")String administDipId){
		
		MozAdministDip administDipInfo = mozAdministDipRepository.findOneByAdministDipId(administDipId);
		MozCourtDcsn courtDcsnInfo = mozCourtDcsnRepository.findOneByAdministDipId(administDipId);
		
		model.addAttribute("administDipInfo", administDipInfo);
		model.addAttribute("courtDcsnInfo", courtDcsnInfo);
		
		return "views/administmng/administMngModify";
	}
	
	/**
	 * @Method Name : administMngUpdate
	 * @Date : 2024. 1. 17.
	 * @Author : IK.MOON
	 * @Method Brief : 판결 수정
	 * @param model
	 * @return
	 */
	@Authority(type = MethodType.UPDATE)
    @PostMapping("/mng/update.ajax")
    public @ResponseBody CommonResponse<?> administMngUpdate(MozCourtDcsn mozCourtDcsn){
		administService.updateCourtDcsn(mozCourtDcsn);
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"As informações foram corrigidas na sequência de uma decisão judicial.");
    }
	
	/**
	 * @brief : 교통단속 법률관리 리스트 화면
	 * @details : 교통단속 법률관리 리스트 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLwInfo
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/law/list.do")
	public String lawList(Model model, @ModelAttribute MozTfcLwInfo tfcLwInfo) {
		int page = tfcLwInfo.getPage();
		int totalCnt = administService.getLawListCnt(tfcLwInfo);
		Pagination pagination = new Pagination(totalCnt, page);

		tfcLwInfo.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("tfcLwInfo", tfcLwInfo);
		model.addAttribute("lawLists", administService.getLawList(tfcLwInfo));
		model.addAttribute("pagination", pagination);

		return "views/administmng/lawList";
	}

	/**
	 * @brief : 교통단속 법률관리 등록 화면
	 * @details : 교통단속 법률관리 등록 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/law/save.do")
	public String lawRegist(Model model) {
		model.addAttribute("lawTypeCd", commonCdService.getCdList("LAW_TYPE_CD"));
		model.addAttribute("cntrvTyCd", commonCdService.getCdList("CONTRAVENTION_TYPE_CODE"));
		model.addAttribute("addlPnCd", commonCdService.getCdList("ADDITIONAL_PENALTY_CODE"));
		model.addAttribute("trnTyCd", commonCdService.getCdList("TRANSGRESSION_TYPE_CODE"));
		return "views/administmng/lawRegist";
	}

	/**
	 * @brief : 교통단속 법률관리 등록
	 * @details : 교통단속 법률관리 등록
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLwInfo
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping(value = "/law/save.ajax")
	@ResponseBody
	public CommonResponse<?> lawSaveAjax(@RequestBody MozTfcLwInfo tfcLwInfo) {
		if (administService.checkDuplicateByArtclNo(tfcLwInfo)) {
			// número do artigo가 이미 존재합니다
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "O número do artigo já existe");
		}
		
		if (administService.checkDuplicateByTfcLwFineIdList(tfcLwInfo.getMozTfcLwFineInfoArr())) {
			// 범칙금 ID가 이미 존재합니다.
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "O ID da multa já existe");
		}
		
		try {
			administService.lawSave(tfcLwInfo);
		} catch (CommonException e) {
			// 성공적으로 등록되지 않았습니다
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Não foi registrado com sucesso.");
		}
		// 성공적으로 등록되었습니다.
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Você foi registrado com sucesso.");
	}

	/**
	 * @brief : 법률 추가 개정등록
	 * @details : 법률 추가 개정 등록
	 * @author : KY.LEE
	 * @date : 2024.02.24
	 * @param : tfcLwInfo
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping(value = "/law/revise.ajax")
	@ResponseBody
	public CommonResponse<?> lawReviseAjax(@RequestBody MozTfcLwInfo tfcLwInfo) {
		try {
			administService.lawRevise(tfcLwInfo);
		} catch (CommonException e) {
			// 성공적으로 등록되지 않았습니다
			CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Não foi registrado com sucesso.");
		}
		// 성공적으로 등록되었습니다.
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Você foi registrado com sucesso.");
	}

	/**
	 * @brief : 범칙금 추가
	 * @details : 범칙금 추가
	 * @author : KY.LEE
	 * @date : 2024.02.24
	 * @param : tfcLwInfo
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping(value = "/law/addFine.ajax")
	@ResponseBody
	public CommonResponse<?> lawAddFineAjax(@RequestBody MozTfcLwInfo tfcLwInfo) {
		
		if (administService.checkDuplicateByTfcLwFineIdList(tfcLwInfo.getMozTfcLwFineInfoArr())) {
			// 범칙금 ID가 이미 존재합니다.
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "O ID da multa já existe");
		}
		
		try {
			administService.lawAddFine(tfcLwInfo);
		} catch (CommonException e) {
			// 성공적으로 등록되지 않았습니다
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Não foi registrado com sucesso.");
		}
		// 성공적으로 등록되었습니다.
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Você foi registrado com sucesso.");
	}

	/**
	 * @brief : 법률 폐지 처리
	 * @details : 법률 폐지 처리
	 * @author : KY.LEE
	 * @date : 2024.02.23
	 * @param : tfcLawId
	 */
	@Authority(type = MethodType.DELETE)
	@PostMapping(value = "/law/abolition.ajax")
	@ResponseBody
	public CommonResponse<?> lawAbolitionAjax(@RequestParam("tfcLawId") String tfcLawId) {
		try {
			administService.lawAbolition(tfcLawId);
		} catch (CommonException e) {
			CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Houve um erro ao revogar a lei.");
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "A lei foi revogada.");
	}

	/**
	 * @brief : 법률 복구 처리
	 * @details : 법률 복구 처리
	 * @author : KY.LEE
	 * @date : 2024.02.23
	 * @param : tfcLawId
	 */
	@Authority(type = MethodType.DELETE)
	@PostMapping(value = "/law/lawRecovery.ajax")
	@ResponseBody
	public CommonResponse<?> lawRecoveryAjax(@RequestParam("tfcLawId") String tfcLawId) {
		try {
			administService.lawRecovery(tfcLawId);
		} catch (CommonException e) {
			CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Ocorreu um erro durante a recuperação jurídica.");
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "A lei foi restaurada.");
	}

	/**
	 * @brief : 교통단속 법률관리 상세 조회
	 * @details : 교통단속 법률관리 상세 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLawId
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/law/detail.do")
	public String lawDetail(Model model, @RequestParam("lawId") String tfcLawId) {
		MozTfcLwInfo tfcLwInfo = administService.getLawDetail(tfcLawId);
		
		model.addAttribute("cntrvTyCd", commonCdService.getCdList("CONTRAVENTION_TYPE_CODE"));
		model.addAttribute("addlPnCd", commonCdService.getCdList("ADDITIONAL_PENALTY_CODE"));
		model.addAttribute("trnTyCd", commonCdService.getCdList("TRANSGRESSION_TYPE_CODE"));
		
		model.addAttribute("tfcLwInfo", tfcLwInfo);
		model.addAttribute("tfcLwFineInfoList", administService.getLawFineListJoinCmCd(tfcLawId));
		model.addAttribute("tfcLwAdtnRvsnList", administService.getLawAdtnRvsnList(tfcLawId));
		return "views/administmng/lawDetail";
	}

	/**
	 * @brief : 교통단속 법률관리 수정 화면
	 * @details : 교통단속 법률관리 수정 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLawId
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/law/update.do")
	public String lawUpdate(Model model, @RequestParam("tfcLawId") String tfcLawId) {
		MozTfcLwInfo tfcLwInfo = administService.getLawDetail(tfcLawId);
		
		model.addAttribute("lawTypeCd", commonCdService.getCdList("LAW_TYPE_CD"));
		model.addAttribute("cntrvTyCd", commonCdService.getCdList("CONTRAVENTION_TYPE_CODE"));
		model.addAttribute("addlPnCd", commonCdService.getCdList("ADDITIONAL_PENALTY_CODE"));
		model.addAttribute("trnTyCd", commonCdService.getCdList("TRANSGRESSION_TYPE_CODE"));
		
		model.addAttribute("tfcLwInfo", tfcLwInfo);
		model.addAttribute("tfcLwFineInfoList", administService.getLawFineList(tfcLawId));
		model.addAttribute("tfcLwAdtnRvsnList", administService.getLawAdtnRvsnList(tfcLawId));
		return "views/administmng/lawModify";
	}

	/**
	 * @brief : 교통단속 법률관리 정보 수정
	 * @details : 교통단속 법률관리 정보 수정
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLwInfo
	 * @return :
	 */
	@Authority(type = MethodType.UPDATE)
	@PostMapping(value = "/law/update.ajax")
	@ResponseBody
	public CommonResponse<?> lawUpdateAjax(@RequestBody MozTfcLwInfo tfcLwInfo) {
		
		if (administService.checkDuplicateByArtclNoAndLawId(tfcLwInfo)) {
			// número do artigo가 이미 존재합니다
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "o número do artigo já existe");
		}
		
		try {
			administService.updateLaw(tfcLwInfo);
		} catch (CommonException e) {
			CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Não conseguiu alterar a lei.");
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "A alteração da lei foi bem sucedida.");
	}
	
	/**
	  * @Method Name : fineUpdateAjax
	  * @Date : 2024. 6. 20.
	  * @Author : IK.MOON
	  * @Method Brief : 범칙금 정보 수정
	  * @param fineInfo
	  * @return
	  */
	@Authority(type = MethodType.UPDATE)
	@PostMapping(value = "/law/fineUpdate.ajax")
	@ResponseBody
	public CommonResponse<?> fineUpdateAjax(MozTfcLwFineInfo fineInfo) {
		if (administService.checkDuplicateByTfcLwFineId(fineInfo)) {
			// 범칙금 ID가 이미 존재합니다.
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "O ID da multa já existe");
		}
		
		try {
			administService.updateFineInfo(fineInfo);
		} catch (CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Não conseguiu alterar a lei.");
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "A alteração da lei foi bem sucedida.");
	}
}
