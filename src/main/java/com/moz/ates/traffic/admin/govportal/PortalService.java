package com.moz.ates.traffic.admin.govportal;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.common.entity.board.MozAtchFile;
import com.moz.ates.traffic.common.entity.board.MozBrd;
import com.moz.ates.traffic.common.entity.board.MozComplaintsReg;
import com.moz.ates.traffic.common.entity.board.MozFaq;
import com.moz.ates.traffic.common.entity.board.MozInqry;
import com.moz.ates.traffic.common.entity.board.MozObjReg;
import com.moz.ates.traffic.common.entity.board.MozTfcSftyEdctn;
import com.moz.ates.traffic.common.entity.board.MozTfcSftyInfrm;
import com.moz.ates.traffic.common.entity.payment.MozPlPymntInfo;

/**
 * className : PortalService
 * author : Mike Lim
 * description : 포털 관련 서비스
 */
public interface PortalService {
	
    /**
     * @brief : 포탈 공지사항 등록
     * @details : 포탈 공지사항 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @param : uploadFiles
     * @return : 
     */
    void registNotice(MozBrd brd, MultipartFile[] uploadFiles);

    /**
     * @brief : 포탈 공지사항 리스트 조회
     * @details : 포탈 공지사항 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    List<MozBrd> getNoticeList(MozBrd brd);

    /**
     * @brief : 포탈 공지사항 리스트 개수 조회
     * @details : 포탈 공지사항 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    int getNoticeListCnt(MozBrd brd);
    
    /**
     * @brief : 포탈 공지사항 상세 화면
     * @details : 공지사항 상세 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : boardIdx
     * @return : 
     */
    MozBrd getNoticeDetail(String boardIdx);
    
    /**
     * @brief : 포탈 공지사항 수정 
     * @details : 포탈 공지사항 수정 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @param : uploadFiles
     * @return : 
     */
    void updateNotice(MozBrd brd, MultipartFile[] uploadFiles);
    
    /**
     * @brief : FAQ 리스트 조회
     * @details : FAQ 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    List<MozFaq> getFaqList(MozFaq faq);

    /**
     * @brief : FAQ 리스트 개수 조회
     * @details : FAQ 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    int getFaqListCnt(MozFaq faq);
    
    /**
     * @brief : FAQ 등록 
     * @details : FAQ 등록 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    void registFaq(MozFaq faq);
    
    /**
     * @brief : FAQ 상세 조회
     * @details : FAQ 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faqIdx
     * @return : 
     */
    MozFaq getFaqDetail(String faqIdx);
    
    /**
     * @brief : FAQ 수정
     * @details : FAQ 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faq
     * @return : 
     */
    void updateFaq(MozFaq faq);
    
    /**
     * @brief : 이의제기 리스트 조회
     * @details : 이의제기 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : objIdx
     * @return : 
     */
    List<MozObjReg> getObjectionList(MozObjReg objReg);

    /**
     * @brief : 이의제기 리스트 개수 조회
     * @details : 이의제기 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : objIdx
     * @return : 
     */
    int getObjectionListCnt(MozObjReg objReg);
    
    /**
     * @brief : 이의제기 상세 조회
     * @details : 이의제기 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : objIdx
     * @return : 
     */
    MozObjReg getObjectionDetail(String objIdx);

    /**
     * @brief : 불만사항 리스트 조회
     * @details : 이의제기 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : complaintsReg
     * @return : 
     */
    List<MozComplaintsReg> getComplaintList(MozComplaintsReg complaintsReg);

    /**
     * @brief : 불만사항 리스트 개수 조회
     * @details : 이의제기 상세 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : complaintsReg
     * @return : 
     */
    int getComplaintListCnt(MozComplaintsReg complaintsReg);
    
    /**
     * @brief : 불만사항 상세 조회
     * @details : 불만사항 상세
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : complaintsIdx
     * @return : 
     */
    MozComplaintsReg getComplaintDetail(String complaintsIdx);
    
    /**
     * @brief : 범칙금 납부처 등록 
     * @details : 범칙금 납부처 등록
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : 
     * @return : 
     */
    void registPenaltyPlace(MozPlPymntInfo plPymntInfo);

    /**
     * @brief : 범칙금 납부처 리스트 조회 
     * @details : 범칙금 납부처 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : plPymntInfo
     * @return : 
     */
    List<MozPlPymntInfo> getPenaltyPlaceList(MozPlPymntInfo plPymntInfo);
    
    /**
     * @brief : 범칙금 납부처 리스트 개수 조회 
     * @details : 범칙금 납부처 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : plPymntInfo
     * @return : 
     */
    int getPenaltyPlaceListCnt(MozPlPymntInfo plPymntInfo);
    
    /**
     * @brief : 범칙금 납부처 상세 
     * @details : 범칙금 납부처 상세
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : placePymntId
     * @return : 
     */
    MozPlPymntInfo getPenaltyPlaceDetail(String placePymntId);
    
    /**
     * @brief : 범칙금 납부처 수정
     * @details : 범칙금 납부처 수정
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : placePymntId
     * @return : 
     */
    void updatePenaltyPlace(MozPlPymntInfo plPymntInfo);
    
    /**
     * @brief : 범칙금 납부처 삭제
     * @details : 범칙금 납부처 삭제 
     * @author : KC.KIM
     * @date : 2023.08.08
     * @param : placePymntId
     * @return : 
     */
    void deletePenaltyPlace(String placePymntId);
    
    /**
     * @brief : 공지사항 삭제
     * @details : 공지사항 삭제 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : boardIdx
     * @return : 
     */
    void deleteNotice(String boardIdx);
    
