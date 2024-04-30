package com.moz.ates.traffic.admin.myinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.common.util.LoginOprtrUtils;
import com.moz.ates.traffic.admin.common.util.PasswordUtils;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;
import com.moz.ates.traffic.common.entity.operator.MozWebOprtrDTO;
import com.moz.ates.traffic.common.repository.operator.MozWebOprtrRepository;

@Service
public class MyInfoServiceImpl implements MyInfoService {

	@Autowired
	MozWebOprtrRepository mozWebOprtrRepository;
	
    /**
     * @brief : 내 정보 조회
     * @details : 내 정보 조회
     * @author : KY.LEE
     * @date : 2023.04.23
     */
    @Override
    public MozWebOprtr getMyInfoDetail() {
    	MozWebOprtrDTO loginUser = LoginOprtrUtils.getMozWebOprtr();
    	return mozWebOprtrRepository.findOneMozWebOprtr(loginUser.getOprtrId());
    }
    
	/**
	  * @Method Name : verfyPassword
      * @author : KY.LEE
      * @date : 2023.04.23
	  * @Method Brief : 비밀번호 체크
	  * @param mozPolInfo
	  * @return
	  */
	@Override
	public boolean verfyPassword(MozWebOprtr mozWebOprtr) {
		String password = mozWebOprtrRepository.findPasswordByOprtrId(LoginOprtrUtils.getOprtrId());
		return PasswordUtils.matches(mozWebOprtr.getOprtrAccountPw(), password);
	}

	/**
	  * @Method Name : modifyPassword
     * @author : KY.LEE
     * @date : 2023.04.23
	  * @Method Brief : 비밀번호 변경
	  * @param MozWebOprtr
	  * @return
	  */
	@Override
	public void modifyPassword(MozWebOprtr mozWebOprtr) {
		String newPassword = PasswordUtils.encodePassword(mozWebOprtr.getOprtrAccountPw());
		mozWebOprtr.setOprtrId(LoginOprtrUtils.getOprtrId());
		mozWebOprtr.setTmpPwIssuedYn("N");
		mozWebOprtr.setOprtrAccountPw(newPassword);
		
		mozWebOprtrRepository.updateOprtrAccountPw(mozWebOprtr);
	}

	/**
	  * @Method Name : modifyMyProfile
      * @author : KY.LEE
      * @date : 2023.04.23
	  * @Method Brief : 내 정보 번경
	  * @param MozWebOprtr
	  * @return
	  */
	@Override
	public void modifyMyProfile(MozWebOprtr mozWebOprtr) {
		mozWebOprtrRepository.updateMyInfo(mozWebOprtr);
	}

}
