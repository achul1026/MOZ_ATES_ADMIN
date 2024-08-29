package com.moz.ates.traffic.admin.trafficenforcementmng;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.moz.ates.traffic.common.entity.driver.MozVioInfo;
import com.moz.ates.traffic.common.support.exception.CommonResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.moz.ates.traffic.admin.common.enums.MethodType;
import com.moz.ates.traffic.admin.config.Authority;
import com.moz.ates.traffic.admin.gis.service.GisService;
import com.moz.ates.traffic.common.component.Pagination;
import com.moz.ates.traffic.common.component.validate.ValidateBuilder;
import com.moz.ates.traffic.common.component.validate.ValidateChecker;
import com.moz.ates.traffic.common.component.validate.ValidateResult;
import com.moz.ates.traffic.common.entity.api.ItesApiResponse;
import com.moz.ates.traffic.common.entity.api.MojApiRequest;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.common.EnforcementDomain;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfMaster;
import com.moz.ates.traffic.common.entity.equipment.MozCameraEnfOrg;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfFileInfo;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfFineInfo;
import com.moz.ates.traffic.common.entity.law.MozTfcLwFineInfo;
import com.moz.ates.traffic.common.entity.law.MozTfcLwInfo;
import com.moz.ates.traffic.common.entity.payment.MozPlPymntInfo;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.support.exception.ErrorCode;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Controller
@RequestMapping(value = "/enf")
public class TrafficEnfController {

	@Autowired
	private TrafficEnfService trafficEnfService;
	
	@Autowired
	private CommonCdService commonCdService;
	
	@Autowired
	private GisService gService;
    @Autowired
    private MozatesCommonUtils mozatesCommonUtils;

	/**
	 * @brief : 운전자 정보 조회 화면
	 * @details : 운전자 정보 조회 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : finePymntInfo
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/driver/list.do")
	public String searchDriver(Model model) {
		return "views/enforcementmng/searchDriver";
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
	public String searchDriverDetail(Model model,@ModelAttribute ItesApiResponse.ItesApiResponseData itesApiResponseData) {
		if (itesApiResponseData.getCodigo() == 0) {
			throw new CommonResponseException(ErrorCode.INVALID_PARAMETER);
		}
		
		if (!MozatesCommonUtils.isNull(itesApiResponseData.getDataDeNascimento())) {
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
		return "views/enforcementmng/searchDriverDetail";
	}

	/**
	 * methodName : searchDriverListAjax
	 * author : IK.MOON
	 * date : 2024-08-20
	 * description : 면허 정보 - 단속내역 ajax
	 *
	 * @param model
	 * @param vioInfo
	 * @return String
	 */
	@Authority(type = MethodType.READ)
	@PostMapping(value = "/driver/list.ajax")
	public String searchDriverListAjax(Model model, MozVioInfo vioInfo) {

		int page = vioInfo.getPage();
		int totalCnt = trafficEnfService.getViolationInfoCount(vioInfo);
		Pagination pagination = new Pagination(totalCnt, page);

		vioInfo.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("enfHistory", trafficEnfService.getViolationInfoList(vioInfo));
		model.addAttribute("pagination", pagination);
		model.addAttribute("type", "driver");

		return "views/enforcementmng/searchListAjax";
	}

