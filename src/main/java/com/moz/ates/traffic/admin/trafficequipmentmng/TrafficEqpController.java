package com.moz.ates.traffic.admin.trafficequipmentmng;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.admin.common.CommonCdService;
import com.moz.ates.traffic.admin.common.FeaturesLayerDTO;
import com.moz.ates.traffic.admin.common.enums.MethodType;
import com.moz.ates.traffic.admin.config.Authority;
import com.moz.ates.traffic.common.component.Pagination;
import com.moz.ates.traffic.common.component.validate.ValidateBuilder;
import com.moz.ates.traffic.common.component.validate.ValidateChecker;
import com.moz.ates.traffic.common.component.validate.ValidateResult;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpFileInfo;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfEqpMaster;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEqpMntnHst;
import com.moz.ates.traffic.common.entity.equipment.MozTfcFacilityFileInfo;
import com.moz.ates.traffic.common.entity.equipment.MozTfcFacilityMaster;
import com.moz.ates.traffic.common.entity.equipment.MozTfcFacilityMntnHst;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Controller
@RequestMapping(value = "/eqp")
public class TrafficEqpController {

	@Autowired
	private TrafficEqpService trafficEqpService;
	
	@Autowired
	private CommonCdService commonCdService;
	

	/**
	 * @brief : 단속장비 리스트 화면
	 * @details : 단속장비 리스트 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : finePymntInfo
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/mng/list.do")
	public String mngList(Model model, String pageType) {
		model.addAttribute("pageType", pageType);
		return "views/equipmentmng/eqpMngList";
	}

	/**
	  * @Method Name : mngListMapAjax
	  * @작성일 : 2024. 1. 10.
	  * @작성자 : IK.MOON
	  * @Method 설명 :
	  * @param model
	  * @param tfcEnfEqpMaster
	  * @return
	  */
	@Authority(type = MethodType.READ)
	@RequestMapping(value = "/mng/eqpMngMapAjax")
	public String mngListMapAjax(Model model, @ModelAttribute MozTfcEnfEqpMaster tfcEnfEqpMaster) {
		tfcEnfEqpMaster.setUseYn("Y");

		int page = tfcEnfEqpMaster.getPage();
		int totalCnt = trafficEqpService.getEqpListCnt(tfcEnfEqpMaster);
		Pagination pagination = new Pagination(totalCnt, page, 5, 5);
		
		tfcEnfEqpMaster.setLength(5);
		tfcEnfEqpMaster.setStart((page - 1) * pagination.getPageSize());
		
		model.addAttribute("tfcEnfEqpMaster", tfcEnfEqpMaster);
		model.addAttribute("eqpList", trafficEqpService.getEqpList(tfcEnfEqpMaster));
		model.addAttribute("pagination", pagination);
		return "views/equipmentmng/eqpMngMapAjax";
	}

	@Authority(type = MethodType.READ)
	@GetMapping(value = "/mng/facMngGeojson.ajax")
	public @ResponseBody
	FeaturesLayerDTO getFacilityGeoJson(Map<String,String> param) {
		return trafficEqpService.getFacilityGeoJson(param);
	}
	
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/mng/enfMngGeojson.ajax")
	public @ResponseBody
	FeaturesLayerDTO getEnforcementGeoJson(Map<String,String> param) {
		return trafficEqpService.getEnforcementGeoJson(param);
	}
	
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/mng/actMngGeojson.ajax")
	public @ResponseBody
	FeaturesLayerDTO getAccidentGeoJson(Map<String,String> param) {
		return trafficEqpService.getAccidentGeoJson(param);
	}
	
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/mng/eqpMngGeojson.ajax")
	public @ResponseBody
	FeaturesLayerDTO getEquipmentGeoJson(Map<String,String> param) {
		param.put("eqpTy", "EQT001");
		param.put("useYn", "Y");
		return trafficEqpService.getEquipmentGeoJson(param);
	}
	
