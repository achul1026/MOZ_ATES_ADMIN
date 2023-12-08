package com.moz.ates.traffic.admin.trafficenforcementmng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.admin.gis.service.GisService;
import com.moz.ates.traffic.common.component.Pagination;
import com.moz.ates.traffic.common.component.validate.ValidateBuilder;
import com.moz.ates.traffic.common.component.validate.ValidateChecker;
import com.moz.ates.traffic.common.component.validate.ValidateResult;
import com.moz.ates.traffic.common.entity.common.EnforcementDomain;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfHst;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfMaster;
import com.moz.ates.traffic.common.entity.law.MozTfcLwInfo;
import com.moz.ates.traffic.common.entity.payment.MozFinePymntInfo;

@Controller
@RequestMapping(value = "enf")
public class TrafficEnfController {

	@Value("${file.upload.path}")
	private String applPath;

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
	@GetMapping("/search/driver/list.do")
	public String searchDriver(Model model, @ModelAttribute EnfSearchVO enfSearchVO) {

		DriverVO driverDetail = new DriverVO();

		if (enfSearchVO.getName() != null) {
			driverDetail = trafficEnfService.getDriverDetail(enfSearchVO);
		}

		model.addAttribute("driverDetail", driverDetail);
		model.addAttribute("enfSearchVO", enfSearchVO);
		return "views/enforcementmng/searchDriver";
	}

	/**
	 * @brief : 운전자 정보 조회
	 * @details : 운전자 정보 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : finePymntInfo
	 * @return :
	 */
	@PostMapping(value = "searchDriverAjax")
	public String searchDriverAjax(Model model, @ModelAttribute EnfSearchVO enfSearchVO) {
		DriverVO driverDetail = new DriverVO();

		if (enfSearchVO.getCarNum() != null) {
			driverDetail = trafficEnfService.getDriverDetail(enfSearchVO);
		}

		model.addAttribute("driverDetail", driverDetail);
		model.addAttribute("enfSearchVO", enfSearchVO);

		return "views/enforcementmng/searchDriverAjax";
	}

//    @GetMapping(value = "searchDriverDetail")
//    public String searchDriverAjax(Model model, @RequestParam("tfcEnfId")String tfcEnfId){
//        DriverVO driverDetail = new DriverVO();
//
//        model.addAttribute("driverDetail", driverDetail);
//        return "views/enforcementmng/searchDriverDetail";
//    }

	/**
	 * @brief : 차량 정보 조회 화면
	 * @details : 차량 정보 조회 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : finePymntInfo
	 * @return :
	 */
	@GetMapping("/search/car/list.do")
	public String searchCar(Model model, @ModelAttribute EnfSearchVO enfSearchVO) {
		DriverVO driverDetail = new DriverVO();

		if (enfSearchVO.getName() != null) {
			driverDetail = trafficEnfService.getDriverDetail(enfSearchVO);
		}

		model.addAttribute("driverDetail", driverDetail);
		model.addAttribute("enfSearchVO", enfSearchVO);

		return "views/enforcementmng/searchCar";
	}

//	/**
//	 * @brief : 차량 정보 조회
//	 * @details : 차량 정보 조회
//	 * @author : KC.KIM
//	 * @date : 2023.08.08
//	 * @param : finePymntInfo
//	 * @return :
//	 */
//	@PostMapping(value = "searchCarAjax")
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
	@GetMapping(value = "/info/list.do")
	public String infoList(Model model, @ModelAttribute MozTfcEnfMaster tfcEnfMaster) {
		
		int page = 1;
		int totalCnt = trafficEnfService.getInfoListCnt(tfcEnfMaster);
		Pagination pagination = new Pagination(totalCnt, page);
		
		
		model.addAttribute("tfcEnfMaster", tfcEnfMaster);
		model.addAttribute("infoList", trafficEnfService.getInfoList(tfcEnfMaster));
		model.addAttribute("pagination", pagination);
//		model.addAttribute("totalCnt", trafficEnfService.getInfoListCnt(tfcEnfMaster));
		return "views/enforcementmng/infoList";
	}

