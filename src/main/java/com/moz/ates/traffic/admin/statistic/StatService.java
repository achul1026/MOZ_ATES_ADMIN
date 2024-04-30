package com.moz.ates.traffic.admin.statistic;

import java.util.Map;

import com.moz.ates.traffic.common.entity.stat.ChartSearchDTO;

public interface StatService {

	/**
     * @brief : 통계 데이터 조회
     * @details : 통계 데이터 조회
     * @author : KC.KIM
     * @date : 2024.04.24
     * @param : chartDTO
     * @return : 
     */
	Map<String, Object> findAllChartData(ChartSearchDTO chartDTO);
}