	/**
	 * @brief : 단속장비 리스트 화면
	 * @details : 단속장비 리스트 화면
	 * @author : IK.MOON
	 * @date : 2024.01.10
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@RequestMapping(value = "/mng/eqpMngListAjax")
	public String mngListAjax(Model model, @ModelAttribute MozTfcEnfEqpMaster tfcEnfEqpMaster) {
		tfcEnfEqpMaster.setUseYn(null);
		
		int page = tfcEnfEqpMaster.getPage();
		int totalCnt = trafficEqpService.getEqpListCnt(tfcEnfEqpMaster);
		Pagination pagination = new Pagination(totalCnt, page);
		
		tfcEnfEqpMaster.setStart((page - 1) * pagination.getPageSize());
		
		model.addAttribute("tfcEnfEqpMaster", tfcEnfEqpMaster);
		model.addAttribute("eqpList", trafficEqpService.getEqpList(tfcEnfEqpMaster));
		model.addAttribute("pagination", pagination);
		return "views/equipmentmng/eqpMngListAjax";
	}
	
	/**
	 * @brief : 단속장비 등록 화면
	 * @details : 단속장비 등록 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@GetMapping("/mng/save.do")
	public String mngRegist(Model model) {
		List<MozCmCd> cdList = commonCdService.getCdList("EQUIPMENT_TYPE");
		model.addAttribute("cdList", cdList);
		return "views/equipmentmng/eqpMngRegist";
	}

	/**
	 * @brief : 단속장비 등록
	 * @details : 단속장비 등록
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : uploadFiles
	 * @param : tfcEnfMaster
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping("/mng/save.ajax")
	public @ResponseBody CommonResponse<?> mngRegistAjax(MozTfcEnfEqpMaster tfcEnfEqpMaster
			, @RequestPart(required = false) MultipartFile[] uploadFiles){
		
		ValidateBuilder dtoValidator = new ValidateBuilder(tfcEnfEqpMaster);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("tfcEnfEqpId", new ValidateChecker().setRequired().setMaxLength(50))
				.addRule("eqpTy", new ValidateChecker().setRequired())
				.addRule("eqpNm", new ValidateChecker().setRequired()
						.setMaxLength(100, "O nome do equipamento não pode ter mais de 100 caracteres."))
				.addRule("modelNm", new ValidateChecker().setRequired()
						.setMaxLength(100, "O nome do modelo não pode ter mais de 100 caracteres."))
				.addRule("mnfctr", new ValidateChecker().setRequired()
						.setMaxLength(100, "O fabricante não pode ter mais de 100 caracteres."))
				.addRule("instlYn", new ValidateChecker().setRequired())
				.addRule("useYn", new ValidateChecker().setRequired())
				.addRule("roadAddr", new ValidateChecker().setRequired()
						.setMaxLength(100, "O local de instalação não pode ter mais de 100 caracteres."))
				.addRule("lat", new ValidateChecker().setRequired().setLatitude())
				.addRule("lng", new ValidateChecker().setRequired().setLongitude())
				.addRule("instlDate", new ValidateChecker().setRequired())
				.addRule("instler", new ValidateChecker().setRequired()
						.setMaxLength(100, "The installation location cannot be longer than 100 characters."))
				.addRule("crOprtrId", new ValidateChecker().setRequired())
				.addRule("tfcEnfEqpInfo", new ValidateChecker().setMaxLength(1000))
				.addRule("crOprtrId", new ValidateChecker().setRequired())
				.isValid();
		
		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}

		int duplicateCnt = trafficEqpService.getEqpDupliCnt(tfcEnfEqpMaster.getTfcEnfEqpId());

		if (duplicateCnt > 0) {
			// Duplicate ID exists.
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Existe um ID duplicado.");
		}

		try {
			trafficEqpService.registEqp(tfcEnfEqpMaster, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Este equipamento de tráfego foi registado.");
	}

	/**
	 * @brief : 단속장비 상세 조회
	 * @details : 단속장비 상세 조회
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : tfcEnfEqpId
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/mng/detail.do")
	public String mngDetail(Model model, @RequestParam("tfcEnfEqpId") String tfcEnfEqpId) {
		MozTfcEnfEqpMaster tfcEnfEqpMaster = trafficEqpService.getEqpDetail(tfcEnfEqpId);
		model.addAttribute("tfcEnfEqpMaster", tfcEnfEqpMaster);
		model.addAttribute("eqpMntnHstList", trafficEqpService.getEqpMntnHstList(tfcEnfEqpId));
		return "views/equipmentmng/eqpMngDetail";
	}
	
	/**
	 * @brief : 단속장비 수정 화면
	 * @details : 단속장비 수정 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : tfcEnfEqpId
	 * @return :
	 */
	@Authority(type = MethodType.UPDATE)
	@GetMapping("/mng/update.do")
	public String mngModify(Model model, @RequestParam("tfcEnfEqpId") String tfcEnfEqpId) {

		MozTfcEnfEqpMaster tfcEnfEqpMaster = trafficEqpService.getEqpDetail(tfcEnfEqpId);
		List<String> oldFileArr = tfcEnfEqpMaster.getTfcEnfEqpFileInfoList().stream()
				.map(MozTfcEnfEqpFileInfo::getFileOrgNm)
				.filter(Objects::nonNull)
				.collect(Collectors.toList())
				;
				
		List<MozCmCd> cdList = commonCdService.getCdList("EQUIPMENT_TYPE");
		
		model.addAttribute("cdList", cdList);
		model.addAttribute("tfcEnfEqpMaster", tfcEnfEqpMaster);
		model.addAttribute("oldFileArr", oldFileArr);
		model.addAttribute("eqpMntnHstList", trafficEqpService.getEqpMntnHstList(tfcEnfEqpId));
		model.addAttribute("mntnSttsCdList", commonCdService.getCdList("MNTN_STTS_CD"));
		model.addAttribute("mntnTypeCdList", commonCdService.getCdList("MNTN_TYPE_CD"));
				
		return "views/equipmentmng/eqpMngModify";
	}

