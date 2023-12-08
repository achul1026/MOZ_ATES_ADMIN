package com.moz.ates.traffic.admin.user;

import java.util.List;

import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;
import com.moz.ates.traffic.common.entity.police.MozPolInfo;

public interface UserService {
    /**
     * @brief : 아이디 중복 조회
     * @details : 아이디 중복 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : webOprtr
     * @return : 
     */
    int getDupliChk(MozWebOprtr webOprtr);

    /**
     * @brief : 관리자 등록
     * @details : 관리자 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : webOprtr
     * @return : 
     */
    void registUser(MozWebOprtr webOprtr);
    
//    /**
//     * @brief : 관리자 리스트 조회
//     * @details : 관리자 리스트 조회
//     * @author : KC.KIM
//     * @date : 2023.08.04
//     * @param : webOprtr
//     * @return : DataTableVO
//     */
//    DataTableVO getUserListDatatable(MozWebOprtr webOprtr);
//
//    List getUserList(MozWebOprtr webOprtr);
//
//    int getUserListCnt(MozWebOprtr webOprtr);
    
    /**
     * @brief : 관리자 상세 조회
     * @details : 관리자 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : oprtrId
     * @return : 
     */
    MozWebOprtr getUserDetail(String oprtrId);

    /**
     * @brief : 관리자 수정
     * @details : 관리자 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : webOprtr
     * @return : 
     */
    void updateUser(MozWebOprtr webOprtr);

    /**
     * @brief : 사용자 리스트 조회
     * @details : 사용자 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : polInfo
     * @return : 
     */
    List<MozPolInfo> getPolInfoList(MozPolInfo polInfo);

    /**
     * @brief : 사용자 리스트 개수 조회
     * @details : 사용자 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : polInfo
     * @return : 
     */
    int getPolInfoListCnt(MozPolInfo polInfo);
    
    /**
     * @brief : 사용자 상세 조회
     * @details : 사용자 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.18
     * @param : oprtrId
     * @return : 
     */
	MozPolInfo getPolInfoDetail(String polLcenId);

    /**
     * @brief : 사용자 리스트 조회
     * @details : 사용자 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.18
     * @param : webOprtr
     * @return : 
     */
	List<MozWebOprtr> getWebOprtrList(MozWebOprtr webOprtr);
	
    /**
     * @brief : 사용자 리스트 개수 조회
     * @details : 사용자 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.18
     * @param : webOprtr
     * @return : 
     */
	int getWebOprtrListCnt(MozWebOprtr webOprtr);
	
}
