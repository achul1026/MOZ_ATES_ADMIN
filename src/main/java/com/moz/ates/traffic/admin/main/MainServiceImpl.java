package com.moz.ates.traffic.admin.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.common.entity.operator.MozWebOprtr;
import com.moz.ates.traffic.common.repository.operator.MozWebOprtrRepository;
import com.moz.ates.traffic.common.util.MozatesCommonUtils;


@Service
public class MainServiceImpl implements MainService {

    @Autowired
    MozWebOprtrRepository webOprtrRepository;
    
    @Override
    public MozWebOprtr getUserById(MozWebOprtr webOprtr) {
    	return webOprtrRepository.selectUserById(webOprtr);

    }
    
	/**
	 * @briedf 어드민 관리자 회원가입
	 * @author KY.LEE
	 * @Date 2023.08.11
	 * @param webOprtr
	 */
	@Override
	public void registWebOprtr(MozWebOprtr webOprtr) {
		BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
		String encodePw = encode.encode(webOprtr.getOprtrAccountPw());
		if(this.dupChkUserEmail(webOprtr)) {
			webOprtr.setOprtrId(MozatesCommonUtils.getUuid());
			webOprtr.setOprtrAccountPw(encodePw);
			webOprtr.setCrtr(webOprtr.getOprtrEmail());
			webOprtr.setLoginFailCnt("0");
			webOprtr.setOprtrPermission("sup");
			webOprtrRepository.insertUser(webOprtr);
		}
	}

	
	/**
	 * @briedf 어드민 관리자아이디 중복체크
	 * @author KY.LEE
	 * @Date 2023.08.11
	 * @param webOprtr
	 */
	@Override
	public boolean dupChkUserEmail(MozWebOprtr webOprtr) {
		boolean result = false;
		int isUserChk = webOprtrRepository.selectDupliChk(webOprtr);
		if(0 == isUserChk) {
			result = true;
		}
		return result;
	}
	
}