	/**
	 * @brief : 단속장비 정보 수정
	 * @details : 단속장비 정보 수정
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : tfcEnfEqpMaster
	 * @param : uploadFiles
	 * @return :
	 */
	@Authority(type = MethodType.UPDATE)
	@PostMapping("/mng/update.ajax")
	public @ResponseBody CommonResponse<?> mngModifyAjax(@ModelAttribute MozTfcEnfEqpMaster tfcEnfEqpMaster,
			@RequestPart(required = false) MultipartFile[] uploadFiles){
		ValidateBuilder dtoValidator = new ValidateBuilder(tfcEnfEqpMaster);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("tfcEnfEqpId", new ValidateChecker().setRequired())
				.addRule("newTfcEnfEqpId", new ValidateChecker().setRequired().setMaxLength(50))
				.addRule("eqpTy", new ValidateChecker().setRequired())
				.addRule("eqpNm", new ValidateChecker().setRequired()
						.setMaxLength(100, "O nome do equipamento não pode ter mais de 100 caracteres."))
				.addRule("modelNm", new ValidateChecker().setRequired()
						.setMaxLength(100, "O nome do modelo não pode ter mais de 100 caracteres."))
				.addRule("mnfctr", new ValidateChecker().setRequired()
						.setMaxLength(100, "O fabricante não pode ter mais de 100 caracteres."))
				.addRule("instlYn", new ValidateChecker().setRequired())
				.addRule("useYn", new ValidateChecker().setRequired())
				.addRule("roadAddr", new ValidateChecker().setRequired()
						.setMaxLength(100, "O local de instalação não pode ter mais de 100 caracteres."))
				.addRule("lat", new ValidateChecker().setRequired().setLatitude())
				.addRule("lng", new ValidateChecker().setRequired().setLongitude())
				.addRule("instlDate", new ValidateChecker().setRequired())
				.addRule("instler", new ValidateChecker().setRequired()
						.setMaxLength(100, "The installation location cannot be longer than 100 characters."))
				.addRule("crOprtrId", new ValidateChecker().setRequired())
				.addRule("tfcEnfEqpInfo", new ValidateChecker().setMaxLength(1000))
				.addRule("crOprtrId", new ValidateChecker().setRequired())
				.isValid();
		
		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}

		if (!tfcEnfEqpMaster.getTfcEnfEqpId().equals(tfcEnfEqpMaster.getNewTfcEnfEqpId())) {
			int duplicateCnt = trafficEqpService.getEqpDupliCnt(tfcEnfEqpMaster.getNewTfcEnfEqpId());

			if (duplicateCnt > 0) {
				// Duplicate ID exists.
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Existe um ID duplicado.");
			}
		}