	/**
	 * @brief : 차량 정보 조회 화면
	 * @details : 차량 정보 조회 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : finePymntInfo
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/vehicle/list.do")
	public String searchCar(Model model) {
		return "views/enforcementmng/searchCar";
	}
	
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/vehicle/detail.do")
	public String searchVehicleDetail(Model model, @ModelAttribute MojApiRequest mojApiRequest) {
		if (MozatesCommonUtils.isNull(mojApiRequest.getVhRegNo())) {
			throw new CommonResponseException(ErrorCode.INVALID_PARAMETER);
		}

		if(!MozatesCommonUtils.isNull(mojApiRequest.getDatadenascimento())) {
			String birthDayFormat = MozatesCommonUtils.changeDateFormat(mojApiRequest.getDatadenascimento(), "yyyy-MM-dd'T'HH:mm:ss", "dd.MM.yyyy");
			mojApiRequest.setVioBrth(birthDayFormat);
			mojApiRequest.setVioAddr(MozatesCommonUtils.formatAddress(mojApiRequest.getDomicilio(), mojApiRequest.getProvincia(), mojApiRequest.getDistrito()));
		}

		model.addAttribute("driverInfo", mojApiRequest);

		return "views/enforcementmng/searchCarDetail";
	}

	/**
	 * methodName : searchVehicleListAjax
	 * author : IK.MOON
	 * date : 2024-08-20
	 * description : 차량 정보 - 단속내역 ajax
	 *
	 * @param model
	 * @param tfcEnfMaster
	 * @return String
	 */
	@Authority(type = MethodType.READ)
	@PostMapping(value = "/vehicle/list.ajax")
	public String searchVehicleListAjax(Model model, MozTfcEnfMaster tfcEnfMaster) {

		int page = tfcEnfMaster.getPage();
		int totalCnt = trafficEnfService.getViolationCountByVehicleNo(tfcEnfMaster);
		Pagination pagination = new Pagination(totalCnt, page);

		tfcEnfMaster.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("enfHistory", trafficEnfService.getViolationInfoByVehicleNo(tfcEnfMaster));
		model.addAttribute("pagination", pagination);
		model.addAttribute("type", "vehicle");

		return "views/enforcementmng/searchListAjax";
	}

