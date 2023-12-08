package com.moz.ates.traffic.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.common.DataTableVO;
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


//    /**
//     * @brief : 관리자 리스트 조회
//     * @details : 관리자 리스트 조회
//     * @author : KC.KIM
//     * @date : 2023.08.04
//     * @param : webOprtr
//     * @return : DataTableVO
//     */
//    @Override
//    public DataTableVO getUserListDatatable(MozWebOprtr webOprtr) {
//        return new DataTableVO(this.getUserList(webOprtr), this.getUserListCnt(webOprtr));
//    }
//
//    @Override
//    public List getUserList(MozWebOprtr webOprtr) {
//    	return webOprtrRepository.selectUserList(webOprtr);
//    }
//    
//    @Override
//    public int getUserListCnt(MozWebOprtr webOprtr) {
//    	return webOprtrRepository.selectUserListCnt(webOprtr);
//    }
    
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
    public MozPolInfo getPolInfoDetail(String polLcenId) {
    	return polInfoRepository.findOneMozPolInfo(polLcenId);
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
}
