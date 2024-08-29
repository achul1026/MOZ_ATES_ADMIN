package com.moz.ates.traffic.admin.trafficaccidentmng;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntTrgtInfo;
import com.moz.ates.traffic.common.entity.driver.MozVioInfo;
import com.moz.ates.traffic.common.support.exception.CommonResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import com.moz.ates.traffic.admin.common.enums.MethodType;
import com.moz.ates.traffic.admin.config.Authority;
import com.moz.ates.traffic.admin.gis.service.GisService;
import com.moz.ates.traffic.admin.trafficenforcementmng.EnfSearchVO;
import com.moz.ates.traffic.common.component.Pagination;
import com.moz.ates.traffic.common.component.validate.ValidateBuilder;
import com.moz.ates.traffic.common.component.validate.ValidateChecker;
import com.moz.ates.traffic.common.component.validate.ValidateResult;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntFileInfo;
import com.moz.ates.traffic.common.entity.accident.MozTfcAcdntMaster;
import com.moz.ates.traffic.common.entity.api.ItesApiResponse;
import com.moz.ates.traffic.common.entity.api.MojApiRequest;
import com.moz.ates.traffic.common.entity.common.AccidentDomain;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.support.exception.ErrorCode;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Controller
@RequestMapping(value = "/acdnt")
public class TrafficAcdntController {

	@Autowired
	private CommonCdService commonCdService;

	@Autowired
	private TrafficAcdntService trafficAcdntService;

	@Autowired
	private GisService gService;
	
	/**
	 * @brief : 운전자 정보 조회 화면
	 * @details : 운전자 정보 조회 화면
	 * @author : KY.LEE
	 * @date : 2024.04.08
	 * @param : searchDriver
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/driver/search.do")
	public String searchDriver(Model model, @ModelAttribute EnfSearchVO enfSearchVO) {

		return "views/accidentmng/searchDriver";
	}
	
	/**
	 * @brief : 운전자 정보 조회 상세 화면
	 * @details : 운전자 정보 조회 상세 화면
	 * @author : KY.LEE
	 * @date : 2023.04.10
	 * @param : searchDriverDetail
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/driver/detail.do")
	public String searchDriverDetail(Model model, @ModelAttribute ItesApiResponse.ItesApiResponseData itesApiResponseData) {
		if (itesApiResponseData.getCodigo() == 0) {
			throw new CommonResponseException(ErrorCode.INVALID_PARAMETER);
		}
		
		if(!MozatesCommonUtils.isNull(itesApiResponseData.getDataDeNascimento())) {
			String birthDayFormat = MozatesCommonUtils.changeDateFormat(itesApiResponseData.getDataDeNascimento(), "yyyy-MM-dd", "dd.MM.yyyy");
			itesApiResponseData.setDataDeNascimento(birthDayFormat);
			String fullAddr = MozatesCommonUtils.createAddress(
					itesApiResponseData.getEnderesso1(), 
					itesApiResponseData.getEnderesso2(), 
					itesApiResponseData.getEnderesso3(),
					itesApiResponseData.getEnderesso4());
			model.addAttribute("vioAddr", fullAddr);
		}
		
		model.addAttribute("driverInfo", itesApiResponseData);
		return "views/accidentmng/searchDriverDetail";
	}

	/**
	 * methodName : searchDriverListAjax
	 * author : IK.MOON
	 * date : 2024-08-20
	 * description : 운전자 정보 조회 사고 리스트 ajax
	 *
	 * @param model
	 * @param tfcAcdntTrgtInfo
	 * @return String
	 */
	@Authority(type = MethodType.READ)
	@PostMapping(value = "/driver/list.ajax")
	public String searchDriverListAjax(Model model, MozTfcAcdntTrgtInfo tfcAcdntTrgtInfo) {

		int page = tfcAcdntTrgtInfo.getPage();
		int totalCnt = trafficAcdntService.getAcdntTrgtCountByDvrLcenId(tfcAcdntTrgtInfo);
		Pagination pagination = new Pagination(totalCnt, page);

		tfcAcdntTrgtInfo.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("acdntHistory", trafficAcdntService.getAcdntTrgtListByDvrLcenId(tfcAcdntTrgtInfo));
		model.addAttribute("pagination", pagination);
		model.addAttribute("type", "driver");

		return "views/accidentmng/searchListAjax";
	}

