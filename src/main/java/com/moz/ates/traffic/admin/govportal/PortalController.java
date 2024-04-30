package com.moz.ates.traffic.admin.govportal;

import java.util.List;

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
import com.moz.ates.traffic.admin.common.enums.MethodType;
import com.moz.ates.traffic.admin.config.Authority;
import com.moz.ates.traffic.common.component.Pagination;
import com.moz.ates.traffic.common.component.validate.ValidateBuilder;
import com.moz.ates.traffic.common.component.validate.ValidateChecker;
import com.moz.ates.traffic.common.component.validate.ValidateResult;
import com.moz.ates.traffic.common.entity.board.MozAtchFile;
import com.moz.ates.traffic.common.entity.board.MozBrd;
import com.moz.ates.traffic.common.entity.board.MozComplaintsReg;
import com.moz.ates.traffic.common.entity.board.MozFaq;
import com.moz.ates.traffic.common.entity.board.MozInqry;
import com.moz.ates.traffic.common.entity.board.MozObjReg;
import com.moz.ates.traffic.common.entity.board.MozTfcSftyEdctn;
import com.moz.ates.traffic.common.entity.board.MozTfcSftyInfrm;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.payment.MozPlPymntInfo;
import com.moz.ates.traffic.common.support.exception.CommonException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "portal")
public class PortalController {

	@Autowired
	private CommonCdService commonCdService;

	@Autowired
	private PortalService portalService;


