package com.moz.ates.traffic.admin.finentc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moz.ates.traffic.admin.common.enums.MethodType;
import com.moz.ates.traffic.admin.config.Authority;
import com.moz.ates.traffic.common.component.Pagination;
import com.moz.ates.traffic.common.entity.common.CommonResponse;
import com.moz.ates.traffic.common.entity.driver.MozVioInfo;
import com.moz.ates.traffic.common.entity.equipment.MozTfcEnfFileInfo;
import com.moz.ates.traffic.common.entity.finentc.MozFineNtcInfo;
import com.moz.ates.traffic.common.enums.NtcTypeCd;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfFileInfoRepository;
import com.moz.ates.traffic.common.support.exception.CommonException;
import com.moz.ates.traffic.common.support.exception.ErrorCode;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "finentc")
public class FineNtcController {
	
	@Autowired
	FineNtcService fineNtcService;
	
	@Autowired
	MozTfcEnfFileInfoRepository mozTfcEnfFileInfoRepository;
	
    /**
     * @brief : 고지 관리 리스트 화면
     * @details : 고지 관리 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : 
     * @return : 
     */
	@Authority(type = MethodType.READ)
	@GetMapping("/first/list.do")
	public String firstNtcList(Model model, @ModelAttribute MozFineNtcInfo fineNtcInfo) {
		fineNtcInfo.setNtcTy(NtcTypeCd.FIRST_NOTICE.getCode());
		
		int page = fineNtcInfo.getPage();
		int totalCnt = fineNtcService.getFineNtcListCnt(fineNtcInfo);
		Pagination pagination = new Pagination(totalCnt, page);
		
		fineNtcInfo.setStart((page - 1) * pagination.getPageSize());
		
		model.addAttribute("fineNtcInfo", fineNtcInfo);
		model.addAttribute("fineNtcList", fineNtcService.findAllFineNtcList(fineNtcInfo));
		model.addAttribute("pagination", pagination);
		return "views/finentc/fineNtcFirstList";
	}
	
//	@Authority(type = MethodType.READ)
//	@GetMapping("/first/bill/detail.do")
//	public String fineNtcFirstBillDetail(Model model , @RequestParam(name="fineNtcId") String fineNtcId) {
//		model.addAttribute("fineNtcInfo", fineNtcService.findOneNtcDetailByFineNtcId(fineNtcId));
//		return "views/finentc/fineNtcFirstBillDetail";
//	}
	
    /**
     * @brief : 고지 관리 상세
     * @details : 고지 관리 상세
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : 
     * @return : 
     */
	@Authority(type = MethodType.READ)
	@GetMapping("/first/detail.do")
	public String fineNtcFirstDetail(Model model , @RequestParam(name="fineNtcId") String fineNtcId) {
		model.addAttribute("fineNtcInfo", fineNtcService.findOneNtcDetailByFineNtcId(fineNtcId));
		return "views/finentc/fineNtcFirstDetail";
	}

	/**
	 * @brief : 고지 관리 수정
	 * @details : 고지 관리 수정
	 * @author : KY.LEE
	 * @date : 2024.02.20
	 * @param : 
	 * @return : 
	 */
	@Authority(type = MethodType.UPDATE)
	@GetMapping("/first/modify.do")
	public String fineNtcFirstModify(Model model , @RequestParam(name="fineNtcId") String fineNtcId) {
		model.addAttribute("fineNtcInfo", fineNtcService.findOneNtcDetailByFineNtcId(fineNtcId));
		return "views/finentc/fineNtcFirstModify";
	}
	
