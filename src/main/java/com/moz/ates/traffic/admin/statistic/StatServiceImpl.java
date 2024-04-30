package com.moz.ates.traffic.admin.statistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moz.ates.traffic.common.entity.common.MozCmCd;
import com.moz.ates.traffic.common.entity.stat.ChartSearchDTO;
import com.moz.ates.traffic.common.enums.NtcTypeCd;
import com.moz.ates.traffic.common.repository.accident.MozTfcAcdntMasterRepository;
import com.moz.ates.traffic.common.repository.common.MozCmCdRepository;
import com.moz.ates.traffic.common.repository.enforcement.MozTfcEnfMasterRepository;
import com.moz.ates.traffic.common.repository.equipment.MozTfcEnfEqpMasterRepository;
import com.moz.ates.traffic.common.repository.finentc.MozFineNtcInfoRepository;

@Service
public class StatServiceImpl implements StatService {
	
	@Autowired
	MozTfcAcdntMasterRepository mozTfcAcdntMasterRepository;
	
	@Autowired
	MozTfcEnfMasterRepository mozTfcEnfMasterRepository;
	
	@Autowired
	MozFineNtcInfoRepository mozFineNtcInfoRepository;
	
	@Autowired
	MozTfcEnfEqpMasterRepository mozTfcEnfEqpMasterRepository;
	
	@Autowired
	MozCmCdRepository mozCmCdRepository;
	
	/**
     * @brief : 통계 데이터 조회
     * @details : 통계 데이터 조회
     * @author : KC.KIM
     * @date : 2024.04.24
     * @param : chartDTO
     * @return : 
     */
	@Override
	public Map<String, Object> findAllChartData(ChartSearchDTO chartDTO) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 전체 조회
		if(chartDTO.getCate().equals("findAll")) {
			switch(chartDTO.getType()) {
			case "enforce":
				List<MozCmCd> enfTyCdList = mozCmCdRepository.findAllCdList("LAW_CHTR_CD");
				List<String> chartList = new ArrayList<String>();
				String labelNm = "";
				for(int i=0; i < enfTyCdList.size(); i++) {
					chartDTO.setCate(enfTyCdList.get(i).getCdId());
					Map<String, Object> enfChartMap = mozTfcEnfMasterRepository.findAllChartData(chartDTO);
					chartList.add((String) enfChartMap.get("COUNT"));
					
					if(i == enfTyCdList.size() - 1) {
						resultMap.put("chartLabel", ((String) enfChartMap.get("LABEL")));	
						labelNm += enfTyCdList.get(i).getCdNm();
					}else {
						labelNm += enfTyCdList.get(i).getCdNm() + ",";
					}
				}
				resultMap.put("chartDataList", chartList);
				resultMap.put("labelList", labelNm);
				break;
			case "accident":
				List<MozCmCd> acdntTyCdList = mozCmCdRepository.findAllCdList("ACCIDENT_TYPE");
				List<String> acdntChartList = new ArrayList<String>();
				String acdntLabelNm = "";
				for(int i=0; i < acdntTyCdList.size(); i++) {
					chartDTO.setCate(acdntTyCdList.get(i).getCdId());
					Map<String, Object> acdntChartMap = mozTfcAcdntMasterRepository.findAllChartData(chartDTO);
					acdntChartList.add((String) acdntChartMap.get("COUNT"));
					
					if(i == acdntTyCdList.size() - 1) {
						resultMap.put("chartLabel", ((String) acdntChartMap.get("LABEL")));	
						acdntLabelNm += acdntTyCdList.get(i).getCdNm();
					}else {
						acdntLabelNm += acdntTyCdList.get(i).getCdNm() + ",";
					}
				}
				resultMap.put("chartDataList", acdntChartList);
				resultMap.put("labelList", acdntLabelNm);
				break;
			case "penalty":
				List<MozCmCd> ntcTyCdList = mozCmCdRepository.findAllCdList("NOTICE_TYPE");
				List<String> ntcChartList = new ArrayList<String>();
				String ntcLabelNm = "";
				for(int i=0; i < ntcTyCdList.size(); i++) {
					if(ntcTyCdList.get(i).getCdId().equals(NtcTypeCd.FIRST_NOTICE.getCode())
							|| ntcTyCdList.get(i).getCdId().equals(NtcTypeCd.SECOND_NOTICE.getCode())) {
						chartDTO.setCate(ntcTyCdList.get(i).getCdId());
						Map<String, Object> ntcChartMap = mozFineNtcInfoRepository.findAllChartData(chartDTO);
						ntcChartList.add((String) ntcChartMap.get("COUNT"));
						
						if(i == ntcTyCdList.size() - 2) {
							resultMap.put("chartLabel", ((String) ntcChartMap.get("LABEL")));
							ntcLabelNm += ntcTyCdList.get(i).getCdNm();
						}else {
							ntcLabelNm += ntcTyCdList.get(i).getCdNm() + ",";
						}
					}
				}
				resultMap.put("chartDataList", ntcChartList);
				resultMap.put("labelList", ntcLabelNm);
				break;
			case "facility":
				List<MozCmCd> eqpTyCdList = mozCmCdRepository.findAllCdList("EQUIPMENT_TYPE");
				List<String> eqpChartList = new ArrayList<String>();
				String eqpLabelNm = "";
				for(int i=0; i < eqpTyCdList.size(); i++) {
					chartDTO.setCate(eqpTyCdList.get(i).getCdId());
					Map<String, Object> eqpChartMap = mozTfcEnfEqpMasterRepository.findAllChartData(chartDTO);
					eqpChartList.add((String) eqpChartMap.get("COUNT"));
					
					if(i == eqpTyCdList.size() - 1) {
						resultMap.put("chartLabel", ((String) eqpChartMap.get("LABEL")));
						eqpLabelNm += eqpTyCdList.get(i).getCdNm();
					}else {
						eqpLabelNm += eqpTyCdList.get(i).getCdNm() + ",";
					}
				}
				resultMap.put("chartDataList", eqpChartList);
				resultMap.put("labelList", eqpLabelNm);
				break;
			}
		}else {
			//유형 조회
			Map<String, Object> charDataMap = new HashMap<String, Object>();
			switch(chartDTO.getType()) {
			case "enforce":
				charDataMap = mozTfcEnfMasterRepository.findAllChartData(chartDTO);
				break;
			case "accident":
				charDataMap = mozTfcAcdntMasterRepository.findAllChartData(chartDTO);
				break;
			case "penalty":
				charDataMap = mozFineNtcInfoRepository.findAllChartData(chartDTO);
				break;
			case "facility":
				charDataMap = mozTfcEnfEqpMasterRepository.findAllChartData(chartDTO);
				break;
			}
			
			resultMap.put("chartDataList", ((String) charDataMap.get("COUNT")));
			resultMap.put("chartLabel", ((String) charDataMap.get("LABEL")));
		}
		return resultMap;
	}
}