	/**
	 * @brief : 교통단속 정보 리스트 화면
	 * @details : 교통단속 정보 리스트 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfMaster
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/info/list.do")
	public String infoList(Model model, @ModelAttribute MozTfcEnfMaster tfcEnfMaster) {
		int page = tfcEnfMaster.getPage();
		int totalCnt = trafficEnfService.getInfoListCnt(tfcEnfMaster);
		Pagination pagination = new Pagination(totalCnt, page);

		tfcEnfMaster.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("tfcEnfMaster", tfcEnfMaster);
		model.addAttribute("infoList", trafficEnfService.getInfoList(tfcEnfMaster));
		model.addAttribute("pagination", pagination);

		return "views/enforcementmng/infoList";
	}

	/**
	 * @brief : 교통단속 정보 등록 화면
	 * @details : 교통단속 정보 등록 화면
	 * @author : IK.MOON
	 * @date : 2024.01.10
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/info/save.do")
	public String infoRegist(Model model) {
		//법률 목록
		List<MozTfcLwInfo> trafficLawList = trafficEnfService.getTrafficLawsListByNotNullFineInfo();
		//납부지 목록
		List<MozPlPymntInfo> placePaymentList = trafficEnfService.getPlacePaymentList();
		//라이센스 타입
		List<MozCmCd> dvrLcenTyList = commonCdService.getCmCdByCdGroupId("DVR_LCEN_TY");
		List<MozCmCd> vhTyList = commonCdService.getCmCdByCdGroupId("VEHICLE_TYPE_CD");
		model.addAttribute("trafficLawList",trafficLawList);
		model.addAttribute("dvrLcenTyList",dvrLcenTyList);
		model.addAttribute("vhTyList",vhTyList);
		model.addAttribute("placePaymentList",placePaymentList);
		model.addAttribute("prvList", trafficEnfService.getProvinceList());
		model.addAttribute("distList", trafficEnfService.getDistrictList());
		
		return "views/enforcementmng/infoRegist";
	}
	
	/**
	 * @brief : 교통단속 정보 법률 조회
	 * @details : 교통단속 정보 법률 조회
	 * @author : KC.KIM
	 * @date : 2024.03.19
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@PostMapping(value="/info/lwFineInfo.ajax")
	@ResponseBody
	public CommonResponse<?> viewFineNtcInfo(Model model , @RequestParam("tfcLawId") String tfcLawId) {
		List<MozTfcLwFineInfo> lawFineInfoList = trafficEnfService.getLawFineInfoList(tfcLawId);
		return CommonResponse.ResponseSuccess(HttpStatus.OK,"범칙금 정보 조회 성공", null, lawFineInfoList);
	}
	
	/**
	 * @brief : 교통단속 정보 등록
	 * @details : 교통단속 정보 등록
	 * @author : KC.KIM
	 * @date : 2024.03.06
	 * @param : tfcAcdntMaster
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping(value = "/info/save.ajax")
	public @ResponseBody CommonResponse<?>  infoRegistAjax(@ModelAttribute MozTfcEnfMaster tfcEnfMaster
			,	@RequestPart(required = false) MultipartFile[] uploadFiles){
		ValidateBuilder dtoValidator = new ValidateBuilder(tfcEnfMaster);
		
		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("tfcEnfTtl", new ValidateChecker().setRequired().setMaxLength(200, "O título não pode ter mais de 200 caracteres."))
				.addRule("tfcEnfDt", new ValidateChecker().setRequired())
				.addRule("vhTy", new ValidateChecker().setRequired().setMaxLength(200, "A classificação não pode ter mais de 200 caracteres."))
				.addRule("vhRegNo", new ValidateChecker().setRequired().setMaxLength(200, "O número da matrícula do veículo não pode ter mais de 200 caracteres."))
				.addRule("polId", new ValidateChecker().setRequired())
				.addRule("roadAddr", new ValidateChecker().setRequired().setMaxLength(200, "A localização não pode ter mais de 200 caracteres."))
				.addRule("lat", new ValidateChecker().setRequired().setLatitude())
				.addRule("lng", new ValidateChecker().setRequired().setLongitude())
				.addRule("prvId", new ValidateChecker().setRequired())
				.addRule("distId", new ValidateChecker().setRequired())
				.isValid();
		
		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}
		
		try {
			trafficEnfService.insertMozTfcEnfMaster(tfcEnfMaster, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Esta informação sobre o controlo do tráfego foi registada.");
	}

	/**
	 * @brief : 교통단속 정보 상세 조회
	 * @details : 교통단속 정보 상세 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfId
	 * @return :
	 * @throws Exception 
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/info/detail.do")
	public String infoDetail(Model model, @RequestParam("tfcEnfId") String tfcEnfId) throws Exception {
		MozTfcEnfMaster tfcEnfMaster = null;
		EnforcementDomain eDomain = null;
		List<MozTfcEnfFineInfo> fineInfoList = null;
		try {
			tfcEnfMaster = trafficEnfService.getTrafficEnfDetail(tfcEnfId);
			eDomain = gService.getMapInfo(tfcEnfId);
			fineInfoList = trafficEnfService.getAllTfcEnfFineInfo(tfcEnfId);
		} catch (CommonException e){
			throw new Exception(e.getMessage());
		}
		
		model.addAttribute("eDomain", eDomain);
		model.addAttribute("tfcEnfMaster", tfcEnfMaster);
		model.addAttribute("fineInfoList", fineInfoList);
		return "views/enforcementmng/infoDetail";
	}

	/**
	 * @brief : 교통단속 정보 수정 화면
	 * @details : 교통단속 정보 수정 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfId
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/info/update.do")
	public String infoUpdate(Model model, @RequestParam("tfcEnfId") String tfcEnfId) {
		MozTfcEnfMaster tfcEnfMaster = trafficEnfService.getTrafficEnfDetail(tfcEnfId);
		if (tfcEnfMaster.getFinePymntInfo().getPymntStts().equals("Y")) {
			throw new CommonResponseException(ErrorCode.INVALID_PARAMETER);
		}

		List<MozTfcEnfFineInfo> fineInfoList = trafficEnfService.getAllTfcEnfFineInfo(tfcEnfId);
		//납부지 목록
		List<MozPlPymntInfo> placePaymentList = trafficEnfService.getPlacePaymentList();
		
		//법률 목록
		List<MozTfcLwInfo> trafficLawList = trafficEnfService.getTrafficLawsListByNotNullFineInfo();
		
		List<MozCmCd> dvrLcenTyList = commonCdService.getCmCdByCdGroupId("DVR_LCEN_TY");
		List<MozCmCd> vhTyList = commonCdService.getCmCdByCdGroupId("VEHICLE_TYPE_CD");
		
		List<String> oldFileArr = tfcEnfMaster.getTfcEnfFileInfoList().stream()
				.map(MozTfcEnfFileInfo::getFileNm)
				.filter(Objects::nonNull)
				.collect(Collectors.toList())
				;
		
		model.addAttribute("placePaymentList",placePaymentList);
		model.addAttribute("trafficLawList",trafficLawList);
		model.addAttribute("tfcEnfMaster", tfcEnfMaster);
		model.addAttribute("oldFileArr", oldFileArr);
		model.addAttribute("fineInfoList", fineInfoList);
		model.addAttribute("dvrLcenTyList", dvrLcenTyList);
		model.addAttribute("vhTyList", vhTyList);
		model.addAttribute("prvList", trafficEnfService.getProvinceList());
		model.addAttribute("distList", trafficEnfService.getDistrictList());
		return "views/enforcementmng/infoModify";
	}

	/**
	 * @brief : 교통단속 정보 수정
	 * @details : 교통단속 정보 수정
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfMaster
	 * @param : imageFile
	 * @param : totalPrice
	 * @return :
	 */
	@Authority(type = MethodType.UPDATE)
	@PostMapping(value = "/info/update.ajax")
	public @ResponseBody CommonResponse<?> infoUpdateAjax(@ModelAttribute MozTfcEnfMaster tfcEnfMaster,
			@RequestPart(required = false) MultipartFile[] uploadFiles){
		MozTfcEnfMaster dbTfcEnfMaster = trafficEnfService.getTrafficEnfDetail(tfcEnfMaster.getTfcEnfId());
		if (dbTfcEnfMaster.getFinePymntInfo().getPymntStts().equals("Y")) {
			throw new CommonException(ErrorCode.ENTITY_UPDATE_FAIL);
		}

		ValidateBuilder dtoValidator = new ValidateBuilder(tfcEnfMaster);
		
		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("tfcEnfTtl", new ValidateChecker().setRequired().setMaxLength(200, "O título não pode ter mais de 200 caracteres."))
				.addRule("tfcEnfDt", new ValidateChecker().setRequired())
				.addRule("vhTy", new ValidateChecker().setRequired().setMaxLength(200, "A classificação não pode ter mais de 200 caracteres."))
				.addRule("vhRegNo", new ValidateChecker().setRequired().setMaxLength(200, "O número da matrícula do veículo não pode ter mais de 200 caracteres."))
				.addRule("polId", new ValidateChecker().setRequired())
				.addRule("roadAddr", new ValidateChecker().setRequired().setMaxLength(200, "A localização não pode ter mais de 200 caracteres."))
				.addRule("lat", new ValidateChecker().setRequired().setLatitude())
				.addRule("lng", new ValidateChecker().setRequired().setLongitude())
				.addRule("prvId", new ValidateChecker().setRequired())
				.addRule("distId", new ValidateChecker().setRequired())
				.isValid();
		
		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}
		
