package com.moz.ates.traffic.admin.sitemng.policeNotice;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.common.component.FileUploadComponent;
import com.moz.ates.traffic.common.entity.board.MozAtchFile;
import com.moz.ates.traffic.common.entity.board.MozBrd;
import com.moz.ates.traffic.common.entity.common.UploadFileInfo;
import com.moz.ates.traffic.common.enums.BoradType;
import com.moz.ates.traffic.common.enums.NoticeCateCd;
import com.moz.ates.traffic.common.enums.RegistantType;
import com.moz.ates.traffic.common.repository.board.MozAtchFileRepository;
import com.moz.ates.traffic.common.repository.board.MozBrdRepository;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;

@Service
public class PoliceNoticeServiceImpl implements PoliceNoticeService {
	
    @Autowired
    FileUploadComponent fileUploadComponent;
    
    @Autowired
    MozAtchFileRepository mozAtchFileRepository;
    
    @Autowired
    MozBrdRepository brdRepository;
	
    /**
     * @brief : 경찰 공지사항 등록
     * @details : 경찰 공지사항 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @param : uploadFiles
     * @return : 
     */
    @Override
    public void registNotice(MozBrd brd, MultipartFile[] uploadFiles) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	
    	String crtr = LoginOprtrUtils.getOprtrId();
    	String wrtrNm = LoginOprtrUtils.getOprtrNm();
    	String boardIdx = MozatesCommonUtils.getUuid();
    	
    	brd.setOprtrId(crtr);
    	brd.setWrtrNm(wrtrNm);
    	brd.setBoardIdx(boardIdx);
    	brd.setCateCd(NoticeCateCd.POLICE_APPLICATION.getCode());
    	
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
        		atchFile.setBrdTy(BoradType.NOTICE);
        		atchFile.setRgsTy(RegistantType.ADMIN_USER);
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
     * @brief : 경찰 공지사항 리스트 조회
     * @details : 경찰 공지사항 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    @Override
    public List<MozBrd> getNoticeList(MozBrd brd) {
    	brd.setCateCd(NoticeCateCd.POLICE_APPLICATION.getCode());
    	return brdRepository.findAllMozBrd(brd);
    }
    
    /**
     * @brief : 경찰 공지사항 리스트 개수 조회
     * @details : 경찰 공지사항 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    @Override
    public int getNoticeListCnt(MozBrd brd) {
    	brd.setCateCd(NoticeCateCd.POLICE_APPLICATION.getCode());
    	return brdRepository.countMozBrd(brd);
    }
    
    /**
     * @brief : 경찰 공지사항 상세 화면
     * @details : 경찰 공지사항 상세 화면
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
     * @brief : 경찰 공지사항 수정 
     * @details : 경찰 공지사항 수정 
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
        		atchFile.setBrdTy(BoradType.NOTICE);
        		atchFile.setRgsTy(RegistantType.ADMIN_USER);
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
     * @brief : 경찰 공지사항 삭제
     * @details : 경찰 공지사항 삭제 
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
}
