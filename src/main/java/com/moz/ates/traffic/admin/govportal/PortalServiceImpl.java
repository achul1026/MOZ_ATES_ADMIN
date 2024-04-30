package com.moz.ates.traffic.admin.govportal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.common.component.FileUploadComponent;
import com.moz.ates.traffic.common.entity.board.MozAtchFile;
import com.moz.ates.traffic.common.entity.board.MozBrd;
import com.moz.ates.traffic.common.entity.board.MozComplaintsReg;
import com.moz.ates.traffic.common.entity.board.MozFaq;
import com.moz.ates.traffic.common.entity.board.MozInqry;
import com.moz.ates.traffic.common.entity.board.MozObjReg;
import com.moz.ates.traffic.common.entity.board.MozTfcSftyEdctn;
import com.moz.ates.traffic.common.entity.board.MozTfcSftyInfrm;
import com.moz.ates.traffic.common.entity.common.UploadFileInfo;
import com.moz.ates.traffic.common.entity.payment.MozPlPymntInfo;
import com.moz.ates.traffic.common.enums.NoticeCateCd;
import com.moz.ates.traffic.common.repository.board.MozAtchFileRepository;
import com.moz.ates.traffic.common.repository.board.MozBrdRepository;
import com.moz.ates.traffic.common.repository.board.MozComplaintsRegRepository;
import com.moz.ates.traffic.common.repository.board.MozFaqRepository;
import com.moz.ates.traffic.common.repository.board.MozInqryRepository;
import com.moz.ates.traffic.common.repository.board.MozObjRegRepository;
import com.moz.ates.traffic.common.repository.board.MozTfcSftyEdctnRepository;
import com.moz.ates.traffic.common.repository.board.MozTfcSftyInfrmRepository;
import com.moz.ates.traffic.common.repository.payment.MozPlPymntInfoRepository;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

/**
 * className : PortalServiceImpl
 * author : Mike Lim
 * description : 포털 관련 impl
 */
@Service
@Component
public class PortalServiceImpl implements PortalService {
    
    @Autowired
    private MozBrdRepository brdRepository;
    
    @Autowired
    private MozFaqRepository faqRepository;
    
    @Autowired
    private MozObjRegRepository objRegRepository;
    
    @Autowired
    private MozComplaintsRegRepository complaintsRegRepository;
    
    @Autowired
    private MozPlPymntInfoRepository plPymntInfoRepository;
    
    @Autowired
    private MozTfcSftyInfrmRepository mozTfcSftyInfrmRepository;
    
    @Autowired
    private MozTfcSftyEdctnRepository mozTfcSftyEdctnRepository;
    
    @Autowired
    private MozInqryRepository mozInqryRepository;
    
    @Autowired
    private MozAtchFileRepository mozAtchFileRepository;
    
    @Autowired
    FileUploadComponent fileUploadComponent;
    
    /**
     * @brief : 포탈 공지사항 등록
     * @details : 공지사항 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @param : uploadFiles
     * @return : 
     */
    @Override
    public void registNotice(MozBrd brd, MultipartFile[] uploadFiles) {
    	String crtr = LoginOprtrUtils.getOprtrId();
    	String wrtrNm = LoginOprtrUtils.getOprtrNm();
    	String boardIdx = MozatesCommonUtils.getUuid();
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	
    	brd.setOprtrId(crtr);
    	brd.setWrtrNm(wrtrNm);
    	brd.setBoardIdx(boardIdx);
    	brd.setCateCd(NoticeCateCd.PORTAL.getCode());
    	
    	for(MultipartFile uploadFile : uploadFiles) {
    		if(!uploadFile.isEmpty() && !MozatesCommonUtils.isNull(uploadFile.getOriginalFilename())){
    			String[] extArr = {"xlx", "xlsx", "pptx", "pdf", "doc", "word"};
        		UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(uploadFile, extArr);    		
        		
        		MozAtchFile atchFile = new MozAtchFile();
        		String fileIdx = MozatesCommonUtils.getUuid();
        		atchFile.setFileIdx(fileIdx);
        		atchFile.setAtchIdx(boardIdx);
        		atchFile.setOprtrId(crtr);
        		atchFile.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
        		atchFile.setFileSaveNm(uploadFileInfo.getFileNm());
        		atchFile.setFilePath(uploadFileInfo.getFilePath());
        		atchFile.setFileSize(uploadFileInfo.getFileSize());
        		atchFile.setFileExts(uploadFileInfo.getFileExt());
        		mozAtchFileRepository.insertMozAtchFile(atchFile);
    		}
    	}
    	if(!MozatesCommonUtils.isNull(brd.getPostStrDt())
    			&& !MozatesCommonUtils.isNull(brd.getPostEndDt())) {
    		brd.setPostStrDtString(formatter.format(brd.getPostStrDt()));
    		brd.setPostEndDtString(formatter.format(brd.getPostEndDt()));    		
    	}
    	
    	brdRepository.saveMozBrd(brd);
    }