		try {
			 trafficEnfService.updateInfo(tfcEnfMaster, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Estas informações sobre o controlo do tráfego foram alteradas.");
	}
	
	/**
	 * @brief : 교통단속 정보 삭제(soft Delete)
	 * @details : 교통단속 정보 삭제(soft Delete)
	 * @author : KC.KIM
	 * @date : 2024.03.11
	 * @param : tfcEnfId
	 * @return :
	 */
	@Authority(type = MethodType.UPDATE)
	@PostMapping("/info/deleteByTfcEnfId.ajax")
	@ResponseBody
	public CommonResponse<?> tfcEnfDeleteAjax(@RequestParam("tfcEnfId") String tfcEnfId) {

		MozTfcEnfMaster dbTfcEnfMaster = trafficEnfService.getTrafficEnfDetail(tfcEnfId);
		if (dbTfcEnfMaster.getFinePymntInfo().getPymntStts().equals("Y")) {
			throw new CommonException(ErrorCode.ENTITY_UPDATE_FAIL);
		}

		MozTfcEnfMaster tfcEnfMaster = new MozTfcEnfMaster();
		tfcEnfMaster.setTfcEnfId(tfcEnfId);

		ValidateBuilder dtoValidator = new ValidateBuilder(tfcEnfMaster);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("tfcEnfId", new ValidateChecker().setRequired())
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}

		try {
			 trafficEnfService.deleteTfcEnfMaster(tfcEnfMaster);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Estas informações sobre o controlo do tráfego foram suprimidas.");
	}
	
	/**
	 * @brief : 교통단속 정보 삭제
	 * @details : 교통단속 정보 삭제
	 * @author : KC.KIM
	 * @date : 2024.03.11
	 * @param : tfcEnfId
	 * @return :
	 */
	@Authority(type = MethodType.DELETE)
	@PostMapping("/info/delete.ajax")
	@ResponseBody
	public CommonResponse<?> sftyInfrmDeleteAjax(@RequestParam("tfcEnfId") String tfcEnfId) {

		MozTfcEnfMaster tfcEnfMaster = new MozTfcEnfMaster();
		tfcEnfMaster.setTfcEnfId(tfcEnfId);

		ValidateBuilder dtoValidator = new ValidateBuilder(tfcEnfMaster);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("tfcEnfId", new ValidateChecker().setRequired())
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}

		try {
			trafficEnfService.deleteTfcEnfMasterByTfcEnfId(tfcEnfMaster);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Estas informações sobre o controlo do tráfego foram suprimidas.");
	}

	/**
	 * @brief : 단속장비 단속자 리스트
	 * @details : 단속장비 단속자 리스트
	 * @author : KY.LEE
	 * @date : 2024.04.06
	 */
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/detection/list.do")
	public String detectionList(Model model , MozCameraEnfOrg mozCameraEnfOrg) {
		int page = mozCameraEnfOrg.getPage();
		int totalCnt = trafficEnfService.getViolationCount(mozCameraEnfOrg);
		
		Pagination pagination = new Pagination(totalCnt, page);
		mozCameraEnfOrg.setStart((page - 1) * pagination.getPageSize());
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("detectionList", trafficEnfService.getViolationList(mozCameraEnfOrg));
		model.addAttribute("searchOption", mozCameraEnfOrg);
		
		return "views/enforcementmng/detectionList";
	}
	
	/**
	 * @brief : 단속장비 단속자 리스트
	 * @details : 단속장비 단속자 리스트
	 * @author : KY.LEE
	 * @date : 2024.04.06
	 */
	@Authority(type = MethodType.READ)
	@GetMapping(value = "/detection/detail.do")
	public String detectionList(Model model , @RequestParam(name="idx", required=true) Long idx) {
		model.addAttribute("detectionDetail", trafficEnfService.getViolationDetail(idx));
		model.addAttribute("detectionImageList", trafficEnfService.getViolationImageList(idx));
		return "views/enforcementmng/detectionDetail";
	}
	
	/**
	 * @brief : 단속 장비 촬영 이미지 view
	 * @details : 단속 장비 촬영 이미지 view
	 * @author : KY.LEE
	 * @date : 2024.04.06
	 */
	@Authority(type = MethodType.READ)
    @GetMapping("/detection/viewImage.do")
    public ResponseEntity<Resource> viewImage(@RequestParam(name="idx",required = true) Long idx) {
    	String filePath = trafficEnfService.getViolationImage(idx);
    	
        try {
            Path file = Paths.get(filePath);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // MIME 타입 설정
                        .body(resource);
            } else {
                // 파일이 존재하지 않거나 읽을 수 없는 경우의 처리
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
        	ResponseEntity.badRequest().build();
        }
        return ResponseEntity.notFound().build();
    }

	/**
	 * methodName : carPlateUpdateAjax
	 * author : IK.MOON
	 * date : 2024-08-23
	 * description : 차량번호 업데이트
	 *
	 * @param model
	 * @param cameraEnfOrg
	 * @return common response
	 */
	@Authority(type = MethodType.UPDATE)
	@PostMapping("/detection/carplate/update.ajax")
	public @ResponseBody CommonResponse<?> carPlateUpdateAjax(Model model, MozCameraEnfOrg cameraEnfOrg) {
		ValidateBuilder dtoValidator = new ValidateBuilder(cameraEnfOrg);
		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("idx", new ValidateChecker().setRequired())
				.addRule("carPlate", new ValidateChecker().setRequired().setMaxLength(20))
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}

		try {
			trafficEnfService.updateCarPlate(cameraEnfOrg);
		} catch (CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "");
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "");
	}

	/**
	 * @brief : 단속장비 단속 등록
	 * @details : 단속장비 단속 등록
	 * @author : KY.LEE
	 * @date : 2024.04.06
	 */
	@Authority(type = MethodType.CREATE)
	@GetMapping(value = "/detection/save.do")
	public String detectionSave(Model model , @RequestParam(name="idx", required=true) Long idx) {
		//법률 목록
		List<MozTfcLwInfo> trafficLawList = trafficEnfService.getTrafficLawsListByNotNullFineInfo();
		//납부지 목록
		List<MozPlPymntInfo> placePaymentList = trafficEnfService.getPlacePaymentList();
		List<MozCmCd> dvrLcenTyList = commonCdService.getCmCdByCdGroupId("DVR_LCEN_TY");
		List<MozCmCd> vhTyList = commonCdService.getCmCdByCdGroupId("VEHICLE_TYPE_CD");
		
		model.addAttribute("detectionDetail", trafficEnfService.getViolationDetail(idx));
		model.addAttribute("detectionImageList", trafficEnfService.getViolationImageList(idx));
		model.addAttribute("trafficLawList",trafficLawList);
		model.addAttribute("placePaymentList",placePaymentList);
		model.addAttribute("prvList", trafficEnfService.getProvinceList());
		model.addAttribute("distList", trafficEnfService.getDistrictList());
		model.addAttribute("dvrLcenTyList", dvrLcenTyList);
		model.addAttribute("vhTyList", vhTyList);
		return "views/enforcementmng/detectionRegist";
	}
	
	/**
	 * @brief : 교통단속 정보 등록
	 * @details : 교통단속 정보 등록
	 * @author : KC.KIM
	 * @date : 2024.03.06
	 * @param : tfcAcdntMaster
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping(value = "/detection/save.ajax")
	public @ResponseBody CommonResponse<?>  detectionRegistAjax(@ModelAttribute MozTfcEnfMaster tfcEnfMaster){
		ValidateBuilder dtoValidator = new ValidateBuilder(tfcEnfMaster);
		
		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("tfcEnfDt", new ValidateChecker().setRequired())
				.addRule("totalPrice", new ValidateChecker().setRequired())
				.addRule("idx", new ValidateChecker().setRequired())
				.addRule("prvId", new ValidateChecker().setRequired())
				.addRule("distId", new ValidateChecker().setRequired())
				.isValid();
		
		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}
		String tfcEnfId = "";

		try {
			 tfcEnfId = trafficEnfService.insertMozTfcEnfMasterForEquipment(tfcEnfMaster);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseSuccess(HttpStatus.OK,
				"Esta informação sobre o controlo do tráfego foi registada.",
				"/enf/info/detail.do",
				tfcEnfId);
	}
}
