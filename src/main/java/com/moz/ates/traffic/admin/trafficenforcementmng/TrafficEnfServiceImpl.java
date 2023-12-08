package com.moz.ates.traffic.admin.trafficenforcementmng;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.admin.common.DataTableVO;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfHst;
import com.moz.ates.traffic.common.entity.enforcement.MozTfcEnfMaster;
import com.moz.ates.traffic.common.entity.law.MozTfcLwInfo;
import com.moz.ates.traffic.common.entity.payment.MozFinePymntInfo;
import com.moz.ates.traffic.common.repository.enforcement.MozTfcEnfHstRepository;
import com.moz.ates.traffic.common.repository.enforcement.MozTfcEnfMasterRepository;
import com.moz.ates.traffic.common.repository.law.MozTfcLwInfoRepository;
import com.moz.ates.traffic.common.repository.payment.MozFinePymntInfoRepository;

@Service
public class TrafficEnfServiceImpl implements TrafficEnfService {

	@Autowired
	MozTfcEnfMasterRepository tfcEnfMasterRepository;

	@Autowired
	MozFinePymntInfoRepository finePymntInfoRepository;

	@Autowired
	MozTfcEnfHstRepository tfcEnfHstRepository;

	@Autowired
	MozTfcLwInfoRepository tfcLwInfoRepository;

	@Override
	public DriverVO getDriverDetail(EnfSearchVO enfSearchVO) {

		// test
		DriverVO driverVO = new DriverVO();
//        driverVO.setName("홍길동");
//        driverVO.setPhoneNum("010-1111-2456");

		if ("hs".equals(enfSearchVO.getName()) && "010".equals(enfSearchVO.getPhone())) {
			driverVO.setName(enfSearchVO.getName());
			driverVO.setPhone(enfSearchVO.getPhone());
			driverVO.setDriverLicense("111-23-4555");
			driverVO.setNid("123123123123");
			driverVO.setNationality("모잠비크");
			driverVO.setLicenseRenewal("2회");
			driverVO.setAddress("moz africa city");
			driverVO.setBirth("920926");
			driverVO.setSex("m");
			driverVO.setEmail("mike.lim@bluedus.com");
			driverVO.setDrivetLicenseDate("2017-07-27");
			driverVO.setLicenseCheck("유효");
//            driverVO.set_LicenseCheck(true);
		}
//        return sqlSession.selectOne("TrafficEnf.selectSearchDriverDetail",tfcEnfId);

		return driverVO;
		// test end

//        return sqlSession.selectOne("TrafficEnf.selectDriverDetail",enfSearchVO);
	}

	@Override
	public CarVO getCarDetail(EnfSearchVO enfSearchVO) {

		// text
		CarVO carVO = new CarVO();

		String carNum = "9199";
		enfSearchVO.setCarNum("9199");

		if (carNum.equals(enfSearchVO.getCarNum())) {
			carVO.setCarNum(enfSearchVO.getCarNum());
			carVO.setCarDriverName("lim hs");
			carVO.setRegDt("2020-11-22");
			carVO.setCarType("Avante(2022 XD)");
			carVO.setCarClassification("소형");
			carVO.setCompetentAuthority("Maputo");
			carVO.setWtCarIsScrapped("해당 사항 없음");
			carVO.setNote("비고");
			carVO.setAutomotiveUse("자가용");
		}

		return carVO;
		// test end

//        return sqlSession.selectOne("TrafficEnf.selectCarDetail",enfSearchVO);
	}

	/**
	 * @brief : 교통단속 법률관리 리스트 조회
	 * @details : 교통단속 법률관리 리스트 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLwInfo
	 * @return :
	 */
	@Override
	public List<MozTfcLwInfo> getLawList(MozTfcLwInfo tfcLwInfo) {
		return tfcLwInfoRepository.findAllLawListsByTfcLwInfo(tfcLwInfo);
	}

	@Override
	public int getLawListCnt(MozTfcLwInfo tfcLwInfo) {
		return tfcLwInfoRepository.countLawListsByTfcLwInfo(tfcLwInfo);
	}