    /**
     * @brief : 고지서 위반자 정보 수정
     * @details : 고지서 위반자 정보 수정
     * @author : KY.LEE
     * @date : 2024.02.20
     * @param : 
     * @return : 
     */
	@Authority(type = MethodType.READ)
	@PostMapping("/first/vioUpdate.ajax")
	@ResponseBody
	public CommonResponse<?> firstVioUpdate(@ModelAttribute MozVioInfo mozVioInfo) {
		try {
			fineNtcService.updateMozVioInfo(mozVioInfo);
		} catch (CommonException e) {
			CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Falha ao editar as informações da fatura.");
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Correção das informações da fatura.");
	}
	
	/**
	 * @brief : 고지 관리 첨부파일 다운로드
	 * @details : 고지 관리 첨부파일 다운로드
	 * @author : KY.LEE
	 * @date : 2024.02.20
	 * @throws IOException
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/first/filedown.do")
	public void fineNtcFileDownload(HttpServletResponse response, @RequestParam(name="vioFileId", required = true) String vioFileId) throws IOException {
		if(MozatesCommonUtils.isNull(vioFileId)) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		
		MozTfcEnfFileInfo dbMozTfcEnfFileInfo = mozTfcEnfFileInfoRepository.findOneByMozTfcEnfFileInfoByVioFileId(vioFileId);
		
		if(dbMozTfcEnfFileInfo == null) {
			throw new CommonException(ErrorCode.FILE_NOT_FOUND);
		}
		
		File dFile = new File(dbMozTfcEnfFileInfo.getFilePath());
		
		String encodedFileName = URLEncoder.encode(dbMozTfcEnfFileInfo.getFileNm(), "UTF-8");
		
		if(dFile.exists()) {
			response.setHeader("Content-Disposition", "attachment; fileName=\""+encodedFileName+"\";");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Type", encodedFileName);
			response.setHeader("Content-Length", "" + dbMozTfcEnfFileInfo.getFileSize());
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");
			
			FileInputStream fis = null;
			OutputStream out = null;
			try {
				fis = new FileInputStream(dFile);
				out = response.getOutputStream();
				
				int readCount = 0;
				
				byte[] buffer = new byte[4096];
				while((readCount = fis.read(buffer)) != -1){
					out.write(buffer,0,readCount);
				}
			} finally {
				try {
					if(fis != null) {
						fis.close();
					}
					if(out != null) {
						out.close();
					}
				} catch(CommonException e) {
					throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAIL);
				}
			}
		} else {
			throw new CommonException(ErrorCode.FILE_NOT_FOUND);
		}
	}
	
    /**
     * @brief : 고지서 재발송
     * @details : 고지서 재발송
     * @author : KY.LEE
     * @date : 2024.02.20
     * @param : 
     * @return : 
     */
	@Authority(type = MethodType.READ)
	@PostMapping("/first/renotice.ajax")
	@ResponseBody
	public CommonResponse<?> reNoticeAjax(@RequestParam(name="fineNtcId") String fineNtcId) {
		try {
			//TODO:: 비지니스로직 미구현 SMS연동 필요 
			//fineNtcService.findOneNtcDetailByFineNtcId(fineNtcId);
		} catch (CommonException e) {
			CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "O projeto de lei reenviado não foi aprovado.");
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Reenviámos a sua conta.");
	}
	
	
    /**
     * @brief : 고지 관리 리스트 화면
     * @details : 고지 관리 리스트 화면
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : 
     * @return : 
     */
	@Authority(type = MethodType.READ)
	@GetMapping("/second/list.do")
	public String secondNtcList(Model model, @ModelAttribute MozFineNtcInfo fineNtcInfo) {
		fineNtcInfo.setNtcTy(NtcTypeCd.SECOND_NOTICE.getCode());
		
		int page = fineNtcInfo.getPage();
		int totalCnt = fineNtcService.getFineNtcListCnt(fineNtcInfo);
		Pagination pagination = new Pagination(totalCnt, page);
		
		fineNtcInfo.setStart((page - 1) * pagination.getPageSize());
		
		model.addAttribute("fineNtcInfo", fineNtcInfo);
		model.addAttribute("fineNtcList", fineNtcService.findAllFineNtcList(fineNtcInfo));
		model.addAttribute("pagination", pagination);
		return "views/finentc/fineNtcSecondList";
	}
	
    /**
     * @brief : 고지 관리 상세
     * @details : 고지 관리 상세
     * @author : KC.KIM
     * @date : 2023.08.17
     * @param : 
     * @return : 
     */
	@Authority(type = MethodType.READ)
	@GetMapping("/second/detail.do")
	public String fineNtcSecondDetail(Model model , @RequestParam(name="fineNtcId") String fineNtcId) {
		model.addAttribute("fineNtcInfo", fineNtcService.findOneNtcDetailByFineNtcId(fineNtcId));
		return "views/finentc/fineNtcSecondDetail";
	}

	/**
	 * @brief : 고지 관리 수정
	 * @details : 고지 관리 수정
	 * @author : KY.LEE
	 * @date : 2024.02.20
	 * @param : 
	 * @return : 
	 */
	@Authority(type = MethodType.UPDATE)
	@GetMapping("/second/modify.do")
	public String fineNtcSecondModify(Model model , @RequestParam(name="fineNtcId") String fineNtcId) {
		model.addAttribute("fineNtcInfo", fineNtcService.findOneNtcDetailByFineNtcId(fineNtcId));
		return "views/finentc/fineNtcSecondModify";
	}
	
    /**
     * @brief : 고지서 위반자 정보 수정
     * @details : 고지서 위반자 정보 수정
     * @author : KY.LEE
     * @date : 2024.02.20
     * @param : 
     * @return : 
     */
	@Authority(type = MethodType.READ)
	@PostMapping("/second/vioUpdate.ajax")
	@ResponseBody
	public CommonResponse<?> secondVioUpdate(@ModelAttribute MozVioInfo mozVioInfo) {
		try {
			fineNtcService.updateMozVioInfo(mozVioInfo);
		} catch (CommonException e) {
			CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "Falha ao editar as informações da fatura.");
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Correção das informações da fatura.");
	}
	
	/**
	 * @brief : 고지 관리 첨부파일 다운로드
	 * @details : 고지 관리 첨부파일 다운로드
	 * @author : KY.LEE
	 * @date : 2024.02.20
	 * @throws IOException 
	 */
	@Authority(type = MethodType.READ)
	@GetMapping("/second/filedown.do")
	public void secondFineNtcFileDownload(HttpServletResponse response, @RequestParam(name="vioFileId", required = true) String vioFileId) throws IOException {
		if(MozatesCommonUtils.isNull(vioFileId)) {
			throw new CommonException(ErrorCode.INVALID_PARAMETER);
		}
		
		MozTfcEnfFileInfo dbMozTfcEnfFileInfo = mozTfcEnfFileInfoRepository.findOneByMozTfcEnfFileInfoByVioFileId(vioFileId);
		
		if(dbMozTfcEnfFileInfo == null) {
			throw new CommonException(ErrorCode.FILE_NOT_FOUND);
		}
		
		File dFile = new File(dbMozTfcEnfFileInfo.getFilePath());
		
		String encodedFileName = URLEncoder.encode(dbMozTfcEnfFileInfo.getFileNm(), "UTF-8");
		
		if(dFile.exists()) {
			response.setHeader("Content-Disposition", "attachment; fileName=\""+encodedFileName+"\";");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Type", encodedFileName);
			response.setHeader("Content-Length", "" + dbMozTfcEnfFileInfo.getFileSize());
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");
			
			FileInputStream fis = null;
			OutputStream out = null;
			try {
				fis = new FileInputStream(dFile);
				out = response.getOutputStream();
				
				int readCount = 0;
				
				byte[] buffer = new byte[4096];
				while((readCount = fis.read(buffer)) != -1){
					out.write(buffer,0,readCount);
				}
			} finally {
				try {
					if(fis != null) {
						fis.close();
					}
					if(out != null) {
						out.close();
					}
				} catch(CommonException e) {
					throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAIL);
				}
			}
		} else {
			throw new CommonException(ErrorCode.FILE_NOT_FOUND);
		}
	}
	
    /**
     * @brief : 고지서 재발송
     * @details : 고지서 재발송
     * @author : KY.LEE
     * @date : 2024.02.20
     * @param : 
     * @return : 
     */
	@Authority(type = MethodType.READ)
	@PostMapping("/second/renotice.ajax")
	@ResponseBody
	public CommonResponse<?> secondReNoticeAjax(@RequestParam(name="fineNtcId") String fineNtcId) {
		try {
			//TODO:: 비지니스로직 미구현 SMS연동 필요 
			//fineNtcService.findOneNtcDetailByFineNtcId(fineNtcId);
		} catch (CommonException e) {
			CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, "O projeto de lei reenviado não foi aprovado.");
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "Reenviámos a sua conta.");
	}
}