    /**
     * @brief : FAQ 삭제
     * @details : FAQ 삭제
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : faqIdx
     * @return : 
     */
    void deleteFaq(String faqIdx);

//    void mailSend(ObjectionVO objectionVo);
    
    void updateObjAns(MozObjReg objReg, MultipartFile[] uploadFiles);
    
    void updateCmpAns(MozComplaintsReg complaintsReg, MultipartFile[] uploadFiles);
    
    /**
     * @brief : 교통안전정보 리스트 개수 조회
     * @details : 교통안전정보 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.02.18
     * @param : mozTfcSftyInfrm
     * @return : 
     */
	public int getSftyInfrmListCnt(MozTfcSftyInfrm mozTfcSftyInfrm);

	/**
     * @brief : 교통안전정보 리스트 조회
     * @details : 교통안전정보 리스트 조회
     * @author : KC.KIM
     * @date : 2024.02.18
     * @param : mozTfcSftyInfrm
     * @return : 
     */
	public List<MozTfcSftyInfrm> getSftyInfrmList(MozTfcSftyInfrm mozTfcSftyInfrm);
	
	/**
     * @brief : 교통안전정보 상세 조회
     * @details : 교통안전정보 상세 조회
     * @author : KC.KIM
     * @date : 2024.02.18
     * @param : mozTfcSftyInfrm
     * @return : 
     */
	public MozTfcSftyInfrm getSftyInfrmDetail(String tfcSftyInfrmId);
	
	/**
     * @brief : 교통안전정보 삭제
     * @details : 교통안전정보 삭제
     * @author : KC.KIM
     * @date : 2024.02.19
     * @param : tfcSftyInfrmId
     * @return : 
     */
	public void deleteSftyInfrmByTfcSftyInfrmId(String tfcSftyInfrmId);

	/**
     * @brief : 교통안전정보 등록
     * @details : 교통안전정보 등록
     * @author : KC.KIM
     * @date : 2024.02.19
     * @param : mozTfcSftyInfrm
     * @param : uploadFile
     * @return : 
     */
	public void registSftyInfrm(MozTfcSftyInfrm mozTfcSftyInfrm, MultipartFile uploadFile);

	/**
     * @brief : 교통안전정보 수정
     * @details : 교통안전정보 수정
     * @author : KC.KIM
     * @date : 2024.02.19
     * @param : mozTfcSftyInfrm
     * @param : uploadFile
     * @return : 
     */
	public void updateSftyInfrm(MozTfcSftyInfrm mozTfcSftyInfrm, MultipartFile uploadFile);

	/**
     * @brief : 교통안전교육 리스트 개수 조회
     * @details : 교통안전교육 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : mozTfcSftyEdctn
     * @return : 
     */
	public int getSftyEdctnListCnt(MozTfcSftyEdctn mozTfcSftyEdctn);

	/**
     * @brief : 교통안전교육 리스트 조회
     * @details : 교통안전교육 리스트 조회
     * @author : KC.KIM
     * @date : 2024.02.19
     * @param : mozTfcSftyEdctn
     * @return : 
     */
	public List<MozTfcSftyEdctn> getSftyEdctnList(MozTfcSftyEdctn mozTfcSftyEdctn);
	
	/**
     * @brief : 교통안전교육 등록
     * @details : 교통안전교육 등록
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : mozTfcSftyEdctn
     * @param : uploadFile
     * @return : 
     */
	public void registSftyEdctn(MozTfcSftyEdctn mozTfcSftyEdctn, MultipartFile uploadFile);

	/**
     * @brief : 교통안전교육 상세 정보 조회
     * @details : 교통안전교육 상세 정보 조회
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : tfcSftyEdctnId
     * @return : 
     */
	public MozTfcSftyEdctn getSftyEdctnDetail(String tfcSftyEdctnId);

	/**
     * @brief : 교통안전교육 삭제
     * @details : 교통안전교육 삭제
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : tfcSftyEdctnId
     * @return : 
     */
	public void deleteMozTfcSftyEdctn(String tfcSftyEdctnId);

	/**
     * @brief : 교통안전교육 수정
     * @details : 교통안전교육 수정
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : mozTfcSftyEdctn
     * @param : uploadFile
     * @return : 
     */
	public void updateMozTfcSftyEdctn(MozTfcSftyEdctn mozTfcSftyEdctn, MultipartFile uploadFile);

	/**
     * @brief : 문의하기 리스트 개수 조회
     * @details : 교통안전교육 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : mozInqry
     * @return : 
     */
	public int getMozInqryCnt(MozInqry mozInqry);

	/**
     * @brief : 문의하기 리스트 조회
     * @details : 문의하기 리스트 조회
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : mozInqry
     * @return : 
     */
	public List<MozInqry> getMozInqryList(MozInqry mozInqry);

	/**
     * @brief : 문의하기 상세 조회
     * @details : 문의하기 상세 조회
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : inqryId
     * @return : 
     */
	public MozInqry getMozInqryDetail(String inqryId);

	/**
     * @brief : 문의하기 답변 등록
     * @details : 문의하기 답변 등록
     * @author : KC.KIM
     * @date : 2024.02.20
     * @param : mozInqry
     * @param : uploadFiles
     * @return : 
     */
	public void updateMozInqry(MozInqry mozInqry, MultipartFile[] uploadFiles);

	/**
     * @brief : 문의하기 답변 파일 조회
     * @details : 문의하기 답변 파일 조회
     * @author : KC.KIM
     * @date : 2024.04.16
     * @param : inqryId
     * @return : 
     */
	List<MozAtchFile> findAllMozAtchFileByAtchIdx(String inqryId);
	
	
}
