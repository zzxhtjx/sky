package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Classname ReportServiceImpl
 * @Date 2024/1/6 20:35
 * @Created by dongxuanmang
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkspaceService workspaceService;

    private Long getUser(LocalDate date){
        LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
        //select sum(1) from user where create_time < ?
        Long sum =  userMapper.getUserBefore(endTime);
        return sum == null ? 0L : sum;
    }

    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin到end范围内的时间
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            //计算日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Long> totalUserList = new ArrayList<>();
        List<Long> newUserList = new ArrayList<>();
        Long yesterdayUser = getUser(begin.plusDays(-1));
        for(LocalDate date : dateList){
            Long sum = getUser(date);
            totalUserList.add(sum);
            newUserList.add(sum - yesterdayUser);
            yesterdayUser = sum;
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

/**
 * description:获取订单数据
 * @since: 1.0.0
 * @author: dongxuanmang
 * @date: 2024/1/7 13:07
 * @param begin 开始日期
 * @param end   结束日期
 * @return com.sky.vo.OrderReportVO
 */
    public OrderReportVO getOrderReport(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin到end范围内的时间
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            //计算日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        Integer totalOrderCount = 0;
        Integer validOrderCount = 0;
        for(LocalDate date : dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            Integer order = orderMapper.getOrder(map);
            map.put("status", Orders.COMPLETED);
            Integer validOrder = orderMapper.getValidOrder(map);
            order = order == null ? 0 :order;
            validOrder = validOrder == null ? 0 : validOrder;
            totalOrderCount += order;
            validOrderCount += validOrder;
            orderCountList.add(order);
            validOrderCountList.add(validOrder);
        }
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCompletionRate(((double)validOrderCount / (double)totalOrderCount))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .build();
    }

    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin到end范围内的时间
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            //计算日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Double> turnoverList = new ArrayList<>();
        for(LocalDate date : dateList){
            //营业额指的是当天已完成订单的总金额
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            //select sum(amount) from orders where time > ? and time < ? and status = ?
            Map map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.getByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }
        return TurnoverReportVO.builder()
                        .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    public SalesTop10ReportVO getTop(LocalDate begin, LocalDate end) {
        //计算开始时间和结束时间
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        Integer topNum = 10;
        Map map = new HashMap<>();
        map.put("begin", beginTime);
        map.put("end", endTime);
        map.put("top", topNum);
        List<GoodsSalesDTO> data = orderMapper.getTop(map);
        List<String> namelist = new ArrayList<>();
        List<Integer> numberlist = new ArrayList<>();
        for(GoodsSalesDTO goodsSalesDTO : data){
            namelist.add(goodsSalesDTO.getName());
            numberlist.add(goodsSalesDTO.getNumber());
        }
        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(namelist, ","))
                .numberList(StringUtils.join(numberlist, ","))
                .build();
    }
    
    /**
     * description:导出运营数据
     * @since: 1.0.0
     * @author: dongxuanmang
     * @date: 2024/1/7 21:54
     * @param response
     * @return void
     */
    public void exportBussinessData(HttpServletResponse response) {
        //1.查询导出数据--查询最近三十天数据
        LocalDate beginDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(1);
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(
                LocalDateTime.of(beginDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX));
        //2.通过poi写入excel文件
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/reportModel.xlsx");

        try {
            //基于模板生成新的excel文件
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            //获取sheet标签页
            XSSFSheet sheet = excel.getSheetAt(0);
            //填充数据 -- 填充时间
            sheet.getRow(1).getCell(1).setCellValue("时间: " + beginDate + " 到 " + endDate);

            //填充第四行  营业额
            sheet.getRow(3).getCell(2).setCellValue(businessDataVO.getTurnover());
            //完成率
            sheet.getRow(3).getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            //新增用户
            sheet.getRow(3).getCell(6).setCellValue(businessDataVO.getNewUsers());

            //填充第五行 有效订单
            sheet.getRow(4).getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            //平均单价
            sheet.getRow(4).getCell(4).setCellValue(businessDataVO.getUnitPrice());

            //填充明细数据
            for(int i = 0; i < 30; i++){
                LocalDate date = beginDate.plusDays(i);
                BusinessDataVO businessData = workspaceService.getBusinessData(
                        LocalDateTime.of(date, LocalTime.MIN),
                        LocalDateTime.of(date, LocalTime.MAX));
                XSSFRow row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

            //3.通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            excel.write(outputStream);

            outputStream.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