    /**
     * @brief : 포탈 공지사항 리스트 조회
     * @details : 포탈 공지사항 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    @Override
    public List<MozBrd> getNoticeList(MozBrd brd) {
    	brd.setCateCd(NoticeCateCd.PORTAL.getCode());
    	return brdRepository.findAllMozBrd(brd);
    }
    
    /**
     * @brief : 포탈 공지사항 리스트 개수 조회
     * @details : 포탈 공지사항 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    @Override
    public int getNoticeListCnt(MozBrd brd) {
    	brd.setCateCd(NoticeCateCd.PORTAL.getCode());
    	return brdRepository.countMozBrd(brd);
    }
    
    /**
     * @brief : 포탈 공지사항 상세 화면
     * @details : 포탈 공지사항 상세 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : boardIdx
     * @return : 
     */
    @Override
    public MozBrd getNoticeDetail(String boardIdx) {
    	return brdRepository.findOneNoticeDetail(boardIdx);
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
    @Override
    public void updateNotice(MozBrd brd, MultipartFile[] uploadFiles) {
    	String[] extArr = {"xlx", "xlsx", "pptx", "pdf", "doc", "word"};
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	
    	if(brd.getAtchFileList() != null) {
    		for(MozAtchFile fileItem : brd.getAtchFileList()) {
    			MozAtchFile orinAtchFile = mozAtchFileRepository.findOneMozAtchFileByFileIdx(fileItem.getFileIdx());
    			fileUploadComponent.deleteUploadFile(orinAtchFile.getFilePath());
    			mozAtchFileRepository.deleteMozAtchFileByFileIdx(fileItem.getFileIdx());    			
    		}
		}
    	
    	for(MultipartFile uploadFile : uploadFiles) {
    		if(!uploadFile.isEmpty() && !MozatesCommonUtils.isNull(uploadFile.getOriginalFilename())){
    			UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(uploadFile, extArr);    		
    			
    			MozAtchFile atchFile =new MozAtchFile();
    			String fileIdx = MozatesCommonUtils.getUuid();
    			String crtr = LoginOprtrUtils.getOprtrId();
    			
    			atchFile.setFileIdx(fileIdx);
    			atchFile.setAtchIdx(brd.getBoardIdx());
    			atchFile.setOprtrId(crtr);
    			atchFile.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
    			atchFile.setFileSaveNm(uploadFileInfo.getFileNm());
    			atchFile.setFilePath(uploadFileInfo.getFilePath());
    			atchFile.setFileSize(uploadFileInfo.getFileSize());
    			atchFile.setFileExts(uploadFileInfo.getFileExt());
    			mozAtchFileRepository.insertMozAtchFile(atchFile);
    		}
    	}
    	
    	if(!MozatesCommonUtils.isNull(brd.getPostStrDt())
    			&& !MozatesCommonUtils.isNull(brd.getPostEndDt())) {
    		brd.setPostStrDtString(formatter.format(brd.getPostStrDt()));
    		brd.setPostEndDtString(formatter.format(brd.getPostEndDt()));    		
    	}
    	
    	
    	brdRepository.updateNotice(brd);
    }
    
    /**
     * @brief : 포탈 공지사항 삭제
     * @details : 포탈 공지사항 삭제 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : boardIdx
     * @return : 
     */
    @Override
    public void deleteNotice(String boardIdx) {
    	
    	MozAtchFile atchFile = mozAtchFileRepository.findOneMozAtchFileByAtchFileIdx(boardIdx);
    	if(!MozatesCommonUtils.isNull(atchFile)) {
    		fileUploadComponent.deleteUploadFile(atchFile.getFilePath());    		
    		mozAtchFileRepository.deleteMozAtchFileByFileIdx(atchFile.getFileIdx());
    	}
    	brdRepository.deleteNotice(boardIdx);
    }
    
    /**
     * @brief : FAQ 리스트 조회
     * @details : FAQ 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    @Override
    public List<MozFaq> getFaqList(MozFaq faq) {
    	return faqRepository.findAllMozFaq(faq);
    }
    
    /**
     * @brief : FAQ 리스트 개수 조회
     * @details : FAQ 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    @Override
    public int getFaqListCnt(MozFaq faq) {
    	return faqRepository.countMozFaq(faq);
    }
    
    /**
     * @brief : FAQ 등록 
     * @details : FAQ 등록 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    @Override
    public void registFaq(MozFaq faq) {
    	String crtr = LoginOprtrUtils.getOprtrId();
    	String faqIdx = MozatesCommonUtils.getUuid();
    	
    	faq.setWrtrId(crtr);
    	faq.setFaqIdx(faqIdx);
    	faqRepository.saveMozFaq(faq);
    }
    
    /**
     * @brief : FAQ 상세 조회
     * @details : FAQ 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faqIdx
     * @return : 
     */
    @Override
    public MozFaq getFaqDetail(String faqIdx) {
    	return faqRepository.findOneMozFaq(faqIdx);
    }
    
    /**
     * @brief : FAQ 수정
     * @details : FAQ 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    @Override
    public void updateFaq(MozFaq faq) {
    	faqRepository.updateMozFaq(faq);
    }
    
    /**
     * @brief : FAQ 삭제
     * @details : FAQ 삭제
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faqIdx
     * @return : 
     */
    @Override
    public void deleteFaq(String faqIdx) {
    	faqRepository.deleteMozFaq(faqIdx);
    }

    /**
     * @brief : 이의제기 리스트 조회
     * @details : 이의제기 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : objIdx
     * @return : 
     */
    @Override
    public List<MozObjReg> getObjectionList(MozObjReg objReg) {
    	return objRegRepository.findAllMozObjReg(objReg);
    }

    /**
     * @brief : 이의제기 리스트 개수 조회
     * @details : 이의제기 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : objIdx
     * @return : 
     */
    @Override
    public int getObjectionListCnt(MozObjReg objReg) {
    	return objRegRepository.countMozObjReg(objReg);
    }
    
    /**
     * @brief : 이의제기 상세 조회
     * @details : 이의제기 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : objIdx
     * @return : 
     */
    @Override
    public MozObjReg getObjectionDetail(String objIdx) {
    	return objRegRepository.findOneMozObjReg(objIdx);
    }

    /**
     * @brief : 불만사항 리스트 조회
     * @details : 불만사항 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : complaintsReg
     * @return : 
     */
    @Override
    public List<MozComplaintsReg> getComplaintList(MozComplaintsReg complaintsReg) {
    	return complaintsRegRepository.findAllMozComplaintReg(complaintsReg);
    }

    /**
     * @brief : 불만사항 리스트 개수 조회
     * @details : 불만사항 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : complaintsReg
     * @return : 
     */
    @Override
    public int getComplaintListCnt(MozComplaintsReg complaintsReg) {
    	return complaintsRegRepository.countMozComplaintReg(complaintsReg);
    }
    
    /**
     * @brief : 불만사항 상세 조회
     * @details : 불만사항 상세
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : complaintsIdx
     * @return : 
     */
    @Override
    public MozComplaintsReg getComplaintDetail(String complaintsIdx) {
    	return complaintsRegRepository.findOneComplaintDetail(complaintsIdx);
    }

    /**
     * @brief : 범칙금 납부처 등록 
     * @details : 범칙금 납부처 등록
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : 
     * @return : 
     */
    @Override
    public void registPenaltyPlace(MozPlPymntInfo plPymntInfo) {
    	String crtr = LoginOprtrUtils.getOprtrId();
    	String placePymntId = MozatesCommonUtils.getUuid();
    	
    	plPymntInfo.setCrtr(crtr);
    	plPymntInfo.setPlacePymntId(placePymntId);
    	plPymntInfoRepository.saveMozPlPymntInfo(plPymntInfo);
    }

    /**
     * @brief : 범칙금 납부처 리스트 조회 
     * @details : 범칙금 납부처 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : plPymntInfo
     * @return : 
     */
    @Override
    public List<MozPlPymntInfo> getPenaltyPlaceList(MozPlPymntInfo plPymntInfo) {
    	return plPymntInfoRepository.findAllPenaltyPlaceList(plPymntInfo);
    }

    /**
     * @brief : 범칙금 납부처 리스트 개수 조회 
     * @details : 범칙금 납부처 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : plPymntInfo
     * @return : 
     */
    @Override
    public int getPenaltyPlaceListCnt(MozPlPymntInfo plPymntInfo) {
    	return plPymntInfoRepository.countPenaltyPlaceList(plPymntInfo);
    }

    /**
     * @brief : 범칙금 납부처 상세 
     * @details : 범칙금 납부처 상세
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : placePymntId
     * @return : 
     */
    @Override
    public MozPlPymntInfo getPenaltyPlaceDetail(String placePymntId) {
    	return plPymntInfoRepository.findOnePenaltyPlaceDetail(placePymntId);
    }

    /**
     * @brief : 범칙금 납부처 수정
     * @details : 범칙금 납부처 수정
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : placePymntId
     * @return : 
     */
    @Override
    public void updatePenaltyPlace(MozPlPymntInfo plPymntInfo) {
    	plPymntInfoRepository.updatePenaltyPlace(plPymntInfo);
    }
    
    /**
     * @brief : 범칙금 납부처 삭제
     * @details : 범칙금 납부처 삭제 
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : placePymntId
     * @return : 
     */
    @Override
    public void deletePenaltyPlace(String placePymntId) {
    	plPymntInfoRepository.deletePenaltyPlace(placePymntId);
    }
    
	@Override
	@Transactional
	public void updateObjAns(MozObjReg objReg, MultipartFile[] uploadFiles) {
		String crtr = LoginOprtrUtils.getOprtrId();
		objReg.setOprtrId(crtr);
		
		List<MozAtchFile> deleteAtchFileList = new ArrayList<MozAtchFile>();
		deleteAtchFileList = objReg.getAnsAtchFileList();
		
		if(deleteAtchFileList != null) {
			for(MozAtchFile deleteFileInfo : deleteAtchFileList) {
				if(!MozatesCommonUtils.isNull(deleteFileInfo.getAtchIdx())) {
					MozAtchFile fileInfo = new MozAtchFile();
					fileInfo = mozAtchFileRepository.findOneMozAtchFileByAtchFileIdx(deleteFileInfo.getAtchIdx());
					fileUploadComponent.deleteUploadFile(fileInfo.getFilePath());
					mozAtchFileRepository.deleteMozAtchFileByFileIdx(fileInfo.getFileIdx());				
				}
			}
		}
		
		// 답변 파일 저장
		if(uploadFiles != null && uploadFiles.length > 0) {
			for(MultipartFile uploadFile : uploadFiles) {
				if(!MozatesCommonUtils.isNull(uploadFile.getOriginalFilename())) {
					String[] extArr = {"jpg", "jpeg", "png", "PNG", "doc", "docx", "xlsx", "pdf"};
					UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(uploadFile, extArr); 
					MozAtchFile atchFile = new MozAtchFile();
					String fileIdx = MozatesCommonUtils.getUuid();
					atchFile.setFileIdx(fileIdx);
					atchFile.setAtchIdx(objReg.getObjIdx());
					atchFile.setFilePath(uploadFileInfo.getFilePath());
					atchFile.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
					atchFile.setFileExts(uploadFileInfo.getFileExt());
					atchFile.setFileSaveNm(uploadFileInfo.getFileNm());
					atchFile.setFileSize(uploadFileInfo.getFileSize());
					atchFile.setFileType("2");
					mozAtchFileRepository.insertMozAtchFile(atchFile);					
				}
	    	}
		}
		objRegRepository.updateObjAns(objReg);
	}
	
	@Override
	@Transactional
	public void updateCmpAns(MozComplaintsReg complaintsReg, MultipartFile[] uploadFiles) {
		String crtr = LoginOprtrUtils.getOprtrId();
		complaintsReg.setOprtrId(crtr);
		
		List<MozAtchFile> deleteAtchFileList = new ArrayList<MozAtchFile>();
		deleteAtchFileList = complaintsReg.getAnsAtchFileList();
		
		if(deleteAtchFileList != null) {
			for(MozAtchFile deleteFileInfo : deleteAtchFileList) {
				if(!MozatesCommonUtils.isNull(deleteFileInfo.getAtchIdx())) {
					MozAtchFile fileInfo = new MozAtchFile();
					fileInfo = mozAtchFileRepository.findOneMozAtchFileByAtchFileIdx(deleteFileInfo.getAtchIdx());
					fileUploadComponent.deleteUploadFile(fileInfo.getFilePath());
					mozAtchFileRepository.deleteMozAtchFileByFileIdx(fileInfo.getFileIdx());				
				}
			}
		}
		
		// 답변 파일 저장
		if(uploadFiles != null && uploadFiles.length > 0) {
			for(MultipartFile uploadFile : uploadFiles) {
				if(!MozatesCommonUtils.isNull(uploadFile.getOriginalFilename())) {
					String[] extArr = {"jpg", "jpeg", "png", "PNG", "doc", "docx", "xlsx", "pdf"};
					UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(uploadFile, extArr); 
					MozAtchFile atchFile = new MozAtchFile();
					String fileIdx = MozatesCommonUtils.getUuid();
					atchFile.setFileIdx(fileIdx);
					atchFile.setAtchIdx(complaintsReg.getComplaintsIdx());
					atchFile.setFilePath(uploadFileInfo.getFilePath());
					atchFile.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
					atchFile.setFileExts(uploadFileInfo.getFileExt());
					atchFile.setFileSaveNm(uploadFileInfo.getFileNm());
					atchFile.setFileSize(uploadFileInfo.getFileSize());
					atchFile.setFileType("2");
					mozAtchFileRepository.insertMozAtchFile(atchFile);
				}
	    	}
		}
		complaintsRegRepository.updateCmpAns(complaintsReg);
	}
	
	/**
     * @brief : 교통안전정보 리스트 개수 조회
     * @details : 교통안전정보 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.02.18
     * @param : mozTfcSftyInfrm
     * @return : 
     */
	@Override
	public int getSftyInfrmListCnt(MozTfcSftyInfrm mozTfcSftyInfrm) {
		return mozTfcSftyInfrmRepository.countTfcSftyInfrmList(mozTfcSftyInfrm);
	}

	/**
     * @brief : 교통안전정보 리스트 조회
     * @details : 교통안전정보 리스트 조회
     * @author : KC.KIM
     * @date : 2024.02.18
     * @param : mozTfcSftyInfrm
     * @return : 
     */
	@Override
	public List<MozTfcSftyInfrm> getSftyInfrmList(MozTfcSftyInfrm mozTfcSftyInfrm) {
		return mozTfcSftyInfrmRepository.findAllTfcSftyInfrmList(mozTfcSftyInfrm);
	}
	
	/**
     * @brief : 교통안전정보 상세 조회
     * @details : 교통안전정보 상세 조회
     * @author : KC.KIM
     * @date : 2024.02.18
     * @param : mozTfcSftyInfrm
     * @return : 
     */
	@Override
	public MozTfcSftyInfrm getSftyInfrmDetail(String tfcSftyInfrmId) {
		return mozTfcSftyInfrmRepository.findOneTfcSftyInfrmDetail(tfcSftyInfrmId);
	}
	
	/**
     * @brief : 교통안전정보 삭제
     * @details : 교통안전정보 삭제
     * @author : KC.KIM
     * @date : 2024.02.19
     * @param : tfcSftyInfrmId
     * @return : 
     */
	@Override
	public void deleteSftyInfrmByTfcSftyInfrmId(String tfcSftyInfrmId) {
		
		MozAtchFile atchFile = mozAtchFileRepository.findOneMozAtchFileByAtchFileIdx(tfcSftyInfrmId);
		if(!MozatesCommonUtils.isNull(atchFile)) {
			fileUploadComponent.deleteUploadFile(atchFile.getFilePath());    		
			mozAtchFileRepository.deleteMozAtchFileByFileIdx(atchFile.getFileIdx());
		}
		mozTfcSftyInfrmRepository.deleteSftyInfrmByTfcSftyInfrmId(tfcSftyInfrmId);
	}

	/**
     * @brief : 교통안전정보 등록
     * @details : 교통안전정보 등록
     * @author : KC.KIM
     * @date : 2024.02.19
     * @param : mozTfcSftyInfrm
     * @param : uploadFile
     * @return : 
     */
	@Override
	public void registSftyInfrm(MozTfcSftyInfrm mozTfcSftyInfrm, MultipartFile uploadFile) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String crtr = LoginOprtrUtils.getOprtrId();
    	String tfcSftyInfrmId = MozatesCommonUtils.getUuid();
    	
    	mozTfcSftyInfrm.setCrtr(crtr);
    	mozTfcSftyInfrm.setTfcSftyInfrmId(tfcSftyInfrmId);
    	
    	if(uploadFile != null && !MozatesCommonUtils.isNull(uploadFile.getOriginalFilename())) {
    		String[] extArr = {"xlx", "xlsx", "pptx", "pdf", "doc", "docx" ,"word", "zip"};
    		UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(uploadFile, extArr);    		
    		
    		MozAtchFile atchFile =new MozAtchFile();
    		String fileIdx = MozatesCommonUtils.getUuid();
    		atchFile.setFileIdx(fileIdx);
    		atchFile.setAtchIdx(tfcSftyInfrmId);
    		atchFile.setOprtrId(crtr);
    		atchFile.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
    		atchFile.setFileSaveNm(uploadFileInfo.getFileNm());
    		atchFile.setFilePath(uploadFileInfo.getFilePath());
    		atchFile.setFileSize(uploadFileInfo.getFileSize());
    		atchFile.setFileExts(uploadFileInfo.getFileExt());
    		mozAtchFileRepository.insertMozAtchFile(atchFile);
    	}
    	
    	if(!MozatesCommonUtils.isNull(mozTfcSftyInfrm.getPostStrDe())
    			&& !MozatesCommonUtils.isNull(mozTfcSftyInfrm.getPostEndDe())) {
    		mozTfcSftyInfrm.setPostStrDeString(formatter.format(mozTfcSftyInfrm.getPostStrDe()));
    		mozTfcSftyInfrm.setPostEndDeString(formatter.format(mozTfcSftyInfrm.getPostEndDe()));    		
    	}
    	
		mozTfcSftyInfrmRepository.insertMozTfcSftyInfrm(mozTfcSftyInfrm);
	}

