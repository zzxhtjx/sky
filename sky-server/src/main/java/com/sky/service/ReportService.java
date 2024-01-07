package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

/**
 * @Description 统计数据提供Echarts进行分析
 * @Classname ReportService
 * @Date 2024/1/6 20:34
 * @Created by dongxuanmang
 */
public interface ReportService {
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
