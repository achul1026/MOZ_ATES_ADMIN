package com.moz.ates.traffic.admin.trafficenforcementmng;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

import com.moz.ates.traffic.admin.common.enums.MethodType;
import com.moz.ates.traffic.admin.config.Authority;
import com.moz.ates.traffic.admin.gis.service.GisService;
import com.moz.ates.traffic.common.component.Pagination;
import com.moz.ates.traffic.common.component.validate.ValidateBuilder;
import com.moz.ates.traffic.common.component.validate.ValidateChecker;
import com.moz.ates.traffic.common.component.validate.ValidateResult;
import com.moz.ates.traffic.common.entity.common.ApiDriverInfoDTO;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.common.EnforcementDomain;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfMaster;
import com.moz.ates.traffic.common.entity.equipment.MozCameraEnfOrg;
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
	private GisService gService;

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
	@PostMapping("/driver/detail.do")
	public String searchDriverDetail(Model model, @ModelAttribute ApiDriverInfoDTO apiDriverInfoDTO) {
		if(apiDriverInfoDTO != null && MozatesCommonUtils.isNull(apiDriverInfoDTO.getDriverLicenseId())) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		
		model.addAttribute("driverInfo", apiDriverInfoDTO);
		model.addAttribute("violationList", trafficEnfService.getViolationInfoList(apiDriverInfoDTO.getDriverLicenseId()));
		return "views/enforcementmng/searchDriverDetail";
	}

	/**
	 * @brief : 운전자 정보 조회
	 * @details : 운전자 정보 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : finePymntInfo
	 * @return :
	 */
//	@Authority(type = MethodType.READ)
//	@PostMapping(value = "/searchDriverAjax")
//	public String searchDriverAjax(Model model, @ModelAttribute EnfSearchVO enfSearchVO) {
//		DriverVO driverDetail = new DriverVO();
//
//		if (enfSearchVO.getCarNum() != null) {
//			driverDetail = trafficEnfService.getDriverDetail(enfSearchVO);
//		}
//
//		model.addAttribute("driverDetail", driverDetail);
//		model.addAttribute("enfSearchVO", enfSearchVO);
//
//		return "views/enforcementmng/searchDriverAjax";
//	}

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
	public String searchCar(Model model, @ModelAttribute EnfSearchVO enfSearchVO) {
		DriverVO driverDetail = new DriverVO();

		if (enfSearchVO.getName() != null) {
			driverDetail = trafficEnfService.getDriverDetail(enfSearchVO);
		}

		model.addAttribute("driverDetail", driverDetail);
		model.addAttribute("enfSearchVO", enfSearchVO);

		return "views/enforcementmng/searchCar";
	}
	
	@Authority(type = MethodType.READ)
	@PostMapping(value = "/vehicle/detail.do")
	public String searchVehicleDetail(Model model, @ModelAttribute ApiDriverInfoDTO apiDriverInfoDTO) {
		if(apiDriverInfoDTO != null && MozatesCommonUtils.isNull(apiDriverInfoDTO.getDriverLicenseId())) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		
		model.addAttribute("driverInfo", apiDriverInfoDTO);
		model.addAttribute("violationList", trafficEnfService.getViolationInfoList(apiDriverInfoDTO.getDriverLicenseId()));
		return "views/enforcementmng/searchCarDetail";
	}

	/**
	 * @brief : 차량 정보 조회
	 * @details : 차량 정보 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : finePymntInfo
	 * @return :
	 */