	/**
     * @brief : 교통안전정보 수정
     * @details : 교통안전정보 수정
     * @author : KC.KIM
     * @date : 2024.02.19
     * @param : mozTfcSftyInfrm
     * @param : uploadFile
     * @return : 
     */
	@Override
	public void updateSftyInfrm(MozTfcSftyInfrm mozTfcSftyInfrm, MultipartFile uploadFile) {
		String[] extArr = {"xlx", "xlsx", "pptx", "pdf", "doc", "docx" ,"word", "zip"};
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		if(mozTfcSftyInfrm.getAtchFile() != null && !MozatesCommonUtils.isNull(mozTfcSftyInfrm.getAtchFile().getFileIdx())) {
			MozAtchFile orinAtchFile = mozAtchFileRepository.findOneMozAtchFileByFileIdx(mozTfcSftyInfrm.getAtchFile().getFileIdx());
			fileUploadComponent.deleteUploadFile(orinAtchFile.getFilePath());
			mozAtchFileRepository.deleteMozAtchFileByFileIdx(mozTfcSftyInfrm.getAtchFile().getFileIdx());
		}
		
		if(uploadFile != null && !MozatesCommonUtils.isNull(uploadFile.getOriginalFilename())) {
			UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(uploadFile, extArr);    		
			
			MozAtchFile atchFile =new MozAtchFile();
			String fileIdx = MozatesCommonUtils.getUuid();
			String crtr = LoginOprtrUtils.getOprtrId();
			
			atchFile.setFileIdx(fileIdx);
			atchFile.setAtchIdx(mozTfcSftyInfrm.getTfcSftyInfrmId());
			atchFile.setOprtrId(crtr);
			atchFile.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
			atchFile.setFileSaveNm(uploadFileInfo.getFileNm());
			atchFile.setFilePath(uploadFileInfo.getFilePath());
			atchFile.setFileSize(uploadFileInfo.getFileSize());
			atchFile.setFileExts(uploadFileInfo.getFileExt());
			mozAtchFileRepository.insertMozAtchFile(atchFile);
    	}
		String updr = LoginOprtrUtils.getOprtrId();
		mozTfcSftyInfrm.setUpdr(updr);
		
		if(!MozatesCommonUtils.isNull(mozTfcSftyInfrm.getPostStrDe())
				&& !MozatesCommonUtils.isNull(mozTfcSftyInfrm.getPostEndDe())) {
			mozTfcSftyInfrm.setPostStrDeString(formatter.format(mozTfcSftyInfrm.getPostStrDe()));
			mozTfcSftyInfrm.setPostEndDeString(formatter.format(mozTfcSftyInfrm.getPostEndDe()));			
		}
		
		
		mozTfcSftyInfrmRepository.updateMozTfcSftyInfrm(mozTfcSftyInfrm);
	}