	/**
	 * @brief : 교통단속 정보 상세 조회
	 * @details : 교통단속 정보 상세 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfId
	 * @return :
	 */
	@RequestMapping(value = "/info/detail.do")
	public String infoDetail(Model model, @RequestParam("tfcEnfId") String tfcEnfId) {

		MozTfcEnfMaster tfcEnfMaster = trafficEnfService.getTrafficEnfDetail(tfcEnfId);
		EnforcementDomain eDomain = gService.getMapInfo(tfcEnfId);
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
	@GetMapping(value = "/info/update.do")
	public String infoUpdate(Model model, @RequestParam("tfcEnfId") String tfcEnfId) {
		System.out.println(applPath);

		MozTfcEnfMaster tfcEnfMaster = trafficEnfService.getTrafficEnfDetail(tfcEnfId);

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
	@PostMapping(value = "/info/update.ajax")
	@ResponseBody
	public Map<String, Object> infoUpdateAjax(@ModelAttribute MozTfcEnfMaster tfcEnfMaster,
			@RequestParam("uploadFile") MultipartFile[] imageFile, @RequestParam("totalPrice") String totalPrice)
			throws IOException {
		Map<String, Object> result = new HashMap<>();
		
		// validation check
		ValidateBuilder dtoValidator = new ValidateBuilder(tfcEnfMaster);
		dtoValidator.addRule("roadAddr", new ValidateChecker().setRequired());
		ValidateResult dtoValidateResult = dtoValidator.isValid();
		if(!dtoValidateResult.isSuccess()) {
			// validation FAIL
			System.out.println("validation FAIL");
			result.put("code", "0");
			return result;
		}
		

		if (!imageFile[0].isEmpty()) {
			String uuid = UUID.randomUUID().toString();
			String DBfileName = "";
			String fileName = "";
			String projectPath = System.getProperty("user.dir") + applPath;
			File makeFolder = new File(projectPath);
			String default_Path = projectPath;
			String default_Name = "notimage.png";

			if (!makeFolder.exists()) {
				makeFolder.mkdir();
				System.out.println("폴더 생성 성공");
			} else {
				System.out.println("해당 폴더가 존재 합니다");
			}
			for (int i = 0; i < imageFile.length; i++) {
				fileName = uuid + "_" + imageFile[i].getOriginalFilename();
				if (i == imageFile.length - 1) {
					DBfileName += fileName;
				} else {
					DBfileName += fileName + ",";
				}
				String file_path = projectPath + fileName;
				File file = new File(file_path);
				if (!imageFile[i].isEmpty()) {
					FileOutputStream fo = new FileOutputStream(file);
					byte[] fileBytes = imageFile[i].getBytes();
					fo.write(fileBytes);
					fo.close();
					tfcEnfMaster.setTfcEnfImagepath(file_path);
					tfcEnfMaster.setTfcEnfInfoimage(DBfileName);
				} else {
					tfcEnfMaster.setTfcEnfImagepath(default_Path);
					tfcEnfMaster.setTfcEnfInfoimage(default_Name);
				}
			}
		} else {
			System.out.println("사진파일 수정x");
		}

		try {
			trafficEnfService.updateInfo(tfcEnfMaster);
			// 벌금 수정
			String chgPrice = totalPrice != null && !"".equals(totalPrice) ? totalPrice : "0";
			MozFinePymntInfo finePymntInfo = new MozFinePymntInfo();
			finePymntInfo.setTfcEnfId(tfcEnfMaster.getTfcEnfId());
			finePymntInfo.setTotalPrice(chgPrice);
			trafficEnfService.updateInfoPrice(finePymntInfo);
			result.put("code", "1");
		} catch (Exception e) {
			result.put("code", "0");
		}

		return result;
	}

	/**
	 * @brief : 위반 차량 사진 삭제
	 * @details : 위반 차량 사진 삭제
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfMaster
	 * @return :
	 */
	@PostMapping(value = "/info/image/delete.ajax") //infoImageDelteAjax
	public String imageDelete(@ModelAttribute MozTfcEnfMaster tfcEnfMaster) {
		String default_Name = "notimage.png";
		String default_Path = System.getProperty("user.dir") + applPath;
		tfcEnfMaster.setTfcEnfInfoimage(default_Name);
		tfcEnfMaster.setTfcEnfImagepath(default_Path);

		try {
			trafficEnfService.deleteEnfImage(tfcEnfMaster);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "views/enforcementmng/infoModify";
	}

	/**
	 * @brief : 교통단속 법률관리 리스트 화면
	 * @details : 교통단속 법률관리 리스트 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLwInfo
	 * @return :
	 */
	@RequestMapping(value = "/law/list.do")
	public String lawList(Model model, @ModelAttribute MozTfcLwInfo tfcLwInfo) {

		model.addAttribute("tfcLwInfo", tfcLwInfo);
		model.addAttribute("lawLists", trafficEnfService.getLawList(tfcLwInfo));
		model.addAttribute("totalCnt", trafficEnfService.getLawListCnt(tfcLwInfo));
		return "views/enforcementmng/lawList";
	}

	/**
	 * @brief : 교통단속 법률관리 등록 화면
	 * @details : 교통단속 법률관리 등록 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param :
	 * @return :
	 */
	@RequestMapping(value = "/law/save.do")
	public String lawRegist(Model model) {

		return "views/enforcementmng/lawRegist";
	}

	/**
	 * @brief : 교통단속 법률관리 등록
	 * @details : 교통단속 법률관리 등록
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLwInfo
	 * @return :
	 */
	@PostMapping(value = "/law/save.ajax")
	@ResponseBody
	public Map<String, Object> lawSaveAjax(@ModelAttribute MozTfcLwInfo tfcLwInfo) {
		Map<String, Object> result = new HashMap<>();

		try {
			trafficEnfService.lawSave(tfcLwInfo);
			result.put("code", "1");
		} catch (Exception e) {
			result.put("code", "0");
		}

		return result;
	}

	/**
	 * @brief : 교통단속 법률관리 상세 조회
	 * @details : 교통단속 법률관리 상세 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLawId
	 * @return :
	 */
	@GetMapping(value = "/law/datail.do")
	public String lawDetail(Model model, @RequestParam("lawId") String tfcLawId) {

		MozTfcLwInfo tfcLwInfo = trafficEnfService.getLawDetail(tfcLawId);
		model.addAttribute("tfcLwInfo", tfcLwInfo);

		return "views/enforcementmng/lawDetail";
	}

	/**
	 * @brief : 교통단속 법률관리 수정 화면
	 * @details : 교통단속 법률관리 수정 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLawId
	 * @return :
	 */
	@GetMapping(value = "/law/update.do")
	public String lawUpdate(Model model, @RequestParam("tfcLawId") String tfcLawId) {

		MozTfcLwInfo tfcLwInfo = trafficEnfService.getLawDetail(tfcLawId);
		model.addAttribute("tfcLwInfo", tfcLwInfo);

		return "views/enforcementmng/lawModfy";
	}

	/**
	 * @brief : 교통단속 법률관리 정보 수정
	 * @details : 교통단속 법률관리 정보 수정
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLwInfo
	 * @return :
	 */
	@PostMapping(value = "/law/update.ajax")
	@ResponseBody
	public Map<String, Object> lawUpdateAjax(@ModelAttribute MozTfcLwInfo tfcLwInfo) {
		Map<String, Object> result = new HashMap<>();

		try {
			trafficEnfService.updateLaw(tfcLwInfo);
			result.put("code", "1");
		} catch (Exception e) {
			result.put("code", "0");
		}

		return result;

	}

	/**
	 * @brief : 교통단속 법률관리 정보 수정
	 * @details : 교통단속 법률관리 정보 수정
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLawId
	 * @return :
	 */
	@PostMapping(value = "/law/delete.ajax")
	@ResponseBody
	public Map<String, Object> lawDeleteAjax(@RequestParam("tfcLawId") String tfcLawId) {
		Map<String, Object> result = new HashMap<>();

		try {
			trafficEnfService.deleteLaw(tfcLawId);
			result.put("code", "1");
		} catch (Exception e) {
			result.put("code", "0");
		}

		return result;

	}

	/**
	 * @brief : 교통단속 로그 리스트 화면
	 * @details : 교통단속 로그 리스트 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfHst
	 * @return :
	 */
	@GetMapping(value = "/log/list.do")
	public String logList(Model model, @ModelAttribute MozTfcEnfHst tfcEnfHst) {

		model.addAttribute("tfcEnfHst", tfcEnfHst);
		model.addAttribute("logLists", trafficEnfService.getLogList(tfcEnfHst));
		model.addAttribute("totalCnt", trafficEnfService.getLogListCnt(tfcEnfHst));

		return "views/enforcementmng/logList";
	}

}
