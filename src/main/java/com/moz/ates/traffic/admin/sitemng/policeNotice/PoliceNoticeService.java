package com.moz.ates.traffic.admin.sitemng.policeNotice;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.moz.ates.traffic.common.entity.board.MozBrd;

public interface PoliceNoticeService {
	
    /**
     * @brief : 경찰 공지사항 등록
     * @details : 경찰 공지사항 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @param : uploadFiles
     * @return : 
     */
    void registNotice(MozBrd brd, MultipartFile[] uploadFiles);

    /**
     * @brief : 경찰 공지사항 리스트 조회
     * @details : 경찰 공지사항 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    List<MozBrd> getNoticeList(MozBrd brd);

    /**
     * @brief : 경찰 공지사항 리스트 개수 조회
     * @details : 경찰 공지사항 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @return : 
     */
    int getNoticeListCnt(MozBrd brd);
    
    /**
     * @brief : 경찰 공지사항 상세 화면
     * @details : 경찰 공지사항 상세 화면
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : boardIdx
     * @return : 
     */
    MozBrd getNoticeDetail(String boardIdx);
    
    /**
     * @brief : 경찰 공지사항 수정 
     * @details : 경찰 공지사항 수정 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : brd
     * @param : uploadFiles
     * @return : 
     */
    void updateNotice(MozBrd brd, MultipartFile[] uploadFiles);
    
    /**
     * @brief : 경찰 공지사항 삭제
     * @details : 경찰 공지사항 삭제 
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : boardIdx
     * @return : 
     */
    void deleteNotice(String boardIdx);
}