	/**
     * @brief : 교통안전교육 리스트 개수 조회
     * @details : 교통안전교육 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : mozTfcSftyEdctn
     * @return : 
     */
	@Override
	public int getSftyEdctnListCnt(MozTfcSftyEdctn mozTfcSftyEdctn) {
		return mozTfcSftyEdctnRepository.countMozTfcSftyEdctn(mozTfcSftyEdctn);
	}

	/**
     * @brief : 교통안전교육 리스트 조회
     * @details : 교통안전교육 리스트 조회
     * @author : KC.KIM
     * @date : 2024.02.19
     * @param : mozTfcSftyEdctn
     * @return : 
     */
	@Override
	public List<MozTfcSftyEdctn> getSftyEdctnList(MozTfcSftyEdctn mozTfcSftyEdctn) {
		return mozTfcSftyEdctnRepository.findAllMozTfcSftyEdctn(mozTfcSftyEdctn);
	}

	/**
     * @brief : 교통안전교육 등록
     * @details : 교통안전교육 등록
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : mozTfcSftyEdctn
     * @param : uploadFile
     * @return : 
     */
	@Override
	public void registSftyEdctn(MozTfcSftyEdctn mozTfcSftyEdctn, MultipartFile uploadFile) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		String crtr = LoginOprtrUtils.getOprtrId();
    	String tfcSftyEdctnId = MozatesCommonUtils.getUuid();
    	
