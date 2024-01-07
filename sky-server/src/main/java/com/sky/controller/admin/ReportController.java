package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @Description Echarts数据统计
 * @Classname ReportController
 * @Date 2024/1/6 20:26
 * @Created by dongxuanmang
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "数据统计")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * description: 营业额统计
     * @since: 1.0.0
     * @author: dongxuanmang
     * @date: 2024/1/6 20:34
     * @param begin
     * @param end
     * @return com.sky.result.Result<com.sky.vo.TurnoverReportVO>
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
         @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate end){
        log.info("营业额数据统计: {}, {}", begin, end);
        return Result.success(reportService.getTurnoverStatistics(begin, end));
    }

    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
       @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate end){
        log.info("用户数据统计: {} {}", begin, end);
        return Result.success(reportService.getUserStatistics(begin, end));
    }

    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计接口")
    public Result<OrderReportVO> getOrderReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate end){
        log.info("订单数据统计: {} {}", begin, end);
        return Result.success(reportService.getOrderReport(begin, end));
    }

    @GetMapping("/top10")
    @ApiOperation("查询top10排名接口")
    public Result<SalesTop10ReportVO> getTop(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate end){
        log.info("查询销量排名前10接口: {} {}", begin, end);
        return Result.success(reportService.getTop(begin, end));
    }

    @GetMapping("/export")
    @ApiOperation("导出excel表格")
    public Result<String> export(){
        return Result.success("ok");
    }
}