	/**
	 * @brief : 차량 정보 조회 화면
	 * @details : 차량 정보 조회 화면
	 * @author : KY.LEE
	 * @date : 2024.04.08
	 * @param : searchDriver
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/vehicle/search.do")
	public String searchCar(Model model) {

		return "views/accidentmng/searchCar";
	}
	
	/**
	 * @brief : 차량 정보 조회 상세 화면
	 * @details : 차량 정보 조회 상세 화면
	 * @author : KY.LEE
	 * @date : 2023.04.10
	 * @param : searchDriverDetail
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/vehicle/detail.do")
	public String searchCarDetail(Model model, @ModelAttribute MojApiRequest mojApiRequest) {
		if (MozatesCommonUtils.isNull(mojApiRequest.getVhRegNo())) {
			throw new CommonResponseException(ErrorCode.INVALID_PARAMETER);
		}

		if(!MozatesCommonUtils.isNull(mojApiRequest.getDatadenascimento())) {
			String birthDayFormat = MozatesCommonUtils.changeDateFormat(mojApiRequest.getDatadenascimento(), "yyyy-MM-dd'T'HH:mm:ss", "dd.MM.yyyy");
			mojApiRequest.setVioBrth(birthDayFormat);
			mojApiRequest.setVioAddr(MozatesCommonUtils.formatAddress(mojApiRequest.getDomicilio(), mojApiRequest.getProvincia(), mojApiRequest.getDistrito()));
		}

		model.addAttribute("driverInfo", mojApiRequest);
		return "views/accidentmng/searchCarDetail";
	}

	/**
	 * methodName : searchVehicleListAjax
	 * author : IK.MOON
	 * date : 2024-08-20
	 * description : 차량 정보 조회 사고 리스트 ajax
	 *
	 * @param model
	 * @param tfcAcdntTrgtInfo
	 * @return String
	 */
	@Authority(type = MethodType.READ)
	@PostMapping(value = "/vehicle/list.ajax")
	public String searchVehicleListAjax(Model model, MozTfcAcdntTrgtInfo tfcAcdntTrgtInfo) {

		int page = tfcAcdntTrgtInfo.getPage();
		int totalCnt = trafficAcdntService.getAcdntTrgtCountByDocNidOrVhRegNo(tfcAcdntTrgtInfo);
		Pagination pagination = new Pagination(totalCnt, page);

		tfcAcdntTrgtInfo.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("acdntHistory", trafficAcdntService.getAcdntTrgtListByDocNidOrvhRegNo(tfcAcdntTrgtInfo));
		model.addAttribute("pagination", pagination);
		model.addAttribute("type", "vehicle");

		return "views/accidentmng/searchListAjax";
	}