    	mozTfcSftyEdctn.setCrtr(crtr);
    	mozTfcSftyEdctn.setTfcSftyEdctnId(tfcSftyEdctnId);
    	
    	if(uploadFile != null && !MozatesCommonUtils.isNull(uploadFile.getOriginalFilename())) {
    		String[] extArr = {"xlx", "xlsx", "pptx", "pdf", "doc", "docx" ,"word", "zip"};
    		UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(uploadFile, extArr);    		
    		
    		MozAtchFile atchFile =new MozAtchFile();
    		String fileIdx = MozatesCommonUtils.getUuid();
    		atchFile.setFileIdx(fileIdx);
    		atchFile.setAtchIdx(tfcSftyEdctnId);
    		atchFile.setOprtrId(crtr);
    		atchFile.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
    		atchFile.setFileSaveNm(uploadFileInfo.getFileNm());
    		atchFile.setFilePath(uploadFileInfo.getFilePath());
    		atchFile.setFileSize(uploadFileInfo.getFileSize());
    		atchFile.setFileExts(uploadFileInfo.getFileExt());
    		mozAtchFileRepository.insertMozAtchFile(atchFile);
    	}
    	
    	if(!MozatesCommonUtils.isNull(mozTfcSftyEdctn.getPostStrDe())
    			&& !MozatesCommonUtils.isNull(mozTfcSftyEdctn.getPostEndDe())) {
    		mozTfcSftyEdctn.setPostStrDeString(formatter.format(mozTfcSftyEdctn.getPostStrDe()));
    		mozTfcSftyEdctn.setPostEndDeString(formatter.format(mozTfcSftyEdctn.getPostEndDe()));    		
    	}
    	