		try {
			 trafficEqpService.updateEqp(tfcEnfEqpMaster, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Este Equipamento de Tráfego foi modificado.");
	}
	
	/**
	 * @brief : 단속장비 삭제
	 * @details : 단속장비 삭제
	 * @author : KC.KIM
	 * @date : 2024.03.04
	 * @param : tfcEnfEqpId
	 * @return :
	 */
	@Authority(type = MethodType.DELETE)
	@PostMapping("/mng/delete.ajax")
	@ResponseBody
	public CommonResponse<?> noticeDeleteAjax(@RequestParam("tfcEnfEqpId") String tfcEnfEqpId) {

		MozTfcEnfEqpMaster tfcEnfEqpMaster = new MozTfcEnfEqpMaster();
		tfcEnfEqpMaster.setTfcEnfEqpId(tfcEnfEqpId);

		ValidateBuilder dtoValidator = new ValidateBuilder(tfcEnfEqpMaster);

		ValidateResult dtoValidatorResult =
				dtoValidator.addRule("tfcEnfEqpId", new ValidateChecker().setRequired()).isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
					dtoValidatorResult.getMessage());
		}

		try {
			trafficEqpService.deleteTfcEnfEqpMaster(tfcEnfEqpId);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Esta informação sobre o equipamento foi eliminada.");
	}
	