	/**
	 * @brief : 포탈 공지사항 리스트 화면
	 * @details : 포탈 공지사항 리스트 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : brd
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/notice/list.do")
	public String noticeList(Model model, @ModelAttribute MozBrd brd) {
		int page = brd.getPage();
		int totalCnt = portalService.getNoticeListCnt(brd);
		Pagination pagination = new Pagination(totalCnt, page);

		brd.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("brd", brd);
		model.addAttribute("noticeList", portalService.getNoticeList(brd));
		model.addAttribute("pagination", pagination);

		return "views/govportal/noticeList";
	}

	/**
	 * @brief : 포탈 공지사항 등록 화면
	 * @details : 포탈 공지사항 등록 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/notice/save.do")
	public String noticeRegist(Model model) {

		List<MozCmCd> cdList = commonCdService.getCdList("ntc");
		model.addAttribute("cdList", cdList);

		return "views/govportal/noticeRegist";
	}

	/**
	 * @brief : 포탈 공지사항 등록
	 * @details : 포탈 공지사항 등록
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : brd
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping("/notice/save.ajax")
	public @ResponseBody CommonResponse<?> noticeRegistAjax(MozBrd brd
			, @RequestPart(required = false) MultipartFile[] uploadFiles) {

		ValidateBuilder dtoValidator = new ValidateBuilder(brd);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("boardTitle", new ValidateChecker().setRequired().setMaxLength(200, "Title cannot be more than 200 characters."))
				.addRule("imprtYn", new ValidateChecker().setRequired())
				.addRule("useYn", new ValidateChecker().setRequired())
				.addRule("popupYn", new ValidateChecker().setRequired())
				.addRule("boardContents", new ValidateChecker().setMaxLength(200, "Contents cannot be more than 200 characters."))
				.isValid();
		
		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
					dtoValidatorResult.getMessage());
		}
		
		try {
			portalService.registNotice(brd, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The Notice has been registered.");
	}

	/**
	 * @brief : 포탈 공지사항 삭제
	 * @details : 포탈 공지사항 삭제
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : boardIdx
	 * @return :
	 */
	@Authority(type = MethodType.DELETE)
	@PostMapping("/notice/delete.ajax")
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
			portalService.deleteNotice(boardIdx);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The Notice has been deleted.");
	}

	/**
	 * @brief : 포탈 공지사항 상세 화면
	 * @details : 포탈 공지사항 상세 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : boardIdx
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/notice/detail.do")
	public String noticeDetail(Model model, @RequestParam("boardIdx") String boardIdx) {

		MozBrd brd = portalService.getNoticeDetail(boardIdx);
		model.addAttribute("brd", brd);
		return "views/govportal/noticeDetail";
	}

	/**
	 * @brief : 포탈 공지사항 수정 화면
	 * @details : 포탈 공지사항 수정 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : boardIdx
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/notice/update.do")
	public String noticeModify(Model model, @RequestParam("boardIdx") String boardIdx) {
		MozBrd brd = portalService.getNoticeDetail(boardIdx);
		model.addAttribute("brd", brd);
		
		return "views/govportal/noticeModify";
	}

	/**
	 * @brief : 포탈 공지사항 수정
	 * @details : 포탈 공지사항 수정
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : brd
	 * @param : uploadFiles
	 * @return :
	 */
	@Authority(type = MethodType.UPDATE)
	@PostMapping("/notice/update.ajax")
	@ResponseBody
	public CommonResponse<?> noticeModifyAjax(@ModelAttribute MozBrd brd
			, @RequestPart(required = false) MultipartFile[] uploadFiles) {
		ValidateBuilder dtoValidator = new ValidateBuilder(brd);
		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("boardTitle", new ValidateChecker().setRequired().setMaxLength(200, "Title cannot be more than 200 characters."))
				.addRule("imprtYn", new ValidateChecker().setRequired())
				.addRule("useYn", new ValidateChecker().setRequired())
				.addRule("popupYn", new ValidateChecker().setRequired())
				.addRule("boardContents", new ValidateChecker().setMaxLength(200, "Contents cannot be more than 200 characters."))
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}

		try {
			portalService.updateNotice(brd, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The Notice has been modified.");
	}


	/**
	 * @brief : FAQ 리스트 화면
	 * @details : FAQ 리스트 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : faq
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/faq/list.do")
	public String faqList(Model model, @ModelAttribute MozFaq faq) {
		int page = faq.getPage();
		int totalCnt = portalService.getFaqListCnt(faq);
		Pagination pagination = new Pagination(totalCnt, page);

		faq.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("faq", faq);
		model.addAttribute("faqList", portalService.getFaqList(faq));
		model.addAttribute("pagination", pagination);

		return "views/govportal/faqList";
	}

	/**
	 * @brief : FAQ 등록 화면
	 * @details : FAQ 등록 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/faq/save.do")
	public String faqRegist(Model model) {
		List<MozCmCd> cdList = commonCdService.getCdList("FAQ_TYPE_CD");
		model.addAttribute("cdList", cdList);

		return "views/govportal/faqRegist";
	}

	/**
	 * @brief : FAQ 등록
	 * @details : FAQ 등록
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : faq
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping("/faq/save.ajax")
	@ResponseBody
	public CommonResponse<?> faqRegistAjax(MozFaq faq) {
		ValidateBuilder dtoValidator = new ValidateBuilder(faq);
		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("postTitle", new ValidateChecker().setRequired().setMaxLength(200, "Title cannot be more than 200 characters."))
				.addRule("cateTy", new ValidateChecker().setRequired())
				.addRule("postContent", new ValidateChecker().setRequired().setMaxLength(200, "Content cannot be more than 200 characters."))
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}

		try {
			portalService.registFaq(faq);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The FAQ has been registered.");
	}

	/**
	 * @brief : FAQ 상세 조회
	 * @details : FAQ 상세 조회
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : faqIdx
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/faq/detail.do")
	public String faqDetail(Model model, @RequestParam("faqIdx") String faqIdx) {

		List<MozCmCd> cdList = commonCdService.getCdList("FAQ_TYPE_CD");
		model.addAttribute("cdList", cdList);

		MozFaq faq = portalService.getFaqDetail(faqIdx);
		model.addAttribute("faq", faq);

		return "views/govportal/faqDetail";
	}

	/**
	 * @brief : FAQ 수정 화면
	 * @details : FAQ 수정 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : faqIdx
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/faq/update.do")
	public String faqModify(Model model, @RequestParam("faqIdx") String faqIdx) {

		List<MozCmCd> cdList = commonCdService.getCdList("FAQ_TYPE_CD");
		model.addAttribute("cdList", cdList);

		MozFaq faq = portalService.getFaqDetail(faqIdx);
		model.addAttribute("faq", faq);

		return "views/govportal/faqModify";
	}

	/**
	 * @brief : FAQ 수정
	 * @details : FAQ 수정
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : faq
	 * @return :
	 */
	@Authority(type = MethodType.UPDATE)
	@PostMapping("/faq/update.ajax")
	@ResponseBody
	public CommonResponse<?> faqModifyAjax(@ModelAttribute MozFaq faq) {
		ValidateBuilder dtoValidator = new ValidateBuilder(faq);
		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("postTitle", new ValidateChecker().setRequired().setMaxLength(200, "Title cannot be more than 200 characters."))
				.addRule("cateTy", new ValidateChecker().setRequired())
				.addRule("postContent", new ValidateChecker().setRequired().setMaxLength(200, "Content cannot be more than 200 characters."))
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}

		try {
			portalService.updateFaq(faq);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The FAQ has been modified.");
	}

	/**
	 * @brief : FAQ 삭제
	 * @details : FAQ 삭제
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : faqIdx
	 * @return :
	 */
	@Authority(type = MethodType.DELETE)
	@PostMapping("/faq/delete.ajax")
	@ResponseBody
	public CommonResponse<?> faqDeleteAjax(@RequestParam("faqIdx") String faqIdx) {

		MozFaq mozFaq = new MozFaq();
		mozFaq.setFaqIdx(faqIdx);

		ValidateBuilder dtoValidator = new ValidateBuilder(mozFaq);

		ValidateResult dtoValidatorResult =
				dtoValidator.addRule("faqIdx", new ValidateChecker().setRequired()).isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
					dtoValidatorResult.getMessage());
		}

		try {
			portalService.deleteFaq(faqIdx);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The FAQ has been deleted");
	}
	
	/**
	 * @brief : 문의하기 리스트 화면
	 * @details : 문의하기 리스트 화면
	 * @author : KC.KIM
	 * @date : 2024.01.29
	 * @param : model
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/inqr/list.do")
	public String inquiryList(Model model, @ModelAttribute MozInqry mozInqry) {
		int page = mozInqry.getPage();
		int totalCnt = portalService.getMozInqryCnt(mozInqry);
		Pagination pagination = new Pagination(totalCnt, page);

		mozInqry.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("mozInqry", mozInqry);
		model.addAttribute("inqryList", portalService.getMozInqryList(mozInqry));
		model.addAttribute("pagination", pagination);
		return "views/govportal/inquiryList";
	}

	/**
	 * @Method Name : inquiryDetail
	 * @Date : 2024. 1. 29.
	 * @Author : IK.MOON
	 * @Method Brief : 문의하기 상세 페이지
	 * @return
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/inqr/detail.do")
	public String inquiryDetail(Model model, @RequestParam("inqryId") String inqryId) {
		MozInqry inqry = portalService.getMozInqryDetail(inqryId);
		
		List<MozAtchFile> ansFileList = portalService.findAllMozAtchFileByAtchIdx(inqryId);
		inqry.setAnsAtchFileList(ansFileList);
		model.addAttribute("inqry", inqry);
		return "views/govportal/inquiryDetail";
	}
	
	/**
	 * @brief : 문의하기 답변 등록
	 * @details : 문의하기 답변 등록
	 * @author : KC.KIM
	 * @date : 2024.02.20
	 * @param : mozInqry
	 * @param : uploadFiles
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping("/inqr/answer.ajax")
	@ResponseBody
	public CommonResponse<?> inqryAnswerAjax(@ModelAttribute MozInqry mozInqry
			, @RequestPart(required = false) MultipartFile[] uploadFiles) {
		ValidateBuilder dtoValidator = new ValidateBuilder(mozInqry);
		ValidateResult validationResult = dtoValidator
				.addRule("inqryId", new ValidateChecker().setRequired())
				.addRule("ansCn", new ValidateChecker().setRequired().setMaxLength(200,"Answer Contents cannot be more than 200 characters."))
				.isValid();
		
		if (!validationResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, validationResult.getMessage());
		}
		try {
			portalService.updateMozInqry(mozInqry, uploadFiles);
		} catch (CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Response registration has been completed.");
	}

	/**
	 * @brief : 이의제기 리스트 화면
	 * @details : 이의제기 리스트 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : objReg
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/objection/list.do")
	public String objectionList(Model model, @ModelAttribute MozObjReg objReg) {
		int page = objReg.getPage();
		int totalCnt = portalService.getObjectionListCnt(objReg);
		Pagination pagination = new Pagination(totalCnt, page);

		objReg.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("objReg", objReg);
		model.addAttribute("objectionList", portalService.getObjectionList(objReg));
		model.addAttribute("pagination", pagination);

		return "views/govportal/objectionList";
	}

	/**
	 * @brief : 이의제기 상세 조회
	 * @details : 이의제기 상세 조회
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : objIdx
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/objection/detail.do")
	public String objectionDetail(Model model, @RequestParam("objIdx") String objIdx) {
		MozObjReg objReg = portalService.getObjectionDetail(objIdx);
		
		List<MozAtchFile> ansFileList = portalService.findAllMozAtchFileByAtchIdx(objIdx);
		objReg.setAnsAtchFileList(ansFileList);
		model.addAttribute("objReg", objReg);
		return "views/govportal/objectionDetail";
	}

	/**
	 * @brief : 이의제기 답변 등록
	 * @details : 이의제기 답변 등록
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : objReg
	 * @param : uploadFiles
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping("/objection/answer.ajax")
	@ResponseBody
	public CommonResponse<?> objSendEmail(@ModelAttribute MozObjReg objReg
			, @RequestPart(required = false) MultipartFile[] uploadFiles) {
		ValidateBuilder dtoValidator = new ValidateBuilder(objReg);
		ValidateResult validationResult = dtoValidator
				.addRule("objIdx", new ValidateChecker().setRequired())
				.addRule("ansContents", new ValidateChecker().setRequired().setMaxLength(200,"Answer Contents cannot be more than 200 characters."))
				.isValid();
		
		if (!validationResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, validationResult.getMessage());
		}
		try {
			portalService.updateObjAns(objReg, uploadFiles);
		} catch (CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Response registration has been completed.");
	}

	/**
	 * @brief : 민원 리스트 화면
	 * @details : 민원 리스트 화면
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : complaintsReg
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/complaint/list.do")
	public String complaintList(Model model, @ModelAttribute MozComplaintsReg complaintsReg) {
		int page = complaintsReg.getPage();
		int totalCnt = portalService.getComplaintListCnt(complaintsReg);
		Pagination pagination = new Pagination(totalCnt, page);

		complaintsReg.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("complaintsReg", complaintsReg);
		model.addAttribute("complaintList", portalService.getComplaintList(complaintsReg));
		model.addAttribute("pagination", pagination);
		return "views/govportal/complaintList";
	}

	/**
	 * @brief : 민원 상세 조회
	 * @details : 민원 상세
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : complaintsIdx
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/complaint/detail.do")
	public String complaintDetail(Model model, @RequestParam("complaintsIdx") String complaintsIdx) {
		MozComplaintsReg complaintsReg = portalService.getComplaintDetail(complaintsIdx);
		List<MozAtchFile> ansFileList = portalService.findAllMozAtchFileByAtchIdx(complaintsIdx);
		complaintsReg.setAnsAtchFileList(ansFileList);
		model.addAttribute("complaintsReg", complaintsReg);

		return "views/govportal/complaintDetail";
	}

	/**
	 * @brief : 민원 답변 등록
	 * @details : 민원 답변 등록
	 * @author : KC.KIM
	 * @date : 2023.08.04
	 * @param : complaintsReg
	 * @param : uploadFiles
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping(value = "/complaint/answer.ajax")
	@ResponseBody
	public CommonResponse<?> answerCmplt(@ModelAttribute MozComplaintsReg complaintsReg
			, @RequestPart(required = false) MultipartFile[] uploadFiles) {
		ValidateBuilder dtoValidator = new ValidateBuilder(complaintsReg);
		ValidateResult validationResult = dtoValidator
				.addRule("complaintsIdx", new ValidateChecker().setRequired())
				.addRule("ansContents", new ValidateChecker().setRequired().setMaxLength(200,"Answer Contents cannot be more than 200 characters."))
				.isValid();
		
		if (!validationResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, validationResult.getMessage());
		}
		try {
			portalService.updateCmpAns(complaintsReg, uploadFiles);
		} catch (CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Response registration has been completed.");
	}

	/**
	 * @brief : 범칙금 납부처 리스트 화면
	 * @details : 범칙금 납부처 리스트 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : plPymntInfo
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/penaltyplace/list.do")
	public String penaltyPlaceList(Model model, @ModelAttribute MozPlPymntInfo plPymntInfo) {

		int page = plPymntInfo.getPage();
		int totalCnt = portalService.getPenaltyPlaceListCnt(plPymntInfo);
		Pagination pagination = new Pagination(totalCnt, page);

		plPymntInfo.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("plPymntInfo", plPymntInfo);
		model.addAttribute("plPymntList", portalService.getPenaltyPlaceList(plPymntInfo));
		model.addAttribute("pagination", pagination);
		
		return "views/govportal/penaltyPlaceList";
	}

	/**
	 * @brief : 범칙금 납부처 등록 화면
	 * @details : 범칙금 납부처 등록 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/penaltyplace/save.do")
	public String penaltyPlaceRegist(Model model) {
		List<MozCmCd> cdList = commonCdService.getCdList("PYMNT_PLCCE_TYPE");
		model.addAttribute("cdList", cdList);
		
		return "views/govportal/penaltyPlaceRegist";
	}

	/**
	 * @brief : 범칙금 납부처 등록
	 * @details : 범칙금 납부처 등록
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param :
	 * @return :
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping("/penaltyplace/save.ajax")
	@ResponseBody
	public CommonResponse<?> penaltyPlaceRegistAjax(MozPlPymntInfo plPymntInfo) {

		ValidateBuilder dtoValidator = new ValidateBuilder(plPymntInfo);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("placePymntNm", new ValidateChecker().setRequired().setMaxLength(200, "Name cannot be more than 200 characters."))
				.addRule("placePymntAddr", new ValidateChecker().setRequired().setMaxLength(200, "Address cannot be more than 200 characters."))
				.addRule("placePymntCntc", new ValidateChecker().setRequired().setMaxLength(200, "Address cannot be more than 200 characters."))
				.addRule("operStrTm", new ValidateChecker().setRequired())
				.addRule("operEndTm", new ValidateChecker().setRequired())
				.addRule("clsdDt", new ValidateChecker().setRequired())
				.addRule("placePymntRprsvNm", new ValidateChecker().setRequired().setMaxLength(200, "Representative Name cannot be more than 200 characters."))
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
					dtoValidatorResult.getMessage());
		}

		try {
			portalService.registPenaltyPlace(plPymntInfo);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,
				"The New Payment Location has been saved");
	}

	/**
	 * @brief : 범칙금 납부처 상세
	 * @details : 범칙금 납부처 상세
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : placePymntId
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/penaltyplace/detail.do")
	public String penaltyPlaceDetail(Model model, @RequestParam("placePymntId") String placePymntId) {

		MozPlPymntInfo plPymntInfo = portalService.getPenaltyPlaceDetail(placePymntId);
		model.addAttribute("plPymntInfo", plPymntInfo);

		return "views/govportal/penaltyPlaceDetail";
	}

	/**
	 * @brief : 범칙금 납부처 수정 화면
	 * @details : 범칙금 납부처 수정 화면
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : placePymntId
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/penaltyplace/update.do")
	public String penaltyPlaceModify(Model model, @RequestParam("placePymntId") String placePymntId) {		
		List<MozCmCd> cdList = commonCdService.getCdList("PYMNT_PLCCE_TYPE");
		MozPlPymntInfo plPymntInfo = portalService.getPenaltyPlaceDetail(placePymntId);
		
		model.addAttribute("cdList", cdList);
		model.addAttribute("plPymntInfo", plPymntInfo);

		return "views/govportal/penaltyPlaceModify";
	}

	/**
	 * @brief : 범칙금 납부처 수정
	 * @details : 범칙금 납부처 수정
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : placePymntId
	 * @return :
	 */
	@Authority(type = MethodType.UPDATE)
	@PostMapping("/penaltyplace/update.ajax")
	@ResponseBody
	public CommonResponse<?> penaltyPlaceModifyAjax(@ModelAttribute MozPlPymntInfo plPymntInfo) {

		ValidateBuilder dtoValidator = new ValidateBuilder(plPymntInfo);

		ValidateResult validationResult = dtoValidator
				.addRule("placePymntNm", new ValidateChecker().setRequired().setMaxLength(200, "Name cannot be more than 200 characters."))
				.addRule("placePymntAddr", new ValidateChecker().setRequired().setMaxLength(200, "Address cannot be more than 200 characters."))
				.addRule("placePymntCntc", new ValidateChecker().setRequired().setMaxLength(200, "Address cannot be more than 200 characters."))
				.addRule("operStrTm", new ValidateChecker().setRequired())
				.addRule("operEndTm", new ValidateChecker().setRequired())
				.addRule("clsdDt", new ValidateChecker().setRequired())
				.addRule("placePymntRprsvNm", new ValidateChecker().setRequired().setMaxLength(200, "Representative Name cannot be more than 200 characters."))
				.isValid();

		if (!validationResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
					validationResult.getMessage());
		}

		try {
			portalService.updatePenaltyPlace(plPymntInfo);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,
				"The Payment Location has been modified.");
	}

	/**
	 * @brief : 범칙금 납부처 삭제
	 * @details : 범칙금 납부처 삭제
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : placePymntId
	 * @return :
	 */
	@Authority(type = MethodType.DELETE)
	@PostMapping("/penaltyplace/delete.ajax")
	@ResponseBody
	public CommonResponse<?> penaltyPlaceDeleteAjax(
			@RequestParam("placePymntId") String placePymntId) {

		MozPlPymntInfo plPymntInfo = new MozPlPymntInfo();
		plPymntInfo.setPlacePymntId(placePymntId);

		ValidateBuilder dtoValidator = new ValidateBuilder(plPymntInfo);

		ValidateResult dtoValidatorResult =
				dtoValidator.addRule("placePymntId", new ValidateChecker().setRequired()).isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
					dtoValidatorResult.getMessage());
		}

		try {
			portalService.deletePenaltyPlace(placePymntId);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,
				"The Payment Location has been deleted");
	}
	
	/**
	 * @brief : 교통안전정보 리스트 화면
	 * @details : 교통안전정보 리스트 화면
	 * @author : KC.KIM
	 * @date : 2024.01.29
	 * @param : model
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/sftyInfrm/list.do")
	public String sftyInfrmList(Model model, @ModelAttribute MozTfcSftyInfrm mozTfcSftyInfrm) {
		int page = mozTfcSftyInfrm.getPage();
		int totalCnt = portalService.getSftyInfrmListCnt(mozTfcSftyInfrm);
		Pagination pagination = new Pagination(totalCnt, page);

		mozTfcSftyInfrm.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("mozTfcSftyInfrm", mozTfcSftyInfrm);
		model.addAttribute("sftyInfrmList", portalService.getSftyInfrmList(mozTfcSftyInfrm));
		model.addAttribute("pagination", pagination);
		return "views/govportal/sftyInfrmList";
	}

	/**
	 * @Method Name : safetyInfoDetail
	 * @Date : 2024. 1. 29.
	 * @Author : IK.MOON
	 * @Method Brief : 교통 안전정보 상세 페이지
	 * @return
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/sftyInfrm/detail.do")
	public String safetyInfoDetail(Model model, @RequestParam("tfcSftyInfrmId") String tfcSftyInfrmId) {
		
		MozTfcSftyInfrm sftyInfrm = portalService.getSftyInfrmDetail(tfcSftyInfrmId);
		model.addAttribute("sftyInfrm", sftyInfrm);
		return "views/govportal/sftyInfrmDetail";
	}

	/**
	 * @Method Name : safetyInfoRegist
	 * @Date : 2024. 1. 29.
	 * @Author : IK.MOON
	 * @Method Brief : 교통 안전정보 등록 페이지
	 * @return
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/sftyInfrm/save.do")
	public String safetyInfoRegist() {
		return "views/govportal/sftyInfrmRegist";
	}
	
	/**
	 * @Method Name : safetyInfoRegist
	 * @Date : 2024. 2. 19.
	 * @Author : KC.KIM
	 * @Method Brief : 교통 안전정보 등록
	 * @return
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping("/sftyInfrm/save.ajax")
	public @ResponseBody CommonResponse<?> sftyInfrmRegistAjax(MozTfcSftyInfrm mozTfcSftyInfrm
			, @RequestPart(required = false) MultipartFile uploadFiles) {

		ValidateBuilder dtoValidator = new ValidateBuilder(mozTfcSftyInfrm);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("postTtl", new ValidateChecker().setRequired().setMaxLength(200, "Title cannot be more than 200 characters"))
				.addRule("postCn", new ValidateChecker().setMaxLength(200, "Contents cannot be more than 200 characters"))
				.addRule("expYn", new ValidateChecker().setRequired())
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
					dtoValidatorResult.getMessage());
		}

		try {
			portalService.registSftyInfrm(mozTfcSftyInfrm, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "The Notice has been registered.");
	}
	
	/**
	 * @brief : 교통안전정보 삭제
	 * @details : 교통안전정보 삭제
	 * @author : KC.KIM
	 * @date : 2024.02.19
	 * @param : tfcSftyInfrmId
	 * @return :
	 */
	@Authority(type = MethodType.DELETE)
	@PostMapping("/sftyInfrm/delete.ajax")
	@ResponseBody
	public CommonResponse<?> sftyInfrmDeleteAjax(
			@RequestParam("tfcSftyInfrmId") String tfcSftyInfrmId) {

		MozTfcSftyInfrm sftyInfrm = new MozTfcSftyInfrm();
		sftyInfrm.setTfcSftyInfrmId(tfcSftyInfrmId);

		ValidateBuilder dtoValidator = new ValidateBuilder(sftyInfrm);

		ValidateResult dtoValidatorResult =
				dtoValidator.addRule("tfcSftyInfrmId", new ValidateChecker().setRequired()).isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}

		try {
			portalService.deleteSftyInfrmByTfcSftyInfrmId(tfcSftyInfrmId);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "This Traffic Safety Information has been deleted");
	}
	
	/**
	 * @brief : 교통안전정보 수정 화면 이동
	 * @details : 교통안전정보 수정 화면 이동
	 * @author : KC.KIM
	 * @date : 2024.02.19
	 * @param : tfcSftyInfrmId
	 * @return :
	 */
    @Authority(type = MethodType.READ)
    @GetMapping("/sftyInfrm/update.do")
    public String sftyInfrmModify(Model model,@RequestParam("tfcSftyInfrmId")String tfcSftyInfrmId){
    	MozTfcSftyInfrm mozTfcSftyInfrm = portalService.getSftyInfrmDetail(tfcSftyInfrmId);
		model.addAttribute("sftyInfrm", mozTfcSftyInfrm);

		return "views/govportal/sftyInfrmModify";
    }
    
    /**
	 * @Method Name : safetyInfoRegist
	 * @Date : 2024. 2. 19.
	 * @Author : KC.KIM
	 * @Method Brief : 교통 안전정보 수정
	 * @return
	 */
	@Authority(type = MethodType.UPDATE)
	@PostMapping("/sftyInfrm/update.ajax")
	public @ResponseBody CommonResponse<?> sftyInfrmModifyAjax(MozTfcSftyInfrm mozTfcSftyInfrm
			, @RequestPart(required = false) MultipartFile uploadFiles) {

		ValidateBuilder dtoValidator = new ValidateBuilder(mozTfcSftyInfrm);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("postTtl", new ValidateChecker().setRequired().setMaxLength(200, "Title cannot be more than 200 characters"))
				.addRule("postCn", new ValidateChecker().setMaxLength(200, "Contents cannot be more than 200 characters"))
				.addRule("expYn", new ValidateChecker().setRequired())
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
					dtoValidatorResult.getMessage());
		}

		try {
			portalService.updateSftyInfrm(mozTfcSftyInfrm, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "This Traffic Safety Information has been updated.");
	}
	
	/**
	 * @brief : 교통안전교육 리스트 화면
	 * @details : 교통안전교육 리스트 화면
	 * @author : KC.KIM
	 * @date : 2024.01.29
	 * @param : model
	 * @return :
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/sftyEdu/list.do")
	public String sftyEduList(Model model, @ModelAttribute MozTfcSftyEdctn mozTfcSftyEdctn) {
		int page = mozTfcSftyEdctn.getPage();
		int totalCnt = portalService.getSftyEdctnListCnt(mozTfcSftyEdctn);
		Pagination pagination = new Pagination(totalCnt, page);

		mozTfcSftyEdctn.setStart((page - 1) * pagination.getPageSize());

		model.addAttribute("sftyEdctn", mozTfcSftyEdctn);
		model.addAttribute("sftyEduList", portalService.getSftyEdctnList(mozTfcSftyEdctn));
		model.addAttribute("pagination", pagination);
		return "views/govportal/sftyEduList";
	}

	/**
	 * @Method Name : safetyEduDetail
	 * @Date : 2024. 1. 29.
	 * @Author : IK.MOON
	 * @Method Brief : 교통 안전교육 상세 페이지
	 * @return
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/sftyEdu/detail.do")
	public String safetyEduDetail(Model model, @RequestParam("tfcSftyEdctnId") String tfcSftyEdctnId) {
		MozTfcSftyEdctn sftyEdctn = portalService.getSftyEdctnDetail(tfcSftyEdctnId);
		model.addAttribute("sftyEdctn", sftyEdctn);
		return "views/govportal/sftyEduDetail";
	}

	/**
	 * @Method Name : safetyEduRegist
	 * @Date : 2024. 1. 29.
	 * @Author : IK.MOON
	 * @Method Brief : 교통 안전교육 등록 페이지
	 * @return
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/sftyEdu/save.do")
	public String safetyEduRegist() {
		return "views/govportal/sftyEduRegist";
	}
	
	/**
	 * @Method Name : sftyEduRegistAjax
	 * @Date : 2024. 2. 20.
	 * @Author : KC.KIM
	 * @Method Brief : 교통 안전교육 등록
	 * @return
	 */
	@Authority(type = MethodType.CREATE)
	@PostMapping("/sftyEdu/save.ajax")
	public @ResponseBody CommonResponse<?> sftyEduRegistAjax(MozTfcSftyEdctn mozTfcSftyEdctn
			, @RequestPart(required = false) MultipartFile uploadFiles) {

		ValidateBuilder dtoValidator = new ValidateBuilder(mozTfcSftyEdctn);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("postTtl", new ValidateChecker().setRequired().setMaxLength(200, "Title cannot be more than 200 characters"))
				.addRule("postCn", new ValidateChecker().setMaxLength(200, "Contents cannot be more than 200 characters"))
				.addRule("expYn", new ValidateChecker().setRequired())
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
					dtoValidatorResult.getMessage());
		}

		try {
			portalService.registSftyEdctn(mozTfcSftyEdctn, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "This Traffic Safety Education has been registered.");
	}
	
	/**
	 * @brief : 교통안전교육 삭제
	 * @details : 교통안전교육 삭제
	 * @author : KC.KIM
	 * @date : 2024.02.19
	 * @param : tfcSftyEdctnId
	 * @return :
	 */
	@Authority(type = MethodType.DELETE)
	@PostMapping("/sftyEdu/delete.ajax")
	@ResponseBody
	public CommonResponse<?> sftyEdctnDeleteAjax(
			@RequestParam("tfcSftyEdctnId") String tfcSftyEdctnId) {

		MozTfcSftyEdctn sftyEdctn = new MozTfcSftyEdctn();
		sftyEdctn.setTfcSftyEdctnId(tfcSftyEdctnId);

		ValidateBuilder dtoValidator = new ValidateBuilder(sftyEdctn);

		ValidateResult dtoValidatorResult =
				dtoValidator.addRule("tfcSftyEdctnId", new ValidateChecker().setRequired()).isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, dtoValidatorResult.getMessage());
		}

		try {
			portalService.deleteMozTfcSftyEdctn(tfcSftyEdctnId);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "This Traffic Safety Information has been deleted");
	}
	
	/**
	 * @brief : 교통안전교육 수정 화면 이동
	 * @details : 교통안전교육 수정 화면 이동
	 * @author : KC.KIM
	 * @date : 2024.02.20
	 * @param : tfcSftyEdctnId
	 * @return :
	 */
    @Authority(type = MethodType.READ)
    @GetMapping("/sftyEdu/update.do")
    public String sftyEduModify(Model model,@RequestParam("tfcSftyEdctnId")String tfcSftyEdctnId){
    	MozTfcSftyEdctn mozTfcSftyEdctn = portalService.getSftyEdctnDetail(tfcSftyEdctnId);
		model.addAttribute("tfcSftyEdctn", mozTfcSftyEdctn);

		return "views/govportal/sftyEduModify";
    }
    
    /**
	 * @brief : 교통안전교육 수정 
	 * @details : 교통안전교육 수정
	 * @author : KC.KIM
	 * @date : 2024.02.20
	 * @param : tfcSftyEdctnId
	 * @return :
	 */
	@Authority(type = MethodType.UPDATE)
	@PostMapping("/sftyEdu/update.ajax")
	public @ResponseBody CommonResponse<?> sftyEdctnModifyAjax(MozTfcSftyEdctn mozTfcSftyEdctn
			, @RequestPart(required = false) MultipartFile uploadFiles) {

		ValidateBuilder dtoValidator = new ValidateBuilder(mozTfcSftyEdctn);

		ValidateResult dtoValidatorResult = dtoValidator
				.addRule("postTtl", new ValidateChecker().setRequired().setMaxLength(200, "Title cannot be more than 200 characters"))
				.addRule("postCn", new ValidateChecker().setMaxLength(200, "Contents cannot be more than 200 characters"))
				.addRule("expYn", new ValidateChecker().setRequired())
				.isValid();

		if (!dtoValidatorResult.isSuccess()) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,
					dtoValidatorResult.getMessage());
		}

		try {
			portalService.updateMozTfcSftyEdctn(mozTfcSftyEdctn, uploadFiles);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "This Traffic Safety Education has been updated.");
	}
}