	/**
	 * @brief : 교통단속 법률관리 등록
	 * @details : 교통단속 법률관리 등록
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLwInfo
	 * @return :
	 */
	@Override
	public void lawSave(MozTfcLwInfo tfcLwInfo) {
		tfcLwInfoRepository.insertMozTfcLwInfo(tfcLwInfo);
	}

	/**
	 * @brief : 교통단속 법률관리 상세 조회
	 * @details : 교통단속 법률관리 상세 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLawId
	 * @return :
	 */
	@Override
	public MozTfcLwInfo getLawDetail(String tfcLawId) {
		return tfcLwInfoRepository.findOneLawDetail(tfcLawId);
	}

	/**
	 * @brief : 교통단속 법률관리 정보 수정
	 * @details : 교통단속 법률관리 정보 수정
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLawId
	 * @return :
	 */
	@Override
	public void deleteLaw(String tfcLawId) {
		tfcLwInfoRepository.deleteMozTfcLwInfoByTfcLawId(tfcLawId);
	}

	/**
	 * @brief : 교통단속 법률관리 정보 수정
	 * @details : 교통단속 법률관리 정보 수정
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcLwInfo
	 * @return :
	 */
	@Override
	public void updateLaw(MozTfcLwInfo tfcLwInfo) {
		tfcLwInfoRepository.updateMozTfcLwInfoByTfcLawId(tfcLwInfo);
	}

	/**
	 * @brief : 교통단속 정보 리스트 조회
	 * @details : 교통단속 정보 리스트 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfMaster
	 * @return :
	 */
	@Override
	public List<MozTfcEnfMaster> getInfoList(MozTfcEnfMaster tfcEnfMaster) {
		return tfcEnfMasterRepository.findAllInfoList(tfcEnfMaster);
	}

	@Override
	public int getInfoListCnt(MozTfcEnfMaster tfcEnfMaster) {
		return tfcEnfMasterRepository.countInfoList(tfcEnfMaster);
	}

	/**
	 * @brief : 교통단속 정보 상세 조회
	 * @details : 교통단속 정보 상세 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfId
	 * @return :
	 */
	@Override
	public MozTfcEnfMaster getTrafficEnfDetail(String tfcEnfId) {
		return tfcEnfMasterRepository.findOneMozTfcEnfMasterBytfcEnfId(tfcEnfId);
	}

	/**
	 * @brief : 교통단속 정보 수정
	 * @details : 교통단속 정보 수정
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfMaster
	 * @return :
	 */
	@Override
	public void updateInfo(MozTfcEnfMaster tfcEnfMaster) {
		tfcEnfMasterRepository.updateMozTfcEnfMaster(tfcEnfMaster);
	}

	/**
	 * @brief : 교통단속 로그 리스트 조회
	 * @details : 교통단속 로그 리스트 조회
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfHst
	 * @return :
	 */
	@Override
	public List<MozTfcEnfHst> getLogList(MozTfcEnfHst tfcEnfHst) {
		return tfcEnfHstRepository.findAllLogListsByTfcEnfHst(tfcEnfHst);
	}

	@Override
	public int getLogListCnt(MozTfcEnfHst tfcEnfHst) {
		return tfcEnfHstRepository.countLogListsByTfcEnfHst(tfcEnfHst);
	}

	/**
	 * @brief : 벌금 정보 수정
	 * @details : 벌금 정보 수정
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : finePymntInfo
	 * @return :
	 */
	@Override
	public void updateInfoPrice(MozFinePymntInfo finePymntInfo) {
		finePymntInfoRepository.updateFineTotalPrice(finePymntInfo);
	}

	/**
	 * @brief : 위반 차량 사진 삭제
	 * @details : 위반 차량 사진 삭제
	 * @author : KC.KIM
	 * @date : 2023.08.08
	 * @param : tfcEnfMaster
	 * @return :
	 */
	@Override
	public void deleteEnfImage(MozTfcEnfMaster tfcEnfMaster) {
		tfcEnfMasterRepository.deleteEnfImage(tfcEnfMaster);
	}

}
