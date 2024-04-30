package com.moz.ates.traffic.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;
import com.moz.ates.traffic.common.entity.police.MozPolInfo;
import com.moz.ates.traffic.common.repository.operator.MozWebOprtrRepository;
import com.moz.ates.traffic.common.repository.police.MozPolInfoRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    MozWebOprtrRepository webOprtrRepository;
    
    @Autowired
    MozPolInfoRepository polInfoRepository;
    
    /**
     * @brief : 아이디 중복 조회
     * @details : 아이디 중복 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : webOprtr
     * @return : 
     */
    @Override
    public int getDupliChk(MozWebOprtr webOprtr) {
    	return webOprtrRepository.countMozWebOprtrForDuplChk(webOprtr);
    }

    /**
     * @brief : 관리자 등록
     * @details : 관리자 등록
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : webOprtr
     * @return : 
     */
    @Override
    public void registUser(MozWebOprtr webOprtr) {
    	webOprtrRepository.saveMozWebOprtr(webOprtr);
    }

    /**
     * @brief : 관리자 상세 조회
     * @details : 관리자 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : oprtrId
     * @return : 
     */
    @Override
    public MozWebOprtr getUserDetail(String oprtrId) {
    	return webOprtrRepository.findOneMozWebOprtr(oprtrId);
    }

    /**
     * @brief : 관리자 수정
     * @details : 관리자 수정
     * @author : KC.KIM
     * @date : 2023.08.04
     * @param : webOprtr
     * @return : 
     */
    @Override
    public void updateUser(MozWebOprtr webOprtr) {
    	webOprtrRepository.updateMozWebOprtr(webOprtr);
    }

    /**
   * @brief : 사용자 리스트 조회
   * @details : 사용자 리스트 조회
   * @author : KC.KIM
   * @date : 2023.08.18
   * @param : polInfo
   * @return : 
   */
    @Override
    public List<MozPolInfo> getPolInfoList(MozPolInfo polInfo) {
    	return polInfoRepository.findAllMozPolInfo(polInfo);
    }
    
    /**
   * @brief : 사용자 리스트 개수 조회
   * @details : 사용자 리스트 개수 조회
   * @author : KC.KIM
   * @date : 2023.08.18
   * @param : polInfo
   * @return : 
   */
    @Override
    public int getPolInfoListCnt(MozPolInfo polInfo) {
    	return polInfoRepository.countMozPolInfo(polInfo);
    }
    
    /**
     * @brief : 사용자 상세 조회
     * @details : 사용자 상세 조회
     * @author : KC.KIM
     * @date : 2023.08.18
     * @param : oprtrId
     * @return : 
     */
    @Override
    public MozPolInfo getPolInfoDetail(String polId) {
    	return polInfoRepository.findOneMozPolInfo(polId);
    }

    /**
     * @brief : 사용자 리스트 조회
     * @details : 사용자 리스트 조회
     * @author : KC.KIM
     * @date : 2023.08.18
     * @param : webOprtr
     * @return : 
     */
	@Override
	public List<MozWebOprtr> getWebOprtrList(MozWebOprtr webOprtr) {
		return webOprtrRepository.findAllMozWebOprtr(webOprtr);
	}

    /**
     * @brief : 사용자 리스트 개수 조회 
     * @details : 사용자 리스트 개수 조회
     * @author : KC.KIM
     * @date : 2023.08.18
     * @param : webOprtr
     * @return : 
     */
	@Override
	public int getWebOprtrListCnt(MozWebOprtr webOprtr) {
		return webOprtrRepository.countMozWebOprtr(webOprtr);
	}
	
	/**
	  * @Method Name : deleteUser
	  * @작성일 : 2024. 2. 21.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 사용자 삭제
	  * @param webOprtr
	  */
	@Override
	public void deleteUser(MozWebOprtr webOprtr) {
		webOprtrRepository.deleteWebOprtr(webOprtr);
	}
	
	/**
	  * @Method Name : updatePolice
	  * @작성일 : 2024. 2. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 경찰 수정
	  * @param polInfo
	  */
	@Override
	public void updatePolice(MozPolInfo polInfo) {
		polInfoRepository.updateMozPolInfo(polInfo);
	}
	
	/**
	  * @Method Name : deletePolice
	  * @작성일 : 2024. 2. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 경찰 삭제
	  * @param polInfo
	  */
	@Override
	public void deletePolice(MozPolInfo polInfo) {
		polInfoRepository.deleteMozPolInfo(polInfo);
	}
	
}