	/**
	 * @brief : 교통사고 관리 리스트
	 * @details : 교통사고 관리 리스트
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : tfcAcdntMaster
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/mng/list.do")
	public String acdntMngList(Model model, @ModelAttribute MozTfcAcdntMaster tfcAcdntMaster) {
		int page = tfcAcdntMaster.getPage();
		int totalCnt = trafficAcdntService.getAcdntListCnt(tfcAcdntMaster);
		Pagination pagination = new Pagination(totalCnt, page);
		
		tfcAcdntMaster.setStart((page - 1) * pagination.getPageSize());
		
		model.addAttribute("tfcAcdntMaster", tfcAcdntMaster);
		model.addAttribute("acdntList", trafficAcdntService.getAcdntList(tfcAcdntMaster));
		model.addAttribute("pagination", pagination);
		return "views/accidentmng/acdntMngList";
	}

	/**
	 * @brief : 교통사고 등록 화면
	 * @details : 교통사고 등록 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/mng/save.do")
	public String acdntSave(Model model) {
		List<MozCmCd> acdntCdList = commonCdService.getCdList("ACCIDENT_TYPE");
		List<MozCmCd> dvrLcenTyList = commonCdService.getCmCdByCdGroupId("DVR_LCEN_TY");
		List<MozCmCd> vhTyList = commonCdService.getCmCdByCdGroupId("VEHICLE_TYPE_CD");
		List<MozCmCd> passengerDamageCd = commonCdService.getCdList("PASSENGER_DAMAGE_CD");
		List<MozCmCd> accidentDmgCd = commonCdService.getCdList("ACCIDENT_DMG_CD");
		List<MozCmCd> passengerDriverRelationshipCd = commonCdService.getCdList("PASSENGER_DRIVER_RELATIONSHIP_CD");

		model.addAttribute("acdntCdList", acdntCdList);
		model.addAttribute("passengerDamageCd", passengerDamageCd);
		model.addAttribute("accidentDmgCd", accidentDmgCd);
		model.addAttribute("dvrLcenTyList", dvrLcenTyList);
		model.addAttribute("vhTyList", vhTyList);
		model.addAttribute("passengerDriverRelationshipCd", passengerDriverRelationshipCd);
		return "views/accidentmng/acdntMngRegist";
	}

	/**
	 * @brief : 교통사고 등록
	 * @details : 교통사고 등록
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : tfcAcdntMaster
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping("/mng/save.ajax")
	@Transactional
	public @ResponseBody CommonResponse<?> saveAcdntInfo(@ModelAttribute MozTfcAcdntMaster tfcAcdntMaster
			, @RequestPart(required = false) MultipartFile[] uploadFiles) {
		ValidateBuilder dtoValidator = new ValidateBuilder(tfcAcdntMaster);
		
		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("acdntDt", new ValidateChecker().setRequired())
				.addRule("acdntTyCd", new ValidateChecker().setRequired())
				.addRule("roadAddr", new ValidateChecker().setRequired().setMaxLength(200, "A localização não pode ter mais de 200 caracteres."))
				.addRule("polId", new ValidateChecker().setRequired())
				.addRule("acdntChildYn", new ValidateChecker().setRequired())
				.addRule("acdntTyDtls", new ValidateChecker().setRequired().setMaxLength(200, "Os pormenores não podem ter mais de 200 caracteres."))
				.addRule("lat", new ValidateChecker().setRequired().setLatitude())
				.addRule("lng", new ValidateChecker().setRequired().setLongitude())
				.isValid();
		
		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}
		
		try {
			trafficAcdntService.mozTfcAcdntMasterSave(tfcAcdntMaster, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Esta informação sobre o acidente foi registada.");
	}

	/**
	 * @brief : 교통사고 상세 조회
	 * @details : 교통사고 상세 조회
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : tfcAcdntId
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/mng/detail.do")
	public String acdntMngDetail(Model model, @RequestParam("tfcAcdntId") String tfcAcdntId) {
		MozTfcAcdntMaster tfcAcdntMaster = trafficAcdntService.getMngDetail(tfcAcdntId);
		AccidentDomain aDomain = gService.getAcMapInfo(tfcAcdntId);
		model.addAttribute("aDomain", aDomain);
		model.addAttribute("tfcAcdntMaster", tfcAcdntMaster);

		return "views/accidentmng/acdntMngDetail";
	}

	/**
	 * @brief : 교통사고 수정 화면
	 * @details : 교통사고 수정 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : tfcAcdntId
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/mng/update.do")
	public String acdntMngModify(Model model, @RequestParam("tfcAcdntId") String tfcAcdntId) {

		MozTfcAcdntMaster tfcAcdntMaster = trafficAcdntService.getMngDetail(tfcAcdntId);
		List<String> oldFileArr = tfcAcdntMaster.getTfcAcdntFileInfo().stream()
				.map(MozTfcAcdntFileInfo::getFileOriginNm)
				.filter(Objects::nonNull)
				.collect(Collectors.toList())
				;

		model.addAttribute("oldFileArr", oldFileArr);
		model.addAttribute("tfcAcdntMaster", tfcAcdntMaster);

		List<MozCmCd> acdntCdList = commonCdService.getCdList("ACCIDENT_TYPE");
		List<MozCmCd> dvrLcenTyList = commonCdService.getCmCdByCdGroupId("DVR_LCEN_TY");
		List<MozCmCd> vhTyList = commonCdService.getCmCdByCdGroupId("VEHICLE_TYPE_CD");
		List<MozCmCd> passengerDamageCd = commonCdService.getCdList("PASSENGER_DAMAGE_CD");
		List<MozCmCd> passengerDriverRelationshipCd = commonCdService.getCdList("PASSENGER_DRIVER_RELATIONSHIP_CD");
		List<MozCmCd> accidentDmgCd = commonCdService.getCdList("ACCIDENT_DMG_CD");
		List<MozCmCd> cdList = commonCdService.getCdList("ACCIDENT_TYPE");

		model.addAttribute("acdntCdList", acdntCdList);
		model.addAttribute("passengerDamageCd", passengerDamageCd);
		model.addAttribute("passengerDriverRelationshipCd", passengerDriverRelationshipCd);
		model.addAttribute("accidentDmgCd", accidentDmgCd);
		model.addAttribute("dvrLcenTyList", dvrLcenTyList);
		model.addAttribute("vhTyList", vhTyList);
		model.addAttribute("cdList", cdList);

		return "views/accidentmng/acdntMngModify";
	}

	/**
	 * @brief : 교통사고 정보 수정
	 * @details : 교통사고 정보 수정
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : tfcAcdntMaster
	 * @return :
	 */
	@Authority(type = MethodType.UPDATE)
	@PostMapping("/mng/update.ajax")
	public @ResponseBody CommonResponse<?> acdntMngUpdateAjax(@ModelAttribute MozTfcAcdntMaster tfcAcdntMaster
			, @RequestPart(required = false) MultipartFile[] uploadFiles) {
		ValidateBuilder dtoValidator = new ValidateBuilder(tfcAcdntMaster);
		
		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("acdntDt", new ValidateChecker().setRequired())
				.addRule("acdntTyCd", new ValidateChecker().setRequired())
				.addRule("roadAddr", new ValidateChecker().setRequired().setMaxLength(200, "A localização não pode ter mais de 200 caracteres."))
				.addRule("polId", new ValidateChecker().setRequired())
				.addRule("acdntChildYn", new ValidateChecker().setRequired())
				.addRule("acdntTyDtls", new ValidateChecker().setRequired().setMaxLength(200, "Os pormenores não podem ter mais de 200 caracteres."))
				.addRule("lat", new ValidateChecker().setRequired().setLatitude())
				.addRule("lng", new ValidateChecker().setRequired().setLongitude())
				.isValid();
		
		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}
		
		try {
			trafficAcdntService.upateAcdnt(tfcAcdntMaster, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Estas informações sobre o acidente foram alteradas.");

	}
}