//	@Authority(type = MethodType.READ)
//	@PostMapping(value = "/vehicle/searchCarAjax")
//	public String searchCarAjax(Model model, @ModelAttribute EnfSearchVO enfSearchVO) {
//		CarVO carDetail = new CarVO();
//
//		if (enfSearchVO.getCarNum() != null) {
//			carDetail = trafficEnfService.getCarDetail(enfSearchVO);
//		}
//
//		model.addAttribute("carDetail", carDetail);
//		model.addAttribute("enfSearchVO", enfSearchVO);
//
//		return "views/enforcementmng/searchCarAjax";
//	}

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
	public String infoRegist(Model model,@ModelAttribute ApiDriverInfoDTO apiDriverInfoDTO) {
		//법률 목록
		List<MozTfcLwInfo> trafficLawList = trafficEnfService.getTrafficLawsListByNotNullFineInfo();
		//납부지 목록
		List<MozPlPymntInfo> placePaymentList = trafficEnfService.getPlacePaymentList();
		
		model.addAttribute("apiDriverInfo",apiDriverInfoDTO);
		model.addAttribute("trafficLawList",trafficLawList);
		model.addAttribute("placePaymentList",placePaymentList);
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
				.addRule("tfcEnfTtl", new ValidateChecker().setRequired().setMaxLength(200, "Equipment name cannot be more than 200 characters"))
				.addRule("tfcEnfDt", new ValidateChecker().setRequired())
				.addRule("vhTy", new ValidateChecker().setRequired().setMaxLength(200, "Classification cannot be more than 200 characters"))
				.addRule("vhRegNo", new ValidateChecker().setRequired().setMaxLength(200, "Vehicle Plate No cannot be more than 200 characters"))
				.addRule("polId", new ValidateChecker().setRequired())
				.addRule("roadAddr", new ValidateChecker().setRequired().setMaxLength(200, "Location cannot be more than 200 characters"))
				.addRule("lat", new ValidateChecker().setRequired())
				.addRule("lng", new ValidateChecker().setRequired())
				.isValid();
		
		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}
		
		try {
			trafficEnfService.insertMozTfcEnfMaster(tfcEnfMaster, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "This Traffic Enforcement Information has been registered.");
	}

	/**
	 * @brief : 교통단속 정보 상세 조회
	 * @details : 교통단속 정보 상세 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfId
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/info/detail.do")
	public String infoDetail(Model model, @RequestParam("tfcEnfId") String tfcEnfId) {
		MozTfcEnfMaster tfcEnfMaster = null;
		EnforcementDomain eDomain = null;
		try {
			tfcEnfMaster = trafficEnfService.getTrafficEnfDetail(tfcEnfId);
			eDomain = gService.getMapInfo(tfcEnfId);
		} catch (CommonException e){
			throw new CommonException(ErrorCode.DEFAULT);
		}
		
		model.addAttribute("eDomain", eDomain);
		model.addAttribute("tfcEnfMaster", tfcEnfMaster);
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

		//납부지 목록
		List<MozPlPymntInfo> placePaymentList = trafficEnfService.getPlacePaymentList();
		
		//법률 목록
		List<MozTfcLwInfo> trafficLawList = trafficEnfService.getTrafficLawsListByNotNullFineInfo();
		
		model.addAttribute("placePaymentList",placePaymentList);
		model.addAttribute("trafficLawList",trafficLawList);
		model.addAttribute("tfcEnfMaster", tfcEnfMaster);
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
		ValidateBuilder dtoValidator = new ValidateBuilder(tfcEnfMaster);
		
		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("tfcEnfTtl", new ValidateChecker().setRequired().setMaxLength(200, "Equipment name cannot be more than 200 characters"))
				.addRule("tfcEnfDt", new ValidateChecker().setRequired())
				.addRule("vhTy", new ValidateChecker().setRequired().setMaxLength(200, "Classification cannot be more than 200 characters"))
				.addRule("vhRegNo", new ValidateChecker().setRequired().setMaxLength(200, "Vehicle Plate No cannot be more than 200 characters"))
				.addRule("polId", new ValidateChecker().setRequired())
				.addRule("roadAddr", new ValidateChecker().setRequired().setMaxLength(200, "Location cannot be more than 200 characters"))
				.addRule("lat", new ValidateChecker().setRequired())
				.addRule("lng", new ValidateChecker().setRequired())
				.isValid();
		
		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}
		
		try {
			trafficEnfService.updateInfo(tfcEnfMaster, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "This Traffic Enforcement Information has been modified.");
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
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "This Traffic Enforcement Information has been deleted");
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
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "This Traffic Enforcement Information has been deleted");
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
		
		model.addAttribute("detectionDetail", trafficEnfService.getViolationDetail(idx));
		model.addAttribute("detectionImageList", trafficEnfService.getViolationImageList(idx));
		model.addAttribute("trafficLawList",trafficLawList);
		model.addAttribute("placePaymentList",placePaymentList);
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
				.isValid();
		
		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}
		
		try {
			trafficEnfService.insertMozTfcEnfMasterForEquipment(tfcEnfMaster);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "This Traffic Enforcement Information has been registered.");
	}
}