	/**
	 * @brief : 교통시설물 유지보수 이력 삭제
	 * @details : 교통시설물 유지보수 이력 삭제
	 * @author : KY.LEE
	 * @date : 2024.04.24
	 * @param : mntnHstId
	 */
	@Authority(type = MethodType.DELETE)
	@PostMapping("/mng/hstDelete.ajax")
	@ResponseBody
	public CommonResponse<?> hstDelete(@RequestParam("mntnHstId") String mntnHstId) {

		if (MozatesCommonUtils.isNull(mntnHstId)) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,"A eliminação falhou.");
		}

		try {
			trafficEqpService.deleteEqpHist(mntnHstId);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Eliminado com sucesso.");
	}
	
	/**
	 * @brief : 교통시설물 유지보수 이력 삭제
	 * @details : 교통시설물 유지보수 이력 삭제
	 * @author : KY.LEE
	 * @date : 2024.04.24
	 * @param : mntnHstId
	 */
	@Authority(type = MethodType.DELETE)
	@PostMapping("/facility/mntnDelete.ajax")
	@ResponseBody
	public CommonResponse<?> facMntnDelete(@RequestParam("tfcFacilityLogId") String tfcFacilityLogId) {
		if (MozatesCommonUtils.isNull(tfcFacilityLogId)) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,"A eliminação falhou.");
		}

		try {
			trafficEqpService.deleteFacilityHist(tfcFacilityLogId);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Eliminado com sucesso.");
	}

	/**
	 * @brief : 교통시설물 관리 리스트 화면
	 * @details : 교통시설물 관리 리스트 화면
	 * @author : KC.KIM
	 * @date : 2023.08.17
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/facility/list.do")
	public String facilityMngList(Model model, String pageType) {
		model.addAttribute("pageType", pageType);
		return "views/equipmentmng/facilityMngList";
	}
	
	/**
	 * @brief : 교통시설물 관리 등록 화면
	 * @details : 교통시설물 관리 등록 화면
	 * @author : IK.MOON
	 * @date : 2024.01.10
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@GetMapping(value = "/facility/save.do")
	public String facilityMngRegist(Model model) {
		model.addAttribute("facTyCd", commonCdService.getCdList("TRAFFIC_FACILITY_TYPE"));
		model.addAttribute("facCateCd", commonCdService.getCdList("FACILITY_CATE_CD"));
		model.addAttribute("facDivCd", commonCdService.getCdList("FACILITY_DIV_CD"));
		model.addAttribute("areaCd", commonCdService.getCdList("AREA_CD"));
		return "views/equipmentmng/facilityMngRegist";
	}

	/**
	 * @brief : 교통시설물,감시카메라 유지보수 이력 등록
	 * @details : 교통시설물 관리 등록 화면
	 * @author : IK.MOON
	 * @date : 2024.01.10
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping(value = "/mng/mntnSave.ajax")
	public @ResponseBody CommonResponse<?>  eqpMaintenanceSave(Model model,
			MozTfcEqpMntnHst mozTfcEqpMntnHst
			) {
		try {
			trafficEqpService.saveEquipmentMaintenance(mozTfcEqpMntnHst);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "O histórico de manutenção foi registrado");
	}

	/**
	 * @brief : 교통시설물 유지보수 이력 등록
	 * @details :  교통시설물 유지보수 이력 등록
	 * @author : KY.LEE
	 * @date : 2024.01.10
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping(value = "/facility/mntnSave.ajax")
	public @ResponseBody CommonResponse<?>  facMaintenanceSave(Model model,
			MozTfcFacilityMntnHst mozTfcFacilityMntnHst
			) {
		try {
			trafficEqpService.saveFacilityMaintenance(mozTfcFacilityMntnHst);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "O histórico de manutenção foi registrado");
	}
	
	/**
	 * @brief : 교통시설물 등록
	 * @details : 교통시설물 등록
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : tfcFacilityMaster
	 * @param : uploadFiles
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping("/facility/save.ajax")
	public @ResponseBody CommonResponse<?> facilityRegistAjax(MozTfcFacilityMaster tfcFacilityMaster
			, @RequestPart(required = false) MultipartFile[] uploadFiles){
		ValidateBuilder dtoValidator = new ValidateBuilder(tfcFacilityMaster);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("facilityNm", new ValidateChecker().setRequired()
						.setMaxLength(100, "O nome da instalação não pode ter mais de 100 caracteres."))
				.addRule("facilityTy", new ValidateChecker().setRequired())
				.addRule("facilityDiv", new ValidateChecker().setRequired())
				.addRule("facilityCate", new ValidateChecker().setRequired())
				.addRule("useYn", new ValidateChecker().setRequired())
				.addRule("areaCd", new ValidateChecker().setRequired())
				.addRule("oprtrId", new ValidateChecker().setRequired())
				.addRule("roadAddr", new ValidateChecker().setRequired()
						.setMaxLength(100, "A localização não pode ter mais de 100 caracteres."))
				.addRule("lat", new ValidateChecker().setRequired().setLatitude())
				.addRule("lng", new ValidateChecker().setRequired().setLongitude())
				.addRule("facilityStts", new ValidateChecker().setRequired()
						.setMaxLength(100))
				.addRule("facilityDesc", new ValidateChecker()
						.setMaxLength(1000))
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}
		
		try {
			trafficEqpService.registTfcFacility(tfcFacilityMaster, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Este serviço de tráfego foi registado.");
	}
	
	/**
	 * @brief : 교통시설물 관리 상세 화면
	 * @details : 교통시설물 관리 상세 화면
	 * @author : IK.MOON
	 * @date : 2024.01.10
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/facility/detail.do")
	public String facilityMngDetail(Model model, @RequestParam("tfcFacilityId") String tfcFacilityId) {
		MozTfcFacilityMaster tfcFacilityMaster = trafficEqpService.getFacilityDetail(tfcFacilityId);
		model.addAttribute("tfcFacilityMaster", tfcFacilityMaster);
		model.addAttribute("tfcFacilityMntnHstList", trafficEqpService.getFacilityMntnHstList(tfcFacilityId));
		return "views/equipmentmng/facilityMngDetail";
	}
	
	/**
	 * @brief : 교통시설물 관리 지도 화면
	 * @details : 교통시설물 관리 지도 화면
	 * @author : IK.MOON
	 * @date : 2024.01.10
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@RequestMapping(value = "/facility/facilityMngMapAjax")
	public String facilityMngMapAjax(Model model, @ModelAttribute MozTfcFacilityMaster tfcFacilityMaster) {
		int page = tfcFacilityMaster.getPage();
		int totalCnt = trafficEqpService.getFacilityListCnt(tfcFacilityMaster);
		Pagination pagination = new Pagination(totalCnt, page, 5, 5);
		
		tfcFacilityMaster.setLength(5);
		tfcFacilityMaster.setStart((page - 1) * pagination.getPageSize());
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("facilityList", trafficEqpService.getFacilityList(tfcFacilityMaster));
		model.addAttribute("tfcFacilityMaster", tfcFacilityMaster);
		return "views/equipmentmng/facilityMngMapAjax";
	}
	
	/**
	 * @brief : 교통시설물 관리 리스트 화면
	 * @details : 교통시설물 관리 리스트 화면
	 * @author : IK.MOON
	 * @date : 2024.01.10
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@PostMapping("/facility/facilityMngListAjax")
	public String facilityMngListAjax(Model model, @ModelAttribute MozTfcFacilityMaster tfcFacilityMaster) {
		int page = tfcFacilityMaster.getPage();
		int totalCnt = trafficEqpService.getFacilityListCnt(tfcFacilityMaster);
		Pagination pagination = new Pagination(totalCnt, page);
		
		tfcFacilityMaster.setStart((page - 1) * pagination.getPageSize());
		
		model.addAttribute("facilityInfo", tfcFacilityMaster);
		model.addAttribute("facilityList", trafficEqpService.getFacilityList(tfcFacilityMaster));
		model.addAttribute("pagination", pagination);
		return "views/equipmentmng/facilityMngListAjax";
	}
	
	/**
	 * @brief : 교통시설물 삭제
	 * @details : 교통시설물 삭제
	 * @author : KC.KIM
	 * @date : 2024.03.04
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.DELETE)
	@PostMapping("/facility/delete.ajax")
	@ResponseBody
	public CommonResponse<?> facilityDeleteAjax(@RequestParam("tfcFacilityId") String tfcFacilityId) {
		MozTfcFacilityMaster tfcFacilityMaster = new MozTfcFacilityMaster();
		tfcFacilityMaster.setTfcFacilityId(tfcFacilityId);

		ValidateBuilder dtoValidator = new ValidateBuilder(tfcFacilityMaster);

		ValidateResult dtoValidatorResult =
				dtoValidator.addRule("tfcFacilityId", new ValidateChecker().setRequired()).isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
					dtoValidatorResult.getMessage());
		}

		try {
			trafficEqpService.deleteTfcFacilityMaster(tfcFacilityId);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Esta informação sobre o mecanismo foi suprimida.");
	}
	
	/**
	 * @brief : 단속장비 수정 화면
	 * @details : 단속장비 수정 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : tfcEnfEqpId
	 * @return :
	 */
	@Authority(type = MethodType.UPDATE)
	@GetMapping("/facility/update.do")
	public String facilityModify(Model model, @RequestParam("tfcFacilityId") String tfcFacilityId) {
		MozTfcFacilityMaster tfcFacilityMaster = trafficEqpService.getFacilityDetail(tfcFacilityId);
		List<String> oldFileArr = tfcFacilityMaster.getTfcFacilityFileInfoList().stream()
				.map(MozTfcFacilityFileInfo::getFileOrgNm)
				.filter(Objects::nonNull)
				.collect(Collectors.toList())
				;
		List<MozCmCd> facliltyTypeCodeList = commonCdService.getCdList("TRAFFIC_FACILITY_TYPE");
		// 유관기관 코드 제거
		facliltyTypeCodeList.removeIf(x -> x.getCdId().equals("TFT900"));

		model.addAttribute("facTyCd", facliltyTypeCodeList);
		model.addAttribute("facCateCd", commonCdService.getCdList("FACILITY_CATE_CD"));
		model.addAttribute("facDivCd", commonCdService.getCdList("FACILITY_DIV_CD"));
		model.addAttribute("areaCd", commonCdService.getCdList("AREA_CD"));
		model.addAttribute("tfcFacilityMntnHstList", trafficEqpService.getFacilityMntnHstList(tfcFacilityId));
		model.addAttribute("tfcFacilityMaster", tfcFacilityMaster);
		model.addAttribute("oldFileArr", oldFileArr);
		return "views/equipmentmng/facilityMngModify";
	}
	
	/**
	 * @brief : 단속장비 수정
	 * @details : 단속장비 수정
	 * @author : KC.KIM
	 * @date : 2024.03.04
	 * @param : tfcEnfEqpMaster
	 * @param : uploadFiles
	 * @return :
	 */
	@Authority(type = MethodType.UPDATE)
	@PostMapping("/facility/update.ajax")
	public @ResponseBody CommonResponse<?> facilityModifyAjax(@ModelAttribute MozTfcFacilityMaster tfcFacilityMaster,
			@RequestPart(required = false) MultipartFile[] uploadFiles){
		ValidateBuilder dtoValidator = new ValidateBuilder(tfcFacilityMaster);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("facilityNm", new ValidateChecker().setRequired()
						.setMaxLength(100, "O nome da instalação não pode ter mais de 100 caracteres."))
				.addRule("facilityTy", new ValidateChecker().setRequired())
				.addRule("facilityDiv", new ValidateChecker().setRequired())
				.addRule("facilityCate", new ValidateChecker().setRequired())
				.addRule("useYn", new ValidateChecker().setRequired())
				.addRule("areaCd", new ValidateChecker().setRequired())
				.addRule("oprtrId", new ValidateChecker().setRequired())
				.addRule("roadAddr", new ValidateChecker().setRequired()
						.setMaxLength(100, "A localização não pode ter mais de 100 caracteres."))
				.addRule("lat", new ValidateChecker().setRequired().setLatitude())
				.addRule("lng", new ValidateChecker().setRequired().setLongitude())
				.addRule("facilityStts", new ValidateChecker().setRequired()
						.setMaxLength(100))
				.addRule("facilityDesc", new ValidateChecker()
						.setMaxLength(1000))
				.isValid();
		
		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}
		
		try {
			trafficEqpService.updateMoztfcFacility(tfcFacilityMaster, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Esta instalação de tráfego foi modificada.");
	}

	/**
	 * @brief : 교통시설물 관리 로그 리스트 화면
	 * @details : 교통시설물 관리 로그 리스트 화면
	 * @author : KC.KIM
	 * @date : 2023.08.17
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@RequestMapping(value = "/facilitylog/list.do")
	public String facilityLogList(Model model) {
		return "views/equipmentmng/facilityLogList";
	}

	/**
	 * @brief : 유관기관 리스트 화면
	 * @author : IK.MOON
	 * @date : 2024.07.15
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/orgnz/list.do")
	public String orgnzMngList(Model model, String pageType) {
		model.addAttribute("pageType", pageType);
		return "views/equipmentmng/orgnzMngList";
	}

	/**
	 * @brief : 유관기관 리스트 ajax
	 * @author : IK.MOON
	 * @date : 2024.07.15
	 */
	@Authority(type = MethodType.READ)
	@PostMapping("/orgnz/orgnzMngListAjax")
	public String orgnzMngListAjax(Model model, @ModelAttribute MozTfcFacilityMaster tfcFacilityMaster) {
		int page = tfcFacilityMaster.getPage();
		int totalCnt = trafficEqpService.getOrganizationCount(tfcFacilityMaster);
		Pagination pagination = new Pagination(totalCnt, page);

		tfcFacilityMaster.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("facilityInfo", tfcFacilityMaster);
		model.addAttribute("facilityList", trafficEqpService.getOrganizationList(tfcFacilityMaster));
		model.addAttribute("pagination", pagination);
		return "views/equipmentmng/orgnzMngListAjax";
	}

	/**
	 * @brief : 유관기관 MAP ajax
	 * @author : IK.MOON
	 * @date : 2024.07.15
	 */
	@Authority(type = MethodType.READ)
	@RequestMapping(value = "/orgnz/orgnzMngMapAjax")
	public String orgnzMngMapAjax(Model model, @ModelAttribute MozTfcFacilityMaster tfcFacilityMaster) {
		int page = tfcFacilityMaster.getPage();
		int totalCnt = trafficEqpService.getOrganizationCount(tfcFacilityMaster);
		Pagination pagination = new Pagination(totalCnt, page, 5, 5);

		tfcFacilityMaster.setLength(5);
		tfcFacilityMaster.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("pagination", pagination);
		model.addAttribute("facilityList", trafficEqpService.getOrganizationList(tfcFacilityMaster));
		model.addAttribute("tfcFacilityMaster", tfcFacilityMaster);
		return "views/equipmentmng/orgnzMngMapAjax";
	}

	@Authority(type = MethodType.READ)
	@GetMapping(value = "/mng/orgnzMngGeojson.ajax")
	public @ResponseBody
	FeaturesLayerDTO getOrganizationGeoJson(Map<String,String> param) {
		return trafficEqpService.getOrganizationGeoJson(param);
	}

	/**
	 * @brief : 유관기관 regist 화면
	 * @author : IK.MOON
	 * @date : 2024.07.15
	 */
	@Authority(type = MethodType.CREATE)
	@GetMapping(value = "/orgnz/save.do")
	public String orgnzMngRegist(Model model) {
		return "views/equipmentmng/orgnzMngRegist";
	}

	/**
	 * @brief : 유관기관 Regist ajax
	 * @author : IK.MOON
	 * @date : 2024.07.15
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping("/orgnz/save.ajax")
	public @ResponseBody CommonResponse<?> orgnzRegistAjax(MozTfcFacilityMaster tfcFacilityMaster
			, @RequestPart(required = false) MultipartFile[] uploadFiles){
		ValidateBuilder dtoValidator = new ValidateBuilder(tfcFacilityMaster);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("facilityNm", new ValidateChecker().setRequired().setMaxLength(100))
				.addRule("useYn", new ValidateChecker().setRequired())
				.addRule("roadAddr", new ValidateChecker().setRequired().setMaxLength(100))
				.addRule("lat", new ValidateChecker().setRequired().setLatitude())
				.addRule("lng", new ValidateChecker().setRequired().setLongitude())
				.addRule("facilityDesc", new ValidateChecker().setMaxLength(1000))
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}

		try {
			trafficEqpService.registOrganization(tfcFacilityMaster, uploadFiles);
		} catch (Exception e) {
			// 오류가 발생했습니다
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Um erro ocorreu");
		}

		// 등록을 성공 했습니다.
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "O registro foi realizado com sucesso.");
	}

	/**
	 * @brief : 유관기관 상세 화면
	 * @author : IK.MOON
	 * @date : 2024.07.15
	 */
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/orgnz/detail.do")
	public String orgnzMngDetail(Model model, @RequestParam("tfcFacilityId") String tfcFacilityId) {
		MozTfcFacilityMaster tfcFacilityMaster = trafficEqpService.getFacilityDetail(tfcFacilityId);
		model.addAttribute("tfcFacilityMaster", tfcFacilityMaster);
		model.addAttribute("tfcFacilityMntnHstList", trafficEqpService.getFacilityMntnHstList(tfcFacilityId));
		return "views/equipmentmng/orgnzMngDetail";
	}

	/**
	 * @brief : 유관기관 수정 화면
	 * @author : IK.MOON
	 * @date : 2024.07.18
	 * @param model
	 * @param tfcFacilityId
	 * @return
	 */
	@Authority(type = MethodType.UPDATE)
	@GetMapping("/orgnz/update.do")
	public String organizationModify(Model model, @RequestParam("tfcFacilityId") String tfcFacilityId) {
		MozTfcFacilityMaster tfcFacilityMaster = trafficEqpService.getFacilityDetail(tfcFacilityId);
		List<String> oldFileArr = tfcFacilityMaster.getTfcFacilityFileInfoList().stream()
				.map(MozTfcFacilityFileInfo::getFileOrgNm)
				.filter(Objects::nonNull)
				.collect(Collectors.toList())
				;
		model.addAttribute("tfcFacilityMaster", tfcFacilityMaster);
		model.addAttribute("oldFileArr", oldFileArr);
		return "views/equipmentmng/orgnzMngModify";
	}


	/**
	 * @brief : 유관기관 수정 ajax
	 * @author : IK.MOON
	 * @date : 2024.07.18
	 * @param tfcFacilityMaster
	 * @param uploadFiles
	 * @return
	 */
	@Authority(type = MethodType.UPDATE)
	@PostMapping("/orgnz/update.ajax")
	public @ResponseBody CommonResponse<?> organizationModifyAjax(@ModelAttribute MozTfcFacilityMaster tfcFacilityMaster,
															  @RequestPart(required = false) MultipartFile[] uploadFiles){
		ValidateBuilder dtoValidator = new ValidateBuilder(tfcFacilityMaster);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("facilityNm", new ValidateChecker().setRequired().setMaxLength(100))
				.addRule("useYn", new ValidateChecker().setRequired())
				.addRule("roadAddr", new ValidateChecker().setRequired().setMaxLength(100))
				.addRule("lat", new ValidateChecker().setRequired().setLatitude())
				.addRule("lng", new ValidateChecker().setRequired().setLongitude())
				.addRule("facilityDesc", new ValidateChecker().setMaxLength(1000))
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}

		try {
			trafficEqpService.updateOrganization(tfcFacilityMaster, uploadFiles);
		} catch (Exception e) {
			// 오류가 발생했습니다
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Um erro ocorreu");
		}
		// 수정 되었습니다.
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Está mudado.");
	}
}