		mozTfcSftyEdctnRepository.insertMozTfcSftyEdctn(mozTfcSftyEdctn);
	}

	/**
     * @brief : 교통안전교육 상세 정보 조회
     * @details : 교통안전교육 상세 정보 조회
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : tfcSftyEdctnId
     * @return : 
     */
	@Override
	public MozTfcSftyEdctn getSftyEdctnDetail(String tfcSftyEdctnId) {
		return mozTfcSftyEdctnRepository.findOneMozTfcSftyEdctnByTfcSftyEdctnId(tfcSftyEdctnId);
	}

	/**
     * @brief : 교통안전교육 삭제
     * @details : 교통안전교육 삭제
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : tfcSftyEdctnId
     * @return : 
     */
	@Override
	public void deleteMozTfcSftyEdctn(String tfcSftyEdctnId) {
		MozAtchFile atchFile = mozAtchFileRepository.findOneMozAtchFileByAtchFileIdx(tfcSftyEdctnId);
		if(!MozatesCommonUtils.isNull(atchFile)) {
			fileUploadComponent.deleteUploadFile(atchFile.getFilePath());    		
			mozAtchFileRepository.deleteMozAtchFileByFileIdx(atchFile.getFileIdx());
		}
		mozTfcSftyEdctnRepository.deleteMozTfcSftyEdctnByTfcSftyEdctnId(tfcSftyEdctnId);
	}

	/**
     * @brief : 교통안전교육 수정
     * @details : 교통안전교육 수정
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : mozTfcSftyEdctn
     * @param : uploadFile
     * @return : 
     */
	@Override
	public void updateMozTfcSftyEdctn(MozTfcSftyEdctn mozTfcSftyEdctn, MultipartFile uploadFile) {
		String[] extArr = {"xlx", "xlsx", "pptx", "pdf", "doc", "docx" ,"word", "zip"};
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		if(mozTfcSftyEdctn.getAtchFile() != null && !MozatesCommonUtils.isNull(mozTfcSftyEdctn.getAtchFile().getFileIdx())) {
			MozAtchFile orinAtchFile = mozAtchFileRepository.findOneMozAtchFileByFileIdx(mozTfcSftyEdctn.getAtchFile().getFileIdx());
			fileUploadComponent.deleteUploadFile(orinAtchFile.getFilePath());
			mozAtchFileRepository.deleteMozAtchFileByFileIdx(mozTfcSftyEdctn.getAtchFile().getFileIdx());
		}
		
		if(uploadFile != null && !MozatesCommonUtils.isNull(uploadFile.getOriginalFilename())) {
    		UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(uploadFile, extArr);    		
    		
    		MozAtchFile atchFile = new MozAtchFile();
    		String fileIdx = MozatesCommonUtils.getUuid();
    		String crtr = LoginOprtrUtils.getOprtrId();
    		
    		atchFile.setFileIdx(fileIdx);
    		atchFile.setAtchIdx(mozTfcSftyEdctn.getTfcSftyEdctnId());
    		atchFile.setOprtrId(crtr);
    		atchFile.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
    		atchFile.setFileSaveNm(uploadFileInfo.getFileNm());
    		atchFile.setFilePath(uploadFileInfo.getFilePath());
    		atchFile.setFileSize(uploadFileInfo.getFileSize());
    		atchFile.setFileExts(uploadFileInfo.getFileExt());
    		mozAtchFileRepository.insertMozAtchFile(atchFile);
    	}
    	String updr = LoginOprtrUtils.getOprtrId();
    	mozTfcSftyEdctn.setUpdr(updr);
    	
    	if(!MozatesCommonUtils.isNull(mozTfcSftyEdctn.getPostStrDe())
    			&& !MozatesCommonUtils.isNull(mozTfcSftyEdctn.getPostEndDe())) {
    		mozTfcSftyEdctn.setPostStrDeString(formatter.format(mozTfcSftyEdctn.getPostStrDe()));
    		mozTfcSftyEdctn.setPostEndDeString(formatter.format(mozTfcSftyEdctn.getPostEndDe()));    		
    	}
    	
    	
		mozTfcSftyEdctnRepository.updateMozTfcSftyEdctn(mozTfcSftyEdctn);
	}

	/**
     * @brief : 문의하기 리스트 개수 조회
     * @details : 교통안전교육 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : mozInqry
     * @return : 
     */
	@Override
	public int getMozInqryCnt(MozInqry mozInqry) {
		return mozInqryRepository.countMozInqry(mozInqry);
	}

	/**
     * @brief : 문의하기 리스트 조회
     * @details : 문의하기 리스트 조회
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : mozInqry
     * @return : 
     */
	@Override
	public List<MozInqry> getMozInqryList(MozInqry mozInqry) {
		return mozInqryRepository.findAllMozInqryList(mozInqry);
	}

	/**
     * @brief : 문의하기 상세 조회
     * @details : 문의하기 상세 조회
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : inqryId
     * @return : 
     */
	@Override
	public MozInqry getMozInqryDetail(String inqryId) {
		return mozInqryRepository.findOneMozInqry(inqryId);
	}
	
	/**
     * @brief : 문의하기 답변 파일 조회
     * @details : 문의하기 답변 파일 조회
     * @author : KC.KIM
     * @date : 2024.04.16
     * @param : inqryId
     * @return : 
     */
	@Override
	public List<MozAtchFile> findAllMozAtchFileByAtchIdx(String inqryId) {
		return mozAtchFileRepository.findAllMozAtchFileByAtchIdx(inqryId);
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
	@Override
	@Transactional
	public void updateMozInqry(MozInqry mozInqry, MultipartFile[] uploadFiles) {
		String oprtrId = LoginOprtrUtils.getOprtrId();
		mozInqry.setOprtrId(oprtrId);
		
		List<MozAtchFile> deleteAtchFileList = new ArrayList<MozAtchFile>();
		deleteAtchFileList = mozInqry.getAnsAtchFileList();
		
		if(deleteAtchFileList != null) {
			for(MozAtchFile deleteFileInfo : deleteAtchFileList) {
				if(!MozatesCommonUtils.isNull(deleteFileInfo.getFileIdx())) {
					MozAtchFile fileInfo = new MozAtchFile();
					fileInfo = mozAtchFileRepository.findOneMozAtchFileByFileIdx(deleteFileInfo.getFileIdx());
					fileUploadComponent.deleteUploadFile(fileInfo.getFilePath());
					mozAtchFileRepository.deleteMozAtchFileByFileIdx(fileInfo.getFileIdx());				
				}
			}
		}
		
		// 답변 파일 저장
		if(uploadFiles != null && uploadFiles.length > 0) {
			for(MultipartFile uploadFile : uploadFiles) {
				if(!MozatesCommonUtils.isNull(uploadFile.getOriginalFilename())) {
					String[] extArr = {"jpg", "jpeg", "png", "PNG", "doc", "docx", "xlsx", "pdf"};
					UploadFileInfo uploadFileInfo = fileUploadComponent.uploadFileToUploadFileInfoChkExtension(uploadFile, extArr); 
					MozAtchFile atchFile = new MozAtchFile();
					String fileIdx = MozatesCommonUtils.getUuid();
					atchFile.setFileIdx(fileIdx);
					atchFile.setAtchIdx(mozInqry.getInqryId());
					atchFile.setFilePath(uploadFileInfo.getFilePath());
					atchFile.setFileOrgNm(uploadFileInfo.getOriginalFileNm());
					atchFile.setFileExts(uploadFileInfo.getFileExt());
					atchFile.setFileSaveNm(uploadFileInfo.getFileNm());
					atchFile.setFileSize(uploadFileInfo.getFileSize());
					atchFile.setFileType("2");
					mozAtchFileRepository.insertMozAtchFile(atchFile);
				}
	    	}
		}
		
		mozInqryRepository.updateMozInqry(mozInqry);
	}
}
